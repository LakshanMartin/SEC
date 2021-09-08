package fileComparison;

import java.io.*;
import java.util.concurrent.BlockingQueue;

public class OutputResults 
{
    // CLASS FIELDS
    ComparisonResult result;
    File outputFile;

    // CONSTRUCTOR
    public OutputResults(ComparisonResult result, File outputFile)
    {
        this.result = result;
        this.outputFile = outputFile;
    }
 
    public void writeFile() throws IOException, InterruptedException
    {
        FileWriter writer;

        writer = new FileWriter(outputFile);

        System.out.println("Writing");

        
        writer.write(result.getFile1());
        writer.write(",");
        writer.write(result.getFile2());
        writer.write(",");
        writer.write(String.valueOf(result.getSimilarity()));   
        

        writer.close();
    }
    // public OutputResults(BlockingQueue<ComparisonResult> queue, File outputFile)
    // {
    //     this.queue = queue;
    //     this.outputFile = outputFile;
    // }
 
    // public boolean writeFile() throws IOException, InterruptedException
    // {
    //     FileWriter writer;
    //     ComparisonResult result;
    //     boolean poisoned;

    //     writer = new FileWriter(outputFile);
    //     result = queue.peek();
    //     poisoned = false;

    //     System.out.println("Writing");

    //     if(result != null && !result.getIsPoison())
    //     {
    //         System.out.println("Writing");
    //         result = queue.take();
    //         writer.write(result.getFile1());
    //         writer.write(",");
    //         writer.write(result.getFile2());
    //         writer.write(",");
    //         writer.write(String.valueOf(result.getSimilarity()));   
    //     }
    //     else
    //     {
    //         poisoned = true;
    //     }

    //     writer.close();

    //     return poisoned;
    // }
}
