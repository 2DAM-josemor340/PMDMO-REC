package iesmm.pmdm.bombapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText inputSegundos;
    private Button buttonBomba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputSegundos = findViewById(R.id.editSegundos);
        buttonBomba = findViewById(R.id.buttonComienzo);

        buttonBomba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputSegundos.getText().toString().trim();

                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Introduce un número de segundos", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int segundos = Integer.parseInt(input);

                    if (segundos < 1) {
                        Toast.makeText(MainActivity.this, "Debe ser al menos 1 segundo", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Guardar inicio en fichero
                    long startTime = System.currentTimeMillis();
                    FileUtils.saveStartInfo(MainActivity.this, segundos, new Date(startTime));

                    // Actualizar contador en preferencias
                    SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
                    int count = prefs.getInt("bomb_count", 0);
                    prefs.edit().putInt("bomb_count", count + 1).apply();

                    // Lanzar actividad de cuenta atrás
                    Intent intent = new Intent(MainActivity.this, CuentaAtrasActivity.class);
                    intent.putExtra("segundos", segundos);
                    startActivity(intent);

                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Por favor introduce un número válido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}