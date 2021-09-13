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

public class AccessDirectory implements Runnable
{
    private FilesQueue filesQueue;
    private Path path;
    private int numComparisons;

    public AccessDirectory(FilesQueue filesQueue, Path path) 
    {
        this.filesQueue = filesQueue;
        this.path = path;
    }

    @Override
    public void run() 
    {
        List<String> filesList;
        String[] fileExtensions = {"txt", "md", "java", "cs"};
        CollectionPool collectionPool;

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

            numComparisons = filesList.size();

            collectionPool = new CollectionPool(filesQueue, filesList);
            collectionPool.start();
        } 
        catch(IOException e) 
        {
            System.out.println("IO ERROR: " + e.getMessage());
        }
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

    public int getNumComparisons()
    {
        return numComparisons;
    }
}
