package fileComparison;

public class ComparisonResult 
{
    private final String file1;
    private final String file2;
    private final double similarity;
    private final boolean isPoison;
    
    public ComparisonResult(String file1, String file2, double similarity, boolean isPoison)
    {
        this.file1 = file1;
        this.file2 = file2;
        this.similarity = similarity;
        this.isPoison = isPoison;
    }
    
    public String getFile1() { return file1; }
    public String getFile2() { return file2; }
    public double getSimilarity() { return similarity; }
    public boolean getIsPoison() { return isPoison; }
}
