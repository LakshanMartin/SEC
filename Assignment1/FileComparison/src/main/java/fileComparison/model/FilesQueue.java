package fileComparison.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FilesQueue 
{
    private BlockingQueue<Files> queue = new ArrayBlockingQueue<>(90);

    public FilesQueue() {}

    public void put(Files newFile) throws InterruptedException
    {
        queue.put(newFile);
    }

    public Files get() throws InterruptedException
    {
        return queue.take();
    }
}
