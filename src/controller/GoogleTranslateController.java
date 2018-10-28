package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import com.darkprograms.speech.translator.GoogleTranslate;
import java.io.IOException;

public class GoogleTranslateController {
    @FXML TextArea inputField;
    @FXML TextArea outputField;

    public void inputFieldAction() {
        try {
            outputField.setText(GoogleTranslate.translate("vi", inputField.getText()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
