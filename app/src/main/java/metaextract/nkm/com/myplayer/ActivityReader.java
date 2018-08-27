package metaextract.nkm.com.myplayer;

import java.io.*;
import java.util.Vector;

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
                Time = split[1];
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
                    Time = split[1];
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
    void extractData(MyLambdaFunction [] Functions, String [] functionsName) {
        DataVector dv = new DataVector();
        //add attributes to DataVector File:
        String [] attributes = dv.getVectorAttributes();
        FileManager fm = new FileManager("DataVector",attributes,functionsName);
        ReadFileCSV read = new ReadFileCSV();
        if (LastLine != null) {
            String[] twoActivityLines = read.ReadActivity(LastLine);
            while (twoActivityLines != null && twoActivityLines[1] != null) { //while not EOF || file is not empty
                LastLine = twoActivityLines[1];
                String[][] action = new String[2][];
                action[0] = twoActivityLines[0].split(",");
                action[1] = twoActivityLines[1].split(",");
                dv.GetDataFromSensors(action[0][3], action[0][1], action[1][1]);
                Vector<Double> ProcessedData = dv.getFinalVector(Functions);
                if (ProcessedData.size() != 0) {
                    fm.writeInternalFileCsvNewLINE(String.valueOf(ProcessedData.elementAt(0)), true);
                    for (int i = 1; i < ProcessedData.size(); i++) {
                        fm.writeInternalFileCsvSameLine(String.valueOf(ProcessedData.elementAt(i)), true);
                    }
                    fm.writeInternalFileCsvSameLine(action[1][7], true);
                }
                twoActivityLines = read.ReadActivity(LastLine);
            }
        }
    }
}
