package fileComparison.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class contains the Longest Common Subsequence algorithm used to identify
 * similarities between two Strings
 */
public class SimilarityAlgo 
{
    // EMPTY CONSTRUCTOR
    public SimilarityAlgo() {}

    /**
     * Calculate the similarity of two Strings
     * @param file1
     * @param file2
     * @return
     */
    public double getSimilarity(String file1, String file2)
    {
        String contents1, contents2;
        int LCS, f1Length, f2Length;
        double similarity;

        // Get file contents
        contents1 = extractContents(file1);
        contents2 = extractContents(file2);
        
        f1Length = contents1.length();
        f2Length = contents2.length();

        // Determine length of Longest Common Subsequence (LCS) of substring
        LCS = LCSLength(contents1, contents2);

        // Calculate similarity between the two files
        similarity = ((double)LCS * 2.0) / (double)(f1Length + f2Length);

        // Round similarity
        similarity = roundDouble(similarity);

        return similarity;
    }

    /**
     * REFERENCE: "How to read a file in one line in JDK 7 or Java 8? Example".
     *             https://javarevisited.blogspot.com/2015/02/how-to-read-file-in-one-line-java-8.html
     *             (accessed 25/08/2021).
     * @param file
     * @return
     */
    private String extractContents(String file)
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
     * Execution of a version of the Longest Common Subsequence Algorithm.
     * 
     * REFERENCES: Obtained from Techie Delight. "Longest Common Subsequeuence" 
     *             https://www.techiedelight.com/longest-common-subsequence-lcs-space-optimized-version/
     *             (accessed 12th September 2021)
     */
    public int LCSLength(String contents1, String contents2)
    {
        int m, n, prev, temp;
        int[] curr;
        
        m = contents1.length(); 
        n = contents2.length();
 
        // allocate storage for one-dimensional array `curr`
        curr = new int[n + 1];
 
        // fill the lookup table in a bottom-up manner
        for (int i = 0; i <= m; i++)
        {
            prev = curr[0];
            for (int j = 0; j <= n; j++)
            {
                temp = curr[j];

                if(i == 0 || j == 0) 
                {
                    curr[j] = 0;
                }
                else 
                {
                    // If the current character of `X` and `Y` matches
                    if(contents1.charAt(i - 1) == contents2.charAt(j - 1)) 
                    {
                        curr[j] = prev + 1;
                    }
                    // Else the current character of `X` and `Y` don't match
                    else 
                    {
                        curr[j] = Integer.max(curr[j], curr[j - 1]);
                    }
                }

                prev = temp;
            }
        }

        return curr[n];
    }

    /**
     * Round similarity score to 5 decimal places
     * 
     * REFERENCE: Obtained from Jav_Rock. "What's the best practice to round a 
     *            float to 2 decimals". 
     *            https://stackoverflow.com/questions/8911356/whats-the-best-practice-to-round-a-float-to-2-decimals
     *            (accessed 10th September 2021)
     * @param similarity
     * @return
     */
    private double roundDouble(double similarity)
    {
        BigDecimal bd = new BigDecimal(Double.toString(similarity));

        bd = bd.setScale(5, RoundingMode.CEILING); // Round to 5 decimal places

        return bd.doubleValue();
    }
}
