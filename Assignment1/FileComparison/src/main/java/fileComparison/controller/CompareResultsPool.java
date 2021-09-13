package fileComparison.controller;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fileComparison.model.FilesQueue;
import fileComparison.model.ProgressTracker;
import fileComparison.model.ResultsQueue;
import fileComparison.view.UserInterface;

public class CompareResultsPool 
{
    // CLASS FIELDS
    private UserInterface ui;
    private ExecutorService threadPool;
    private FilesQueue filesQueue;
    private int numFiles;

    // EMPTY CONSTRUCTOR
    public CompareResultsPool(UserInterface ui, FilesQueue filesQueue, int numFiles) 
    {
        this.ui = ui;
        this.filesQueue = filesQueue;
        this.numFiles = numFiles;
    }
    
    public void start(File outputFile)
    {
        ExecutorService threadPool = Executors.newFixedThreadPool(500);
        ResultsQueue resultsQueue = new ResultsQueue();
        ProgressTracker progressTracker;
        int numComparisons;

        numComparisons = calcNumComparisons();
        progressTracker = new ProgressTracker(numComparisons);

        for(int i = 0; i < 500; i++)
        {
            threadPool.execute(new FilesComparer(ui, progressTracker, filesQueue, resultsQueue));
            threadPool.execute(new ResultsOutput(resultsQueue, outputFile));
        }

        threadPool.shutdown();
    }

    private int calcNumComparisons()
    {
        return ((numFiles * numFiles) - numFiles) / 2;
    }

    public void stop()
    {
        threadPool.shutdownNow();
    }
}
