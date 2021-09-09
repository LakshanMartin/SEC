package fileComparison;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Queue 
{
    // CLASS FIELDS
    private BlockingQueue<ComparisonResult> queue = new ArrayBlockingQueue<>(90);
    private boolean stillProducing = true;
    
    // EMPTY CONSTRUCTOR
    public Queue() {}

    public void put(ComparisonResult newResult) throws InterruptedException
    {
        this.queue.put(newResult);
    }

    public ComparisonResult get() throws InterruptedException
    {
        return this.queue.poll(1, TimeUnit.SECONDS);
    }

    public boolean checkProduction()
    {
        return stillProducing;
    }

    public void stoppedProduction()
    {
        stillProducing = false;
    }
}
