package texteditor.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class KeymapParse 
{
    public KeymapParse() {}

    public List<Keybind> startParse() throws IOException, ParseException
    {
        List<Keybind> keybindList = new ArrayList<>();
        long lineCount = 0;
        Path path = Paths.get("keymap");
        lineCount = Files.lines(path).count();
        
        MyParser parser = new MyParser(new FileInputStream("keymap"));

        for(int i = 0; i <lineCount; i++)
        {
            String mainKey = parser.keybind();
            String customKey = parser.customKey();
            String func = parser.func();
            String text = parser.text();
            String pos = parser.pos();

            Keybind keybind = new Keybind(mainKey, customKey, func, text, pos);

            keybindList.add(keybind);
        }

        return keybindList;
    }
}
