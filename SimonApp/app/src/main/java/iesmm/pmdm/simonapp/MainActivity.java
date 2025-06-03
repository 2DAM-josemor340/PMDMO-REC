package iesmm.pmdm.simonapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private Button[] buttons;
    private TextView txtScore;
    private TextView txtEstado;
    private SimonGame game;
    private ArrayList<Integer> userInput;
    private Handler handler;
    private TextToSpeech tts;
    private final int interactionTime = 10000; // tiempo para que el jugador termine

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new SimonGame();
        userInput = new ArrayList<>();
        handler = new Handler();

        txtScore = findViewById(R.id.tvScore);
        txtEstado = findViewById(R.id.tvEstado);

        buttons = new Button[]{
                findViewById(R.id.btnRojo),
                findViewById(R.id.btnAzul),
                findViewById(R.id.btnVerde),
                findViewById(R.id.btnAmarillo)
        };

        for (int i = 0; i < buttons.length; i++) {
            int index = i;
            buttons[i].setOnClickListener(v -> {
                if (txtEstado.getText().toString().contains("Jugador")) {
                    userInput.add(index);
                    if (!isCorrectSoFar()) {
                        tts.speak("¡Fallaste!", TextToSpeech.QUEUE_FLUSH, null, null);
                        game.resetGame();
                        txtScore.setText("Puntuación: 0");
                        startGame();
                        return;
                    }

                    // Solo cuando el jugador haya introducido toda la secuencia se verifica para avanzar
                    if (userInput.size() == game.getSequence().size()) {
                        game.incrementScore();
                        txtScore.setText("Puntuación: " + game.getScore());
                        txtEstado.setText("Turno: Máquina");
                        handler.postDelayed(this::startGame, 1000);
                    }
                }
            });
        }

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("es", "ES"));
                startGame();
            }
        });
    }

    private void startGame() {
        userInput.clear();
        game.nextStep();

        txtEstado.setText("Turno: Máquina");
        new SequenceTask(game.getSequence(), buttons, this::startUserInputPhase).execute();
    }

    private void startUserInputPhase() {
        txtEstado.setText("Turno: Jugador");
        userInput.clear();

        // Se quita el timeout o se puede implementar un timeout más largo si quieres
        handler.postDelayed(() -> {
            if (userInput.size() < game.getSequence().size()) {
                tts.speak("¡Muy lento!", TextToSpeech.QUEUE_FLUSH, null, null);
                game.resetGame();
                txtScore.setText("Puntuación: 0");
                startGame();
            }
        }, interactionTime);
    }

    private boolean isCorrectSoFar() {
        for (int i = 0; i < userInput.size(); i++) {
            if (!userInput.get(i).equals(game.getSequence().get(i))) {
                return false;
            }
        }
        return true;
    }
}
