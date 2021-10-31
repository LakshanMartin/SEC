package texteditor.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * File IO class for loading and saving files.
 */
public class FileIO 
{
    // EMPTY CONSTRUCTOR
    public FileIO(){}

    /**
     * Load file based specific encoding
     * @param file
     * @param encoding
     * @return
     * @throws IOException
     */
    public String load(File file, String encoding) throws IOException
    {
        StringBuilder contents = new StringBuilder();
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, encoding);

        try(BufferedReader br = new BufferedReader(isr))
        {
            String line;

            while((line = br.readLine()) != null)
            {
                contents.append(line);
            }
        }

        return contents.toString();
    }

    /**
     * Save file based specific encoding
     * @param file
     * @param encoding
     * @param contents
     * @throws IOException
     */
    public void save(File file, String encoding, String contents) throws IOException
    {
        try(PrintWriter pw = new PrintWriter(file, encoding))
        {
            pw.println(contents);
        }
    }
}