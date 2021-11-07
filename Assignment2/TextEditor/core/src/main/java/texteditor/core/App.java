/**
 * Software Engineering Concepts - COMP3003
 * Assignment 2
 * Semester 2, 2021
 * 
 * Purpose: This application is an example of a text editor, that incorporates 
 *          I18N, Plugins, Domain Specific Language utilisation through JavaCC.
 * 
 * Author: Lakshan Martin
 * ID: 13983521
 * Submission Date: 31st October 2021
 */

package texteditor.core;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


/**
 * Entry point for the application.
 * 
 * Args can be entered as follows:
 *  ./gradlew run --args='--locale=fake-A' to load relevate language bundles
 * 
 * Keymap will be loaded if in valid format. Else, the application will load
 * without custom keybindings.
 */
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
        List<Keybind> keybindList;
        
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

        // Load keymap
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

