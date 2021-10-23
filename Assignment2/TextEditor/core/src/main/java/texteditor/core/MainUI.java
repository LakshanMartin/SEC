package texteditor.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import texteditor.API.*;

import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainUI implements API
{
    private Stage stage;
    private ResourceBundle bundle;
    private LoadSaveUI loadSaveUI;
    private TextArea textArea;
    private ToolBar toolBar;
    private BtnCallback hotKeyMethod;

    public MainUI(Stage stage, ResourceBundle bundle, LoadSaveUI loadSaveUI, TextArea textArea) 
    {
        this.stage = stage;
        this.bundle = bundle;
        this.loadSaveUI = loadSaveUI;
        this.textArea = textArea;
    }

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
        
        textArea.setPromptText("Enter text here...");
        mainBox.requestFocus(); // Remove focus from textArea so prompt text can be displayed
        
        // Example global keypress handler.
        scene.setOnKeyPressed(keyEvent -> 
        {
            // See the documentation for the KeyCode class to see all the available keys.
            
            KeyCode key = keyEvent.getCode();
            boolean ctrl = keyEvent.isControlDown();
            boolean shift = keyEvent.isShiftDown();
            boolean alt = keyEvent.isAltDown();
        
            if(key == KeyCode.F1)
            {
                new Alert(Alert.AlertType.INFORMATION, "F1", ButtonType.OK).showAndWait();
            }
            else if(key == KeyCode.F3 && hotKeyMethod != null)
            {
                hotKeyMethod.callback();
            }
            else if(ctrl && shift && key == KeyCode.B)
            {
                new Alert(Alert.AlertType.INFORMATION, "ctrl+shift+b", ButtonType.OK).showAndWait();
            }
            else if(ctrl && key == KeyCode.B)
            {
                new Alert(Alert.AlertType.INFORMATION, "ctrl+b", ButtonType.OK).showAndWait();
            }
            else if(alt && key == KeyCode.B)
            {
                new Alert(Alert.AlertType.INFORMATION, "alt+b", ButtonType.OK).showAndWait();
            }
        });
        
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
        
    private void pluginScriptDialog(ObservableList<String> list, ListView<String> listView)
    {
        Button addPluginBtn = new Button(bundle.getString("addPlugin_btn"));
        Button addScriptBtn = new Button(bundle.getString("addScript_btn"));
        ToolBar toolBar = new ToolBar(addPluginBtn, addScriptBtn);

        addPluginBtn.setOnAction(event -> loadPlugin(
            bundle.getString("loadPlugin_txt"), 
            bundle.getString("enterClass_txt"),
            list));
        
        // addScriptBtn.setOnAction(event -> inputPlugin(
        //     bundle.getString("loadScript_txt"), 
        //     bundle.getString("enterClass_txt")));
        
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
                // Display error message
                new Alert(
                    Alert.AlertType.INFORMATION, 
                    e.getMessage(),
                    ButtonType.OK).showAndWait();
            }
        }
    }   

    @Override
    public void createBtn(String btnName, BtnCallback obj) 
    {        
        Button pluginBtn = new Button(bundle.getString(btnName));
        toolBar.getItems().addAll(new Separator());
        toolBar.getItems().add(pluginBtn);

        pluginBtn.setOnAction((event) -> obj.callback());
    }

    @Override
    public void createHotKey(BtnCallback callback)
    {
        hotKeyMethod = callback;
    }

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

            Matcher matcher = pattern.matcher(fromCaret);
            boolean found = matcher.find();

            if(found)
            {
                // Add caretPosition to account for substring used for pattern.matcher
                textArea.selectRange(matcher.start() + caretPosition, matcher.end() + caretPosition);
                textArea.requestFocus();
            }
            // END OF REFERENCED MATERIAL----------------------------------------
        }
    }
}