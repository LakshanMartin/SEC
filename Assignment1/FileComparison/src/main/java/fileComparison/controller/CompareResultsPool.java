package fileComparison.controller;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fileComparison.model.FilesQueue;
import fileComparison.model.ResultsQueue;
import fileComparison.view.UserInterface;

public class CompareResultsPool 
{
    // CLASS FIELDS
    private UserInterface ui;
    private ExecutorService threadPool;
    private FilesQueue filesQueue;

    // EMPTY CONSTRUCTOR
    public CompareResultsPool(UserInterface ui, FilesQueue filesQueue) 
    {
        this.ui = ui;
        this.filesQueue = filesQueue;
        threadPool = Executors.newFixedThreadPool(500);
    }
    
    public void start(File outputFile)
    {
        ResultsQueue resultsQueue = new ResultsQueue();

        for(int i = 0; i < 200; i++)
        {
            threadPool.execute(new FilesComparer(ui, filesQueue, resultsQueue));
        }

        for(int i = 0; i < 150; i++)
        {
            threadPool.execute(new ResultsOutput(resultsQueue, outputFile));
        }

        threadPool.shutdown();
    }

    public void stop()
    {
        threadPool.shutdownNow();
    }
}
