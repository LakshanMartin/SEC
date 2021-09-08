package fileComparison;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompareFiles 
{
    // CLASS FIELDS
    private String file1;
    private String file2;

    public CompareFiles(String file1, String file2)
    {
        this.file1 = file1;
        this.file2 = file2;
    }

    public double getSimilarity()
    {
        String contents1, contents2;
        Map<String, Integer> lookup = new HashMap<>();
        int LCS, f1Length, f2Length;
        double similarity;

        // Get file contents
        contents1 = readFile(file1);
        contents2 = readFile(file2);
        
        f1Length = contents1.length();
        f2Length = contents2.length();

        System.out.println("Contents1: " + contents1);
        System.out.println("Contents2: " + contents2);

        // Determine length of Longest Common Subsequence (LCS) of substring
        LCS = LCSLength(contents1, contents2, f1Length, f2Length, lookup);
        
        System.out.println("LCS Length: " + LCS);

        // Calculate similarity between the two files
        similarity = ((double)LCS * 2.0) / (double)(f1Length + f2Length);
        System.out.println("Rare Similarity: " + similarity);

        // Round similarity
        similarity = roundDouble(similarity);
        System.out.println("Similarity: " + similarity);

        return similarity;
    }

    /**
     * REFERENCE: "How to read a file in one line in JDK 7 or Java 8? Example".
     *             https://javarevisited.blogspot.com/2015/02/how-to-read-file-in-one-line-java-8.html
     *             (accessed 25/08/2021).
     * @param file
     * @return
     */
    private String readFile(String file)
    {
        List<String> lines;
        StringBuilder sb;

        sb = new StringBuilder();

        try
        {
            lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);

            for(String line : lines)
            {
                sb.append(line);
            }
        }
        catch(IOException e)
        {

        }

        return sb.toString();
    }

    /**
     * REFERENCE: https://www.techiedelight.com/longest-common-subsequence/
     * @param contents1
     * @param contents2
     * @param m
     * @param n
     * @param lookup
     * @return
     */
    private int LCSLength(String contents1, String contents2, int m, int n, 
                Map<String, Integer> lookup)
    {
        String key = m + "|" + n;

        if(m == 0 || n == 0)
        {
            return 0;
        }
        
        if(!lookup.containsKey(key))
        {
            if(contents1.charAt(m - 1) == contents2.charAt(n - 1))
            {
                lookup.put(key, LCSLength(contents1, contents2, m-1, n-1, lookup) + 1);
            }
            else
            {
                lookup.put(key, Integer.max(LCSLength(contents1, contents2, m, n-1, lookup), 
                LCSLength(contents1, contents2, m-1, n, lookup)));
            }
        }

        return lookup.get(key);
    }

    /**
     * REFERENCE: https://stackoverflow.com/questions/8911356/whats-the-best-practice-to-round-a-float-to-2-decimals
     * @param similarity
     * @return
     */
    private double roundDouble(double similarity)
    {
        BigDecimal bd = new BigDecimal(Double.toString(similarity));

        bd = bd.setScale(2, RoundingMode.CEILING);

        return bd.doubleValue();
    }
}
