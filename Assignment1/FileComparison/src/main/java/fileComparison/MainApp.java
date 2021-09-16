package fileComparison;

import fileComparison.view.UserInterface;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Software Engineering Concepts - COMP3003
 * Assignment 1
 * Semester 2, 2021
 * 
 * Purpose: This application is a demonstration of a multithreaded system,
 *          utilising blocking queues and thread pools to analyse textual
 *          similarities between two valid files.
 * 
 * Author: Lakshan Martin
 * ID: 13983521
 * Submission Date: 16th September 2021
 */
public class MainApp extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);  
    }

    @Override
    public void start(Stage stage)
    {
        new UserInterface().show(stage);
    }
}
