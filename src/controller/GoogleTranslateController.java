package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class GoogleTranslateController {
    @FXML TextArea inputField;
    @FXML TextArea outputField;

    public void translateStart() {
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Translation translation = translate.translate(inputField.getText(), TranslateOption.sourceLanguage("en"), TranslateOption.targetLanguage("vi"));
        outputField.setText(translation.getTranslatedText());
    }
}
