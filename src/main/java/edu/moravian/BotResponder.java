package edu.moravian;

import edu.moravian.exceptions.InternalServerException;
import edu.moravian.exceptions.NoGameInProgressException;
import edu.moravian.exceptions.NoSuchPlayerException;

import java.util.List;
import java.util.Map;

/**
 * This class is responsible for handling what the bot should do in response to messages.
 */
public class BotResponder {
    private final TriviaGame game;
    private BotState state;

    /**
     * Creates a new BotResponder.
     */
    public BotResponder(TriviaGame game) {
        this.game = game;
        state = BotState.NO_GAME;
    }

    /**
     * Calls the method that should be called based on the message received.
     */
    public String respond(String username, String message) {
        try {
            if (message.equals("!categories"))
                return handleCategories();
            else if(message.equals("!about"))
                return handleAbout();
            else if(message.startsWith("!start"))
                return handleStart(message);
            else if(message.equals("!join"))
                return handleJoin(username);
            else if(message.equals("!go"))
                return handleGo();
            else if(message.equals("!status"))
                return handleStatus();
            else if(message.equals("!quit"))
                return handleQuit();
            else if(message.equals("!help"))
                return handleHelp();
            else if(message.equals("!question"))
                return handleQuestion();
            else if(message.equals("!scores"))
                return handleScores();
            else if(message.startsWith("!"))
                return BotResponses.unknownCommand();
            else
                return handleAnswer(username, message);
        }
        catch (InternalServerException e)
        {
            return BotResponses.serverError();
        }
    }
    private String handleCategories() throws InternalServerException
    {
        try {
            List<String> categories = game.getCategories();
            if (categories.isEmpty()) {
                return BotResponses.noCategories();
            }
            return BotResponses.categories(categories);
        } catch (InternalServerException e) {
            return BotResponses.serverError();
        }
    }
    /**
     * Handles the !start command. Chooses a random category if "random" is passed as the category.
     */
    private String handleStart(String message) throws InternalServerException {
        if (state != BotState.NO_GAME) {
            return BotResponses.gameAlreadyInProgress();
        }
        String[] parts = message.split(" ");
        if (parts.length != 2) {
            return BotResponses.missingCategory();
        }
        String category = parts[1];
        try {
            if (category.equals("random"))
                category = game.getCategories().get((int) (Math.random() * game.getCategories().size()));
            else if (!game.getCategories().contains(category))
                return BotResponses.unknownCategory(category);
            game.startGame(category);
            state = BotState.STARTING;
            return BotResponses.gameStarted(category);
        } catch (InternalServerException e) {
            return BotResponses.serverError();
        }

    }
    private String handleJoin(String username) throws InternalServerException {
        if (state == BotState.NO_GAME)
            return BotResponses.noGameStarted();
        if (state == BotState.IN_PROGRESS)
            return BotResponses.gameAlreadyInProgress();
        if (game.getPlayers().contains(username))
            return BotResponses.playerAlreadyJoined(username);
        game.addPlayer(username);
        return BotResponses.playerJoined(username);
    }

    private String handleGo() throws InternalServerException {
        if (state == BotState.NO_GAME)
            return BotResponses.noGameStarted();
        if (state == BotState.IN_PROGRESS) {
            return BotResponses.gameInProgress(game.getCategory(), game.getScore());
        }
        if (game.getPlayers().isEmpty())
            return BotResponses.noPlayers();
        state = BotState.IN_PROGRESS;
        String starting = BotResponses.gameGoing(game.getCategory());
        try {
            return starting + "\n\n" + generateQuestion();
        } catch (InternalServerException e) {
            return BotResponses.serverError();
        }
    }

    /**
     *
     */
    private String generateQuestion() throws InternalServerException {
        String question = game.getQuestion();
        List<String> options = game.getOptions();
        StringBuilder questionBuilder = new StringBuilder("**Question:** " + question + "\n");
        char option = 'A';
        for (String opt : options) {
            questionBuilder.append("**").append(option).append(")** ").append(opt).append("\n");
            option++;
        }
        return questionBuilder.toString();
    }
    private String handleQuestion() throws InternalServerException {
        if (state == BotState.NO_GAME)
            return BotResponses.noGameStarted();
        if (state == BotState.STARTING)
            return BotResponses.gameStartedButNotGoing();
        return generateQuestion();
    }

    private String handleHelp() {
        return BotResponses.help();
    }
    private String handleAbout() {
        return BotResponses.about();
    }

    private String handleQuit() throws InternalServerException {
        if (state == BotState.STARTING) {
            String category = game.getCategory();
            game.reset();
            return BotResponses.gameAbortedBeforeGo(category);
        } else if (state == BotState.NO_GAME) {
            return BotResponses.noGameStarted();
        }
        String ret = BotResponses.quitGame(game.getCategory(), game.getScore());
        game.reset();
        state = BotState.NO_GAME;
        return ret;
    }

    private String handleStatus() throws InternalServerException {
        if (state == BotState.NO_GAME)
            return BotResponses.noGameStarted();
        else if (state == BotState.STARTING)
            return BotResponses.gameStarting(game.getCategory(), game.getPlayers()) ;
        else
            return BotResponses.gameInProgress(game.getCategory(), game.getScore());
    }

    /**
     * Handles the !scores command. Creates a string and the format with the scores of all players.
     */
    private String handleScores() throws InternalServerException {
        if (state == BotState.NO_GAME)
            return BotResponses.noGameStarted();
        StringBuilder scoresBuilder = new StringBuilder();
        scoresBuilder.append("Scores:\n");
        for (String player : game.getPlayers()) {
            scoresBuilder.append(player).append(": ").append(game.getScore().get(player)).append("\n");
        }
        return scoresBuilder.toString();
    }

    /**
     * Handles the answer to a question. Checks if the answer is correct and updates the score.
     * If the answer is correct, it generates a new question.
     */
    private String handleAnswer(String username, String answer) throws InternalServerException {
        if (state == BotState.NO_GAME) {
            return BotResponses.noGameInProgress();
        }
        if (state == BotState.STARTING) {
            return BotResponses.gameStartedButNotGoing();
        }
        try {
            if (!game.getPlayers().contains(username)) {
                throw new NoSuchPlayerException("Player " + username + " not found");
            }
            boolean isCorrect = game.checkAnswer(answer);
            if (isCorrect) {
                game.setAnswer(username, answer);
                String feedback = BotResponses.correctAnswer(username);

                if (!game.nextQuestion()) {
                    Map<String, Integer> scores = game.getScore();
                    String category = game.getCategory();
                    game.reset();
                    state = BotState.NO_GAME;
                    return feedback + "\n" + BotResponses.outOfQuestionsGameEnd(category, scores);
                }
                return feedback + "\n" + generateQuestion();
            } else {
                return BotResponses.incorrectAnswer(username);
            }
        } catch (NoGameInProgressException e) {
            return BotResponses.noGameInProgress();
        } catch (NoSuchPlayerException e) {
            return BotResponses.unknownPlayer(username);
        }
    }




}
