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

/**
 * This class manages the thread pool related to the comparing of files and
 * output of results
 */
public class CompareResultsPool 
{
    // CLASS FIELDS
    private UserInterface ui;
    private ExecutorService threadPool;
    private FilesQueue filesQueue;
    private int numFiles;
    private ResultsQueue resultsQueue;

    // CONSTRUCTOR
    public CompareResultsPool(UserInterface ui, int numFiles, FilesQueue filesQueue, ResultsQueue resultsQueue) 
    {
        this.ui = ui;
        this.numFiles = numFiles;
        this.filesQueue = filesQueue;
        this.resultsQueue = resultsQueue;
    }
    
    /**
     * Executes the FileComparer and ResultsOutput class in individual threads
     * @param outputFile
     */
    public void start(File outputFile)
    {
        ProgressTracker progressTracker;
        int numThreads, numComparisons;
        
        // Create thread pool
        numThreads = Runtime.getRuntime().availableProcessors();
        threadPool = Executors.newFixedThreadPool(numThreads);
        
        numComparisons = calcNumComparisons();
        progressTracker = new ProgressTracker(numComparisons);

        // Update Status Bar in the GUI thread
        Platform.runLater(() ->
        {
            ui.updateStatusBar(numFiles, numComparisons);
        });

        // Execute threads per loop
        for(int i = 0; i < 1000; i++)
        {
            threadPool.execute(new FilesComparer(ui, progressTracker, filesQueue, resultsQueue));
            threadPool.execute(new ResultsOutput(resultsQueue, outputFile));
        }
    }

    /**
     * Calculate the number of comparisons to be done
     * @return
     */
    private int calcNumComparisons()
    {
        return ((numFiles * numFiles) - numFiles) / 2;
    }

    /**
     * Begins the shutdown process for the threadpool
     */
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
