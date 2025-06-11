package com.example.pmdm_sensores2;

import static java.time.LocalDateTime.now;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private SensorManager sensorManager; // Gestor de sensores
    private Sensor acelerometro; // Objeto sensor a medir
    private Sensor luxometro;
    private View layout;
    private TextView ejeX, ejeY, ejeZ, time;
    private Button start, stop;
    private final String LOGTAG = "PMDM";

    // Variables para guardar los últimos valores del acelerómetro y la hora
    private float ultimoX = 0, ultimoY = 0, ultimoZ = 0;
    private String horaActual = "";
    private final float UMBRAL_LUZ_BAJA = 10f;
    private FileOutputStream fos;
    private boolean midiendo = false;
    private String horaInicio = "";
    private String horaFin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Objetos del layout
        layout = findViewById(R.id.layout);
        ejeX = findViewById(R.id.ejeX);
        ejeY = findViewById(R.id.ejeY);
        ejeZ = findViewById(R.id.ejeZ);
        time = findViewById(R.id.time);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);

        // Asociación de eventos escuchadores
        start.setOnClickListener(this);
        stop.setOnClickListener(this);

        // Restaurar estado si existe
        if (savedInstanceState != null) {
            ultimoX = savedInstanceState.getFloat("x", 0);
            ultimoY = savedInstanceState.getFloat("y", 0);
            ultimoZ = savedInstanceState.getFloat("z", 0);
            horaActual = savedInstanceState.getString("hora", "");
            ejeX.setText(formatearValor(ultimoX));
            ejeY.setText(formatearValor(ultimoY));
            ejeZ.setText(formatearValor(ultimoZ));
            time.setText(horaActual);
        } else {
            ejeX.setText("Eje X");
            ejeY.setText("Eje Y");
            ejeZ.setText("Eje Z");
            time.setText("HH:MM:SS");
        }

        // Inicio por defecto: desactiva el sensor
        stop();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start) {
            start();
        } else if (view.getId() == R.id.stop) {
            stop();
        }
    }

    // Informa el sensor de un valor nuevo recogido
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ultimoX = event.values[0];
            ultimoY = event.values[1];
            ultimoZ = event.values[2];
            horaActual = hora();

            ejeX.setText(formatearValor(ultimoX));
            ejeY.setText(formatearValor(ultimoY));
            ejeZ.setText(formatearValor(ultimoZ));
            time.setText(horaActual);
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lux = event.values[0];
            if (lux < UMBRAL_LUZ_BAJA) {
                layout.setBackgroundColor(Color.BLACK);
                ejeX.setTextColor(Color.WHITE);
                ejeY.setTextColor(Color.WHITE);
                ejeZ.setTextColor(Color.WHITE);
                time.setTextColor(Color.WHITE);
            } else {
                layout.setBackgroundColor(Color.WHITE);
                ejeX.setTextColor(Color.BLACK);
                ejeY.setTextColor(Color.BLACK);
                ejeZ.setTextColor(Color.BLACK);
                time.setTextColor(Color.BLACK);
            }
        }
    }

    // Método para definir el cambio de exactitud y precisión de un sensor
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Puedes mostrar la precisión si lo deseas, pero normalmente no es necesario
        // Por ejemplo: Log.d(LOGTAG, "Precisión cambiada: " + accuracy);
        Log.d(LOGTAG, "Precisión cambiada: " + accuracy);
    }

    // Cancela el registro de los objetos de escucha del sensor cuando se ha terminado de usar el sensor o cuando se detenga la actividad del sensor
    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    // Guardar el estado actual al rotar pantalla o destruir actividad
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("x", ultimoX);
        outState.putFloat("y", ultimoY);
        outState.putFloat("z", ultimoZ);
        outState.putString("hora", horaActual);
    }

    // Inicia el registro del sensor
    public void start() {
        if (sensorManager == null) {
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        }
        if (sensorManager != null) {
            acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            luxometro = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

            if (acelerometro != null) {
                sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                ejeX.setText("No disponible");
                ejeY.setText("No disponible");
                ejeZ.setText("No disponible");
                time.setText("Sin sensor");
            }
            if (luxometro != null) {
                sensorManager.registerListener(this, luxometro, SensorManager.SENSOR_DELAY_NORMAL);
            }
            start.setEnabled(false);
            stop.setEnabled(true);

            // Guardar hora de inicio y abrir fichero
            horaInicio = hora();
            midiendo = true;
            try {
                fos = openFileOutput("sensores.txt", MODE_PRIVATE);
                String inicio = "Hora de inicio: " + horaInicio + "\n";
                fos.write(inicio.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        start.setEnabled(true);
        stop.setEnabled(false);

        // Guardar hora de fin y cerrar fichero
        if (midiendo && fos != null) {
            horaFin = hora();
            String fin = "Hora de fin: " + horaFin + "\n";
            try {
                fos.write(fin.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            midiendo = false;
        }
    }

    public String formatearValor(float num) {
        return String.valueOf(Math.floor(num * 100) / 100);
    }

    public String hora() {
        return now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
