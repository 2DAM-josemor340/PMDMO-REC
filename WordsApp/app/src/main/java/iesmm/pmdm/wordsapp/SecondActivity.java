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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        String username = getIntent().getStringExtra("username");
        String conjunto = getIntent().getStringExtra("conjunto");
        int numPalabras = 0;
        String[] palabras = conjunto.split(",");
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                numPalabras++;
            }
        }

        String palabraMasLarga = "";
        int maxLength = 0;
        for (String palabra : palabras) {
            if (palabra.length() > maxLength) {
                maxLength = palabra.length();
                palabraMasLarga = palabra;
            }
        }


        int numPalabrasConVocal = 0;
        for (String palabra : palabras) {
            if (palabra.length() > 0 && (palabra.charAt(palabra.length() - 1) == 'a' ||
                    palabra.charAt(palabra.length() - 1) == 'e' ||
                    palabra.charAt(palabra.length() - 1) == 'i' ||
                    palabra.charAt(palabra.length() - 1) == 'o' ||
                    palabra.charAt(palabra.length() - 1) == 'u')) {
                numPalabrasConVocal++;
            }
        }

        Toast.makeText(this,
                "Número de palabras: " + numPalabras + "\n" +
                "Palabra más larga: " + palabraMasLarga + "\n" +
                "Número de palabras que terminan en vocal: " + numPalabrasConVocal, Toast.LENGTH_LONG).show();

        EditText busqueda = findViewById(R.id.word);
        Button buscar = findViewById(R.id.botonBusqueda);
        Button coincidencias = findViewById(R.id.botonCoincidencias);

        buscar.setOnClickListener(v -> {
            String palabraBuscada = busqueda.getText().toString().trim();
            if (palabraBuscada.isEmpty()) {
                Toast.makeText(this, "Introduce una palabra para buscar", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean encontrada = false;
            for (String palabra : palabras) {
                if (palabra.equalsIgnoreCase(palabraBuscada)) {
                    encontrada = true;
                    break;
                }
            }

            if (encontrada) {
                Toast.makeText(this, "Existe en el conjunto la palabra: " + palabraBuscada, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No existe en el conjunto la palabra: " + palabraBuscada, Toast.LENGTH_SHORT).show();
            }
        });


        coincidencias.setOnClickListener(v -> {
            String palabraBuscada = busqueda.getText().toString().trim();
            if (palabraBuscada.isEmpty()) {
                Toast.makeText(this, "Introduce una letra para buscar", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder coincidenciasEncontradas = new StringBuilder();
            for (String palabra : palabras) {
                if (palabra.contains(palabraBuscada)) {
                    coincidenciasEncontradas.append(palabra).append("\n");
                }
            }

            if (coincidenciasEncontradas.length() > 0) {
                Toast.makeText(this, "Palabras que contienen '" + palabraBuscada + "':\n" + coincidenciasEncontradas.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "No se encontraron palabras que contenga: " + palabraBuscada, Toast.LENGTH_SHORT).show();
            }

        });


        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String ultimaBusqueda = username + "," + fechaHora + "," + busqueda.getText().toString().trim();
        getSharedPreferences("WordsAppPrefs", MODE_PRIVATE).edit().putString("ultima_busqueda", ultimaBusqueda).apply();


    }
}
