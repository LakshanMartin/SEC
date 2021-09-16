package fileComparison.model;

/**
 * This class represents the data object containing results to be output to the
 * CSV file
 * 
 * REFERENCE: Obtained from Dr David Cooper. "uidemo" (accessed 6th September 2021)
 */
public class ComparisonResult 
{
    // CLASS FIELDS
    private final String file1;
    private final String file2;
    private final double similarity;
    
    // CONSTRUCTOR
    public ComparisonResult(String file1, String file2, double similarity)
    {
        this.file1 = file1;
        this.file2 = file2;
        this.similarity = similarity;
    }
    
    // GETTERS
    public String getFile1() { return file1; }
    public String getFile2() { return file2; }
    public double getSimilarity() { return similarity; }
}
