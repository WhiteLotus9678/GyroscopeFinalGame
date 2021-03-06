package com.example.android.gyroscopefinalgame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class Accelerometer implements SensorEventListener {
    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private float currentX;

    public Accelerometer() {
        manager = (SensorManager)Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    // register all sensor types to the main Listener
    public void register() {
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    // unregister listener when game is paused
    public void pause() {
        manager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    // update current x based on sensor detecting changes
    public void onSensorChanged(SensorEvent event) {
        currentX = event.values[0];
    }

    // returns the current value of the x axis
    public float GetCurrentX()
    {
        return currentX;
    }
}