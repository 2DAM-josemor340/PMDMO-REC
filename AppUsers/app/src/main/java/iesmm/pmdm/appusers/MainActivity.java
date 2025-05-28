package iesmm.pmdm.appusers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private Button btnAcceder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnAcceder = findViewById(R.id.btnAcceder);

        btnAcceder.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            File file = new File(getFilesDir(), "users.csv");
            if (!file.exists()) {
                Toast.makeText(this, "Archivo de usuarios no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String linea;
                boolean encontrado = false;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(";");
                    if (partes.length >= 3) {
                        String user = partes[0].trim();
                        String pass = partes[1].trim();
                        String tipo = partes[2].trim();

                        //Comprobacion si el usuario y la contraseña coinciden

                        if (usuario.equals(user) && password.equals(pass)) {
                            encontrado = true;
                            if (tipo.equalsIgnoreCase("administrador")) {
                                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                            } else if (tipo.equalsIgnoreCase("cliente")) {
                                Intent intent = new Intent(MainActivity.this, ClienteActivity.class);
                                intent.putExtra("usuario", usuario);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Tipo de usuario no reconocido", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    }
                }

                if (!encontrado) {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al leer el archivo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}