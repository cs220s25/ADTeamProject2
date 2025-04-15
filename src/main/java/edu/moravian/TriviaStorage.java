package edu.moravian;

import edu.moravian.exceptions.StorageException;
import java.util.List;

/**
 * This interface represents the storage system for the trivia game.
 */

public interface TriviaStorage {
    void setCategory(String category) throws StorageException;
    String getCategory() throws StorageException;

    void resetCategory() throws StorageException;

    List<String> getCategories() throws StorageException;
    void addCategory(String category, List<Question> questions) throws StorageException;
    List<Question> getQuestionsForCategory(String category) throws StorageException;
    void addPlayer(String playerName) throws StorageException;
    List<String> getPlayers() throws StorageException;
    void resetPlayers() throws StorageException;
}
