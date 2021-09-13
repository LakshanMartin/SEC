package fileComparison.controller;

import fileComparison.model.ComparisonResult;
import fileComparison.model.Files;
import fileComparison.model.FilesQueue;
import fileComparison.model.ResultsQueue;
import fileComparison.view.UserInterface;
import javafx.application.Platform;

public class FilesComparer implements Runnable
{
    // CLASS FIELDS
    private UserInterface ui;
    private FilesQueue filesQueue;
    private ResultsQueue resultsQueue;

    // CONSTRUCTOR
    public FilesComparer(UserInterface ui, FilesQueue filesQueue, ResultsQueue resultsQueue) 
    {
        this.ui = ui;
        this.filesQueue = filesQueue;
        this.resultsQueue = resultsQueue;
    }

    /**
     * 
     * REFERENCE: https://mkyong.com/java/how-to-find-files-with-certain-extension-only/
     */
    @Override
    public void run()
    {
        // String[] filesToComp;
        Files filesToComp;
        double similarity;
        String filename1, filename2;

        try 
        {
            while(true)
            {
                filesToComp = filesQueue.get();

                // Compare file contents and calc similarity
                similarity = new SimilarityAlgo().getSimilarity(
                    filesToComp.getFile1(), 
                    filesToComp.getFile2());
                    
                // Crop filename from path
                filename1 = cropFilename(filesToComp.getFile1());
                filename2 = cropFilename(filesToComp.getFile2());
                
                // Create new ComparisonResult object
                ComparisonResult newResult = new ComparisonResult(filename1, filename2, similarity);
                
                // Add to BlockingQueue
                resultsQueue.put(newResult);
                
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
        catch(InterruptedException e) 
        {
            System.out.println("File Comparison done");
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
