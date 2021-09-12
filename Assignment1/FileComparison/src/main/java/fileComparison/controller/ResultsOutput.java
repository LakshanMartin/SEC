package fileComparison.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fileComparison.model.ComparisonResult;
import fileComparison.model.Queue;

public class ResultsOutput implements Runnable
{
    // CLASS FIELDS
    private Queue queue;
    private File outputFile;

    // CONSTRUCTOR
    public ResultsOutput(Queue queue, File outputFile)
    {
        this.queue = queue;
        this.outputFile = outputFile;
    }

    @Override
    public void run()
    {
        FileWriter writer;
        ComparisonResult result;
        
        try
        {
            // result = queue.get();
            // result = queue.peek();

            // while(result != null)// || !queue.checkProduction())
            // while(!result.getFile1().equals("POISON") && !result.getFile2().equals("PILL"))
            while(true)
            {
                result = queue.get();

                if(result.getFile1().equals("POISON") && result.getFile2().equals("PILL"))
                {
                    queue.put(result); // POISON other threads
                    // System.out.println(Thread.currentThread().getName() + " POISONED");
                    break;
                }

                writer = new FileWriter(outputFile.getAbsolutePath(), true);
                
                // System.out.println(
                //     Thread.currentThread().getName() + ": WRITING TO - " +
                //     outputFile.getName());
                    
                // Write to file in CSV format
                writer.write(result.getFile1());
                writer.write(",");
                writer.write(result.getFile2());
                writer.write(",");
                writer.write(String.valueOf(result.getSimilarity()));  
                writer.write("\n"); 
                
                writer.close();
                
                // result = queue.get();  

                Thread.sleep(100);
            } 

            // System.out.println(Thread.currentThread().getName() + " done");
        }
        catch(IOException e)
        {
            System.out.println("IO ERROR: " + e.getMessage());
        }
        catch(InterruptedException e)
        {
            // System.out.println(Thread.currentThread().getName()+ ": TERMINATED");
        } 
    }
}
