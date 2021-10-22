package texteditor.core;

public class ReflectionException extends Exception
{
    public ReflectionException(String error)
    {
        super("ERROR: " + error);
    }    
}
