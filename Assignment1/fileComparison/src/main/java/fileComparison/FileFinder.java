package fileComparison;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileFinder 
{
    // CLASS FIELDS
    private UserInterface ui;
    private String[] fileExtensions = {"txt", "md", "java", "cs"};

    public FileFinder(UserInterface ui) 
    {
        this.ui = ui;
    }

    public void findFiles(Path path) throws IOException
    {
        List<String> result;

        try(Stream<Path> walk = Files.walk(path, 1))
        {
            result = walk
                        .filter(p -> !Files.isDirectory(p))
                        .map(p -> p.getFileName().toString())
                        .filter(f -> Arrays.stream(fileExtensions).anyMatch(f::endsWith))
                        .collect(Collectors.toList());
        }

        for(int i = 0; i < result.size(); i++)
        {
            System.out.println(result.get(i));
        }
    }
}
