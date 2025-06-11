package iesmm.pmdm.numberapp;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView numero;
    private ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        int nums = (int) (Math.random() * 1000) + 1;
        int min = 1;
        int max = 100;

        Toast.makeText(this, "Números que va a calcular: " + nums +
                " Intervalo a acertar: " + "("+min+"-"+max+")", Toast.LENGTH_SHORT).show();

        SecuenciaTask secuenciaTask = new SecuenciaTask();
        secuenciaTask.execute(nums, min, max);

    }

    private class SecuenciaTask extends AsyncTask<Integer, Integer, Void> {
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        int contAciertos = 0;
        @Override
        protected Void doInBackground(Integer... params) {
            int nums = params[0];
            int min = params[1];
            int max = params[2];

            for (int i = 0; i < nums; i++) {
                int randomNum = (int) (Math.random() * 500) + min;
                numero = findViewById(R.id.number);
                numero.setText(String.valueOf(randomNum));
                imagen = findViewById(R.id.imageView);
                if (randomNum >=min && randomNum <= max) {
                    imagen.setImageResource(R.drawable.yes);
                    contAciertos++;
                } else {
                    imagen.setImageResource(R.drawable.no);
                }
                publishProgress(randomNum);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MainActivity.this, "Se acabo. " +
                    "Aciertos de la máquina: " + contAciertos, Toast.LENGTH_LONG).show();
            ficheroHistorial(fechaHora, contAciertos);
        }

        private void ficheroHistorial(String fechaHora, int aciertos) {
            try {
                FileOutputStream fileOutputStream = openFileOutput("historial.txt", MODE_APPEND);
                fileOutputStream.write(("Fecha y hora en la que empezó la secuencia: " + fechaHora +
                        " - " + "Aciertos de la máquina: "+ aciertos + "\n").getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}