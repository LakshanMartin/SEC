package fileComparison.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fileComparison.model.FilesQueue;

public class CollectionPool 
{
    private FilesQueue filesQueue;
    private List<String> filesList;
    private ExecutorService threadPool;

    public CollectionPool(FilesQueue filesQueue, List<String> filesList)
    {
        this.filesQueue = filesQueue;
        this.filesList = filesList;
    }

    public void start()
    {
        int numThreads = Runtime.getRuntime().availableProcessors() / 2;
        
        threadPool = Executors.newFixedThreadPool(numThreads);

        for(int i = 0; i < filesList.size()-1; i++)
        {
            threadPool.execute(new AddFiles(filesQueue, filesList, i));
        }

        threadPool.shutdown();
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
