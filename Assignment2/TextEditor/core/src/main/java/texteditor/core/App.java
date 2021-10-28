/*
 * This Java source file was generated by the Gradle 'init' task.
 */

package texteditor.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;



public class App extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        ResourceBundle bundle;
        var localeString = getParameters().getNamed().get("locale");
        
        if(localeString != null && localeString.contains("-"))
        {
            String[] split = localeString.split("-");
            
            if(split.length == 2)
            {
                // Specified language property to be used
                Locale locale1 = new Locale(split[0], split[1]);
                bundle = ResourceBundle.getBundle("bundle", locale1);
                System.out.println("Using locale bundle");
            }
            else // Default language properties used
            {
                bundle = ResourceBundle.getBundle("bundle");
            }
        }
        else // Default language properties used
        {
            bundle = ResourceBundle.getBundle("bundle");
        }
        
        KeymapParse keymap = new KeymapParse();
        TextArea textArea = new TextArea();
        FileIO fileIO = new FileIO();
        LoadSaveUI loadSaveUI = new LoadSaveUI(stage, bundle, fileIO, textArea);
        MainUI mainUI = new MainUI(stage, bundle, loadSaveUI, textArea, keymap);

        try
        {
            mainUI.parseKeymap();
        }
        catch(IOException | ParseException e)
        {
            new Alert(
                        Alert.AlertType.ERROR,
                        String.format(
                            bundle.getString("parse_error"), 
                            e.getClass().getName(), 
                            e.getMessage()),
                            new ButtonType(bundle.getString("close_btn"), ButtonBar.ButtonData.CANCEL_CLOSE)
                    ).showAndWait(); 
        }

        mainUI.display();
    } 
}

