package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.DictionaryManagement;
import model.Word;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Vector;

public class Controller {
    @FXML ListView<Word> wordList = new ListView<>(); // For display
    private ObservableList<Word> words = FXCollections.observableArrayList(); // Array of words
    private ObservableList<Word> wordsFind; // Found word
    @FXML TextArea resultField;
    @FXML Label englishWordField;
    @FXML Label pronunciationField;
    @FXML Button speechButton;
    @FXML TextField searchField;
    @FXML MenuItem closeButton;
    @FXML MenuItem deleteButton;
    @FXML MenuItem addButton;
    @FXML MenuItem editButton;
    @FXML MenuItem exportButton;
    @FXML MenuItem googleTranslateButton;
    private int[] startingIndex = new int[26];

    private Main main;

    void setMain(Main main) {
        this.main = main;
        Arrays.fill(startingIndex, -1);
        {
            DictionaryManagement manager = new DictionaryManagement();
            if (!manager.insertFromFile()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("File not found");
                alert.setContentText("Can't open dictionaries.txt!");
                alert.showAndWait();
                Platform.exit();

                return;
            }
            for (int i = 0; i < manager.EnVi.getSize(); i++) {
                words.add(manager.EnVi.getWord(i));
                int first = words.get(i).getWordTarget().toLowerCase().charAt(0) - 97;
                if (startingIndex[first] == -1)
                    startingIndex[first] = i;
            }
            // Display english word
            wordList.setItems(words);

        }
        // Handle mouse click on ListView
        displayResult(false);
    }
    private void displayResult(boolean Null) {
        if (Null) {
            englishWordField.setText("Chọn một từ để tiếp tục...");
            resultField.setText(null);
            pronunciationField.setText(null);
            return;
        }
        wordList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resultField.clear();
                pronunciationField.setText(null);
                if (wordList.getSelectionModel().getSelectedItem() == null)
                    return;
                String enWord = wordList.getSelectionModel().getSelectedItem().getWordTarget(); // enWord: english word
                englishWordField.setText(enWord);
                resultField.appendText((wordList.getSelectionModel().getSelectedItem()!=null?wordList.getSelectionModel().getSelectedItem().getWordExplain():null) + "\n\n"); // Get meaning
                if (GetOnlineResources.checkConnection()) {
                    String pronunciation = "Phiên âm: ";
                    Vector<Vector<String>> data = GetOnlineResources.grabData(enWord);
                    // data[0] = pronunciation
                    // data[1] = example
                    // data[2] = synonyms
                    // data[3] = type
                    Vector<String> allPronunciation = data.get(0);
                    for (int i = 0; i < allPronunciation.size(); i++)
                        pronunciation += (allPronunciation.elementAt(i) + (i == allPronunciation.size() - 1 ? "" : ", "));
                    pronunciationField.setText(pronunciation);
                    resultField.appendText("Kiểu: ");
                    Vector<String> type = data.get(3);
                    for (int i = 0; i < type.size(); i++) {
                        resultField.appendText(type.elementAt(i) + (i==type.size()-1?"":", "));
                    }
                    resultField.appendText("\nVí dụ:\n");
                    Vector<String> allExample = data.get(1);
                    for (int i = 0; i < allExample.size(); i++) {
                        resultField.appendText("\t" + allExample.elementAt(i) + "\n");
                    }
                    resultField.appendText("\nTừ đồng nghĩa: ");
                    Vector<String> synonyms = data.get(2);
                    for (int i = 0; i < synonyms.size(); i++)
                        resultField.appendText(synonyms.elementAt(i) + (i == synonyms.size() - 1 ? "\n" : ", "));
                }
            }
        });
    }
    // Close button
    public void closeButtonAction() {
        Platform.exit();
    }
    // Handle search bar
    public void userSearch() {
        if (searchField.getText().equals("")) {
            wordList.setItems(words); // Restore words array
            return;
        }
        String finder = searchField.getText().toLowerCase();
        wordsFind = FXCollections.observableArrayList();
        for (int i = startingIndex[finder.charAt(0)-97]; i < (finder.charAt(0)-97==25?words.size():startingIndex[finder.charAt(0)-96]); i++)
            if (words.get(i).getWordTarget().toLowerCase().indexOf(finder) == 0)
                wordsFind.add(words.get(i));
        wordList.setItems(wordsFind);
    }
    // Handle delete button
    public void deleteButtonAction() {
        Word temp = wordList.getSelectionModel().getSelectedItem(); // Get index of selected word
        words.remove(temp);
        displayResult(true);
        userSearch();
    }
    // Handle add button
    public void addButtonAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dictionary");
        dialog.setHeaderText("Thêm từ mới");
        dialog.setContentText("Nhập từ tiếng Anh:");
        Optional<String> enWord = dialog.showAndWait();
        Optional<String> viWord;
        while (enWord.isPresent() && enWord.get().equals("")) {
            Alerter.EmptyEnglishWord();
            enWord = dialog.showAndWait();
        }
        if (!enWord.isPresent())
            return; // Quit if field is empty
        else {
            dialog = new TextInputDialog();
            dialog.setTitle("Dictionary");
            dialog.setHeaderText("Thêm từ mới");
            dialog.setContentText("Nhập nghĩa tiếng Việt:");

            viWord = dialog.showAndWait();
            while (viWord.isPresent() && viWord.get().equals("")) {
                Alerter.EmptyVietnameseMeaning();
                viWord = dialog.showAndWait();
                if (!viWord.isPresent())
                    return; // Quit if field is empty
            }
        }
        Word temp = new Word(enWord.get(), viWord.get());
        words.add(temp);
        words.sort(Word::compareTo);
        for (int i = (enWord.get().toLowerCase().charAt(0)-97+1); i < 26; i++)
            startingIndex[i]++;
        userSearch();
    }
    // Handle edit button
    public void editButtonAction() {
        Word temp = wordList.getSelectionModel().getSelectedItem(); // Get index of selected word
        String currentEn = temp.getWordTarget();
        String currentVi = temp.getWordExplain();
        TextInputDialog dialog = new TextInputDialog(currentEn);
        dialog.setTitle("Sửa");
        dialog.setHeaderText("Sửa từ tiếng Anh");
        dialog.setContentText("Sửa " + currentEn + " thành: ");
        Optional<String> enWord = dialog.showAndWait();
        Optional<String> viWord;
        while (enWord.isPresent() && enWord.get().equals("")) {
            Alerter.EmptyEnglishWord();
            enWord = dialog.showAndWait();
        }
        if (!enWord.isPresent())
            return; // Quit if field is empty
        else {
            dialog = new TextInputDialog(currentVi);
            dialog.setTitle("Sửa");
            dialog.setHeaderText("Sửa nghĩa tiếng Việt");
            dialog.setContentText("Đổi nghĩa tiếng Việt thành:");

            viWord = dialog.showAndWait();
            while (viWord.isPresent() && viWord.get().equals("")) {
                // Create error dialog
                Alerter.EmptyVietnameseMeaning();
                viWord = dialog.showAndWait();
                if (!viWord.isPresent())
                    return; // Quit if field is empty
            }
        }
        if (currentEn.equals(enWord.get()) && currentVi.equals(viWord.get()))
            return; // Quit if nothing change
        if (currentEn.equals(enWord.get()))
            temp.setWordExplain(viWord.get());
        if (currentVi.equals(viWord.get()))
            temp.setWordTarget(enWord.get());
        words.sort(Word::compareTo);
        userSearch();
    }
    // Handle speech button
    public void speechButtonAction() {
        if (wordList.getSelectionModel().getSelectedItem() == null)
            return;
        if (GetOnlineResources.checkConnectionToGoogle()) {
            TextToSpeech tts = new TextToSpeech();
            tts.speak(wordList.getSelectionModel().getSelectedItem().getWordTarget());
        }
        else
            Alerter.CannotConnectToGoogle();
    }
    // Handle export button
    public void exportButtonAction() {
        new DictionaryManagement().exportToFile(words);
    }
    // Handle Google Translate button
    public void openGoogleTranslate() {
        if (GetOnlineResources.checkConnectionToGoogle()) { // Check connection to Google
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/GoogleTranslate.fxml"));
                Parent root = (Parent) loader.load();
                Stage stage = new Stage();
                stage.setTitle("Google Translate");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            Alerter.CannotConnectToGoogle();
    }
}
