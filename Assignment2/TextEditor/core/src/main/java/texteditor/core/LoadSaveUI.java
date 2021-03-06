package texteditor.core;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Class used for loading and saving text files to be used by the text editor
 */
public class LoadSaveUI 
{
    // CLASS FIELDS
    private static final int SPACING = 8;

    private Stage stage;
    private ResourceBundle bundle;
    private FileIO fileIO;
    private TextArea textArea;
    private FileChooser fileDialog = new FileChooser();
    private Dialog<String> encodingDialog;

    // CONSTRUCTOR
    public LoadSaveUI(Stage stage, ResourceBundle bundle, FileIO fileIO, TextArea textArea)
    {
        this.stage = stage;
        this.bundle = bundle;
        this.fileIO = fileIO;
        this.textArea = textArea;
    }

    /**
     * Internal method for displaying the encoding dialog and retrieving the name of the chosen 
     * encoding.
     */
    private String getEncoding()
    {
        if(encodingDialog == null)
        {
            var encodingComboBox = new ComboBox<String>();
            var content = new FlowPane();
            encodingDialog = new Dialog<>();
            encodingDialog.setTitle(bundle.getString("loadSave_title"));
            encodingDialog.getDialogPane().setContent(content);

            encodingDialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType(bundle.getString("ok_btn"), ButtonBar.ButtonData.OK_DONE), 
                new ButtonType(bundle.getString("cancel_btn"), ButtonBar.ButtonData.CANCEL_CLOSE)); 

            encodingDialog.setResultConverter(
                btn -> (btn.getButtonData() == ButtonData.OK_DONE) ? encodingComboBox.getValue() : null);
            
            content.setHgap(SPACING);
            content.getChildren().setAll(new Label(bundle.getString("encoding_txt")), encodingComboBox);
            
            encodingComboBox.getItems().setAll("UTF-8", "UTF-16", "UTF-32");
            encodingComboBox.setValue("UTF-8");
        } 

        return encodingDialog.showAndWait().orElse(null);
    }

    /**
     * Display dialog box used to select file to load
     */
    public void load()
    {
        fileDialog.setTitle(bundle.getString("load_title"));

        File file = fileDialog.showOpenDialog(stage);
        
        if(file != null)
        {
            String encoding = getEncoding();

            if(encoding != null)
            {
                try
                {
                    textArea.setText(fileIO.load(file, encoding));
                    textArea.requestFocus(); // Set focus on loaded text
                    textArea.end(); // Move caret to end of loaded text
                }
                catch(IOException e)
                {
                    new Alert(
                        Alert.AlertType.ERROR, 
                        String.format(bundle.getString("load_error_txt") + " %s - %s", e.getClass().getName(), e.getMessage()),
                        new ButtonType(bundle.getString("close_btn"), ButtonBar.ButtonData.CANCEL_CLOSE)
                    ).showAndWait();
                } 
            }
        }
    }

    /**
     * Display dialog box used to save file
     */
    public void save()
    {
        fileDialog.setTitle(bundle.getString("save_title"));

        File file = fileDialog.showSaveDialog(stage);

        if(file != null)
        {
            String encoding = getEncoding();

            if(encoding != null)
            {
                try
                {
                    fileIO.save(file, encoding, textArea.getText());
                }
                catch(IOException e)
                {
                    new Alert(
                        Alert.AlertType.ERROR,
                        String.format(
                            bundle.getString("save_error"), 
                            e.getClass().getName(), 
                            e.getMessage()),
                            new ButtonType(bundle.getString("close_btn"), ButtonBar.ButtonData.CANCEL_CLOSE)
                    ).showAndWait();   
                }
            }
        }
    }
}
