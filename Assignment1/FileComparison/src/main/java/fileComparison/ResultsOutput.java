package fileComparison;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultsOutput implements Runnable
{
    // CLASS FIELDS
    private int id;
    private Queue queue;
    private File outputFile;

    // CONSTRUCTOR
    public ResultsOutput(int id, Queue queue, File outputFile)
    {
        this.id = id;
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
            result = queue.get();

            while(queue.checkProduction() || result != null)
            {
                Thread.sleep(1000);

                writer = new FileWriter(outputFile.getAbsolutePath(), true);

                System.out.println("FileWriter Thread #" + this.id + ": WRITING");

                // Write to file in CSV format
                writer.write(result.getFile1());
                writer.write(",");
                writer.write(result.getFile2());
                writer.write(",");
                writer.write(String.valueOf(result.getSimilarity()));  
                writer.write("\n"); 

                writer.close();
      
                result = queue.get();                  
            } 

            System.out.println("FileWriter Thread #" + this.id + " done");
        }
        catch(IOException e)
        {
            System.out.println("IO ERROR: " + e.getMessage());
        }
        catch(InterruptedException e)
        {
            System.out.println("FileWriter Thread #" + this.id + ": TERMINATED");
        } 
    }
}
