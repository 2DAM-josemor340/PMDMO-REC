package iesmm.pmdm.brujulaapp;



import android.content.Context;
import android.os.Handler;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Medidor implements Runnable {

    private Context context;
    private String currentCardinal = "N";
    private Handler handler = new Handler();

    public Medidor(Context context) {
        this.context = context;
    }

    public void setCurrentCardinal(String direction) {
        this.currentCardinal = direction;
    }

    @Override
    public void run() {
        try {
            String filename = "registro_brujula.csv";
            String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String line = currentCardinal + "@" + time + "\n";
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_APPEND);
            fos.write(line.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Repite cada 10 segundos
        handler.postDelayed(this, 10000);
    }
}
