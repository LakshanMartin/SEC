package fileComparison.controller;

import java.io.File;

import fileComparison.model.ComparisonResult;
import fileComparison.model.FilesQueue;
import fileComparison.model.Queue;
import fileComparison.view.UserInterface;
import javafx.application.Platform;

public class FilesFinder implements Runnable
{
    // CLASS FIELDS
    private UserInterface ui;
    private FilesQueue filesQueue;
    private Queue queue;

    // CONSTRUCTOR
    public FilesFinder(UserInterface ui, FilesQueue filesQueue, Queue queue) 
    {
        this.ui = ui;
        this.filesQueue = filesQueue;
        this.queue = queue;
    }

    /**
     * 
     * REFERENCE: https://mkyong.com/java/how-to-find-files-with-certain-extension-only/
     */
    @Override
    public void run()
    {
        String[] filesToComp;
        double similarity;
        String filename1, filename2;
        ComparisonResult POISON_PILL = new ComparisonResult("POISON", "PILL", 4.4);

        try 
        {
            // filesToComp = filesQueue.get();

            // while(filesToComp != null)
            while(true)
            {
                filesToComp = filesQueue.get();

                if(filesToComp[0].equals("POISON") && filesToComp[1].equals("PILL"))
                {
                    filesQueue.put(filesToComp); // POISON other threads
                    // System.out.println(Thread.currentThread().getName() + " Done comparing");
                    break;
                }
                // Compare file contents and calc similarity
                similarity = new FilesComparer().getSimilarity(
                    filesToComp[0], 
                    filesToComp[1]);

                // Crop filename from path
                filename1 = cropFilename(filesToComp[0]);
                filename2 = cropFilename(filesToComp[1]);

                // Create new ComparisonResult object
                ComparisonResult newResult = new ComparisonResult(filename1, filename2, similarity);

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

            // System.out.println("Compared all files");
            queue.put(POISON_PILL);
            Thread.currentThread().interrupt();
        } 
        catch(InterruptedException e) 
        {
            // System.out.println("FileFinder Thread: TERMINATED");
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
