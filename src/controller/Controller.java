package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.DictionaryManagement;
import model.Word;

import java.util.Vector;

public class Controller {
    // Views
    @FXML ListView<Word> wordList = new ListView<>();
    ObservableList<Word> words = FXCollections.observableArrayList();
    @FXML TextArea resultField;
    @FXML TextField searchField;

    private Main main;
    private DictionaryManagement manager;

    public void setMain(Main main) {
        this.main = main;
        manager = new DictionaryManagement();
        manager.insertFromFile();
        for (int i = 0; i < manager.EnVi.getSize(); i++)
            words.add(manager.EnVi.getWord(i));
        wordList.setItems(words);
        wordList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Word>() {
            @Override
            public void changed(ObservableValue<? extends Word> observable, Word oldValue, Word newValue) {
                resultField.setText(newValue.getWordExplain());
            }
        });
    }

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
}
