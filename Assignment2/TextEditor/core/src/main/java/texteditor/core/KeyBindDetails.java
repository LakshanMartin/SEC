package texteditor.core;

import javafx.scene.input.KeyCode;

public class KeyBindDetails 
{
    private String customKey;

    public KeyBindDetails(String customKey)
    {
        this.customKey = customKey;
    }

    public KeyCode getKeyCode()
    {
        KeyCode keyCode = null;

        switch(customKey)
        {
            case "a":
                keyCode = KeyCode.A;
            break;
            
            case "b":
                keyCode = KeyCode.B;
            break;

            case "c":
                keyCode = KeyCode.C;
            break;

            case "d":
                keyCode = KeyCode.D;
            break;

            case "e":
                keyCode = KeyCode.E;
            break;
            
            case "f":
                keyCode = KeyCode.F;
            break;
            
            case "g":
                keyCode = KeyCode.G;
            break;
            
            case "h":
                keyCode = KeyCode.H;
            break;
            
            case "i":
                keyCode = KeyCode.I;
            break;
            
            case "j":
                keyCode = KeyCode.J;
            break;
            
            case "k":
                keyCode = KeyCode.K;
            break;
            
            case "l":
                keyCode = KeyCode.L;
            break;
            
            case "m":
                keyCode = KeyCode.M;
            break;
            
            case "n":
                keyCode = KeyCode.N;
            break;
            
            case "o":
                keyCode = KeyCode.O;
            break;
            
            case "p":
                keyCode = KeyCode.P;
            break;
            
            case "q":
                keyCode = KeyCode.Q;
            break;

            case "r":
                keyCode = KeyCode.R;
            break;

            case "s":
                keyCode = KeyCode.S;
            break;

            case "t":
                keyCode = KeyCode.T;
            break;

            case "u":
                keyCode = KeyCode.U;
            break;

            case "v":
                keyCode = KeyCode.V;
            break;

            case "w":
                keyCode = KeyCode.W;
            break;

            case "x":
                keyCode = KeyCode.X;
            break;

            case "y":
                keyCode = KeyCode.Y;
            break;

            case "z":
                keyCode = KeyCode.Z;
            break;
        }

        return keyCode;
    }
}
