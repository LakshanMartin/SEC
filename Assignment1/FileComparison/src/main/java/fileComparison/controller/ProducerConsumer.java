package fileComparison.controller;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fileComparison.model.FilesQueue;
import fileComparison.model.Queue;
import fileComparison.view.UserInterface;

public class ProducerConsumer 
{
    // CLASS FIELDS
    private UserInterface ui;
    private Path path;
    private int numThreads;
    private ExecutorService threadPool;
    private FilesQueue filesQueue;

    // EMPTY CONSTRUCTOR
    public ProducerConsumer(UserInterface ui, Path path, /*int numThreads,*/ FilesQueue filesQueue) 
    {
        this.ui = ui;
        this.path = path;
        // this.numThreads = 2000;
        this.filesQueue = filesQueue;
        threadPool = Executors.newFixedThreadPool(5000);
    }
    
    public void start(File outputFile)
    {
        int numFiles;
        Queue queue = new Queue();

        // Create Producer threads
        for(int i = 0; i < 4550; i++)
        {
            threadPool.execute(new FilesFinder(ui, filesQueue, queue));
        }

        for(int i = 0; i < 150; i++)
        {
            threadPool.execute(new ResultsOutput(queue, outputFile));
        }

        threadPool.shutdown();
    }

    public void stop()
    {
        threadPool.shutdownNow();
    }
}
