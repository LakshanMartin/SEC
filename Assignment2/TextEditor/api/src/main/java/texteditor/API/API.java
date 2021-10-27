package texteditor.API;

public interface API
{
    void createBtn(String btnName, Callback callback);

    void createHotKey(Callback callback);

    void printDate();

    void findTerm();
}