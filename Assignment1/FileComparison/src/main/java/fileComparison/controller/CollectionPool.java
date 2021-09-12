package fileComparison.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fileComparison.model.FilesQueue;

public class CollectionPool 
{
    private FilesQueue filesQueue;
    private List<String> filesList;

    public CollectionPool(FilesQueue filesQueue, List<String> filesList)
    {
        this.filesQueue = filesQueue;
        this.filesList = filesList;
    }

    public void start()
    {
        ExecutorService threadPool; 
        // String[] POISON_PILL = {"POISON", "PILL"};
        // Files POISON_PILL = new Files("POISON", "PILL");
        int numThreads = Runtime.getRuntime().availableProcessors() / 2;
        
        threadPool = Executors.newFixedThreadPool(numThreads);
        // threadPool = Executors.newFixedThreadPool(filesList.size());

        for(int i = 0; i < filesList.size()-1; i++)
        {
            threadPool.execute(new AddFiles(filesQueue, filesList, i));
        }
        
        // try 
        // {
        //     filesQueue.put(POISON_PILL);
        //     Thread.sleep(1000);
        // } 
        // catch(InterruptedException e) 
        // {
        //     // e.printStackTrace();
        // }

        threadPool.shutdown();
    }
}
