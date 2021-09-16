package fileComparison.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ResultsQueue 
{
    // CLASS FIELDS
    private BlockingQueue<ComparisonResult> queue = new ArrayBlockingQueue<>(90);
    
    // EMPTY CONSTRUCTOR
    public ResultsQueue() {}

    public void put(ComparisonResult newResult) throws InterruptedException
    {
        this.queue.put(newResult);
    }

    public ComparisonResult get() throws InterruptedException
    {
        return this.queue.take();
    }
}
