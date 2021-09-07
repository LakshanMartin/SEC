package fileComparison;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application 
{
    public static void main(String[] args) 
    {
        Application.launch(args);   
    }

    @Override
    public void start(Stage stage)
    {
        new UserInterface().show(stage);
    }
}
