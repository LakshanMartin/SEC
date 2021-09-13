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

    public double getProgress()
    {
        synchronized(mutex)
        {
            return (double)start / (double)goal;
        }
    }
}
