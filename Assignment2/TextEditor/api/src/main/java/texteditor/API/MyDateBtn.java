package texteditor.API;

public class MyDateBtn implements DateBtn
{
    public MyDateBtn() {}
    
    @Override
    public void onClick(API api) 
    {
        // TODO Auto-generated method stub
        api.printCurrentDate();
    }
}
