package metaextract.nkm.com.myplayer;

import android.content.Intent;
import android.hardware.Sensor;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;


public class ReceiveSensors extends WearableListenerService {

    private static final String TAG = "GETTING MESSAGE";
    private static final String VALUES = "values";

    private ManageSensors manageSensorsAcc = ManageSensors.getInstanceAcc(this);
    private ManageSensors manageSensorsGravity = ManageSensors.getInstanceGravity(this);
    private ManageSensors manageSensorsPressure = ManageSensors.getInstancePressure(this);
    private ManageSensors manageSensorsHeartRate = ManageSensors.getInstanceHeartRate(this);
    private ManageSensors manageSensorsStepCounter = ManageSensors.getInstanceStepCounter(this);
    private ManageSensors manageSensorsOrientation = ManageSensors.getInstanceOrientation(this);
    private ManageSensors manageSensorsMagneticField = ManageSensors.getInstanceMagneticField(this);
    private ManageSensors manageSensorsRotationVector = ManageSensors.getInstanceRotationVector(this);

    private ButtonClickFromWear buttonClickFromWear = ButtonClickFromWear.getInstance(this);

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = dataEvent.getDataItem();
                Uri uri = dataItem.getUri();
                String path = uri.getPath();
                if (path.startsWith("/sensors/")) {
                    unpackSensorData(Integer.parseInt(uri.getLastPathSegment()), DataMapItem.fromDataItem(dataItem).getDataMap()
                    );
                }
            }
        }
    }

    private void unpackSensorData(int sensorType, DataMap dataMap) {
        float[] values = dataMap.getFloatArray(VALUES);
        if (Sensor.TYPE_ACCELEROMETER == sensorType) {
            manageSensorsAcc.addSensorData("Accelerometer", values);
        } else if (Sensor.TYPE_GRAVITY == sensorType) {
            manageSensorsGravity.addSensorData("Gravity", values);
        } else if (Sensor.TYPE_PRESSURE == sensorType) {
            manageSensorsPressure.addSensorData("Pressure", values);
        } else if (Sensor.TYPE_MAGNETIC_FIELD == sensorType) {
            manageSensorsMagneticField.addSensorData("MagneticField", values);
        } else if (Sensor.TYPE_ORIENTATION == sensorType) {
            manageSensorsOrientation.addSensorData("Orientation", values);
        } else if (Sensor.TYPE_ROTATION_VECTOR == sensorType) {
            manageSensorsRotationVector.addSensorData("RotationVector", values);
        } else if (Sensor.TYPE_HEART_RATE == sensorType) {
            manageSensorsHeartRate.addSensorData("HeartRate", values);
        } else if (Sensor.TYPE_STEP_COUNTER == sensorType) {
            manageSensorsStepCounter.addSensorData("StepCounter", values);
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d(TAG, "Getting message, path : " + messageEvent.getPath() + " , data : " + new String(messageEvent.getData()));
        if (messageEvent.getPath().equals("musicPlayer")) {
            buttonClickFromWear.MessageReceive(messageEvent);
        }
        if (messageEvent.getPath().equals("Data_Show_Click")) {
            Intent i = new Intent(getApplicationContext(), ShowPhoneGps.class);
            startActivity(i);
        }
    }
}