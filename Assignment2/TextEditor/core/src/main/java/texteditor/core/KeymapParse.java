package texteditor.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class KeymapParse 
{
    public static void main(String[] args) 
    {
        try
        {
            MyParser parser = new MyParser(new FileInputStream("keymap"));

            for(int i = 0; i <11; i++)
            {
                System.out.println("KEYBIND #" + (i+1));
                String mainKey = parser.keybind();
                String customKey = parser.customKey();
                String func = parser.func();
                String text = parser.text();
                String pos = parser.pos();

                Keybind obj = new Keybind(mainKey, customKey, func, text, pos);
                System.out.println("Keybind from obj: " + obj.getMainKey() + obj.getCustomKey());
                System.out.println("Func from obj: " + obj.getFunc());
                System.out.println("Text from obj: " + obj.getText());
                System.out.println("Count space: " + obj.getText().length());
                System.out.println("Pos from obj: " + obj.getPos());
                
                
                System.out.println("\n");
            }
        }
        catch(FileNotFoundException | ParseException e)
        {
            System.out.println("Parsing error!");
            System.out.println(e.getMessage());
        }
    }
}
