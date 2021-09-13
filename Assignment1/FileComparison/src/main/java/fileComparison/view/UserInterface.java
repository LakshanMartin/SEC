package fileComparison.view;

import java.io.File;

import fileComparison.controller.AccessDirectory;
import fileComparison.controller.CompareResultsPool;
import fileComparison.model.ComparisonResult;
import fileComparison.model.FilesQueue;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UserInterface
{
    // CLASS FIELDS
    private TableView<ComparisonResult> resultTable = new TableView<>();  
    private ProgressBar progressBar = new ProgressBar();
    private Thread fileWalkThread = null;
    private int fileCount = 0;
    private FilesQueue filesQueue = new FilesQueue();
    private CompareResultsPool compareResultsPool = null;
    private static final String OUTPUT_PATH = "src/main/output/";

    // CONSTRUCTOR
    public UserInterface() 
    {
        // this.threads = threads;
    }

    /**
     * REFERENCE #1: Obtained from Dr David Cooper, uidemo. Majority of UI 
     *               components are drawn from the uidemo program provided.
     * REFERENCE #2: Obtained from mysteryHistery, 
     *               https://stackoverflow.com/questions/38371117/close-event-in-java-fx-when-close-button-is-pressed
     *               (Accessed on 10 September 2021).
     *                
     * @param stage
     */
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
            resultTable.getItems().clear();

            if(compareResultsPool != null)
            {
                stopComparison();
            }

            compareFiles(stage);
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

        // Add Window closing event
        // SEE REFERENCE #2
        stage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event) 
            {
                System.out.println("Window closed");

                if(compareResultsPool != null)
                {
                    compareResultsPool.stop();
                }

                fileWalkThread.interrupt();
            }
        });  
    }

    private void compareFiles(Stage stage)
    {
        DirectoryChooser dc = new DirectoryChooser();
        File directory; 
        String filename;
        File outputFile;
        AccessDirectory access;

        // Find directory to compare files
        dc.setInitialDirectory(new File("src/main/resources"));
        dc.setTitle("Choose directory");
        directory = dc.showDialog(stage);

        // Check that 'Cancel' button wasn't selected from dc
        if(directory != null)
        {
            fileCount++;
            filename = "results" + fileCount + ".csv";
            outputFile = new File(OUTPUT_PATH, filename);

            outputFile = checkFilename(filename, outputFile);

            access = new AccessDirectory(filesQueue, directory.toPath());

            fileWalkThread = new Thread(access);
            fileWalkThread.start();

            

            compareResultsPool = new CompareResultsPool(this, filesQueue);
            compareResultsPool.start(outputFile);
        }
    }

    /**
     * Rename filename with incremented fileCount if filename already exists
     * @param filename
     * @param outputFile
     * @return
     */
    private File checkFilename(String filename, File outputFile)
    {
        while(outputFile.exists())
        {
            fileCount++;
            filename = "results" + fileCount + ".csv";
            outputFile = new File(OUTPUT_PATH, filename);
        }

        return outputFile;
    }
    
    private void stopComparison()
    {
        if(compareResultsPool != null)
        {
            System.out.println("Stopping comparison...");
    
            compareResultsPool.stop();
            compareResultsPool = null;
        }
        else
        {
            System.out.println("No comparisons running...");
        }
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
    }
}
