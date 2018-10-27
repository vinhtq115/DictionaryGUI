package model;

import javafx.collections.ObservableList;

import java.io.*;

public class DictionaryManagement {
    public Dictionary EnVi = new Dictionary();

    public boolean insertFromFile() {
        try {
            File dicFile = new File("dictionaries.txt");
            FileReader fileReader = new FileReader(dicFile);
            BufferedReader reader = new BufferedReader(fileReader);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] word = line.split("\t");
                EnVi.add(word[0], word[1]);
            }
            EnVi.sort();
            reader.close();
            return true;
        }
        catch (IOException e) {
            System.out.println("Can't open dictionaries.txt!");
            return false;
        }
    }
    public void exportToFile(ObservableList<Word> words) {
        try {
            File dicFile = new File("dictionaries.txt");
            FileWriter fileWriter = new FileWriter(dicFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (int i = 0; i < words.size(); i++) {
                writer.write(words.get(i).getWordTarget() + "\t" + words.get(i).getWordExplain() + (i==words.size()-1?"":"\n"));
            }
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Can't write!");
        }
    }
}