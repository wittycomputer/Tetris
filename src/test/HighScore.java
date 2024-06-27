package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighScore {
    private ArrayList<Score> scores;

    public HighScore() {
        scores = new ArrayList<>();
    }

    public void addScore(String name, int score) {
        scores.add(new Score(name, score));
        Collections.sort(scores, Comparator.comparingInt(Score::getScore).reversed());
        if (scores.size() > 10) {
            scores.remove(scores.size() - 1); // 10위까지 유지
        }
    }

    public ArrayList<Score> getScores() {
        return scores;
    }

    public static class Score {
        private String name;
        private int score;

        public Score(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}
