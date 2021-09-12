package fileComparison.controller;

import java.util.List;

import fileComparison.model.Files;
import fileComparison.model.FilesQueue;

public class AddFiles implements Runnable
{
    private FilesQueue filesQueue;
    private List<String> filesList;
    private int startPoint;

    public AddFiles(FilesQueue filesQueue, List<String> filesList, int startPoint) 
    {
        this.filesQueue = filesQueue;
        this.filesList = filesList;
        this.startPoint = startPoint;
    }

    @Override
    public void run()
    {
        // String[] filesToComp = new String[2];
        String file1, file2;
        Files files;

        for(int i = startPoint; i < filesList.size()-1; i++)
        {
            // filesToComp[0] = filesList.get(startPoint).toString();
            // filesToComp[1] = filesList.get(i + 1).toString();  
            file1 = filesList.get(startPoint).toString();
            file2 = filesList.get(i + 1).toString();  

            // System.out.println(filesToComp[0] + " " + filesToComp[1]);
            // System.out.println(file1 + " " + file2);

            files = new Files(file1, file2);

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
