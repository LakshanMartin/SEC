package fileComparison;

import fileComparison.view.UserInterface;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
        //Application.launch(args);   
    }

    @Override
    public void start(Stage stage)
    {
        new UserInterface().show(stage);
    }
}
