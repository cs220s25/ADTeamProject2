package edu.moravian;

import edu.moravian.exceptions.StorageException;


import java.util.*;

/**
 * This class represents a storage system that stores trivia game data in memory throughout the game itself.
 */

public class MemoryStorage implements TriviaStorage {
    private String gameCategory;
    private final Set<String> categories;
    private final HashMap<String, List<Question>> categoryQuestions;
    private final HashMap<String, Integer> playerPoints;
    private final Set<String> players;

    public MemoryStorage() {
        gameCategory = "";
        categories = new HashSet<>();
        categoryQuestions = new HashMap<>();
        playerPoints = new HashMap<>();
        players = new HashSet<>();
    }

    @Override
    public void setCategory(String category) throws StorageException {
        if (!categories.contains(category)) {
            throw new StorageException("Category not found: " + category);
        }
        this.gameCategory = category;
    }

    @Override
    public String getCategory() throws StorageException {
        return gameCategory;
    }

    @Override
    public void resetCategory() throws StorageException {
        gameCategory = "";
    }

    @Override
    public List<String> getCategories() throws StorageException {
        return new ArrayList<>(categories);
    }

    @Override
    public void addCategory(String categoryName, List<Question> questions) throws StorageException {
        if (categories.contains(categoryName)) {
            throw new StorageException("Category already exists: " + categoryName);
        }
        categories.add(categoryName);
        categoryQuestions.put(categoryName, new ArrayList<>(questions));
    }

    @Override
    public List<Question> getQuestionsForCategory(String category) throws StorageException {
        if (!categories.contains(category)) {
            throw new StorageException("Category not found: " + category);
        }
        return new ArrayList<>(categoryQuestions.getOrDefault(category, new ArrayList<>()));
    }

    @Override
    public void addPlayer(String playerName) throws StorageException {
        if (players.contains(playerName)) {
            throw new StorageException("Player already exists: " + playerName);
        }
        players.add(playerName);
        playerPoints.put(playerName, 0);

    }

    @Override
    public List<String> getPlayers() throws StorageException {
        return new ArrayList<>(players);
    }

    @Override
    public void resetPlayers() throws StorageException {
        players.clear();
        playerPoints.clear();
    }
}
