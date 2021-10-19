import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileIO 
{
    public FileIO(){}

    public String load(File file) throws IOException
    {
        StringBuilder contents = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;

            while((line = br.readLine()) != null)
            {
                contents.append(line);
            }
        }

        return contents.toString();
    }

    public void save(File file, String contents) throws IOException
    {
        try(PrintWriter pw = new PrintWriter(file))
        {
            pw.println(contents);
        }
    }
}
