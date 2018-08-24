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


public class SensorReceiverService extends WearableListenerService {

    private static final String TAG = "GETTING MESSAGE";
    private static final String TIMESTAMP = "timestamp";
    private static final String VALUES = "values";
    private static final String TYPE = "type";

    private DataReceiveManager dataReceiveManagerAcc = DataReceiveManager.getInstanceAcc(this);
    private DataReceiveManager dataReceiveManagerGravity = DataReceiveManager.getInstanceGravity(this);
    private DataReceiveManager dataReceiveManagerPressure = DataReceiveManager.getInstancePressure(this);
    private DataReceiveManager dataReceiveManagerHeartRate = DataReceiveManager.getInstanceHeartRate(this);
    private DataReceiveManager dataReceiveManagerStepCounter = DataReceiveManager.getInstanceStepCounter(this);
    private DataReceiveManager dataReceiveManagerOrientation = DataReceiveManager.getInstanceOrientation(this);
    private DataReceiveManager dataReceiveManagerMagneticField = DataReceiveManager.getInstanceMagneticField(this);
    private DataReceiveManager dataReceiveManagerRotationVector = DataReceiveManager.getInstanceRotationVector(this);

    private MessageReceiveManager MRM = MessageReceiveManager.getInstance(this);

/*    @Override
    public void onCreate() {
        super.onCreate();
     //   MRM = MessageReceiveManager.getInstanceGps(this);
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        //Log.i(TAG, "Connected: " + peer.getDisplayName() + " (" + peer.getId() + ")");
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        super.onPeerDisconnected(peer);

       // Log.i(TAG, "Disconnected: " + peer.getDisplayName() + " (" + peer.getId() + ")");
    }*/

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = dataEvent.getDataItem();
                Uri uri = dataItem.getUri();
                String path = uri.getPath();
                if (path.startsWith("/sensors/")) {
                    unpackSensorData(
                            Integer.parseInt(uri.getLastPathSegment()),
                            DataMapItem.fromDataItem(dataItem).getDataMap()
                    );
                }
            }
        }
    }

    private void unpackSensorData(int sensorType, DataMap dataMap) {
        String SensorTypeString = dataMap.getString(TYPE);
        long timestamp = dataMap.getLong(TIMESTAMP);
        float[] values = dataMap.getFloatArray(VALUES);
        if (Sensor.TYPE_ACCELEROMETER == sensorType) {
            dataReceiveManagerAcc.addSensorData(SensorTypeString, sensorType, timestamp, values);
        } else if (Sensor.TYPE_GRAVITY == sensorType) {
            dataReceiveManagerGravity.addSensorData(SensorTypeString, sensorType, timestamp, values);
        } else if (Sensor.TYPE_PRESSURE == sensorType) {
            dataReceiveManagerPressure.addSensorData(SensorTypeString, sensorType, timestamp, values);
        } else if (Sensor.TYPE_MAGNETIC_FIELD == sensorType) {
            dataReceiveManagerMagneticField.addSensorData(SensorTypeString, sensorType, timestamp, values);
        } else if (Sensor.TYPE_ORIENTATION == sensorType) {
            dataReceiveManagerOrientation.addSensorData(SensorTypeString, sensorType, timestamp, values);
        } else if (Sensor.TYPE_ROTATION_VECTOR == sensorType) {
            dataReceiveManagerRotationVector.addSensorData(SensorTypeString, sensorType, timestamp, values);
        } else if (Sensor.TYPE_HEART_RATE == sensorType) {
            dataReceiveManagerHeartRate.addSensorData(SensorTypeString, sensorType, timestamp, values);
        } else if (Sensor.TYPE_STEP_COUNTER == sensorType) {
            dataReceiveManagerStepCounter.addSensorData(SensorTypeString, sensorType, timestamp, values);
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d(TAG, "getting message. Path : " + messageEvent.getPath() + " , Data : " + new String(messageEvent.getData()));
        if (messageEvent.getPath().equals("Player")) {
            MRM.MessageReceive(messageEvent);

        }
        if (messageEvent.getPath().equals("Data_Shoe_Click")) {
            Intent i = new Intent(getApplicationContext(), DataShow.class);
            startActivity(i);
        }
    }
}