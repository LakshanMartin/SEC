package fileComparison.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fileComparison.model.ComparisonResult;
import fileComparison.model.ResultsQueue;

/**
 * This class is responsible for the outputting of comparison results to a CSV
 * file
 */
public class ResultsOutput implements Runnable
{
    // CLASS FIELDS
    private ResultsQueue resultsQueue;
    private File outputFile;

    // CONSTRUCTOR
    public ResultsOutput(ResultsQueue resultsQueue, File outputFile)
    {
        this.resultsQueue = resultsQueue;
        this.outputFile = outputFile;
    }

    /**
     * Retrieves the ComparisonResult data from the 2nd BlockingQueue to be output
     * the CSV file
     */
    @Override
    public void run()
    {
        FileWriter writer;
        ComparisonResult result;
        
        try
        {
            while(!Thread.currentThread().isInterrupted())
            {
                result = resultsQueue.get();

                // Checks if retrieved data is a poison pill
                if(result.getFile1().equals("POISON") && result.getFile2().equals("PILL"))
                {
                    resultsQueue.put(result); // POISON other threads
                    break;
                }
                
                writer = new FileWriter(outputFile.getAbsolutePath(), true);
                
                // Write to file in CSV format
                writer.write(result.getFile1());
                writer.write(",");
                writer.write(result.getFile2());
                writer.write(",");
                writer.write(String.valueOf(result.getSimilarity()));  
                writer.write("\n"); 
                
                writer.close(); 
            } 
        }
        catch(IOException e)
        {
            System.out.println("IO ERROR: " + e.getMessage());
        }
        catch(InterruptedException e)
        {
            System.out.println(Thread.currentThread().getName() + ": ResultsOutput interrupted");
        } 
        finally
        {
            Thread.currentThread().interrupt();
        }
    }
}
