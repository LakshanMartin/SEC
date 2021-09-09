package fileComparison.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fileComparison.model.ComparisonResult;
import fileComparison.model.Queue;
import fileComparison.view.UserInterface;
import javafx.application.Platform;

public class FilesFinder implements Runnable
{
    // CLASS FIELDS
    private UserInterface ui;
    private Path path;
    private Queue queue;
    private String[] fileExtensions = {"txt", "md", "java", "cs"};
    private List<String> result;

    // CONSTRUCTOR
    public FilesFinder(UserInterface ui, Path path, Queue queue) 
    {
        this.ui = ui;
        this.path = path;
        this.queue = queue;
    }

    /**
     * 
     * REFERENCE: https://mkyong.com/java/how-to-find-files-with-certain-extension-only/
     */
    @Override
    public void run()
    {
        double similarity;
        String filename1, filename2;

        try
        {
            try(Stream<Path> walk = Files.walk(path, 1))
            {
                result = walk
                            .filter(p -> !checkEmpty(p.toFile()))
                            .map(p -> p.toString())
                            .filter(f -> Arrays.stream(fileExtensions).anyMatch(f::endsWith))
                            .collect(Collectors.toList());
            }

            for(int i = 0; i < result.size(); i++)
            {
                for(int j = i+1; j < result.size(); j++)
                {
                    // Calculate current progress of comparisons
                    calcProgress(i+1, result.size());

                    // Compare file contents and calc similarity
                    similarity = new FilesComparer().getSimilarity(
                                        result.get(i).toString(), 
                                        result.get(j).toString());

                    // Crop filename from path
                    filename1 = cropFilename(result.get(i));
                    filename2 = cropFilename(result.get(j));

                    // Create new ComparisonResult object
                    ComparisonResult newResult = new ComparisonResult(filename1, filename2, similarity, false); 
                    
                    // Add to BlockingQueue
                    queue.put(newResult);

                    // Update GUI with similarity results > 0.5
                    if(newResult.getSimilarity() > 0.5)
                    {
                        Platform.runLater(() ->
                        {
                            ui.updateResultsTable(newResult);
                        });
                    }

                    Thread.sleep(100);
                }
            }

            // End of production
            queue.stoppedProduction();
            System.out.println("FileFinding Thread done");
            
            // Update progress upon completion of comparisons
            calcProgress(result.size(), result.size());
            Platform.runLater(() ->
            {
                ui.updateProgressBar(0.0);
            });
        }
        catch(IOException e)
        {
            System.out.println("IO ERROR: " + e.getMessage());
        }
        catch(InterruptedException e)
        {
            System.out.println("FileFinder Thread: TERMINATED");
        }
    }

    /**
     * Calculate current progress and update GUI progress bar
     * @param current
     * @param end
     * @throws InterruptedException
     */
    private void calcProgress(int current, int end) throws InterruptedException
    {
        double progress;

        progress = (double)current / (double)end;

        // Update GUI
        Platform.runLater(() ->
        {
            ui.updateProgressBar(progress);
        });
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
