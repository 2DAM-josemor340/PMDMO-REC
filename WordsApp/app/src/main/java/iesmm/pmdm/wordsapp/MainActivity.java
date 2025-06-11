package iesmm.pmdm.wordsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText username = findViewById(R.id.username);
        Button btnStart = findViewById(R.id.button);
        EditText password = findViewById(R.id.password);

        File csvFile = new File(getFilesDir(), "words.csv");

        btnStart.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                int fallos = 0;
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split("@");
                    String[] partesInicio = partes[0].split(":");
                    String nombre = partesInicio[0];
                    String contraseña= partesInicio[1];
                    if (nombre.equals(user) && contraseña.equals(pass)) {
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.putExtra("username", user);
                        intent.putExtra("conjunto", partes[1]);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        fallos++;
                        if (fallos>=3){
                            btnStart.setEnabled(false);
                        }
                        return;
                    }
                }
                Toast.makeText(this, "Acceso concedido", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error leyendo usuarios", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
