package texteditor;

import texteditor.API.*;

public class DatePlugin implements Plugin
{
    public DatePlugin() {}

    @Override
    public void start(API api) 
    {
        api.createDateBtn(); 
    }
}
