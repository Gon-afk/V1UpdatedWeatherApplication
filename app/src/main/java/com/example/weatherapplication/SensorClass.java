package com.example.weatherapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class SensorClass extends AppCompatActivity implements SensorEventListener {

    SensorManager mSensorManager;
    List<Sensor> sensorList;
    ListView lv;
    Sensor mSensorProximity;
    TextView mTextSensorProximity;
    TextView mTextSteps;
    private float initialStepCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sensor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lv = findViewById(R.id.list_view);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder sensorText = new StringBuilder();

        for (Sensor currentSensor : sensorList ) {
            sensorText.append(currentSensor.getName()).append(
                    System.lineSeparator());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getSensorNames(sensorList)
        );

        lv.setAdapter(adapter);

        mTextSensorProximity = (TextView) findViewById(R.id.proximity_txt);

        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        mTextSteps = (TextView) findViewById(R.id.steps);


    }

    private List<String> getSensorNames(List<Sensor> sensors) {
        List<String> sensorNames = new ArrayList<>();
        for (Sensor sensor : sensors) {
            sensorNames.add(sensor.getName());
        }
        return sensorNames;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            float distance = sensorEvent.values[0];

            mTextSensorProximity.setText("Proximity Sensor Value: " + distance + " cm");
        }
            // Step counter value from the sensor
            float stepCount = sensorEvent.values[0];

            if (initialStepCount == 0) {
                initialStepCount = stepCount;  // Store the initial value to show steps taken since the app started
            }
            Toast.makeText(this,"Steps: "+ (stepCount - initialStepCount), Toast.LENGTH_LONG).show();            // Display the number of steps taken since the app started
            mTextSteps.setText("Steps: " + (stepCount - initialStepCount));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mSensorProximity != null) {
            mSensorManager.registerListener(this, mSensorProximity,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }
}