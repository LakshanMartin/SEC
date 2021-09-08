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
    public void filesToCompare(Path path) throws IOException
    {
        List<ComparisonResult> newResults = new ArrayList<>();
        CompareFiles compare;
        double similarity;
        String filename1, filename2;

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
                // Compare file contents and calc similarity
                compare = new CompareFiles(result.get(i).toString(), result.get(j).toString());
                similarity = compare.getSimilarity();

                // Crop filename from path
                filename1 = cropFilename(result.get(i));
                filename2 = cropFilename(result.get(j));

                ComparisonResult newResult = new ComparisonResult(filename1, filename2, similarity); 

                Platform.runLater(() ->
                {
                    ui.updateResultsTable(newResult);
                });

                // newResults.add(
                //     new ComparisonResult(filename1, filename2, similarity)); 

                // Platform.runLater(() ->
                // {
                //     ui.updateResultsTable(newResults);
                // });
            }
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

    /**
     * Extract the filename from the file path
     * @param path
     * @return
     */
    private String cropFilename(String path)
    {
        String filename;
        String[] split;

        split = path.split("/");
        filename = split[split.length - 1];

        return filename;
    }
}
