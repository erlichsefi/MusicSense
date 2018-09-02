package metaextract.nkm.com.myplayer;

import java.io.*;

import static metaextract.nkm.com.myplayer.FileManager.getPublicPicturesDirectory;

class ActivityReader {

    private String LastLine = null;

    ActivityReader(String StartingTime) {
        File f = getPublicPicturesDirectory("Log");
        File input = new File(f, "Activity.csv");
        String Time;
        try {
            FileReader fr = new FileReader(input);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            br.readLine();
            br.readLine();
            String row = br.readLine();
            if (row != null) {
                String[] split = row.split(",");
                Time = split[2];
                String[] regTime = Time.split(":");
                double convertedCurrentTime = Double.parseDouble(regTime[0] + regTime[1] + regTime[2]);
                String[] regStart = StartingTime.split(":");
                double convertedStartingTime = Double.parseDouble(regStart[0] + regStart[1] + regStart[2]);

                //find the correct line to start reading from
                while (convertedCurrentTime < convertedStartingTime) {
                    row = br.readLine();
                    if (row == null) {
                        LastLine = null;
                        br.close();
                        fr.close();
                        return;
                    }
                    split = row.split(",");
                    Time = split[2];
                    regTime = Time.split(":");
                    convertedCurrentTime = Double.parseDouble(regTime[0] + regTime[1] + regTime[2]);
                }
            }
            LastLine = row;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //iterate over all the lines in activity.csv, and extract vectors. keep the last line
    void extractData(MyLambdaFunction[] Functions, String[] functionsName) {
        DataVector dv = new DataVector();
        //add attributes to DataVector File:
        String[] attributes = dv.getVectorAttributes();
        FileManager fm = new FileManager("DataVector", attributes, functionsName);
        ReadFileCSV read = new ReadFileCSV();
        if (LastLine != null) {
            String[] twoActivityLines = read.ReadActivity(LastLine);
            while (twoActivityLines != null && twoActivityLines[1] != null) { //while not EOF || file is not empty
                LastLine = twoActivityLines[1];
                String[][] action = new String[2][];
                action[0] = twoActivityLines[0].split(",");
                action[1] = twoActivityLines[1].split(",");
                dv.GetDataFromSensors(action[0][4], action[0][2], action[1][2]);
                double[] ProcessedData = dv.getFinalVector(Functions);
                String [] res = new String[ProcessedData.length+1];
                if (ProcessedData.length != 0) {
                    for (int i = 0; i < ProcessedData.length; i++) {
                        res[i] = (String.valueOf(ProcessedData[i]));
                    }
                    res[res.length-1] = action[1][8];
                    fm.WriteToFile(res);
                }
                twoActivityLines = read.ReadActivity(LastLine);
            }
        }
    }

    void updateClassifier(MyLambdaFunction[] Functions, PredictAction p){
        DataVector dv = new DataVector();
        ReadFileCSV read = new ReadFileCSV();
        if(LastLine != null){
            String[] twoActivityLines = read.ReadActivity(LastLine);
            while(twoActivityLines != null && twoActivityLines[1] != null){
                LastLine = twoActivityLines[1];
                String[][] action = new String[2][];
                action[0] = twoActivityLines[0].split(",");
                action[1] = twoActivityLines[1].split(",");
                dv.GetDataFromSensors(action[0][4], action[0][2],action[1][2]);
                double [] data = dv.getFinalVector(Functions);
                double [] dataWithClass = new double[data.length+1];
                System.arraycopy(data, 0, dataWithClass, 0, data.length);
                p.updateModel(dataWithClass,action[1][8]);
                twoActivityLines = read.ReadActivity(LastLine);
            }
        }
    }
}
