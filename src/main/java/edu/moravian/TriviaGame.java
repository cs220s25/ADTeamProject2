package edu.moravian;

import edu.moravian.exceptions.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the trivia game logic itself.
 */

public class TriviaGame {
    private final TriviaStorage storage;
    private List<Question> questionList;
    private Question currentQuestion;
    private int currentQuestionIndex;
    private final Map<String, Integer> playerScores;


    public TriviaGame(TriviaStorage storage) {
        this.storage = storage;
        this.playerScores = new HashMap<>();

    }
    public void startGame(String category) throws InternalServerException {
        if (isGameActive()) {
            throw new GameIsActiveExpection();
        }
        if (!getCategories().contains(category)) {
            throw new NoSuchCategoryException(category);
        }
        try {
            storage.setCategory(category);
            questionList = storage.getQuestionsForCategory(category);
            if (questionList.isEmpty()) {
                throw new InternalServerException("No questions found for category");
            }
            Collections.shuffle(questionList);
            currentQuestionIndex = 0;
            currentQuestion = questionList.get(currentQuestionIndex);
        } catch (StorageException e) {
            throw new InternalServerException("Error while starting game");
        }
    }
    public void addPlayer(String playerName) throws InternalServerException {
        if(!isGameActive()) {
            throw new NoGameInProgressException();
        }
        try {
            storage.addPlayer(playerName);
            playerScores.put(playerName, 0);
        } catch (StorageException e) {
            throw new InternalServerException("Error while adding player");
        }
    }
    public String getQuestion() throws InternalServerException {
        if (!isGameActive()) {
            throw new NoGameInProgressException();
        }
        return currentQuestion.getQuestionText();
    }
    public List<String> getOptions() throws InternalServerException {
        if (!isGameActive()) {
            throw new NoGameInProgressException();
        }
        return currentQuestion.getOptions();
    }

    public List<String> getPlayers() throws InternalServerException {
        if(!isGameActive())
            throw new NoGameInProgressException();

        try
        {
            return storage.getPlayers();
        }
        catch (StorageException e)
        {
            throw new InternalServerException("Error while getting player list");
        }
    }

    public boolean isGameActive() throws InternalServerException {
        {
            try
            {
                String category = storage.getCategory();
                return category != null && !category.isEmpty();
            }
            catch (StorageException e)
            {
                throw new InternalServerException("Error during game activity check");
            }
        }
    }
    public List<String> getCategories() throws InternalServerException{
        try
        {
            return storage.getCategories();
        }
        catch (StorageException e)
        {
            throw new InternalServerException("Error while getting categories");
        }

    }
    public String getCategory() throws InternalServerException {
        if (!isGameActive()) {
            throw new NoGameInProgressException();
        }
        try {
            return storage.getCategory();
        } catch (StorageException e) {
            throw new InternalServerException("Error while getting category");
        }
    }
    public HashMap<String, Integer> getScore() throws InternalServerException {
        return new HashMap<>(playerScores);
    }
    public boolean checkAnswer(String answer) {
        if (currentQuestion == null) {
            throw new IllegalStateException("No question available");
        }
        answer = answer.trim();
        List<String> options = currentQuestion.getOptions();
        if (answer.length() == 1 && Character.isLetter(answer.charAt(0))) {
            char answerLabel = Character.toUpperCase(answer.charAt(0));
            int optionIndex = answerLabel - 'A';
            if (optionIndex >= 0 && optionIndex < options.size()) {
                answer = options.get(optionIndex);
            }
        }
        return currentQuestion.checkAnswer(answer);
    }

    public void setAnswer(String playerName, String answer) throws InternalServerException {
        if (!isGameActive()) {
            throw new NoGameInProgressException();
        }
        if (!getPlayers().contains(playerName)) {
            throw new NoSuchPlayerException("Player " + playerName + " not found");
        }
        boolean correct = checkAnswer(answer);
        if (correct) {
            int currentScore = playerScores.getOrDefault(playerName, 0);
            playerScores.put(playerName, currentScore + 1);
            System.out.println("Current score for player " + playerName + ": " + currentScore);
        } else {
            System.out.println("Incorrect answer from player " + playerName);
        }
        nextQuestion();

    }
    public boolean nextQuestion() throws InternalServerException {
        if (!isGameActive()) {
            throw new NoGameInProgressException();
        }
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            currentQuestion = questionList.get(currentQuestionIndex);
            return true;
        } else {
            currentQuestion = null;
            return false;
        }

    }
    public boolean gameInProgress() throws InternalServerException {
        try {
            return !storage.getCategory().isEmpty();
        } catch (StorageException e) {
            throw new InternalServerException("Error while checking game in progress");
        }
    }
    public void reset() throws InternalServerException
    {
        try
        {
            storage.resetCategory();
            storage.resetPlayers();
            playerScores.clear();
            questionList = null;
            currentQuestion = null;
            currentQuestionIndex = 0;
        }
        catch (StorageException e)
        {
            throw new InternalServerException("Error while resetting");
        }
    }


}
