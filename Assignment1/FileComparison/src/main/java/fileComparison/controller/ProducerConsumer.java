package fileComparison.controller;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fileComparison.model.Queue;
import fileComparison.view.UserInterface;

public class ProducerConsumer 
{
    // CLASS FIELDS
    private UserInterface ui;
    private Path path;
    private int numThreads = 7;
    private ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);

    // EMPTY CONSTRUCTOR
    public ProducerConsumer(UserInterface ui, Path path) 
    {
        this.ui = ui;
        this.path = path;
    }
    
    public void start(File outputFile)
    {
        Queue queue = new Queue();
        
        // Create single Producer thread
        threadPool.execute(new FilesFinder(ui, path, queue));

        // Use remaining thread pool to create Consumer threads
        for(int i = 1; i < numThreads; i++)
        {
            threadPool.execute(new ResultsOutput(i, queue, outputFile));
        }

        threadPool.shutdown();
    }

    public void stop()
    {
        threadPool.shutdownNow();
    }
}
