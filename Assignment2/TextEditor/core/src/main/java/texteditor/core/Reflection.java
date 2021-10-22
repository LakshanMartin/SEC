package texteditor.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import texteditor.API.API;

public class Reflection 
{
    public Reflection(){}

    public Object getReflection(String pluginName) throws ReflectionException
    {
        try
        {
            // Get Class
            Class<?> pluginClass = getClass(pluginName);

            // Get Constructor
            Constructor<?> pluginConst = pluginClass.getConstructor();

            // Create Class Object using IAPI parameter
            Object pluginObj = pluginConst.newInstance();

            // Get start() method
            checkMethod(pluginClass, "start", API.class);

            return pluginObj;
        }
        catch(ClassNotFoundException | NoSuchMethodException | SecurityException
            | IllegalAccessException | IllegalArgumentException | InvocationTargetException |
            InstantiationException e)
        {
            throw new ReflectionException(e.getMessage());
        }
    }

    /**
     * Check if Class of className exists. If so, return Class.
     * @param className
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> getClass(String className) throws ClassNotFoundException
    {
        try       
        {
            return Class.forName(className);
        }
        catch(ClassNotFoundException e)
        {
            throw new ClassNotFoundException(e.getClass().getSimpleName() + 
                " - Class of name [" + className + "] doesn't exist.");
        }
    }

    private void checkMethod(Class<?> pluginClass, String methodName, Class<API> api) throws NoSuchMethodException, SecurityException
    {
        try 
        {
            pluginClass.getMethod(methodName, api);
        }
        catch(NoSuchMethodException e) 
        {
            throw new NoSuchMethodException(e.getClass().getSimpleName() + 
                    " - Constructor with a single String parameter doesn't exist.");
        }
        catch(SecurityException e)
        {
            throw new SecurityException(e.getClass().getSimpleName());
        }
    }
}
