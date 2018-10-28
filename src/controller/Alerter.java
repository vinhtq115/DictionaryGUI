package controller;

import javafx.scene.control.Alert;

class Alerter {
    static void EmptyEnglishWord() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Empty English word");
        alert.setContentText("English word must not be empty!");
        alert.showAndWait();
    }
    static void EmptyVietnameseMeaning() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Empty Vietnamese meaning");
        alert.setContentText("Vietnamese meaning must not be empty!");
        alert.showAndWait();
    }
    static void CannotConnectToGoogle() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Can't establish connection to Google server");
        alert.setContentText("Connection to Google is needed to use this feature.");
        alert.showAndWait();
    }
}
