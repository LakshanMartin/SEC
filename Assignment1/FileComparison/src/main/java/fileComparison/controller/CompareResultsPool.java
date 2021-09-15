package fileComparison.controller;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fileComparison.model.FilesQueue;
import fileComparison.model.ProgressTracker;
import fileComparison.model.ResultsQueue;
import fileComparison.view.UserInterface;
import javafx.application.Platform;

public class CompareResultsPool 
{
    // CLASS FIELDS
    private UserInterface ui;
    private ExecutorService threadPool;
    private FilesQueue filesQueue;
    private int numFiles;
    private ResultsQueue resultsQueue;

    // EMPTY CONSTRUCTOR
    public CompareResultsPool(UserInterface ui, int numFiles, FilesQueue filesQueue, ResultsQueue resultsQueue) 
    {
        this.ui = ui;
        this.numFiles = numFiles;
        this.filesQueue = filesQueue;
        this.resultsQueue = resultsQueue;
    }
    
    public void start(File outputFile)
    {
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        
        ProgressTracker progressTracker;
        int numComparisons;

        numComparisons = calcNumComparisons();
        progressTracker = new ProgressTracker(numComparisons);

        // Update Status Bar in the GUI thread
        Platform.runLater(() ->
        {
            ui.updateStatusBar(numFiles, numComparisons);
        });

        for(int i = 0; i < 10; i++)
        {
            threadPool.execute(new FilesComparer(ui, progressTracker, filesQueue, resultsQueue));
            threadPool.execute(new ResultsOutput(resultsQueue, outputFile));
        }
    }

    private int calcNumComparisons()
    {
        return ((numFiles * numFiles) - numFiles) / 2;
    }

    public void stop()
    {        
        try
        {
            threadPool.shutdown();
            threadPool.awaitTermination(1, TimeUnit.SECONDS);
            threadPool.shutdownNow();
        }
        catch(InterruptedException e)
        {
            threadPool.shutdownNow();
        }
    }
}
