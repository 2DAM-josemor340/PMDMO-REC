package iesmm.pmdm.simonapp;

import java.util.ArrayList;
import java.util.Random;

public class SimonGame {
    private final ArrayList<Integer> sequence = new ArrayList<>();
    private int score = 0;

    public void resetGame() {
        sequence.clear();
        score = 0;
    }

    public void nextStep() {
        sequence.add(new Random().nextInt(4)); // 0-red, 1-blue, 2-green, 3-yellow
    }

    public ArrayList<Integer> getSequence() {
        return sequence;
    }

    public boolean checkUserSequence(ArrayList<Integer> userInput) {
        return userInput.equals(sequence);
    }

    public void incrementScore() {
        score++;
    }

    public int getScore() {
        return score;
    }
}


