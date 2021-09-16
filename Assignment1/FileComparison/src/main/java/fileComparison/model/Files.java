package fileComparison.model;

/**
 * This class contains data on the two files to be compared and added to the 1st 
 * BlockingQueue
 */
public class Files 
{
    // CLASS FIELDS
    private String file1;
    private String file2;

    // CONSTRUCTOR
    public Files(String file1, String file2)
    {
        this.file1 = file1;
        this.file2 = file2;
    }

    // GETTERS
    public String getFile1()
    {
        return file1;
    }

    public String getFile2()
    {
        return file2;
    }
}
