package fileComparison.model;

/**
 * This class is used to Track the progress of the comparison process.
 */
public class ProgressTracker 
{
    // CLASS FIELDS
    private Object mutex = new Object();
    private int start = 0;
    private int goal;

    // CONSTRUCTOR
    public ProgressTracker(int goal)
    {
        this.goal = goal;
    }

    /**
     * Increments per processed comparison
     */
    public void add()
    {
        synchronized(mutex)
        {
            start++;
        }
    }

    /**
     * Returns the current progress in relation to total number of comparisons 
     * required for completion (goal). 
     * @return
     */
    public double getProgress()
    {
        synchronized(mutex)
        {
            return (double)start / (double)goal;
        }
    }
}
