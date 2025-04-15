package edu.moravian;

import edu.moravian.exceptions.StorageException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a storage system that stores trivia game data in Redis.
 */
public class RedisStorage implements TriviaStorage
{
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private final Jedis jedis;

    public RedisStorage(String hostname, int port) throws StorageException {
        try {
            jedis = new Jedis(hostname, port);
        } catch (JedisException e) {
            throw new StorageException("Cannot Connect to Redis");
        }
    }
    @Override
    public void resetCategory() throws StorageException {
        try {
            jedis.del("gameCategory");
        } catch (JedisException e) {
            throw new StorageException("Cannot Connect to Redis");
        }
    }
    @Override
    public void resetPlayers() throws StorageException {
        try {
            for (String player : jedis.smembers("players")) {
                jedis.del("points:" + player);
            }
            jedis.del("players");
        } catch (JedisException e) {
            throw new StorageException("Cannot Connect to Redis");
        }
    }
    @Override
    public void addCategory(String categoryName, List<Question> questions) throws StorageException {
        try {
            jedis.sadd("categories", categoryName);
            String questionsKey = "questions:" + categoryName;

            for (Question question : questions) {
                String questionString = question.getQuestionText() + "|" + String.join(",", question.getOptions()) + "|" + question.getCorrectAnswer();
                jedis.rpush(questionsKey, questionString);
            }
        } catch (JedisException e) {
            throw new StorageException("Error while interacting with Redis");
        }
    }
    @Override
    public List<String> getCategories() throws StorageException {
        try {
            return new LinkedList<>(jedis.smembers("categories"));
        } catch (JedisException e) {
            throw new StorageException("Cannot Connect to Redis");
        }
    }
    @Override
    public String getCategory() throws StorageException {
        try {
            return jedis.get("gameCategory");
        } catch (JedisException e) {
            throw new StorageException("Cannot Connect to Redis");
        }
    }
    @Override
    public void setCategory(String category) throws StorageException {
        try {
            jedis.set("gameCategory", category);
        } catch (JedisException e) {
            throw new StorageException("Cannot Connect to Redis");
        }
    }
    public void testConnection() throws StorageException
    {
        try
        {
            jedis.ping();
        }
        catch (JedisException e)
        {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    @Override
    public List<Question> getQuestionsForCategory(String category) throws StorageException {
        try {
            List<String> questionStrings = jedis.lrange("questions:" + category, 0, -1);
            List<Question> questionList = new LinkedList<>();

            for (String questionString : questionStrings) {
                String[] parts = questionString.split("\\|");
                if (parts.length != 3) {
                    throw new StorageException("Invalid question format in Redis for category " + category);
                }
                String questionText = parts[0];
                List<String> options = List.of(parts[1].split(","));
                String correctAnswer = parts[2];
                questionList.add(new Question(questionText, options, correctAnswer));
            }
            return questionList;
        } catch (JedisException e) {
            throw new StorageException("Failed to get questions for category from Redis: " + e.getMessage());
        }
    }
    public void resetToEmpty() throws StorageException
    {
        try
        {
            jedis.flushAll();
        }
        catch (JedisException e)
        {
            throw new StorageException("Could not connect to Redis server");
        }
    }
    @Override
    public void addPlayer(String playerName) throws StorageException {
        try {
            jedis.sadd("players", playerName);
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }
    @Override
    public List<String> getPlayers() throws StorageException {
        try {
            return new LinkedList<>(jedis.smembers("players"));
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }
}
