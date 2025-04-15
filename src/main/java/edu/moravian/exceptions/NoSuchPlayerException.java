package edu.moravian.exceptions;

public class NoSuchPlayerException extends RuntimeException
{
    public NoSuchPlayerException(String player)
    {
        super("No such player: " + player);
    }
}
