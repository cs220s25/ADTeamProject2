package edu.moravian;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryStorageTest {
    @Test
    public void testNewInstance() throws Exception
    {
        MemoryStorage memoryStorage = new MemoryStorage();
        assertEquals(List.of(), memoryStorage.getCategories());
        assertEquals("", memoryStorage.getCategory());
        assertEquals(List.of(), memoryStorage.getPlayers());


    }
    @Test
    public void testAddCategory() throws Exception {
        MemoryStorage memoryStorage = new MemoryStorage();
        memoryStorage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")
        ));
        assertEquals(List.of("Geography"), memoryStorage.getCategories());
    }
    @Test
    public void testResetCategory() throws Exception
    {
        MemoryStorage memoryStorage = new MemoryStorage();
        memoryStorage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")
        ));
        memoryStorage.setCategory("Geography");
        memoryStorage.resetCategory();
        assertEquals("", memoryStorage.getCategory());
    }
    @Test
    public void testResetPlayers() throws Exception
    {
        MemoryStorage memoryStorage = new MemoryStorage();
        memoryStorage.addCategory("Geography", List.of(
                new Question("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris")
        ));
        memoryStorage.addPlayer("player1");
        memoryStorage.resetPlayers();
        assertEquals(List.of(), memoryStorage.getPlayers());
    }
    @Test
    public void testAddPlayer() throws Exception {
        MemoryStorage memoryStorage = new MemoryStorage();
        memoryStorage.addPlayer("player1");
        assertEquals(List.of("player1"), memoryStorage.getPlayers());
    }
    @Test
    public void testAddAndRetrieveQuestions() throws Exception {
        MemoryStorage memoryStorage = new MemoryStorage();
        memoryStorage.addCategory("Science", List.of(
                new Question("What is the boiling point of water?", List.of("100C", "0C", "50C", "200C"), "100C"),
                new Question("What is the freezing point of water?", List.of("100C", "0C", "50C", "200C"), "0C")
        ));

        List<Question> questions = memoryStorage.getQuestionsForCategory("Science");
        assertEquals(2, questions.size());
        assertEquals("What is the boiling point of water?", questions.get(0).getQuestionText());
        assertEquals("100C", questions.get(0).getCorrectAnswer());
    }
    @Test
    public void testGetCategoriesWhenEmpty() throws Exception {
        MemoryStorage memoryStorage = new MemoryStorage();
        assertEquals(List.of(), memoryStorage.getCategories());
    }
    @Test
    public void testCategoryExistsAfterAdding() throws Exception {
        MemoryStorage memoryStorage = new MemoryStorage();
        memoryStorage.addCategory("Art", List.of(
                new Question("Who painted the Mona Lisa?", List.of("Leonardo da Vinci", "Pablo Picasso", "Vincent van Gogh", "Claude Monet"), "Leonardo da Vinci")
        ));

        assertTrue(memoryStorage.getCategories().contains("Art"));
    }
    @Test
    public void testCategoryNotExists() throws Exception {
        MemoryStorage memoryStorage = new MemoryStorage();
        assertFalse(memoryStorage.getCategories().contains("Science"));
    }
    @Test
    public void testAddCategoryWithEmptyQuestions() throws Exception {
        MemoryStorage memoryStorage = new MemoryStorage();
        memoryStorage.addCategory("EmptyCategory", List.of());
        assertTrue(memoryStorage.getQuestionsForCategory("EmptyCategory").isEmpty());
    }



}