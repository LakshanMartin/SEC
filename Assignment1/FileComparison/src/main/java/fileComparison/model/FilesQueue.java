package fileComparison.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FilesQueue 
{
    // private BlockingQueue<String[]> queue = new ArrayBlockingQueue<>(90);
    private BlockingQueue<Files> queue = new ArrayBlockingQueue<>(90);

    public FilesQueue() {}

    // public void put(String[] newFile) throws InterruptedException
    public void put(Files newFile) throws InterruptedException
    {
        queue.put(newFile);
    }

    // public String[] get() throws InterruptedException
    public Files get() throws InterruptedException
    {
        return queue.take();
    }

    // public String[] peek() throws InterruptedException
    // {
    //     return this.queue.peek();
    // }
}
