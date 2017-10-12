package com.example.yzhan14.ibalance;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SensorEventListener{
    private final Handler myHandler = new Handler();

    //TODO: need handlers here?
    private Runnable r1;
    private Runnable r2;
    private Runnable r3;
    private GraphView graphView;
    //TODO: check
    private LineGraphSeries<DataPoint> series1;
    private LineGraphSeries<DataPoint> series2;
    private LineGraphSeries<DataPoint> series3;
    private double acclX = 0;
    private double acclY = 0;
    private double acclZ = 0;
    //TODO: linked this to time, since display time at which accl data changed
    private int data_count = 1;

    private SensorManager sensorManager;
    private Sensor sensor;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        graphView = (GraphView) rootView.findViewById(R.id.live);

        series1 = new LineGraphSeries<>(new DataPoint[]{});
        graphView.addSeries(series1);

        series2 = new LineGraphSeries<>(new DataPoint[]{});
        graphView.addSeries(series2);

        series3 = new LineGraphSeries<>(new DataPoint[]{});
        graphView.addSeries(series3);

        // set viewport, namely window for displaying data
        //TODO: customize
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(100);
        graphView.getViewport().setScalable(true);
        //graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalableY(true);
        //graphView.getViewport().setScrollableY(true);

        // sensor service for accelerometer data
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: or fastest
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO: more research on this
        // check whether sensor is reliable
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE){
            return;
        }

        acclX = event.values[0];
        acclY = event.values[1];
        acclZ = event.values[2];

        //how many data points you want to preserve
        //TODO: max datapoints?
        series1.appendData(new DataPoint(data_count, acclX),true,Integer.MAX_VALUE);
        series2.appendData(new DataPoint(data_count, acclY),true,Integer.MAX_VALUE);
        series3.appendData(new DataPoint(data_count, acclZ),true,Integer.MAX_VALUE);

        data_count ++;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    //TODO: do nothing for now
    }
}
