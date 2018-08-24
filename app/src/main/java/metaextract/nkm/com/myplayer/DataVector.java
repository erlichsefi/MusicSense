package metaextract.nkm.com.myplayer;

import java.io.File;
import java.util.Vector;

import static metaextract.nkm.com.myplayer.FileManager.getPublicPicturesDirectory;

/*
This class uses ReadFileCsv to receive vector with data from a specific point in time.
using this, we can calculate different parameters from different sensors.
 */
public class DataVector {
    Vector<Vector<Double>> Datavec = new Vector<>();

    public void GetDataFromSensors(String SongName, String StartTime, String FinishTime, String Date) {
        File f = getPublicPicturesDirectory("Log");
        String path = f.getPath();
        String orientation = path + "/" + SongName + "_Orientation.csv";
        String gravity = path + "/" + SongName + "_Gravity.csv";
        String accelerometer = path + "/" + SongName + "_ACC.csv";
        String magneticField = path + "/" + SongName + "_MagneticField.csv";
        String pressure = path + SongName + "/" + "_Pressure.csv";
        String rotationVector = path + "/" + SongName + "_RotationVector.csv";
        String heartRate = path + "/" + SongName + ".csv";

        ReadFileCSV read = new ReadFileCSV();

        Datavec = new Vector<>();
        //orientation_x
        Datavec.add(read.ReadSensor(orientation, 2, StartTime, FinishTime, Date));
        //orientation_y
        Datavec.add(read.ReadSensor(orientation, 3, StartTime, FinishTime, Date));
        //orientation_z
        Datavec.add(read.ReadSensor(orientation, 4, StartTime, FinishTime, Date));
        //gravity_x
        Datavec.add(read.ReadSensor(gravity, 2, StartTime, FinishTime, Date));
        //gravity_y
        Datavec.add(read.ReadSensor(gravity, 3, StartTime, FinishTime, Date));
        //gravity_z
        Datavec.add(read.ReadSensor(gravity, 4, StartTime, FinishTime, Date));
        //accelerometer_x
        Datavec.add(read.ReadSensor(accelerometer, 2, StartTime, FinishTime, Date));
        //accelerometer_y
        Datavec.add(read.ReadSensor(accelerometer, 3, StartTime, FinishTime, Date));
        //accelerometer_z
        Datavec.add(read.ReadSensor(accelerometer, 4, StartTime, FinishTime, Date));
        //magneticField_x
        Datavec.add(read.ReadSensor(magneticField, 2, StartTime, FinishTime, Date));
        //magneticField_y
        Datavec.add(read.ReadSensor(magneticField, 3, StartTime, FinishTime, Date));
        //magneticField_z
        Datavec.add(read.ReadSensor(magneticField, 4, StartTime, FinishTime, Date));
        //pressure
        Datavec.add(read.ReadSensor(pressure, 2, StartTime, FinishTime, Date));
        //rotationVector_1
        Datavec.add(read.ReadSensor(rotationVector, 2, StartTime, FinishTime, Date));
        //rotationVector_2
        Datavec.add(read.ReadSensor(rotationVector, 3, StartTime, FinishTime, Date));
        //rotationVector_3
        Datavec.add(read.ReadSensor(rotationVector, 4, StartTime, FinishTime, Date));
        //rotationVector_4
        Datavec.add(read.ReadSensor(rotationVector, 5, StartTime, FinishTime, Date));
        //rotationVector_5
        Datavec.add(read.ReadSensor(rotationVector, 6, StartTime, FinishTime, Date));
        //heartRate_
        Datavec.add(read.ReadSensor(heartRate, 3, StartTime, FinishTime, Date));

    }

    public Vector<Double> getFinalVector() {
        ArithmeticFunctions arith = new ArithmeticFunctions();
        Vector<Double> res = new Vector<>();
        for (Vector<Double> v :
                Datavec) {
            if (v != null && v.size() != 0) res.add(arith.getAvg().Calculate(v));
            else res.add(null);
        }
        for (Vector<Double> v :
                Datavec) {
            if (v != null && v.size() != 0) res.add(arith.getSD().Calculate(v));
            else res.add(null);
        }
        return res;
    }
}
