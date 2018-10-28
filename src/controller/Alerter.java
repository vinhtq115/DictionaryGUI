package controller;

import javafx.scene.control.Alert;

class Alerter {
    static void EmptyEnglishWord() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText("Trống phần từ tiếng Anh");
        alert.setContentText("Phần này không được bỏ trống!");
        alert.showAndWait();
    }
    static void EmptyVietnameseMeaning() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText("Trống phần nghĩa tiếng Việt");
        alert.setContentText("Phần này không được bỏ trống!");
        alert.showAndWait();
    }
    static void CannotConnectToGoogle() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText("Không thể kết nối đến máy chủ Google");
        alert.setContentText("Cần kết nối đến Google để sử dụng chức năng này.");
        alert.showAndWait();
    }
}
