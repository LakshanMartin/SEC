package fileComparison.model;

public class ProgressTracker 
{
    private Object mutex = new Object();
    private int start = 0;
    private int goal;

    
    public ProgressTracker(int goal)
    {
        this.goal = goal;
    }

    public void add()
    {
        synchronized(mutex)
        {
            start++;
        }
    }

    public int getProgress()
    {
        synchronized(mutex)
        {
            return goal - start;
        }
    }
}
