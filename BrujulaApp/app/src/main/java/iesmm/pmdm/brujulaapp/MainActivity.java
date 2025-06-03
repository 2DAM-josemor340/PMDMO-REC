package iesmm.pmdm.brujulaapp;

import android.app.Activity;
import android.hardware.*;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;



public class MainActivity extends Activity implements SensorEventListener {

    private ImageView brujulaImage;
    private TextView directionText;
    private float currentDegree = 0f;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] gravity;
    private float[] geomagnetic;
    private Handler handler = new Handler();
    private Medidor medidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        brujulaImage = findViewById(R.id.brujula_image);
        directionText = findViewById(R.id.direction_text);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        medidor = new Medidor(this);
        handler.postDelayed(medidor, 10000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        handler.removeCallbacks(medidor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravity = lowPass(event.values.clone(), gravity, alpha);

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = lowPass(event.values.clone(), geomagnetic, alpha);

        if (gravity != null && geomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                float azimuthInRadians = orientation[0];
                float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);
                azimuthInDegrees = (azimuthInDegrees + 360) % 360;

                // Actualiza el compás
                RotateAnimation ra = new RotateAnimation(currentDegree, -azimuthInDegrees,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setDuration(250);
                ra.setFillAfter(true);
                brujulaImage.startAnimation(ra);
                currentDegree = -azimuthInDegrees;

                String cardinal = getCardinalDirection(azimuthInDegrees);
                directionText.setText(cardinal + " (" + (int) azimuthInDegrees + "º)");

                // Actualiza el valor en Medidor
                medidor.setCurrentCardinal(cardinal);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private float[] lowPass(float[] input, float[] output, float alpha) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++)
            output[i] = alpha * output[i] + (1 - alpha) * input[i];
        return output;
    }

    private String getCardinalDirection(float azimuth) {
        if (azimuth >= 337.5 || azimuth < 22.5) return "NORTE";
        if (azimuth >= 22.5 && azimuth < 67.5) return "NORESTE";
        if (azimuth >= 67.5 && azimuth < 112.5) return "ESTE";
        if (azimuth >= 112.5 && azimuth < 157.5) return "SURESTE";
        if (azimuth >= 157.5 && azimuth < 202.5) return "SUR";
        if (azimuth >= 202.5 && azimuth < 247.5) return "SUROESTE";
        if (azimuth >= 247.5 && azimuth < 292.5) return "OESTE";
        if (azimuth >= 292.5 && azimuth < 337.5) return "NOROESTE";
        return "?";
    }
}
