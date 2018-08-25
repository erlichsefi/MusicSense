package metaextract.nkm.com.myplayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import static metaextract.nkm.com.myplayer.FileManager.getPublicPicturesDirectory;

class ReadFileCSV {

    Vector<Double> ReadSensor(String FileName, int column, String StartingTime, String finishTime) {
        Vector<Double> data = new Vector<>();
        File input = new File(FileName);
        String Time;
        try {
            FileReader fr = new FileReader(input);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            br.readLine();
            String row = br.readLine();
            if (row == null) {
                br.close();
                fr.close();
                return null;
            }
            String[] split = row.split(",");
            if (split.length > 1 && split.length > column) {
                Time = split[1];
                String convertedStartingTime;
                String convertedFinishTime;
                String convertedCurrentTime;
                String[] split1 = StartingTime.split(":");
                convertedStartingTime = split1[0] + split1[1] + split1[2];
                String[] split2 = Time.split(":");
                convertedCurrentTime = split2[0] + split2[1] + split2[2];
                String[] split3 = finishTime.split(":");
                convertedFinishTime = split3[0] + split3[1] + split3[2];

                //find the correct line to start reading from
                while (Double.parseDouble(convertedCurrentTime) < Double.parseDouble(convertedStartingTime)) {
                    row = br.readLine();
                    if (row == null) {
                        br.close();
                        fr.close();
                        return null;
                    }
                    split = row.split(",");
                    if (split.length < 3) {
                        br.close();
                        fr.close();
                        return null;
                    }
                    Time = split[1];
                    split2 = Time.split(":");
                    if (split2.length < 3) {
                        break;
                    }
                    convertedCurrentTime = split2[0] + split2[1] + split2[2];
                }

                //start reading and pushing data to the vector until the finish time
                while (Double.parseDouble(convertedCurrentTime) <= Double.parseDouble(convertedFinishTime)) {
                    data.add(Double.parseDouble(split[column]));
                    row = br.readLine();
                    if (row == null) {
                        break;
                    }
                    split = row.split(",");
                    if (split.length < 3) {
                        break;
                    }
                    Time = split[1];
                    split2 = Time.split(":");
                    if (split2.length < 3) {
                        break;
                    }
                    convertedCurrentTime = split2[0] + split2[1] + split2[2];
                }


            }

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return data;
    }

    String[] ReadActivity(String rowToStart) {
        File f = getPublicPicturesDirectory("Log");
        File input = new File(f, "Activity.csv");
        String[] res = new String[2];
        try {
            FileReader fr = new FileReader(input);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            br.readLine();
            br.readLine();
            String row = br.readLine();
            while (!row.equals(rowToStart)) {
                row = br.readLine();
                if (row == null) return null;
            }

            res[0] = row;
            row = br.readLine();
            br.close();
            fr.close();
            if (row == null) {
                res[1] = null;
                return res;
            } else res[1] = row;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}