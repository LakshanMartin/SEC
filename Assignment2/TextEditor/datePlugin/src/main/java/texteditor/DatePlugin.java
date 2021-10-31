package texteditor;

import texteditor.API.*;

/**
 * DatePlugin class to added through the main application
 */
public class DatePlugin implements Plugin
{
    public DatePlugin() {}

    @Override
    public void start(API api) 
    {
        // Create button using relevent language text from bundle and callback method
        api.createBtn("date_btn", () -> api.printDate()); 
    }
}
