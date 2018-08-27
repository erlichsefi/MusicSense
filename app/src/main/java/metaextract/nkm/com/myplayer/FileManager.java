package metaextract.nkm.com.myplayer;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    private static final String TAG = "FileManager";
    protected File file;

    FileManager(String Filename, boolean append) {

        if (isExternalStorageWritable()) {
            Log.d(TAG, "External storage writable");
        } else {
            Log.d(TAG, "External storage not writable");
        }
        File outputStream = getPublicPicturesDirectory("Log");
        String filePath = outputStream.getPath();
        file = new File(filePath, Filename + ".csv");
        if (file.length() == 0) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, append));
                bw.write(String.valueOf(Filename + "\n"));
                bw.close();
                Log.d(TAG, "File created - " + Filename);
                // If we first time install the app, create head-line for the file.
                if (Filename.equals("Activity")) {
                    writeInternalFileCsvNewLINE("Date", true);
                    writeInternalFileCsvSameLine("Time", true);
                    writeInternalFileCsvSameLine("Song id", true);
                    writeInternalFileCsvSameLine("Full song name", true);
                    writeInternalFileCsvSameLine("Song length", true);
                    writeInternalFileCsvSameLine("Previous Song", true);
                    writeInternalFileCsvSameLine("Position in the SeekBar", true);
                    writeInternalFileCsvSameLine("Activity", true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //use this constructor to fill the header with values for each column.
    FileManager(String Filename ,String [] attribute, String [] functions){
        if (isExternalStorageWritable()) {
            Log.d(TAG, "External storage writable");
        } else {
            Log.d(TAG, "External storage not writable");
        }
        File outputStream = getPublicPicturesDirectory("Log");
        String filePath = outputStream.getPath();
        file = new File(filePath, Filename + ".csv");
        if(file.length() == 0) {
            String [] header = addFunctionsToAttributesNames(attribute,functions);
            for (String att :
                    header) {
                writeInternalFileCsvSameLine(att, true);
            }
        }
    }

    //gets 2 Strings Arrays and combines them together.
    private String[] addFunctionsToAttributesNames(String [] Attributes , String [] Functions){
        int counter = 0;
        String [] res = new String[(Attributes.length*Functions.length)+1];
        for(int i = 0; i < Functions.length; i++){
            for (String attribute :
                    Attributes) {
                res[counter++] = attribute + Functions[i];
            }
        }
        res [res.length-1] = "Activity";
        return res;
    }

    public void writeInternalFileCsvNewLINE(String content, boolean append) {
        try {
            FileWriter _file;
            _file = new FileWriter(file.getAbsoluteFile(), append);
            _file.append('\n');
            _file.append(content + ",");
            _file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeInternalFileCsvSameLine(String content, boolean append) {
        try {
            FileWriter _file;
            _file = new FileWriter(file.getAbsoluteFile(), append);
            _file.append(content + ",");
            _file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile() {
        return file.delete();
    }

    /**
     * We check to see if there is External Storage is External Storage Writable
     */
    private static boolean isExternalStorageWritable() {
        // We question the environment of the device ( Environment.______ )
        String state = Environment.getExternalStorageState();
        // Checks whether there is External Storage and return true or false
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    static File getPublicPicturesDirectory(String picturesFolder) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), picturesFolder);
        if (!file.mkdirs()) {
            Log.d(TAG, "Directory not created");
        }
        return file;
    }
}