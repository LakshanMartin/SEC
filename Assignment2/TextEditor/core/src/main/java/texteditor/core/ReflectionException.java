package texteditor.core;

/**
 * Custom exception used to output to user specific Reflection based exception
 * errors.
 */
public class ReflectionException extends Exception
{
    public ReflectionException(String error)
    {
        super("ERROR: " + error);
    }    
}
