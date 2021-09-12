package fileComparison.model;

public class Files 
{
    private String file1;
    private String file2;

    public Files(String file1, String file2)
    {
        this.file1 = file1;
        this.file2 = file2;
    }

    public String getFile1()
    {
        return file1;
    }

    public String getFile2()
    {
        return file2;
    }
}
