package fileComparison;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OutputResults 
{
    // CLASS FIELDS
    private BlockingQueue<ComparisonResult> queue;
    private File outputFile;
    private ExecutorService pool;

    // CONSTRUCTOR
    public OutputResults(BlockingQueue<ComparisonResult> queue, File outputFile)
    {
        this.queue = queue;
        this.outputFile = outputFile;
        this.pool = Executors.newFixedThreadPool(4);
    }

    public void runTasks()
    {
        for(int i = 0; i < 2; i++)
        {
            Runnable writeFileTask = () ->
            {
                writeFile();
            };

            pool.execute(writeFileTask);
        }

        pool.shutdown();
    }

    private void writeFile()
    {
        FileWriter writer;
        ComparisonResult result;

        while(true)
        {
            result = queue.peek();

            if(result != null)
            {
                if(!result.getIsPoison())
                {
                    try
                    {
                        result = queue.take();
                        writer = new FileWriter(outputFile.getAbsolutePath(), true);
        
                        writer.write(result.getFile1());
                        writer.write(",");
                        writer.write(result.getFile2());
                        writer.write(",");
                        writer.write(String.valueOf(result.getSimilarity()));  
                        writer.write("\n"); 

                        writer.close();

                        Thread.sleep(500);
                    }
                    catch(IOException e)
                    {
                        System.out.println("IO ERROR: " + e.getMessage());
                    }
                    catch(InterruptedException e)
                    {
                        System.out.println("Thread in Pool: TERMINATED");
                    } 
                }
                else
                {
                    break;
                }
            }
        } 
    }
}
