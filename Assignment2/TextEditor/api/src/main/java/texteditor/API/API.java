package texteditor.API;

/**
 * API interface to be implemented by the main application.
 */
public interface API
{
    void createBtn(String btnName, Callback callback);

    void createHotKey(Callback callback);

    void printDate();

    void findTerm();

    void runScript();
}