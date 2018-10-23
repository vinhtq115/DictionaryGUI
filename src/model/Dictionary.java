package model;

import java.util.Collections;
import java.util.Vector;

public class Dictionary {
    private Vector<Word> dictionary = new Vector<>();
    private int NumberOfWord;
    public int getSize() {
        return NumberOfWord;
    }
    public Word getWord(int i) {
        return dictionary.elementAt(i);
    }
    public void add(String en, String vi) {
        dictionary.add(new Word(en, vi));
        NumberOfWord = dictionary.size();
    }
    public void delete(String en) {
        for (int i = 0; i < dictionary.size(); i++)
            if (dictionary.elementAt(i).getWordTarget().equals(en))
                dictionary.remove(i);
        NumberOfWord = dictionary.size();
    }
    public String getEn(int index) {
        return dictionary.elementAt(index).getWordTarget();
    }
    public String getVi(int index) {
        return dictionary.elementAt(index).getWordExplain();
    }
    public void sort() {
        Collections.sort(dictionary);
    }
}
