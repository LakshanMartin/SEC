package fileComparison;

import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UserInterface
{
    // CLASS FIELDS
    private TableView<ComparisonResult> resultTable = new TableView<>();  
    private ProgressBar progressBar = new ProgressBar();
    private Thread finderThread = null;
    private FilesFinder filesFinder;
    private ComparisonResult result;
    private OutputResults output;
    private File outputFile; 
    private FileWriter writer;
    // private ExecutorService pool = Executors.newFixedThreadPool(4);
    private BlockingQueue<ComparisonResult> queue = new ArrayBlockingQueue<>(100);

    // CONSTRUCTOR
    public UserInterface() 
    {
        filesFinder = new FilesFinder(this, queue);
    }

    public void show(Stage stage)
    {
        stage.setTitle("File Comparison App");
        stage.setMinWidth(600);

        // Create toolbar
        Button compareBtn = new Button("Compare...");
        Button stopBtn = new Button("Stop");
        ToolBar toolBar = new ToolBar(compareBtn, stopBtn);
        
        // Set up button event handlers.
        compareBtn.setOnAction((event) ->
        { 
            compareFiles(stage);
            // checkResults();
            outputResults();
        });

        stopBtn.setOnAction(event -> stopComparison());
        
        // Initialise progressbar
        progressBar.setProgress(0.0);
        
        TableColumn<ComparisonResult,String> file1Col = new TableColumn<>("File 1");
        TableColumn<ComparisonResult,String> file2Col = new TableColumn<>("File 2");
        TableColumn<ComparisonResult,String> similarityCol = new TableColumn<>("Similarity");
        
        // The following tell JavaFX how to extract information from a ComparisonResult 
        // object and put it into the three table columns.
        file1Col.setCellValueFactory(   
            (cell) -> new SimpleStringProperty(cell.getValue().getFile1()) );
            
        file2Col.setCellValueFactory(   
            (cell) -> new SimpleStringProperty(cell.getValue().getFile2()) );
            
        similarityCol.setCellValueFactory(  
            (cell) -> new SimpleStringProperty(
                String.format("%.1f%%", cell.getValue().getSimilarity() * 100.0)) );
          
        // Set and adjust table column widths.
        file1Col.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.40));
        file2Col.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.40));
        similarityCol.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.20));            
        
        // Add the columns to the table.
        resultTable.getColumns().add(file1Col);
        resultTable.getColumns().add(file2Col);
        resultTable.getColumns().add(similarityCol);

        // Add the main parts of the UI to the window.
        BorderPane mainBox = new BorderPane();
        mainBox.setTop(toolBar);
        mainBox.setCenter(resultTable);
        mainBox.setBottom(progressBar);
        Scene scene = new Scene(mainBox);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();      
    }

    private void compareFiles(Stage stage)
    {
        DirectoryChooser dc = new DirectoryChooser();
        File directory; 
        
        // Find directory to compare files
        dc.setInitialDirectory(new File("."));
        dc.setTitle("Choose directory");
        directory = dc.showDialog(stage);

        // Find and compare files in separate thread
        Runnable fileFinderTask = () ->
        {
            try 
            {
                filesFinder.filesToCompare(directory.toPath());
            } 
            catch(IOException e) 
            {
                System.out.println("IO ERROR: " + e.getMessage());
            }
            catch(InterruptedException e)
            {
                System.out.println(finderThread.getName() + ": TERMINATED");
            }
        };

        finderThread = new Thread(fileFinderTask, "Finder-Thread");
        finderThread.start();
        
        System.out.println("Comparing files within " + directory + "...");
        
        // Extremely fake way of demonstrating how to use the progress bar (noting that it can 
        // actually only be set to one value, from 0-1, at a time.)
        // progressBar.setProgress(0.25);
        // progressBar.setProgress(0.5);
        // progressBar.setProgress(0.6);
        // progressBar.setProgress(0.85);
        // progressBar.setProgress(1.0);

        // Extremely fake way of demonstrating how to update the table (noting that this shouldn't
        // just happen once at the end, but progressively as each result is obtained.)
        // List<ComparisonResult> newResults = new ArrayList<>();
        // newResults.add(new ComparisonResult("Example File 1", "Example File 2", 0.75));
        // newResults.add(new ComparisonResult("Example File 1", "Example File 3", 0.31));
        // newResults.add(new ComparisonResult("Example File 2", "Example File 3", 0.45));
        
        // resultTable.getItems().setAll(newResults);        
        
        // progressBar.setProgress(0.0); // Reset progress bar after successful comparison?
    }
    
    private void stopComparison()
    {
        System.out.println("Stopping comparison...");
    }

    /**
     * Add newResult data to results table
     * @param newResult
     */
    public void updateResultsTable(ComparisonResult newResult)
    {
        resultTable.getItems().add(newResult);
    }

    /**
     * Update the progress bar with the imported value
     * @param progress
     */
    public void updateProgressBar(double progress)
    {
        progressBar.setProgress(progress);
        System.out.println("Progress: " + progress);
    }

    private void outputResults()
    {
        outputFile = new File("src/main/output/", "results.csv");
        output = new OutputResults(queue, outputFile);
        output.runTasks();
    }
}
