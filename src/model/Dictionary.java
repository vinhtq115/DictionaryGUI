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
    public int getIndex(Word word) {
        return dictionary.indexOf(word);
    }
    public int add(String en, String vi) {
        Word temp = new Word(en, vi);
        dictionary.add(temp);
        NumberOfWord = dictionary.size();
        sort();
        return dictionary.indexOf(temp);
    }
    public void delete(String en) {
        for (int i = 0; i < dictionary.size(); i++)
            if (dictionary.elementAt(i).getWordTarget().equals(en))
                dictionary.remove(i);
        NumberOfWord = dictionary.size();
    }
    public void deleteAt(int index) {
        dictionary.remove(index);
        NumberOfWord = dictionary.size();
    }
    public String getEn(int index) {
        return dictionary.elementAt(index).getWordTarget();
    }
    public void setWord(int index, String en, String vi) {
        dictionary.elementAt(index).setWordTarget(en);
        dictionary.elementAt(index).setWordExplain(vi);
    }
    public void setWord(int index, Word word) {
        dictionary.set(index, word);
    }
    public String getVi(int index) {
        return dictionary.elementAt(index).getWordExplain();
    }
    public void sort() {
        Collections.sort(dictionary);
    }
}
