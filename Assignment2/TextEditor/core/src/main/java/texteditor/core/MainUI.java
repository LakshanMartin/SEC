package texteditor.core;

import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import texteditor.API.*;
import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Class dedicated to the presentation of the user interface. All textual buttons
 * and descriptions will be represented in the specific language entered as 
 * an CLI argument.
 */
public class MainUI implements API
{
    // CLASS FIELDS
    private Stage stage;
    private ResourceBundle bundle;
    private LoadSaveUI loadSaveUI;
    private TextArea textArea;
    private KeymapParse keymap;
    private List<Keybind> keybindList;
    private ToolBar toolBar;
    private Callback hotKeyMethod;

    // CONSTRUCTOR
    public MainUI(Stage stage, ResourceBundle bundle, LoadSaveUI loadSaveUI, TextArea textArea, KeymapParse keymap) 
    {
        this.stage = stage;
        this.bundle = bundle;
        this.loadSaveUI = loadSaveUI;
        this.textArea = textArea;
        this.keymap = keymap;
    }

    /**
     * Start parsing the keymap and generate a list of keybinds to be implemented
     * @throws IOException
     * @throws ParseException
     */
    public void parseKeymap() throws IOException, ParseException
    {
        keybindList = keymap.startParse();

        // Output to terminal to verify parsed keybinding
        for(int i = 0; i < keybindList.size(); i++)
        {
            Keybind keybind = keybindList.get(i);

            System.out.println("KEYBIND #" + (i+1));
            System.out.println("Keybind keys: " + keybind.getMainKey() + keybind.getCustomKey());
            System.out.println("Function: " + keybind.getFunc());
            System.out.println("Text: " + keybind.getText());
            System.out.println("Text position: " + keybind.getPos());
            System.out.println("\n");
        }
    }

    /**
     * Construct UI elements
     */
    public void display()
    {
        stage.setTitle(bundle.getString("main_title"));
        stage.setMinWidth(800);

        // Create default toolbar
        Button loadBtn = new Button(bundle.getString("load_btn"));
        Button saveBtn = new Button(bundle.getString("save_btn"));
        Button pluginScriptBtn = new Button(bundle.getString("pluginScript_btn"));
        toolBar = new ToolBar(
            loadBtn, saveBtn, pluginScriptBtn);

        // Plugin/Scripts List
        ObservableList<String> list = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<>(list);
        
        // Subtle user experience tweaks
        toolBar.setFocusTraversable(false);
        toolBar.getItems().forEach(btn -> btn.setFocusTraversable(false));
        textArea.setStyle("-fx-font-family: 'monospace'"); // Set the font
        
        // Add the main parts of the UI to the window.
        BorderPane mainBox = new BorderPane();
        mainBox.setTop(toolBar);
        mainBox.setCenter(textArea);
        Scene scene = new Scene(mainBox);        
        
        // Button event handlers.
        loadBtn.setOnAction(event -> loadSaveUI.load());
        saveBtn.setOnAction(event -> loadSaveUI.save());
        pluginScriptBtn.setOnAction(event -> pluginScriptDialog(list, listView));
        
        // TextArea event handlers & caret positioning.
        textArea.textProperty().addListener((object, oldValue, newValue) -> 
        {
            System.out.println("caret position is " + textArea.getCaretPosition() + 
                               "; text is\n---\n" + newValue + "\n---\n");
        });
        
        // Remove focus from textArea so prompt text can be displayed
        textArea.setPromptText(bundle.getString("textPrompt_txt"));
        mainBox.requestFocus(); 

        
        // Example global keypress handler.
        scene.setOnKeyPressed(keyEvent -> 
        {
            // See the documentation for the KeyCode class to see all the available keys.
            
            KeyCode key = keyEvent.getCode();

            setKeyBindings(keyEvent);

            // Specific keybinding saved for FindPlugin
            if(key == KeyCode.F3 && hotKeyMethod != null)
            {
                 hotKeyMethod.callback();
            }
            else if(key == KeyCode.ESCAPE)
            {
                // Added in case keybind uses 'Shift' key while within text area,
                // which would capitalise a letter rather than activate keybind function
                mainBox.requestFocus();
            }
        });
        
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Establish keybinding as parsed from the keymap file
     * @param keyEvent
     */
    private void setKeyBindings(KeyEvent keyEvent)
    {
        // Set up new keybinding with each iteration
        for(int i = 0; i < keybindList.size(); i++)
        {
            KeyCode key = keyEvent.getCode();
            boolean ctrl = keyEvent.isControlDown();
            boolean shift = keyEvent.isShiftDown();
            boolean alt = keyEvent.isAltDown();

            Keybind keybind = keybindList.get(i);
            String mainKey = keybind.getMainKey();
            String customKey = keybind.getCustomKey();
            String func = keybind.getFunc();
            String text = keybind.getText();
            String pos = keybind.getPos();
            KeyCode keyCode = new KeyBindDetails(customKey).getKeyCode();
            
            switch(mainKey)
            {
                case "ctrl+":
                    if(ctrl && !shift && !alt && key == keyCode)
                    {
                        if(func.equals("insert"))
                        {
                            if(pos.equals("at start of line"))
                            {
                                int caretPos = getStartOfLine(textArea.getText());
    
                                textArea.requestFocus();
                                textArea.insertText(caretPos, text);
                            }
                            else // "at caret"
                            {
                                textArea.requestFocus();
                                textArea.insertText(textArea.getCaretPosition(), text);
                            }
                        }
                        else // "delete"
                        {
                            if(pos.equals("at start of line"))
                            {
                                int len = text.length();
                                String toFind = textArea.getText().substring(0, len);
                                
                                if(toFind.equals(text))
                                {
                                    textArea.requestFocus();
                                    textArea.deleteText(0, len);
                                }
                            }
                            else // "at caret"
                            {
                                int len = text.length();
                                int caretPos = textArea.getCaretPosition();
                                
                                if(textArea.getText().length() >= (caretPos+len))
                                {
                                    String toFind = textArea.getText().substring(caretPos, (caretPos+len));

                                    if(toFind.equals(text))
                                    {
                                        textArea.requestFocus();
                                        textArea.deleteText(caretPos, (caretPos+len));
                                    }
                                }
                            }
                        }
                    }
                break;

                case "shift+":
                    if(shift && !ctrl && !alt && key == keyCode)
                    {
                        if(func.equals("insert"))
                        {
                            if(pos.equals("at start of line"))
                            {
                                int caretPos = getStartOfLine(textArea.getText());
    
                                textArea.requestFocus();
                                textArea.insertText(caretPos, text);
                            }
                            else // "at caret"
                            {
                                textArea.requestFocus();
                                textArea.insertText(textArea.getCaretPosition(), text);
                            }
                        }
                        else // "delete"
                        {
                            if(pos.equals("at start of line"))
                            {
                                int len = text.length();
                                String toFind = textArea.getText().substring(0, len);
                                
                                if(toFind.equals(text))
                                {
                                    textArea.requestFocus();
                                    textArea.deleteText(0, len);
                                }
                            }
                            else // "at caret"
                            {
                                int len = text.length();
                                int caretPos = textArea.getCaretPosition();
                                
                                if(textArea.getText().length() >= (caretPos+len))
                                {
                                    String toFind = textArea.getText().substring(caretPos, (caretPos+len));

                                    if(toFind.equals(text))
                                    {
                                        textArea.requestFocus();
                                        textArea.deleteText(caretPos, (caretPos+len));
                                    }
                                }
                            }
                        }
                    }
                break;

                case "alt+":
                    if(alt && !ctrl && !shift && key == keyCode)
                    {
                        if(func.equals("insert"))
                        {
                            if(pos.equals("at start of line"))
                            {
                                int caretPos = getStartOfLine(textArea.getText());
    
                                textArea.requestFocus();
                                textArea.insertText(caretPos, text);
                            }
                            else // "at caret"
                            {
                                textArea.requestFocus();
                                textArea.insertText(textArea.getCaretPosition(), text);
                            }
                        }
                        else // "delete"
                        {
                            if(pos.equals("at start of line"))
                            {
                                int len = text.length();
                                String toFind = textArea.getText().substring(0, len);
                                
                                if(toFind.equals(text))
                                {
                                    textArea.requestFocus();
                                    textArea.deleteText(0, len);
                                }
                            }
                            else // "at caret"
                            {
                                int len = text.length();
                                int caretPos = textArea.getCaretPosition();
                                
                                if(textArea.getText().length() >= (caretPos+len))
                                {
                                    String toFind = textArea.getText().substring(caretPos, (caretPos+len));

                                    if(toFind.equals(text))
                                    {
                                        textArea.requestFocus();
                                        textArea.deleteText(caretPos, (caretPos+len));
                                    }
                                }
                            }
                        }
                    }
                break;

                case "ctrl+shift+": case "shift+ctrl+":
                    if(ctrl && shift && !alt && key == keyCode)
                    {
                        if(func.equals("insert"))
                        {
                            if(pos.equals("at start of line"))
                            {
                                int caretPos = getStartOfLine(textArea.getText());
    
                                textArea.requestFocus();
                                textArea.insertText(caretPos, text);
                            }
                            else // "at caret"
                            {
                                textArea.requestFocus();
                                textArea.insertText(textArea.getCaretPosition(), text);
                            }
                        }
                        else // "delete"
                        {
                            if(pos.equals("at start of line"))
                            {
                                int len = text.length();
                                String toFind = textArea.getText().substring(0, len);
                                
                                if(toFind.equals(text))
                                {
                                    textArea.requestFocus();
                                    textArea.deleteText(0, len);
                                }
                            }
                            else // "at caret"
                            {
                                int len = text.length();
                                int caretPos = textArea.getCaretPosition();
                                
                                if(textArea.getText().length() >= (caretPos+len))
                                {
                                    String toFind = textArea.getText().substring(caretPos, (caretPos+len));

                                    if(toFind.equals(text))
                                    {
                                        textArea.requestFocus();
                                        textArea.deleteText(caretPos, (caretPos+len));
                                    }
                                }
                            }
                        }
                    }
                break;

                case "ctrl+alt+": case "alt+ctrl+":
                    if(ctrl && alt && !shift && key == keyCode)
                    {
                        if(func.equals("insert"))
                        {
                            if(pos.equals("at start of line"))
                            {
                                int caretPos = getStartOfLine(textArea.getText());
    
                                textArea.requestFocus();
                                textArea.insertText(caretPos, text);
                            }
                            else // "at caret"
                            {
                                textArea.requestFocus();
                                textArea.insertText(textArea.getCaretPosition(), text);
                            }
                        }
                        else // "delete"
                        {
                            if(pos.equals("at start of line"))
                            {
                                int len = text.length();
                                String toFind = textArea.getText().substring(0, len);
                                
                                if(toFind.equals(text))
                                {
                                    textArea.requestFocus();
                                    textArea.deleteText(0, len);
                                }
                            }
                            else // "at caret"
                            {
                                int len = text.length();
                                int caretPos = textArea.getCaretPosition();
                                
                                if(textArea.getText().length() >= (caretPos+len))
                                {
                                    String toFind = textArea.getText().substring(caretPos, (caretPos+len));

                                    if(toFind.equals(text))
                                    {
                                        textArea.requestFocus();
                                        textArea.deleteText(caretPos, (caretPos+len));
                                    }
                                }
                            }
                        }
                    }
                break;

                case "shift+alt+": case "alt+shift+":
                    if(shift && alt && !ctrl && key == keyCode)
                    {
                        if(func.equals("insert"))
                        {
                            if(pos.equals("at start of line"))
                            {
                                int caretPos = getStartOfLine(textArea.getText());
    
                                textArea.requestFocus();
                                textArea.insertText(caretPos, text);
                            }
                            else // "at caret"
                            {
                                textArea.requestFocus();
                                textArea.insertText(textArea.getCaretPosition(), text);
                            }
                        }
                        else // "delete"
                        {
                            if(pos.equals("at start of line"))
                            {
                                int len = text.length();
                                String toFind = textArea.getText().substring(0, len);
                                
                                if(toFind.equals(text))
                                {
                                    textArea.requestFocus();
                                    textArea.deleteText(0, len);
                                }
                            }
                            else // "at caret"
                            {
                                int len = text.length();
                                int caretPos = textArea.getCaretPosition();
                                
                                if(textArea.getText().length() >= (caretPos+len))
                                {
                                    String toFind = textArea.getText().substring(caretPos, (caretPos+len));

                                    if(toFind.equals(text))
                                    {
                                        textArea.requestFocus();
                                        textArea.deleteText(caretPos, (caretPos+len));
                                    }
                                }
                            }
                        }
                    }
                break;

                case "ctrl+shift+alt+": case "ctrl+alt+shift+": case "shift+ctrl+alt+":
                case "shift+alt+ctrl+": case "alt+ctrl+shift+": case "alt+shift+ctrl+":
                    if(ctrl && shift && alt && key == keyCode)
                    {        
                        if(func.equals("insert"))
                        {
                            if(pos.equals("at start of line"))
                            {
                                int caretPos = getStartOfLine(textArea.getText());
    
                                textArea.requestFocus();
                                textArea.insertText(caretPos, text);
                            }
                            else // "at caret"
                            {
                                textArea.requestFocus();
                                textArea.insertText(textArea.getCaretPosition(), text);
                            }
                        }
                        else // "delete"
                        {
                            if(pos.equals("at start of line"))
                            {
                                int len = text.length();
                                String toFind = textArea.getText().substring(0, len);
                                
                                if(toFind.equals(text))
                                {
                                    textArea.requestFocus();
                                    textArea.deleteText(0, len);
                                }
                            }
                            else // "at caret"
                            {
                                int len = text.length();
                                int caretPos = textArea.getCaretPosition();
                                
                                if(textArea.getText().length() >= (caretPos+len))
                                {
                                    String toFind = textArea.getText().substring(caretPos, (caretPos+len));

                                    if(toFind.equals(text))
                                    {
                                        textArea.requestFocus();
                                        textArea.deleteText(caretPos, (caretPos+len));
                                    }
                                }
                            }
                        }
                    }                   
                break;
            }
        }
    }   

    /**
     * Identifies the start of a newline
     * @param text
     * @return
     */
    private int getStartOfLine(String text)
    {
        int caretPos = textArea.getCaretPosition();
        int newLineMarker = 0;

        for(int i = 0; i < caretPos; i++)
        {
            if(text.charAt(i) == ('\n'))
            {
                newLineMarker = i + 1;
            }
        }

        if(newLineMarker == 1)
        {
            newLineMarker = 0;
        }

        return newLineMarker;
    }
        
    /**
     * Dialog box for adding plugins and scripts
     * @param list
     * @param listView
     */
    private void pluginScriptDialog(ObservableList<String> list, ListView<String> listView)
    {
        Button addPluginBtn = new Button(bundle.getString("addPlugin_btn"));
        Button addScriptBtn = new Button(bundle.getString("addScript_btn"));
        ToolBar toolBar = new ToolBar(addPluginBtn, addScriptBtn);

        addPluginBtn.setOnAction(event -> loadPlugin(
            bundle.getString("loadPlugin_txt"), 
            bundle.getString("enterClass_txt"),
            list));
                
        BorderPane box = new BorderPane();
        box.setTop(toolBar);
        box.setCenter(listView);
        
        Dialog dialog = new Dialog();
        dialog.setTitle(bundle.getString("pluginScript_title"));
        dialog.setHeaderText(bundle.getString("pluginScript_txt"));
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    /**
     * Validate and load Plugins utilising Reflection
     * @param title
     * @param headerText
     * @param list
     */
    private void loadPlugin(String title, String headerText, ObservableList<String> list)
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        String inputStr = dialog.showAndWait().orElse(null);

        Plugin pluginObj;

        if(inputStr != null)
        {
            try 
            {
                Reflection reflection = new Reflection();
                pluginObj = (Plugin)reflection.getReflection(inputStr);
                pluginObj.start(this);
                list.add(pluginObj.getClass().getSimpleName());
            } 
            catch(ReflectionException e) 
            {
                new Alert(
                    Alert.AlertType.INFORMATION, 
                    e.getMessage(),
                    ButtonType.OK).showAndWait();
            }
        }
    }   

/********************************************************************************
* Methods to override from Api interface
********************************************************************************/

    /**
     * Utilised by plugins to create their own button within the UI
     */
    @Override
    public void createBtn(String btnName, Callback obj) 
    {        
        Button pluginBtn = new Button(bundle.getString(btnName));
        toolBar.getItems().addAll(new Separator());
        toolBar.getItems().add(pluginBtn);

        pluginBtn.setOnAction((event) -> obj.callback());
    }

    /**
     * Used by FindPlugin to establish it's own keybind
     */
    @Override
    public void createHotKey(Callback callback)
    {
        hotKeyMethod = callback;
    }

    /**
     * Used by DatePlugin to output date to text area, based on language specific
     * format
     */
    @Override
    public void printDate()
    {
        StringBuilder contents = new StringBuilder(textArea.getText());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
            bundle.getString("date_format"));

        contents.append(dtf.format(LocalDate.now()));

        textArea.setText(contents.toString());
    }

    /**
     * Prompt user to enter a search term and highlight next occurrence of term
     * within text editor after current caret position
     * 
     * REFERENCE: purring pigeon, 2017. "How to implement ctrl+f in a javafx text area".
     *            StackOverflow. 
     *            https://stackoverflow.com/questions/42790255/how-to-implement-ctrlf-in-a-javafx-text-area?rq=1 (accessed 23/10/21)
     * 
     * @param title
     * @param headerText
     */
    @Override
    public void findTerm()
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("find_title"));
        dialog.setHeaderText(bundle.getString("find_txt"));

        String inputStr = dialog.showAndWait().orElse(null);

        if(inputStr != null && !textArea.getText().isEmpty())
        {
            // REFERENCED MATERIAL ----------------------------------------------
            Pattern pattern = Pattern.compile(inputStr);

            int caretPosition = textArea.getCaretPosition();
            
            // Isolate textArea from current caret position
            String fromCaret = textArea.getText().substring(caretPosition);

            // Normalise content
            String normText = Normalizer.normalize(fromCaret, Normalizer.Form.NFKC);

            // Matcher matcher = pattern.matcher(fromCaret);
            Matcher matcher = pattern.matcher(normText);
            boolean found = matcher.find();

            if(found)
            {
                // Add caretPosition to account for substring used for pattern.matcher
                textArea.selectRange(
                    matcher.start() + caretPosition, 
                    matcher.end() + caretPosition);
                textArea.requestFocus();
            }
            // END OF REFERENCED MATERIAL----------------------------------------
        }
    }
}