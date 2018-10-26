package controller;

import javafx.scene.control.Alert;

public class Alerter {
    public static void EmptyEnglishWord() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Empty English word");
        alert.setContentText("English word must not be empty!");
        alert.showAndWait();
    }
    public static void EmptyVietnameseMeaning() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Empty Vietnamese meaning");
        alert.setContentText("Vietnamese meaning must not be empty!");
        alert.showAndWait();
    }
}
