package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.DictionaryManagement;
import model.Word;
import org.w3c.dom.Text;

import java.util.Optional;

public class Controller {
    @FXML ListView<Word> wordList = new ListView<>();
    ObservableList<Word> words = FXCollections.observableArrayList();
    @FXML TextArea resultField;
    @FXML TextField searchField;
    @FXML MenuItem closeButton;
    @FXML MenuItem deleteButton;
    @FXML MenuItem addButton;
    @FXML MenuItem editButton;

    private Main main;
    private DictionaryManagement manager;

    public void setMain(Main main) {
        this.main = main;
        manager = new DictionaryManagement();
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
        // Handle mouse click on ListView
        wordList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resultField.setText(manager.EnVi.getVi(wordList.getSelectionModel().getSelectedIndex()));
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
            wordList.setItems(words);
            return;
        }
        String finder = searchField.getText().toLowerCase();
        ObservableList<Word> wordsFind = FXCollections.observableArrayList();
        for (int i = 0; i < manager.EnVi.getSize(); i++)
            if (manager.EnVi.getWord(i).getWordTarget().toLowerCase().indexOf(finder) == 0)
                wordsFind.add(manager.EnVi.getWord(i));
        wordList.setItems(wordsFind);
    }
    // Handle delete button
    public void deleteButtonAction() {
        int index = wordList.getSelectionModel().getSelectedIndex(); // Get index of selected word
        manager.EnVi.deleteAt(index);
        words.remove(index);
        wordList.setItems(words);
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
        int index = manager.EnVi.add(enWord.get(), viWord.get());
        words.add(index, manager.EnVi.getWord(index));
        wordList.setItems(words);
    }
    // Handle edit button
    public void editButtonAction() {
        int index = wordList.getSelectionModel().getSelectedIndex(); // Get index of selected word
        String currentEn = manager.EnVi.getEn(index);
        String currentVi = manager.EnVi.getVi(index);
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
        Word temp = new Word(enWord.get(), viWord.get());
        manager.EnVi.setWord(index, temp);
        if (currentEn.equals(enWord.get())) {
            words.set(index, manager.EnVi.getWord(index));
        }
        else {
            manager.EnVi.sort();
            words.remove(index);
            int indexNew = manager.EnVi.getIndex(temp);
            words.add(indexNew, temp);
        }
        wordList.setItems(words);
        resultField.setText(manager.EnVi.getVi(wordList.getSelectionModel().getSelectedIndex()));
    }
}
