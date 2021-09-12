package fileComparison;

import fileComparison.view.UserInterface;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application 
{
    private static String[] threads;

    public static void main(String[] args) 
    {
        // threads = args;
        launch(args);  
    }

    @Override
    public void start(Stage stage)
    {
        // new UserInterface(threads).show(stage);
        new UserInterface().show(stage);
    }
}
