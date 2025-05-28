package iesmm.pmdm.appsemaforo;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Button btnRojo, btnVerde, btnAmarillo;

    private Handler handler;
    private int estado = 0;
    private int contVerde = 0;
    private int contRojo = 0;
    private int contAmarillo = 0;
    private int rojoConsecutivo = 0;

    private final Runnable cambioColor = new Runnable() {
        @Override
        public void run() {
            cambiarColor();
            handler.postDelayed(this, 10000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRojo = findViewById(R.id.btnRojo);
        btnVerde = findViewById(R.id.btnVerde);
        btnAmarillo = findViewById(R.id.btnAmarillo);

        handler = new Handler();
        handler.post(cambioColor);
    }

    private void cambiarColor() {
        //Apagar todos los botones
        btnRojo.setBackgroundColor(Color.BLACK);
        btnVerde.setBackgroundColor(Color.BLACK);
        btnAmarillo.setBackgroundColor(Color.BLACK);

        switch (estado) {
            case 0: //Verde
                btnVerde.setBackgroundColor(Color.GREEN);
                estado = 1;
                contVerde++;
                break;

            case 1: //Rojo
                btnRojo.setBackgroundColor(Color.RED);
                rojoConsecutivo++;
                contRojo++;
                estado = (rojoConsecutivo == 3) ? 2 : 0;
                break;

            case 2: //Amarillo
                btnAmarillo.setBackgroundColor(Color.YELLOW);
                rojoConsecutivo = 0;
                contAmarillo++;
                estado = 0;
                break;
        }

        guardarHistorial();
    }

    private void guardarHistorial() {
        String historial = "Verde: " + contVerde + "\nRojo: " + contRojo + "\nAmarillo: " + contAmarillo;
        try {
            FileOutputStream fos = openFileOutput("historial.txt", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(historial);
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}