package com.example.myplayerwear;

import android.content.Context;


public class ManageOfSensors {

    public static ManageOfSensors MOS;
    private Context context;
    private HeartRate H;
    private StepCounter SC;
    private Accelerometer AC;
    private Gravity gravity;
    private MagneticField magneticField;
    private Orientation orientation;
    private Pressure pressure;
    private RotationVector rotationVector;

    public static ManageOfSensors getInstance(Context context) {
        if (MOS == null) {
            MOS = new ManageOfSensors(context.getApplicationContext());
        }
        return MOS;
    }

    public ManageOfSensors(Context context) {
        this.context = context;
        AC = new Accelerometer(context);
        H = new HeartRate(context);
        SC = new StepCounter(context);
        gravity = new Gravity(context);
        magneticField = new MagneticField(context);
        orientation = new Orientation(context);
        pressure = new Pressure(context);
        rotationVector = new RotationVector(context);


    }

//ALL SENSORS

    public void StartAllSensors() {
        StartStepCounter();
        StartAccelerometer();
        H.startMeasurement();
        StartGravity();
        StartMagneticField();
        StartOrientation();
        StartPressure();
        StartRotationVector();
    }

    public void StopAllSensors() {
        StopStepCounter();
        StopAccelerometer();
        H.stopMeasurement();
        StopGravity();
        StopMagneticField();
        StopOrientation();
        StopPressure();
        StopRotationVector();
    }

    public void StartStepCounter() {
        SC.startMeasurement();
    }

    public void StopStepCounter() {
        SC.stopMeasurement();
    }

    public void StartAccelerometer() {
        AC.startMeasurement();
    }

    public void StopAccelerometer() {
        AC.stopMeasurement();
    }

    public void StartHeartrRate() {
        H.startMeasurement();
    }

    public void StopHeartrRate() {
        H.stopMeasurement();
    }

    public void StartGravity() {
        gravity.startMeasurement();
    }

    public void StopGravity() {
        gravity.stopMeasurement();
    }


    public void StartMagneticField() {
        magneticField.startMeasurement();
    }

    public void StopMagneticField() {
        magneticField.stopMeasurement();
    }

    public void StartOrientation() {
        orientation.startMeasurement();
    }

    public void StopOrientation() {
        orientation.stopMeasurement();
    }

    public void StartPressure() {
        pressure.startMeasurement();
    }

    public void StopPressure() {
        pressure.stopMeasurement();
    }

    public void StartRotationVector() {
        rotationVector.startMeasurement();
    }

    public void StopRotationVector() {
        rotationVector.stopMeasurement();
    }


}
