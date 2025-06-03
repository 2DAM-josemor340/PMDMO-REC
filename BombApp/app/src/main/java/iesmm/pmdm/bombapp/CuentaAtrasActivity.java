package iesmm.pmdm.bombapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CuentaAtrasActivity extends AppCompatActivity {

    private TextView cuentaAtrasText;
    private ImageView bombaImagen;
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_atras);

        cuentaAtrasText = findViewById(R.id.textCuentaAtras);
        bombaImagen = findViewById(R.id.imagenBomba);

        // Recibir segundos de la otra actividad
        int segundos = getIntent().getIntExtra("segundos", 0);
        if (segundos <= 0) {
            Toast.makeText(this, "Error en el valor recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Actualizar SharedPreferences (contador de bombas)
        SharedPreferences prefs = getSharedPreferences("BombAppPrefs", Context.MODE_PRIVATE);
        int totalBombas = prefs.getInt("totalBombas", 0) + 1;
        prefs.edit().putInt("totalBombas", totalBombas).apply();

        // Iniciar cuenta atrás
        iniciarCuentaAtras(segundos);
    }

    private void iniciarCuentaAtras(int segundos) {
        countDownTimer = new CountDownTimer(segundos * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int segundosRestantes = (int) (millisUntilFinished / 1000);
                cuentaAtrasText.setText(String.valueOf(segundosRestantes));
            }

            @Override
            public void onFinish() {
                cuentaAtrasText.setText("¡¡¡¡BOOM!!!!");
                bombaImagen.setImageResource(R.drawable.explosion);
                reproducirSonido();
            }
        }.start();
    }

    private void reproducirSonido() {
        mediaPlayer = MediaPlayer.create(this, R.raw.explosion);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
        });
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
        if (mediaPlayer != null) mediaPlayer.release();
    }
}
