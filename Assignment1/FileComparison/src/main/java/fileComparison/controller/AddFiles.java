package fileComparison.controller;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import fileComparison.model.Files;

/**
 * This class sorts the files to be compared and adds it to the BlockingQueue
 */
public class AddFiles implements Runnable
{
    // CLASS FIELDS
    private BlockingQueue<Files> filesQueue;
    private List<String> filesList;
    private int startPoint;

    // CONSTRUCTOR
    public AddFiles(BlockingQueue<Files> filesQueue, List<String> filesList, int startPoint) 
    {
        this.filesQueue = filesQueue;
        this.filesList = filesList;
        this.startPoint = startPoint;
    }

    /**
     * Creates a Files object containing pairs of files to be compared later
     */
    @Override
    public void run()
    {
        String file1, file2;
        Files files;

        // Loop adds the initial file and subsequent following files to Files object
        for(int i = startPoint; i < filesList.size()-1; i++)
        {
            file1 = filesList.get(startPoint).toString();
            file2 = filesList.get(i + 1).toString();  

            files = new Files(file1, file2);

            // Add Files object to BlockingQueue
            try 
            {
                filesQueue.put(files);

                Thread.sleep(100);
            } 
            catch(InterruptedException e) 
            {
                System.out.println("CollectionPool shutdown");
            }
        }
    }
}
