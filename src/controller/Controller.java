package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.DictionaryManagement;
import model.Word;

import java.util.Optional;

public class Controller {
    @FXML ListView<Word> wordList = new ListView<>(); // For display
    ObservableList<Word> words = FXCollections.observableArrayList(); // Array of words
    ObservableList<Word> wordsFind; // Found word
    @FXML TextArea resultField;
    @FXML TextField searchField;
    @FXML MenuItem closeButton;
    @FXML MenuItem deleteButton;
    @FXML MenuItem addButton;
    @FXML MenuItem editButton;

    private Main main;

    public void setMain(Main main) {
        this.main = main;
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
        for (int i = 0; i < manager.EnVi.getSize(); i++)
            words.add(manager.EnVi.getWord(i));
        // Display english word
        wordList.setItems(words);
        manager = null;
        // Handle mouse click on ListView
        displayResult(false);
    }
    public void displayResult(boolean Null) {
        if (Null) {
            resultField.setText(null);
            return;
        }
        wordList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resultField.setText(wordList.getSelectionModel().getSelectedItem()!=null?wordList.getSelectionModel().getSelectedItem().getWordExplain():null);
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
        for (int i = 0; i < words.size(); i++)
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
        dialog.setHeaderText("Add a new word");
        dialog.setContentText("Enter English word:");
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
            dialog.setHeaderText("Add a new word");
            dialog.setContentText("Enter Vietnamese meaning:");

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
        userSearch();
    }
    // Handle edit button
    public void editButtonAction() {
        Word temp = wordList.getSelectionModel().getSelectedItem(); // Get index of selected word
        String currentEn = temp.getWordTarget();
        String currentVi = temp.getWordExplain();
        TextInputDialog dialog = new TextInputDialog(currentEn);
        dialog.setTitle("Edit");
        dialog.setHeaderText("Change English word");
        dialog.setContentText("Change " + currentEn + " to: ");
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
            dialog.setTitle("Edit");
            dialog.setHeaderText("Change Vietnamese meaning");
            dialog.setContentText("Change Vietnamese meaning to:");

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
}
