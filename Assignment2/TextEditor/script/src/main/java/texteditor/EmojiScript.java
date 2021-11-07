package texteditor;

import org.python.util.PythonInterpreter;

import texteditor.API.*;

public class EmojiScript implements ScriptHandler
{
    public EmojiScript() {}

    @Override
    public void loadScript(API api, String pythonScript) 
    {
        PythonInterpreter interpreter = new PythonInterpreter();
        
        interpreter.set("api", api);

        interpreter.exec(pythonScript);
    }
}
