package fileComparison.view;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fileComparison.controller.AccessDirectory;
import fileComparison.controller.CollectionPool;
import fileComparison.controller.CompareResultsPool;
import fileComparison.model.ComparisonResult;
import fileComparison.model.Files;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class contains all functions related to the User and System interface.
 * 
 * REFERENCE #1: Obtained from Dr David Cooper, uidemo. Majority of UI 
 *               components are drawn from the uidemo program provided
 * REFERENCE #2: Obtained from mysteryHistery, 
 *               https://stackoverflow.com/questions/38371117/close-event-in-java-fx-when-close-button-is-pressed
 *               (Accessed on 10 September 2021).
 */
public class UserInterface
{
    // CLASS FIELDS
    private TableView<ComparisonResult> resultTable = new TableView<>();  
    private ProgressBar progressBar = new ProgressBar();
    private Label numFiles;
    private Label numComparisons;
    private ExecutorService es;
    private int resultsCount = 0;
    private BlockingQueue<Files> filesQueue = new ArrayBlockingQueue<>(100);
    private BlockingQueue<ComparisonResult> resultsQueue = new ArrayBlockingQueue<>(100);
    private CompareResultsPool compareResultsPool;
    private CollectionPool collectionPool;
    private static final String OUTPUT_PATH = "src/main/output/";

    // EMPTY CONSTRUCTOR
    public UserInterface() {}

    /**
     * Generates the window interface to be used             
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

        // Create status bar
        numFiles = new Label("No. of files found: ...");
        numComparisons = new Label("No. of comparisons to be done: ...");
        Separator separator1 = new Separator(Orientation.VERTICAL);
        Separator separator2 = new Separator(Orientation.VERTICAL);
        HBox statusBar = new HBox(20,progressBar, separator1, numFiles, separator2, numComparisons);
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setStyle("-fx-padding: 4 0 4 0");
        
        // Set up button event handlers.
        compareBtn.setOnAction((event) ->
        { 
            resultTable.getItems().clear();

            if(compareResultsPool != null)
            {
                // Shut down existing pools
                collectionPool.stop();
                compareResultsPool.stop();

                // Clear queues
                filesQueue.clear();
                resultsQueue.clear();
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
        mainBox.setBottom(statusBar);
        Scene scene = new Scene(mainBox);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();    

        // Add Window closing event -- SEE REFERENCE #2
        stage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event) 
            {
                System.out.println("Program closed");

                if(compareResultsPool != null)
                {
                    System.exit(0);
                }
            }
        }); // -- END OF REFERENCE #2 MATERIAL
    }

    /**
     * Step by step process of comparing files within a directory
     * @param stage
     */
    private void compareFiles(Stage stage)
    {
        File directory, resultsFile;
        List<String> filesList = null;

        directory = selectDirectory(stage);

        // Check that the 'Cancel' button wasn't selected from the
        // 'Choose directory' window
        if(directory != null)
        {
            resultsFile = generateResultsFile();
            filesList = getFilesList(directory);

            if(filesList.size() != 0)
            {
                startThreadPools(filesList, resultsFile);
            }
        }
    }

    /**
     * Open window for user to select a directory containing files to compare
     * 
     * REFERENCE: Obtained from Dr David Cooper, uidemo.
     * @param stage
     * @return
     */
    private File selectDirectory(Stage stage)
    {
        DirectoryChooser dc = new DirectoryChooser();
        File directory; 

        // Find directory to compare files
        dc.setInitialDirectory(new File("src/main/resources"));
        dc.setTitle("Choose directory");
        directory = dc.showDialog(stage);
        
        return directory;
    }

    private File generateResultsFile()
    {
        String filename;
        File resultsFile;

        // Increment results.csv file number based on number of directories
        // selected for file comparisons
        resultsCount++;
        filename = "results" + resultsCount + ".csv";
        resultsFile  = new File(OUTPUT_PATH, filename);

        resultsFile = checkFilename(filename, resultsFile);

        return resultsFile;
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
            resultsCount++;
            filename = "results" + resultsCount + ".csv";
            outputFile = new File(OUTPUT_PATH, filename);
        }

        return outputFile;
    }

    /**
     * Retrieve valid files from chosen directory
     * @param directory
     */
    private List<String> getFilesList(File directory)
    {
        AccessDirectory access;
        List<String> filesList = null;
      
        // Open directory and use single thread pool to retrieve files
        access = new AccessDirectory(directory.toPath());
        es  = Executors.newSingleThreadExecutor();
        Future<List<String>> future = es.submit(access);

        // Retrieve list of files and shutdown thread pool
        try 
        {
            filesList = future.get();
            es.shutdown();
            es = null;
        } 
        catch(InterruptedException | ExecutionException e) 
        {
            e.printStackTrace();
        }

        return filesList;
    }

    /**
     * Initialise and start the CollectionPool and CompareResultsPool
     * @param filesList
     * @param outputFile
     */
    private void startThreadPools(List<String> filesList, File outputFile)
    {
        collectionPool = new CollectionPool(filesQueue, filesList);
        collectionPool.start();
        compareResultsPool = new CompareResultsPool(this, filesList.size(), filesQueue, resultsQueue);
        compareResultsPool.start(outputFile);
    }
    
    /**
     * Shut down thread pools
     */
    private void stopComparison()
    {
        if(collectionPool != null || compareResultsPool != null)
        {
            System.out.println("Stopping comparison...");
    
            collectionPool.stop();
            compareResultsPool.stop();
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

    /**
     * Update status bar to provide the following info:
     *  - number of valid files found to compare
     *  - number of comparisons to be done
     * @param files
     * @param comparisons
     */
    public void updateStatusBar(int files, int comparisons)
    {
        numFiles.setText("No. of files found: " + files);
        numComparisons.setText("No. of comparisons to be done: " + comparisons);
    }
}
