package texteditor.API;

public interface API
{
    void createBtn(String btnName, BtnCallback callback);

    void createHotKey(BtnCallback callback);

    void printDate();

    void findTerm();
}