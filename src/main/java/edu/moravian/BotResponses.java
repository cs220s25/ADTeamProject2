package edu.moravian;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides static methods to generate responses for the bot.
 */
public class BotResponses {
    public static String help() {
        return "**Trivia Game!**\n" +
                "Here is a list of commands to play!\n\n" +
                "* `!about` - Get information!\n" +
                "* `!categories` - See all categories.\n" +
                "* `!start <category>` - Start a new game with picked category.\n" +
                "* `!start random` - Start new game with a random category.\n" +
                "* `!join` - Join the game. Can only join when game is in progress or in progress.\n" +
                "* `!go` - Start the game when all players are ready!\n" +
                "* `!question` - Get the question.\n" +
                "* `!status` - See the game status, category, and the current scores in the game.\n" +
                "* `!quit` - Quit.\n" +
                "* `!help` - Get all game commands.\n" +
                "* `!scores` - Get the current score of the game.\n";
    }
    public static String serverError() {
        return "Oh no! Something went wrong! Please try again later.";
    }

    /**
     * Generate a response with the available categories and creates a layout for the way they are outputted.
     */
    public static String categories(List<String> categories) {
        StringBuilder response = new StringBuilder("Here are the available categories:\n");
        for (String category : categories) {
            response.append("* ").append(category).append("\n");
        }
        return response.toString();
    }




    public static String gameStarted(String category) {
        return "Game started! The category is: " + category + "\n\n" +
                "Players can join the game by typing **!join**.\n" +
                "When all players have joined, begin the game with **!go**.";
    }

    public static String gameAlreadyInProgress() {
        return "A game is already in progress. \nUse **!status** to see the current game status.";
    }

    public static String missingCategory() {
        return "You haven't picked a category! \nUse **!start <category>** or **!start random** to pick." +
                "\nUse **!categories** to see the available categories.";
    }

    public static String unknownCategory(String category) {
        return "Uh oh! The category " + category + " doesn't exist.\n" +
                "Please use **!categories** to see the available categories.";
    }

    public static String noGameStarted() {
        return "No game has been started. Use **!start <category>** to start a new game.";
    }

    public static String playerJoined(String username) {
        return "Player " + username + " has joined the game.\n" +
                "When all players have joined, begin the game by typing **!go**.";
    }

    public static String noGameInProgress() {
        return "No game is in progress.\n Use **!start <category>** to start a new game.";
    }

    /**
     * Generate a response with the current scores of the game and makes the layout.
     */
    public static String gameInProgress(String category, HashMap<String, Integer> PlayerScores) {
        StringBuilder response = new StringBuilder("The game is in progress.\n The category is: " + category + ".\n\n");
        response.append("The current scores are:\n");
        for (Map.Entry<String, Integer> entry : PlayerScores.entrySet()) {
            response.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return response.toString();
    }

    public static String noCategories() {
        return "No categories available.";
    }

    public static String gameAbortedBeforeGo(String category) {
        return "The game was aborted before it started.\n The category was: " + category;
    }

    public static String gameStartedButNotGoing() {
        return "The game has started but it's not going.";
    }

    public static String incorrectAnswer(String username) {
        return "Sorry, " + username + ". Your answer is incorrect. Try again!";
    }

    public static String correctAnswer(String username) {
        return "Congratulations, " + username + "! Your answer is correct!";
    }

    /**
     * Generate a response with the current scores of the game and makes the layout.
     */
    public static String quitGame(String category, Map<String, Integer> scores) {
        StringBuilder response = new StringBuilder("**Game quit!**\n\n");
        response.append("Category: **").append(category).append("**\n\n");
        response.append("Scores:\n");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            response.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return response.toString();
    }


    public static String noPlayers() {
        return "No players have joined the game.\nUse **!join** to join the game.";
    }


    public static String gameStarting(String category, List<String> players) {
        return "A game is starting in category " + category + ". \nPlayers in the game: " + String.join(", ", players) + ". \n" +
                "When all players have joined, begin the game by typing **!go**.";
    }

    /**
     * Generate a response with the current scores of the game and makes the layout.
     */
    public static String outOfQuestionsGameEnd(String category, Map<String, Integer> score) {
        StringBuilder response = new StringBuilder("The game has ended.\nThe category was: " + category + ".\n\n");
        response.append("The final scores are:\n");
        for (Map.Entry<String, Integer> entry : score.entrySet()) {
            response.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return response.toString();
    }

    public static String unknownPlayer(String username) {
        return "The player " + username + " is unknown.";
    }

    public static String about() {
        return "Welcome to the Trivia Game! This is a classic game of trivia where the first person to answer\n" +
                "the question correctly gets a point. The game will continue until all questions in the category\n" +
                "have been answered or you say `!quit`. The player with the most points at the end of the game wins!";
    }

    public static String gameGoing(String category) {
        return "Game is going!\nCategory is: " + category + ".\nBe the first player to answer (letter or whole word) to get the point.";

    }

    public static String playerAlreadyJoined(String username) {
        return "Player " + username + " has already joined the game.";
    }

    public static String unknownCommand() {
        return "Unknown command. Use **!help** to see all available commands.";
    }
}
