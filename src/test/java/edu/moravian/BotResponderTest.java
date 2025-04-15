package edu.moravian;

import static org.junit.jupiter.api.Assertions.*;
import edu.moravian.exceptions.StorageException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BotResponderTest {
    private BotResponder createResponderFromNewGame() throws StorageException {
        MemoryStorage storage = new MemoryStorage();
        List<Question> geographyQuestions = new ArrayList<>();
        geographyQuestions.add(new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris"));
        geographyQuestions.add(new Question("What is the capital of Germany?", List.of("Paris", "London", "Berlin", "Madrid"), "Berlin"));
        List<Question> scienceQuestions = new ArrayList<>();
        scienceQuestions.add(new Question("What is the boiling point of water?", List.of("100C", "0C", "50C", "200C"), "100C"));
        scienceQuestions.add(new Question("What is the freezing point of water?", List.of("100C", "0C", "50C", "200C"), "0C"));
        storage.addCategory("Geography", geographyQuestions);
        storage.addCategory("Science", scienceQuestions);
        TriviaGame game = new TriviaGame(storage);
        return new BotResponder(game);
    }
    @Test
    public void testCanGetCategories() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        String response = responder.respond("user1", "!categories");
        assertEquals(BotResponses.categories(List.of("Geography", "Science")), response);
    }
    @Test
    public void testStartGameWithCategory() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        String response = responder.respond("user1", "!start Geography");
        assertEquals(BotResponses.gameStarted("Geography"), response);
    }
    @Test
    public void testStartGameWithNoCategory() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        String response = responder.respond("user1", "!start");
        assertEquals(BotResponses.missingCategory(), response);
    }
    @Test
    public void testStartGameWithUnknownCategory() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        String response = responder.respond("user1", "!start History");
        assertEquals(BotResponses.unknownCategory("History"), response);
    }
    @Test
    public void testStartGameWithRandomCategory() throws Exception {
        MemoryStorage storage = new MemoryStorage();
        storage.addCategory("Geography", List.of(new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")));
        TriviaGame game = new TriviaGame(storage);
        BotResponder responder = new BotResponder(game);
        String response = responder.respond("user1", "!start random");
        assertEquals(BotResponses.gameStarted("Geography"), response);
    }
    @Test
    public void testStartGameWhenGameInProgress() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        String response = responder.respond("user1", "!start Geography");
        assertEquals(BotResponses.gameAlreadyInProgress(), response);
    }
    @Test
    public void testJoinGameWhenNoGameInProgress() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        String response = responder.respond("user1", "!join");
        assertEquals(BotResponses.noGameStarted(), response);
    }
    @Test
    public void testJoinGameWhenGameInProgress() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        String response1 = responder.respond("user2", "!join");
        assertEquals(BotResponses.playerJoined("user2"), response1);
        String response2 = responder.respond("user3", "!join");
        assertEquals(BotResponses.playerJoined("user3"), response2);
    }
    @Test
    public void testCheckCategoriesWhileInGame() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        String response = responder.respond("user1", "!categories");
        assertEquals(BotResponses.categories(List.of("Geography", "Science")), response);
    }
    @Test
    public void testGameStatusWhenNotStarted() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        String response = responder.respond("user1", "!status");
        assertEquals(BotResponses.noGameStarted(), response);
    }
    @Test
    public void testGameStatusWhenStarting() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        String response = responder.respond("user1", "!status");
        assertEquals(BotResponses.gameStarting("Geography", List.of()), response);
    }
    @Test
    public void testGameStatusWhenInProgress() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        responder.respond("user2", "!join");
        responder.respond("user1", "!go");
        String response = responder.respond("user1", "!status");
        HashMap<String, Integer> expectedScores = new HashMap<>();
        expectedScores.put("user2", 0);
        assertEquals(BotResponses.gameInProgress("Geography", expectedScores), response);
    }

    @Test public void testQuitBeforeGameStarted() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        String response = responder.respond("user1", "!quit");
        assertEquals(BotResponses.noGameStarted(), response);
    }
    @Test
    public void testGoWhenNoGame() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        String response = responder.respond("user1", "!go");
        assertEquals(BotResponses.noGameStarted(), response);
    }
    @Test
    public void testJoinAfterGameStarts() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        responder.respond("user2", "!join");
        responder.respond("user1", "!go");
        String response = responder.respond("user3", "!join");
        assertEquals(BotResponses.gameAlreadyInProgress(), response);
    }
    @Test
    public void testAnswerWithoutJoining() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        responder.respond("user1", "!join");
        responder.respond("user1", "!go");
        String response = responder.respond("user2", "A");
        assertEquals(BotResponses.unknownPlayer("user2"), response);
    }
    @Test
    public void testAnswerAfterGameEnds() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        responder.respond("user2", "!join");
        responder.respond("user1", "!go");
        responder.respond("user2", "A");
        responder.respond("user1", "!quit");
        String response = responder.respond("user2", "B");
        assertEquals(BotResponses.noGameInProgress(), response);
    }
    @Test
    public void testStatusAfterQuit() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        responder.respond("user2", "!join");
        responder.respond("user1", "!go");
        responder.respond("user1", "!quit");
        String response = responder.respond("user1", "!status");
        assertEquals(BotResponses.noGameStarted(), response);
    }
    @Test
    public void testRestartAfterQuit() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        responder.respond("user2", "!join");
        responder.respond("user1", "!go");
        responder.respond("user1", "!quit");
        String response = responder.respond("user1", "!start Science");
        assertEquals(BotResponses.gameStarted("Science"), response);
    }
    @Test
    public void testNoRepeatedQuestions() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        responder.respond("user2", "!join");
        responder.respond("user1", "!go");
        responder.respond("user2", "A");
        String response = responder.respond("user1", "!status");
        assertFalse(response.contains("What is the capital of France?"));
    }
    @Test
    public void testQuitWhenGameInProgress() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        responder.respond("user2", "!join");
        responder.respond("user1", "!go");
        String response = responder.respond("user1", "!quit");
        Map<String, Integer> expectedScores = new HashMap<>();
        expectedScores.put("user2", 0);
        assertEquals(BotResponses.quitGame("Geography", expectedScores), response);
    }
    @Test
    public void testIncorrectAnswer() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        responder.respond("user2", "!join");
        responder.respond("user1", "!go");
        String response = responder.respond("user2", "B");
        assertTrue(response.contains(BotResponses.incorrectAnswer("user2")));
    }
    @Test
    public void testDuplicateJoin() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        responder.respond("user1", "!start Geography");
        responder.respond("user2", "!join");
        String response = responder.respond("user2", "!join");
        assertEquals(BotResponses.playerAlreadyJoined("user2"), response);
    }
    @Test
    public void testHelp() throws Exception {
        BotResponder responder = createResponderFromNewGame();
        String response = responder.respond("user1", "!help");
        assertEquals(BotResponses.help(), response);
    }


}