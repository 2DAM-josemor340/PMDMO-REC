package iesmm.pmdm.bombapp;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class FileUtils {
    public static void saveStartInfo(Context context, int seconds, Date startTime) {
        try {
            File file = new File(context.getFilesDir(), "bomba_log.txt");
            FileWriter writer = new FileWriter(file, true);
            writer.write("Inicio: " + startTime.toString() + " - Duración: " + seconds + " segundos\n");
            writer.close();
        } catch (IOException e) {
            Log.e("BombApp", "Error al guardar archivo", e);
        }
    }
}
