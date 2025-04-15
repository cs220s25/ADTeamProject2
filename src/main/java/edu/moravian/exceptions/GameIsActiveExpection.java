package edu.moravian.exceptions;

public class GameIsActiveExpection extends RuntimeException {
    public GameIsActiveExpection() {
        super("Game already in progress");
    }
}