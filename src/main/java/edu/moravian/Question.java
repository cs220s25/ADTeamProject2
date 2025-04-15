package edu.moravian;

import java.util.List;

/**
 * This class represents a question in the trivia game.
 */
public class Question {
    private final String questionText;
    private final List<String> options;
    private final String correctAnswer;

    public Question(String questionText, List<String> options, String correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer.trim().toUpperCase();
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public boolean checkAnswer(String answer) {
        return correctAnswer.equalsIgnoreCase(answer.trim());
    }
    @Override
    public String toString() {
        return questionText + "|" + String.join(",", options) + "|" + correctAnswer;
    }
}
