package metaextract.nkm.com.myplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;

import java.util.ArrayList;
import java.util.Calendar;


public class DataReceiveManager {

    @SuppressLint("StaticFieldLeak")
    private static DataReceiveManager dataReceiveManagerHeartRate, dataReceiveManagerGps, dataReceiveManagerStepCounter,
            dataReceiveManagerAcc, dataReceiveManagerGravity, dataReceiveManagerPressure, dataReceiveManagerMagneticField,
            dataReceiveManagerOrientation, dataReceiveManagerRotationVector;

    private Context context;
    private FileManager fileManager;
    private Calendar cc = Calendar.getInstance();


    public static synchronized DataReceiveManager getInstanceHeartRate(Context context) {
        if (dataReceiveManagerHeartRate == null) {
            dataReceiveManagerHeartRate = new DataReceiveManager(context.getApplicationContext());
        }
        return dataReceiveManagerHeartRate;
    }

    public static synchronized DataReceiveManager getInstanceGps(Context context) {
        if (dataReceiveManagerGps == null) {
            dataReceiveManagerGps = new DataReceiveManager(context.getApplicationContext());
        }
        return dataReceiveManagerGps;
    }

    public static synchronized DataReceiveManager getInstanceStepCounter(Context context) {
        if (dataReceiveManagerStepCounter == null) {
            dataReceiveManagerStepCounter = new DataReceiveManager(context.getApplicationContext());
        }
        return dataReceiveManagerStepCounter;
    }

    public static synchronized DataReceiveManager getInstanceAcc(Context context) {
        if (dataReceiveManagerAcc == null) {
            dataReceiveManagerAcc = new DataReceiveManager(context.getApplicationContext());
        }
        return dataReceiveManagerAcc;
    }

    public static synchronized DataReceiveManager getInstanceGravity(Context context) {
        if (dataReceiveManagerGravity == null) {
            dataReceiveManagerGravity = new DataReceiveManager(context.getApplicationContext());
        }
        return dataReceiveManagerGravity;
    }

    public static synchronized DataReceiveManager getInstancePressure(Context context) {
        if (dataReceiveManagerPressure == null) {
            dataReceiveManagerPressure = new DataReceiveManager(context.getApplicationContext());
        }
        return dataReceiveManagerPressure;
    }

    public static synchronized DataReceiveManager getInstanceMagneticField(Context context) {
        if (dataReceiveManagerMagneticField == null) {
            dataReceiveManagerMagneticField = new DataReceiveManager(context.getApplicationContext());
        }
        return dataReceiveManagerMagneticField;
    }

    public static synchronized DataReceiveManager getInstanceOrientation(Context context) {
        if (dataReceiveManagerOrientation == null) {
            dataReceiveManagerOrientation = new DataReceiveManager(context.getApplicationContext());
        }
        return dataReceiveManagerOrientation;
    }

    public static synchronized DataReceiveManager getInstanceRotationVector(Context context) {
        if (dataReceiveManagerRotationVector == null) {
            dataReceiveManagerRotationVector = new DataReceiveManager(context.getApplicationContext());
        }
        return dataReceiveManagerRotationVector;
    }

    private DataReceiveManager(Context context) {
        this.context = context;
    }

    DataReceiveManager(Context context, String filename) {
        this.context = context;
        fileManager = new FileManager(filename, false);
    }

    public void setSongName(String songName) {
        fileManager = new FileManager(songName, false);
        new GPS(context);
    }

    /**
     * The function writes to file named SongList.
     *
     * @param songsList - song list.
     */
    public void addSongList(ArrayList<Song> songsList) {
        fileManager.deleteFile();
        int i = 1;
        fileManager.writeInternalFileCsvNewLINE("Date: " + getDateString(), true);
        for (Song song : songsList) {
            long id = song.getID();
            String title = song.getTitle();
            String artist = song.getArtist();
            String data = song.getdata();
            String album = song.getAlbum();
            String genre = song.getGenre();
            String Duration = song.getDuration();
            fileManager.writeInternalFileCsvNewLINE(Integer.toString(i), true);
            fileManager.writeInternalFileCsvSameLine(Long.toString(id), true);
            fileManager.writeInternalFileCsvSameLine(title, true);
            fileManager.writeInternalFileCsvSameLine(artist, true);
            fileManager.writeInternalFileCsvSameLine(album, true);
            fileManager.writeInternalFileCsvSameLine(Duration, true);
            fileManager.writeInternalFileCsvSameLine(genre, true);
            fileManager.writeInternalFileCsvSameLine(data, true);
            i++;
        }
    }

    /**
     * The function writes to file named Activity.
     *
     * @param currentSongName  - current song name.
     * @param currentSongIndex - id of the song.
     * @param timeOfSong       - duration of the song.
     * @param lastSongName     - last song that was played.
     * @param progress         - position of the last song in the SeekBar.
     * @param activity         - activity that was performed: play, stop etc.
     */
    public void ActivityFILE(String currentSongName, int currentSongIndex, String timeOfSong, String lastSongName, String progress, String activity) {
        fileManager.writeInternalFileCsvNewLINE(getDateString(), true);
        fileManager.writeInternalFileCsvSameLine(getTimeString(), true);
        fileManager.writeInternalFileCsvSameLine(Integer.toString(currentSongIndex), true);
        fileManager.writeInternalFileCsvSameLine(currentSongName, true);
        fileManager.writeInternalFileCsvSameLine(timeOfSong, true);
        fileManager.writeInternalFileCsvSameLine(lastSongName, true);
        fileManager.writeInternalFileCsvSameLine(progress, true);
        fileManager.writeInternalFileCsvSameLine(activity, true);
    }

    /**
     * The function writes to file named .
     *
     * @param SensorTypeString - sensor type.
     * @param sensorType       - integer that represents the sensor type.
     * @param timestamp        - time that the data of the sensor was collected.
     * @param values           - data of the sensor, because it is accelerometer we have 3 values (x,y,z).
     */
    public void addSensorData(String SensorTypeString, int sensorType, long timestamp, float[] values) {
        fileManager.writeInternalFileCsvNewLINE(getDateString(), true);
        fileManager.writeInternalFileCsvSameLine(getTimeString(), true);
        if (Sensor.TYPE_ACCELEROMETER == sensorType) {
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[0]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[1]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[2]), true);
            fileManager.writeInternalFileCsvSameLine(Long.toString(timestamp), true);
        } else if (Sensor.TYPE_GRAVITY == sensorType) {
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[0]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[1]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[2]), true);
            fileManager.writeInternalFileCsvSameLine(Long.toString(timestamp), true);
        } else if (Sensor.TYPE_MAGNETIC_FIELD == sensorType) {
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[0]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[1]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[2]), true);
            fileManager.writeInternalFileCsvSameLine(Long.toString(timestamp), true);
        } else if (Sensor.TYPE_ORIENTATION == sensorType) {
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[0]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[1]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[2]), true);
            fileManager.writeInternalFileCsvSameLine(Long.toString(timestamp), true);
        } else if (Sensor.TYPE_ROTATION_VECTOR == sensorType) {
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[0]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[1]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[2]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[3]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[4]), true);
            fileManager.writeInternalFileCsvSameLine(Long.toString(timestamp), true);
        } else if (Sensor.TYPE_PRESSURE == sensorType) {
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[0]), true);
            fileManager.writeInternalFileCsvSameLine(Long.toString(timestamp), true);

        } else if (SensorTypeString.equals("GPS")) {
            fileManager.writeInternalFileCsvSameLine(SensorTypeString, true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[0]), true);
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[1]), true);

        } else if (Sensor.TYPE_STEP_COUNTER == sensorType) {
            fileManager.writeInternalFileCsvSameLine(SensorTypeString, true);
        } else if (Sensor.TYPE_HEART_RATE == sensorType) {
            fileManager.writeInternalFileCsvSameLine(Float.toString(values[0]), true);
        }
    }

    @SuppressLint("DefaultLocale")
    private String getDateString() {
        return String.format("%02d/%02d/%d", cc.get(Calendar.DAY_OF_MONTH), cc.get(Calendar.MONTH) + 1, cc.get(Calendar.YEAR));
    }

    @SuppressLint("DefaultLocale")
    private String getTimeString() {
        return String.format("%02d:%02d:%d", cc.get(Calendar.HOUR_OF_DAY), cc.get(Calendar.MINUTE), cc.get(Calendar.SECOND));
    }
}