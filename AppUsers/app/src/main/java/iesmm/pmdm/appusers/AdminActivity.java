package iesmm.pmdm.appusers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;

public class AdminActivity extends AppCompatActivity {
    private EditText etNuevoUsuario, etNuevaPassword;
    private Spinner spinnerTipo;
    private Button btnAgregar, btnMostrarTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        etNuevoUsuario = findViewById(R.id.etNuevoUsuario);
        etNuevaPassword = findViewById(R.id.etNuevaPassword);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        btnAgregar = findViewById(R.id.btnAnadir);
        btnMostrarTodos = findViewById(R.id.btnVerUsuarios);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipos_usuario, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> {
            String usuario = etNuevoUsuario.getText().toString().trim();
            String password = etNuevaPassword.getText().toString().trim();
            String tipo = spinnerTipo.getSelectedItem().toString();

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Campos vacíos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Verificar duplicado
                File file = new File(getFilesDir(), "users.csv");
                boolean duplicado = false;
                if (file.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        String[] partes = linea.split(";");
                        if (partes.length >= 1 && partes[0].equals(usuario)) {
                            duplicado = true;
                            break;
                        }
                    }
                    br.close();
                }

                if (duplicado) {
                    Toast.makeText(this, "Nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
                    return;
                }

                FileWriter fw = new FileWriter(file, true);
                fw.write(usuario + ";" + password + ";" + tipo + "\n");
                fw.close();

                Toast.makeText(this, "Usuario añadido", Toast.LENGTH_SHORT).show();
                etNuevoUsuario.setText("");
                etNuevaPassword.setText("");
                spinnerTipo.setSelection(0);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });
        Button btnSalir = findViewById(R.id.btnSalir);

        btnSalir.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnMostrarTodos.setOnClickListener(v -> {
            File file = new File(getFilesDir(), "users.csv");
            if (!file.exists()) {
                Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(";");
                    if (partes.length == 3) {
                        sb.append("Usuario: ").append(partes[0])
                                .append("\nContraseña: ").append(partes[1])
                                .append("\nTipo: ").append(partes[2])
                                .append("\n\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al leer el archivo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sb.length() == 0) {
                sb.append("No hay usuarios registrados.");
            }

            //Mostrar los datos en un AlertDialog
            new AlertDialog.Builder(this)
                    .setTitle("Lista de usuarios")
                    .setMessage(sb.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}
