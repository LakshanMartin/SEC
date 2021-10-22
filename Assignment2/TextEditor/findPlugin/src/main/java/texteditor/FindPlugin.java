package texteditor;

import texteditor.API.*;

public class FindPlugin implements Plugin
{
    public FindPlugin() {}

    @Override
    public void start(API api) 
    {
        api.createFindBtn();
    }
}
