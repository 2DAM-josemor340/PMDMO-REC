package iesmm.pmdm.t2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private final String LOGTAG = "PMDMO";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
        Log.d(LOGTAG, "Se ha creado la actividad");


    }
    @Override
    protected void onStart() {
        Log.d(LOGTAG, "Se ha iniciado la actividad");
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        Log.d(LOGTAG, "Se ha ejecuta la actividad");
        super.onPostResume();
    }
    @Override
    protected void onPause() {
        Log.d(LOGTAG, "Se ha pausado la actividad");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOGTAG, "Se ha parado la actividad");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOGTAG, "Se ha destruido la actividad");
        super.onDestroy();
    }
}