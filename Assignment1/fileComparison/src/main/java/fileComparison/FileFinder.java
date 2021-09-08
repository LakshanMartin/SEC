package fileComparison;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Platform;

public class FileFinder 
{
    // CLASS FIELDS
    private UserInterface ui;
    private String[] fileExtensions = {"txt", "md", "java", "cs"};
    private List<String> result;

    public FileFinder(UserInterface ui) 
    {
        this.ui = ui;
    }

    /**
     * REFERENCE: https://mkyong.com/java/how-to-find-files-with-certain-extension-only/
     * @param path
     * @throws IOException
     */
    public void findFiles(Path path) throws IOException
    {
        List<ComparisonResult> newResults = new ArrayList<>();
        CompareFiles compare;

        try(Stream<Path> walk = Files.walk(path, 1))
        {
            result = walk
                        // .filter(p -> !Files.isDirectory(p))
                        .filter(p -> !checkEmpty(p.toFile()))
                        // .map(p -> p.getFileName().toString())
                        .map(p -> p.toString())
                        .filter(f -> Arrays.stream(fileExtensions).anyMatch(f::endsWith))
                        .collect(Collectors.toList());
        }

        for(int i = 0; i < result.size(); i++)
        {
            for(int j = i+1; j < result.size(); j++)
            {
                compare = new CompareFiles(result.get(i).toString(), result.get(j).toString());
                compare.getSimilarity();

                newResults.add(new ComparisonResult(
                    result.get(i), 
                    result.get(j), 0.75));
            }
        }

        Platform.runLater(() ->
        {
            ui.updateResultsTable(newResults);
        });
    }

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
