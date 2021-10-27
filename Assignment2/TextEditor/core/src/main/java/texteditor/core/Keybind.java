package texteditor.core;

public class Keybind 
{
    private String mainKey;
    private String customKey;
    private String func;
    private String text;
    private String pos;
    
    public Keybind(String mainKey, String customKey, String func, String text, String pos)
    {
        this.mainKey = mainKey;
        this.customKey = customKey;
        this.func = func;
        this.text = text;
        this.pos = pos;
    }

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
        return text;
    }

    public String getPos()
    {
        return pos;
    }
}
