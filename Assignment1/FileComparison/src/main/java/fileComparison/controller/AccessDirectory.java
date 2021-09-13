package fileComparison.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fileComparison.model.FilesQueue;

public class AccessDirectory implements Callable<Integer>
{
    private FilesQueue filesQueue;
    private Path path;
    private CollectionPool collectionPool;

    public AccessDirectory(FilesQueue filesQueue, Path path) 
    {
        this.filesQueue = filesQueue;
        this.path = path;
    }

    @Override
    public Integer call()
    {
        List<String> filesList;
        int numFiles = 0; 
        String[] fileExtensions = {"txt", "md", "java", "cs"};

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

            numFiles = filesList.size();

            collectionPool = new CollectionPool(filesQueue, filesList);
            collectionPool.start();
        } 
        catch(IOException e) 
        {
            System.out.println("IO ERROR: " + e.getMessage());
        }

        return numFiles;
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
