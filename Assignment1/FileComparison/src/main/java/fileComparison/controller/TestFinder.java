package fileComparison.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fileComparison.model.FilesQueue;

public class TestFinder implements Runnable
{
    private FilesQueue filesQueue;
    private List<String> filesList;
    private Path path;
    private String[] fileExtensions = {"txt", "md", "java", "cs"};

    public TestFinder(FilesQueue filesQueue, Path path) 
    {
        this.filesQueue = filesQueue;
        this.path = path;
    }

    @Override
    // public Integer call()
    public void run() 
    {
        int numFiles = 0;
        String[] filesToComp = new String[2];
        String[] POISON_PILL = {"POISON", "PILL"};

        try 
        {
            try(Stream<Path> walk = Files.walk(path, 1))
            {
                filesList = walk
                            .filter(p -> !checkEmpty(p.toFile()))
                            .map(p -> p.toString())
                            .filter(f -> Arrays.stream(fileExtensions).anyMatch(f::endsWith))
                            .collect(Collectors.toList());
            }
          
            for(int i = 0; i < filesList.size(); i++)
            {
                // System.out.println("BASE: " + filesList.get(i).toString());

                for(int j = i+1; j < filesList.size(); j++)
                {
                    filesToComp[0] = filesList.get(i).toString();
                    filesToComp[1] = filesList.get(j).toString();

                    numFiles++;
                    filesQueue.put(filesToComp);
                    // System.out.println("Added: " + numFiles);
                    // System.out.println("Adding to filesQueue:");
                    // System.out.println(filesToComp[0]);
                    // System.out.println(filesToComp[1]);

                    Thread.sleep(100);
                }
            }



            System.out.println("Found all valid files");
            filesQueue.put(POISON_PILL);
            Thread.currentThread().interrupt();
        } 
        catch(IOException e) 
        {
            System.out.println("IO ERROR: " + e.getMessage());
        }
        catch(InterruptedException e)
        {
            // System.out.println("FileFinder Thread: TERMINATED");
        }

        // return numFiles;
    }

    /**
     * Confirm whether file is empty
     * @param file
     * @return
     */
    private boolean checkEmpty(File file)
    {
        boolean result = true;

        if(file.length() > 0)
        {
            result = false;
        }

        return result;
    }
}
