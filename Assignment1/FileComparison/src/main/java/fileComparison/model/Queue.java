package fileComparison.model;

import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Queue 
{
    // CLASS FIELDS
    private BlockingQueue<ComparisonResult> queue = new ArrayBlockingQueue<>(90);
    private HashSet<String> produced = new HashSet<>();
    private Object mutex = new Object();
    private boolean stillProducing = true;
    
    // EMPTY CONSTRUCTOR
    public Queue() {}

    public void put(ComparisonResult newResult) throws InterruptedException
    {
        this.queue.put(newResult);

        synchronized(mutex)
        {
            produced.add(newResult.getFile1() + newResult.getFile2());
        }
    }

    public ComparisonResult get() throws InterruptedException
    {
        return this.queue.take();
        // return this.queue.poll(30, TimeUnit.SECONDS);
    }

    public boolean checkProcessed(String toCheck)
    {

        synchronized(mutex)
        {
            return produced.contains(toCheck); 
        }
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
