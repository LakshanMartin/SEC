package texteditor.core;

import java.util.List;

import javafx.scene.input.KeyCode;

public class FindKeyPressed 
{
    private List<Keybind> keybindList;

    public FindKeyPressed(List<Keybind> keybindList) 
    {
        this.keybindList = keybindList;
    }    

    public boolean isValidKeyCode(String inMainKeys, KeyCode inCustomKey)
    {
        boolean isValid = false;
        String mainKeys = null;
        String customKey = null;

        for(Keybind keybind : keybindList)
        {
            mainKeys = keybind.getMainKey();
            customKey = keybind.getCustomKey().toUpperCase();

            if(mainKeys.equals(inMainKeys) && customKey.equals(inCustomKey.getChar()))
            {
                isValid = true;
                break;
            }
        }

        return isValid;
    }
}
