package com.example.myplayerwear;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.support.wear.widget.WearableRecyclerView;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private SendToPhone sendToPhone;
    private ManageOfSensors manageOfSensors;
    private WearableRecyclerView mWearableRecyclerView;

    private static TextView accelerometer_x_Text, accelerometer_y_Text, accelerometer_z_Text,
            heartRateText, stepCounterText, gravity_x_Text, gravity_y_Text, gravity_z_Text,
            magneticField_x_Text, magneticField_y_Text, magneticField_z_Text,
            orientation_x_Text, orientation_y_Text, orientation_z_Text,
            pressureText, rotationVector_x_Text, rotationVector_y_Text, rotationVector_z_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendToPhone = SendToPhone.getInstance(this);
        manageOfSensors = ManageOfSensors.getInstance(this);

        mWearableRecyclerView = findViewById(R.id.recycler_view);
        mWearableRecyclerView.setEdgeItemsCenteringEnabled(true);
        mWearableRecyclerView.setFocusable(false);

        // Checking for permission
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck == 0) {
            manageOfSensors = ManageOfSensors.getInstance(this);
            initialization();
        }

        // Checks whether there was a request for permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission denied
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            // Requesting permission
            else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BODY_SENSORS,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the result of the request.
            }
        }
    }

    /**
     * Getting approval for permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            // READ_EXTERNAL_STORAGE
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED //BODY_SENSORS
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED //BODY_SENSORS
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED)//ACCESS_COARSE_LOCATION
                {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    initialization();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    private void initialization() {
        heartRateText = findViewById(R.id.heartRateText);
        //startHeartRate();

        accelerometer_x_Text = findViewById(R.id.accelerometer_x);
        accelerometer_y_Text = findViewById(R.id.accelerometer_y);
        accelerometer_z_Text = findViewById(R.id.accelerometer_z);
        //startAccelerometer();

        stepCounterText = findViewById(R.id.stepCounterText);
        //startStepCounter();

        gravity_x_Text = findViewById(R.id.gravity_x);
        gravity_y_Text = findViewById(R.id.gravity_y);
        gravity_z_Text = findViewById(R.id.gravity_z);
        //startGravity();

        magneticField_x_Text = findViewById(R.id.magneticField_x);
        magneticField_y_Text = findViewById(R.id.magneticField_y);
        magneticField_z_Text = findViewById(R.id.magneticField_z);
        //startMagneticField();

        orientation_x_Text = findViewById(R.id.orientation_x);
        orientation_y_Text = findViewById(R.id.orientation_y);
        orientation_z_Text = findViewById(R.id.orientation_z);
        //startOrientation();

        pressureText = findViewById(R.id.pressureText);
        //startPressure();

        rotationVector_x_Text = findViewById(R.id.rotationVector_x);
        rotationVector_y_Text = findViewById(R.id.rotationVector_y);
        rotationVector_z_Text = findViewById(R.id.rotationVector_z);
        //startRotationVector();
    }

    public void play(View view) {
        sendToPhone.sendMessage("musicPlayer", "play");
    }

    public void next(View view) {
        sendToPhone.sendMessage("musicPlayer", "next");
    }

    public void forward(View view) {
        sendToPhone.sendMessage("musicPlayer", "forward");
    }

    public void backward(View view) {
        sendToPhone.sendMessage("musicPlayer", "backward");
    }

    public void previous(View view) {
        sendToPhone.sendMessage("musicPlayer", "previous");
    }


    public void startStepCounter() {
        manageOfSensors.StartStepCounter();
    }

    public void startAccelerometer() {
        manageOfSensors.StartAccelerometer();
    }

    public void startHeartRate() {
        manageOfSensors.StartHeartrRate();
    }

    public void startGravity() {
        manageOfSensors.StartGravity();
    }

    public void startMagneticField() {
        manageOfSensors.StartMagneticField();
    }

    public void startOrientation() {
        manageOfSensors.StartOrientation();
    }

    public void startPressure() {
        manageOfSensors.StartPressure();
    }

    public void startRotationVector() {
        manageOfSensors.StartRotationVector();
    }

    public static void print(String sensor, SensorEvent event) {
        switch (sensor) {
            case "Accelerometer":
                accelerometer_x_Text.setText("x: " + (int) event.values[0]);
                accelerometer_y_Text.setText("y: " + (int) event.values[1]);
                accelerometer_z_Text.setText("z: " + (int) event.values[2]);
                break;
            case "HeartRate":
                String heartRateValue = "" + (int) event.values[0];
                heartRateText.setText(heartRateValue);
                break;
            case "StepCounter":
                String stepCounterValue = "" + (int) event.values[0];
                stepCounterText.setText(stepCounterValue);
                break;
            case "Gravity":
                gravity_x_Text.setText("x: " + (int) event.values[0]);
                gravity_y_Text.setText("y: " + (int) event.values[1]);
                gravity_z_Text.setText("z: " + (int) event.values[2]);
                break;
            case "MagneticField":
                magneticField_x_Text.setText("x: " + (int) event.values[0]);
                magneticField_y_Text.setText("y: " + (int) event.values[1]);
                magneticField_z_Text.setText("z: " + (int) event.values[2]);
                break;
            case "Orientation":
                orientation_x_Text.setText("x: " + (int) event.values[0]);
                orientation_y_Text.setText("y: " + (int) event.values[1]);
                orientation_z_Text.setText("z: " + (int) event.values[2]);
                break;
            case "Pressure":
                pressureText.setText("" + (int) event.values[0]);
                break;
            case "RotationVector":
                rotationVector_x_Text.setText("x: " + (int) event.values[0]);
                rotationVector_y_Text.setText("y: " + (int) event.values[1]);
                rotationVector_z_Text.setText("z: " + (int) event.values[2]);
                break;
        }
    }
}