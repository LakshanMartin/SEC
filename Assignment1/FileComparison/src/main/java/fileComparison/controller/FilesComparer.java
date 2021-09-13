package fileComparison.controller;

import fileComparison.model.ComparisonResult;
import fileComparison.model.Files;
import fileComparison.model.FilesQueue;
import fileComparison.model.ProgressTracker;
import fileComparison.model.ResultsQueue;
import fileComparison.view.UserInterface;
import javafx.application.Platform;

public class FilesComparer implements Runnable
{
    // CLASS FIELDS
    private UserInterface ui;
    private ProgressTracker progressTracker;
    private FilesQueue filesQueue;
    private ResultsQueue resultsQueue;
    private static final ComparisonResult POISON_PILL = new ComparisonResult("POISON", "PILL", 4.4);

    // CONSTRUCTOR
    public FilesComparer(UserInterface ui, ProgressTracker progressTracker, FilesQueue filesQueue,        ResultsQueue resultsQueue) 
    {
        this.ui = ui;
        this.progressTracker = progressTracker;
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
                
                // Update progress tracker
                progressTracker.add();

                // Update GUI ProgressBar
                Platform.runLater(() ->
                {
                    ui.updateProgressBar(progressTracker.getProgress());
                });

                
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
                
                // Reset ProgressBar to zero when comparisons are complete
                if(progressTracker.getProgress() == 1.0)
                {
                    Platform.runLater(() ->
                    {
                        ui.updateProgressBar(0.0);
                    });

                    break;
                }
                
                Thread.sleep(100);
            }
            
            resultsQueue.put(POISON_PILL);

            Thread.currentThread().interrupt();
        } 
        catch(InterruptedException e) 
        {
            System.out.println("FileComparer interrupted");
        }
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
