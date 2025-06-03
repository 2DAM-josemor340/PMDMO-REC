package iesmm.pmdm.appsemaforo2;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Button btnRojo, btnVerde, btnAmarillo;

    private int estado = 0;
    private int contVerde = 0;
    private int contRojo = 0;
    private int contAmarillo = 0;
    private int rojoConsecutivo = 0;

    private SemaforoTask tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRojo = findViewById(R.id.btnRojo);
        btnVerde = findViewById(R.id.btnVerde);
        btnAmarillo = findViewById(R.id.btnAmarillo);

        tarea = new SemaforoTask();
        tarea.execute();
    }

    private class SemaforoTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while (true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    break;
                }
                publishProgress(estado);
                estado = getNextState();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int current = values[0];

            // Apagar todos
            btnRojo.setBackgroundColor(Color.BLACK);
            btnVerde.setBackgroundColor(Color.BLACK);
            btnAmarillo.setBackgroundColor(Color.BLACK);

            switch (current) {
                case 0: // Verde
                    btnVerde.setBackgroundColor(Color.GREEN);
                    contVerde++;
                    break;
                case 1: // Rojo
                    btnRojo.setBackgroundColor(Color.RED);
                    contRojo++;
                    rojoConsecutivo++;
                    break;
                case 2: // Amarillo
                    btnAmarillo.setBackgroundColor(Color.YELLOW);
                    contAmarillo++;
                    rojoConsecutivo = 0;
                    break;
            }

            guardarHistorial();
        }
    }

    private int getNextState() {
        if (estado == 0) return 1;
        if (estado == 1) {
            if (rojoConsecutivo >= 2) return 2;
            else return 0;
        }
        return 0; // después de amarillo va verde
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
