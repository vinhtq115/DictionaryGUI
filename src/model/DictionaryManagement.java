package model;

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
}