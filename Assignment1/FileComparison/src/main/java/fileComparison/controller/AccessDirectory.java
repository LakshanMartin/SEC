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

/**
 * This class operates independently within it's own thread with a callable
 * function to return the list of valid files found within a directory path
 */
public class AccessDirectory implements Callable<List<String>>
{
    // CLASS FIELDS
    private Path path;

    // CONSTRUCTOR
    public AccessDirectory(Path path) 
    {
        this.path = path;
    }

    /**
     * Compile a list of file paths within the directory
     * REFERENCE: Obtained from Mkyong. "How to find files with the file
     *            extension in Java". Accessed 6th September 2021.
     */
    @Override
    public List<String> call()
    {
        List<String> filesList = null;
        String[] fileExtensions = {"txt", "md", "java", "cs"};

        try 
        {
            // SEE REFERENCE
            try(Stream<Path> walk = Files.walk(path, 1))
            {
                filesList = walk
                            .filter(p -> !checkEmpty(p.toFile())) // Ignore empty files
                            .map(p -> p.toString()) // Convert Path to String
                            .filter(f -> Arrays.stream(
                                fileExtensions)
                                    .anyMatch(f::endsWith)) // Ignore file extensions not listed
                            .collect(Collectors.toList()); // Compile to List
            } // -- END OF REFERENCE MATERIAL
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
