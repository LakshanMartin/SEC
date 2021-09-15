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

public class AccessDirectory implements Callable<List<String>>
{
    private Path path;

    public AccessDirectory(Path path) 
    {
        this.path = path;
    }

    @Override
    public List<String> call()
    {
        List<String> filesList = null;
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
        } 
        catch(IOException e) 
        {
            System.out.println("IO ERROR: " + e.getMessage());
        }

        return filesList;
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
