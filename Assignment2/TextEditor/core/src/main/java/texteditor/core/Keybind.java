package texteditor.core;

/**
 * Class used to contain all the relevent information extracted from the Keymap
 * file for each Keybinding.
 */
public class Keybind 
{
    // CLASS FIELDS
    private String mainKey;
    private String customKey;
    private String func;
    private String text;
    private String pos;
    
    // CONSTRUCTOR
    public Keybind(String mainKey, String customKey, String func, String text, String pos)
    {
        this.mainKey = mainKey;
        this.customKey = customKey;
        this.func = func;
        this.text = text;
        this.pos = pos;
    }

    // ACCESSORS
    public String getMainKey()
    {
        return mainKey;
    }

    public String getCustomKey()
    {
        return customKey;
    }

    public String getFunc()
    {
        return func;
    }

    public String getText()
    {
        // Remove quotation marks surrounding text
        return text.substring(1, text.length()-1);
    }

    public String getPos()
    {
        return pos;
    }
}
