package iesmm.pmdm.pmdm_t2_layout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.calendar_layout);
        setContentView(R.layout.linear_layout_vertical_4);

        /*asosciacion de eventos de escuchadores*/
        Button b = (Button) this.findViewById(R.id.boton_iniciar_sesion);
        b.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.boton_iniciar_sesion) {
            EditText usuario = (EditText) this.findViewById(R.id.input_usuario);
            EditText password = (EditText) this.findViewById(R.id.input_contrasena);

            String u = usuario.getText().toString();
            String p = password.getText().toString();


            if (!u.isEmpty() || !p.isEmpty()) {
                if (comprobarUsuario(u, p)) {
                    Toast.makeText(this, "Usuario correcto", Toast.LENGTH_SHORT).show();
                TextView conectado= (TextView) this.findViewById(R.id.texto_conectar);
                conectado.setText("CONECTADO");
                conectado.setTextColor(Color.RED);
                } else {
                    Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Usuario o contraseña vacios", Toast.LENGTH_SHORT).show();
            }
        }

        Toast.makeText(this, "Boton pulsado", Toast.LENGTH_SHORT).show();
    }

    private boolean comprobarUsuario(String usuario, String password) {
        return usuario.equals("admin") && password.equals("1234");
    }

}