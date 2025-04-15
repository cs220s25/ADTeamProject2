package edu.moravian;
import com.sun.jna.Memory;
import edu.moravian.exceptions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriviaGameTest {
    @Test
    public void testNoInstanceGame() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        storage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")
        ));
        TriviaGame game = new TriviaGame(storage);
        assertFalse(game.gameInProgress());

    }
    @Test
    public void testStartGameWithCategory() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        storage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")
        ));
        TriviaGame game = new TriviaGame(storage);
        game.startGame("Geography");
        assertTrue(game.gameInProgress());
        assertEquals("Geography", game.getCategory());
        assertEquals(List.of(), game.getPlayers());

    }
    @Test
    public void testStartGameWithNoCategory() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        TriviaGame game = new TriviaGame(storage);
        assertThrows(NoSuchCategoryException.class, () -> {
            game.startGame("");
        });
    }
    @Test
    public void testStartGameWithUnknownCategory() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        storage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")
        ));
        TriviaGame game = new TriviaGame(storage);
        assertThrows(NoSuchCategoryException.class, () -> {
            game.startGame("Science");
        });


    }
    @Test
    public void testAddPlayer() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        storage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")
        ));
        TriviaGame game = new TriviaGame(storage);
        game.startGame("Geography");
        game.addPlayer("user1");
        assertEquals(List.of("user1"), game.getPlayers());
    }
    @Test
    public void testAddPlayerWhenGameNotStarted() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        TriviaGame game = new TriviaGame(storage);
        assertThrows(NoGameInProgressException.class, () -> {
            game.addPlayer("user1");
        });
    }
    @Test
    public void testGetPlayers() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        storage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")
        ));
        TriviaGame game = new TriviaGame(storage);
        game.startGame("Geography");
        game.addPlayer("user1");
        assertEquals(List.of("user1"), game.getPlayers());
    }
    @Test
    public void testAnswerQuestion() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        storage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")
        ));
        TriviaGame game = new TriviaGame(storage);
        game.startGame("Geography");
        game.addPlayer("user1");
        String question = game.getQuestion();
        assertNotNull(question);
        game.setAnswer("user1", "A");
        assertEquals(1, game.getScore().get("user1"));
    }
    @Test
    public void testAnswerQuestionWithWrongAnswer() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        storage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")
        ));
        TriviaGame game = new TriviaGame(storage);
        game.startGame("Geography");
        game.addPlayer("user1");
        String question = game.getQuestion();
        assertNotNull(question);
        game.setAnswer("user1", "D");
        assertEquals(0, game.getScore().get("user1"));
    }
    @Test
    public void testNextQuestion() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        storage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris"),
                new Question("What is the capital of Spain?", List.of("Paris", "London", "Berlin", "Madrid"), "Madrid")
        ));
        TriviaGame game = new TriviaGame(storage);
        game.startGame("Geography");
        String question1 = game.getQuestion();
        assertNotNull(question1);
        game.nextQuestion();
        String question2 = game.getQuestion();
        assertNotEquals(question1, question2);
    }
    @Test
    public void testResetGame() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        storage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris"),
                new Question("What is the capital of Spain?", List.of("Paris", "London", "Berlin", "Madrid"), "Madrid")
        ));
        TriviaGame game = new TriviaGame(storage);
        game.startGame("Geography");
        game.addPlayer("user1");
        game.reset();
        assertFalse(game.gameInProgress());
    }

}