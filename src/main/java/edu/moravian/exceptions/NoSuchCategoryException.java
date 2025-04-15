package edu.moravian.exceptions;

public class NoSuchCategoryException extends RuntimeException
{
    public NoSuchCategoryException(String category)
    {
        super("No such category: " + category);
    }
}
