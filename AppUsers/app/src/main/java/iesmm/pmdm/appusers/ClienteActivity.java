package iesmm.pmdm.appusers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClienteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        TextView tvBienvenida = findViewById(R.id.tvBienvenida);
        TextView tvFechaHora = findViewById(R.id.tvFechaHora);

        //Obtenemos el nombre del usuario que inició sesión
        String usuario = getIntent().getStringExtra("usuario");

        //Mostrar mensaje de bienvenida
        tvBienvenida.setText("Bienvenido, " + usuario);

        //Mostrar fecha y hora actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String fechaHoraActual = sdf.format(new Date());
        tvFechaHora.setText("Fecha y hora actual: " + fechaHoraActual);

        //Botón para salir de la aplicación
        Button btnSalir = findViewById(R.id.btnSalir);

        btnSalir.setOnClickListener(v -> {
            Intent intent = new Intent(ClienteActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}