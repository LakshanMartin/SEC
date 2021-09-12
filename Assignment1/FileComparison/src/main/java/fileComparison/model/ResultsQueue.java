package fileComparison.model;

import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

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
        // return this.queue.poll(1, TimeUnit.SECONDS);
    }

    public ComparisonResult peek() throws InterruptedException
    {
        return this.queue.peek();
    }
}
