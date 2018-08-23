package metaextract.nkm.com.myplayer;

import java.io.*;
import java.util.Vector;

public class ActivityReader {

    private String [] LastLine = null;

    public ActivityReader(String path){
        File input = new File(path);
        try {
            FileReader fr = new FileReader(input);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            br.readLine();
            br.readLine();
            LastLine = br.readLine().split(",");
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ActivityReader(String Time, String Date){
        ReadFileCSV read = new ReadFileCSV();
        String [][] action = read.ReadActivity(Date,Time);
        if(action != null)LastLine = action[0];
        else LastLine = null;
    }


    //iterate over all the lines in activity.csv, and extract vectors. keep the last line
    public void extractData(){
        FileManager fm = new FileManager("DataVector",true);
        DataVector dv = new DataVector();
        ReadFileCSV read = new ReadFileCSV();
        String [][] action = read.ReadActivity(LastLine[0],LastLine[1]);
        while(action != null){ //while not EOF
            LastLine = action[1];
            dv.GetDataFromSensors(action[0][3],action[0][1],action[1][1],action[0][0]);
            Vector<Double> ProcessedData = dv.getFinalVector();
            if (ProcessedData.size() != 0){
                fm.writeInternalFileCsvNewLINE(String.valueOf(ProcessedData.elementAt(0)),true);
                for (int i = 1; i < ProcessedData.size();i++ ) {
                    fm.writeInternalFileCsvSameLine(String.valueOf(ProcessedData.elementAt(i)),true);
                }
                fm.writeInternalFileCsvSameLine(action[1][7],true);
            }
            System.out.println();
            action = read.ReadActivity(LastLine[0],LastLine[1]);
        }
    }

}
