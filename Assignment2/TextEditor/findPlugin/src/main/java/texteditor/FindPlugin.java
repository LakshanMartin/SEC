package texteditor;

import texteditor.API.*;

/**
 * FindPlugin class to be added by the main application
 */
public class FindPlugin implements Plugin
{
    public FindPlugin() {}

    @Override
    public void start(API api) 
    {
        api.createBtn("find_btn", () -> api.findTerm());
        api.createHotKey(() -> api.findTerm());
    }
}
