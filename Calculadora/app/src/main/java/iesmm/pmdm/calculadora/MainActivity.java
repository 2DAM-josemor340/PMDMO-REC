package iesmm.pmdm.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private EditText visor;
    private double valor1 = 0;
    private String operador = "";
    private boolean nuevoNumero = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        visor = findViewById(R.id.visor);

        // IDs de todos los botones que usaremos
        int[] ids = {
                R.id.numero0, R.id.numero1, R.id.numero2, R.id.numero3,
                R.id.numero4, R.id.numero5, R.id.numero6, R.id.numero7,
                R.id.numero8, R.id.numero9, R.id.sumar, R.id.resta,
                R.id.multiplica, R.id.divide, R.id.resultado, R.id.borrar
        };

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button boton = (Button) v;
                String valor = boton.getText().toString();
                // Agregar la operación al visor
                String operacion = "";

                switch (valor) {
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                        try {
                            valor1 = Double.parseDouble(visor.getText().toString());
                            operador = valor;
                            nuevoNumero = true;
                            operacion = valor;
                        } catch (NumberFormatException e) {
                            visor.setText("Error");
                        }
                        break;

                    case "=":
                        try {
                            double valor2 = Double.parseDouble(visor.getText().toString());
                            double resultado = calcularResultado(valor1, valor2, operador);
                            visor.setText(String.valueOf(resultado));
                            nuevoNumero = true;

                            // Guardar la operación y el resultado en el archivo
                            operacion = valor1 + " " + operador + " " + valor2 + " = " + resultado;
                            guardarOperacion(operacion);

                        } catch (NumberFormatException e) {
                            visor.setText("Error");
                        }
                        break;

                    case "CE":
                        visor.setText("0");
                        valor1 = 0;
                        operador = "";
                        nuevoNumero = true;
                        break;

                    default:
                        if (nuevoNumero) {
                            visor.setText(valor);
                            nuevoNumero = false;
                        } else {
                            visor.append(valor);
                        }
                        break;
                }

                // Guardar la operación a medida que se presionan los botones
                if (!operacion.isEmpty()) {
                    guardarOperacion(operacion);
                }
            }
        };

        // Asignar el listener a todos los botones
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private double calcularResultado(double a, double b, String op) {
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return (b != 0) ? a / b : 0;
            default:
                return 0;
        }
    }

    // Método para guardar la operación en la memoria interna
    private void guardarOperacion(String operacion) {
        try {
            // Abre el archivo en modo append (agregar al final)
            FileOutputStream fos = openFileOutput("operaciones.txt", MODE_APPEND);
            fos.write((operacion + "\n").getBytes());
            Toast.makeText(this, "Operación guardada correctamente", Toast.LENGTH_SHORT).show();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar la operación", Toast.LENGTH_SHORT).show();
        }
    }
}