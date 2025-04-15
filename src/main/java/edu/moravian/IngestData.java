package edu.moravian;

/**
 * This class reads the file and parses the questions correctly to be stored in the Redis database and creates
 * the categories for the trivia game.
 */

import edu.moravian.exceptions.StorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IngestData {

    public static List<Question> readFile(String fileName) throws IOException, StorageException {
        Path filePath = Paths.get(fileName);
        List<String> lines = Files.readAllLines(filePath);
        return parseQuestions(lines);
    }

    /**
     * Parses the questions from the file and stores them in a list of Question objects.
     */
    private static List<Question> parseQuestions(List<String> lines) throws StorageException {
        List<Question> questions = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length != 3) {
                throw new StorageException("Invalid question format: " + line);
            }
            String questionText = parts[0];
            List<String> options = List.of(parts[1].split(","));
            String correctAnswer = parts[2];

            if (!options.contains(correctAnswer)) {
                throw new StorageException("Correct answer not found in options for question: " + questionText);
            }

            questions.add(new Question(questionText, options, correctAnswer));
        }
        return questions;
    }

    public static void main(String[] args) {
        try {
            RedisStorage redisActions = new RedisStorage("localhost", 6379);
            redisActions.resetToEmpty();

            redisActions.addCategory("science", readFile(Paths.get("src/data/science.txt").toAbsolutePath().toString()));
            System.out.println("science added");
            redisActions.addCategory("art", readFile("src/data/art.txt"));
            System.out.println("art added");
            redisActions.addCategory("history", readFile("src/data/history.txt"));
            System.out.println("history added");
            redisActions.addCategory("geography", readFile(Paths.get("src/data/geography.txt").toAbsolutePath().toString()));
            System.out.println("geography added");
            redisActions.addCategory("sports", readFile(Paths.get("src/data/sports.txt").toAbsolutePath().toString()));
            System.out.println("sports added");
            redisActions.addCategory("popCulture", readFile(Paths.get("src/data/popCulture.txt").toAbsolutePath().toString()));
            System.out.println("popCulture added");
        } catch (StorageException | IOException e) {
            System.out.println("Error while reading files: " + e.getMessage());
        }

    }
}