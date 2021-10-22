package texteditor.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class FileIO 
{
    public FileIO(){}

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

    public void save(File file, String encoding, String contents) throws IOException
    {
        try(PrintWriter pw = new PrintWriter(file, encoding))
        {
            pw.println(contents);
        }
    }
}