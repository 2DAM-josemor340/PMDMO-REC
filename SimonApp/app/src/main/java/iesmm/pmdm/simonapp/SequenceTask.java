package iesmm.pmdm.simonapp;



import android.os.AsyncTask;
import android.widget.Button;

import java.util.List;

public class SequenceTask extends AsyncTask<Void, Integer, Void> {
    private final List<Integer> sequence;
    private final Button[] buttons;
    private final Runnable onFinish;

    public SequenceTask(List<Integer> sequence, Button[] buttons, Runnable onFinish) {
        this.sequence = sequence;
        this.buttons = buttons;
        this.onFinish = onFinish;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            for (int index : sequence) {
                publishProgress(index);
                Thread.sleep(700); // Encendido
                publishProgress(-1); // Apagado
                Thread.sleep(300);
            }
        } catch (InterruptedException ignored) {}
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int index = values[0];
        int[] colores = {0xFFFF0000, 0xFF0000FF, 0xFF00FF00, 0xFFFFFF00}; // rojo, azul, verde, amarillo

        if (index >= 0) {
            buttons[index].setBackgroundColor(colores[index]);
        } else {
            for (Button b : buttons) {
                b.setBackgroundColor(0xFF888888); // apagado
            }
        }
    }

    @Override
    protected void onPostExecute(Void unused) {
        onFinish.run();
    }
}
