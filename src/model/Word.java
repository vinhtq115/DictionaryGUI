package model;

public class Word implements Comparable {
    private String word_target;
    private String word_explain;

    Word(String en, String vi) {
        word_target = en;
        word_explain = vi;
    }
    // Setter
    public void setWordExplain(String word_explain) {
        this.word_explain = word_explain;
    }
    public void setWordTarget(String word_target) {
        this.word_target = word_target;
    }

    // Getter
    public String getWordExplain() {
        return word_explain;
    }
    public String getWordTarget() {
        return word_target;
    }
    @Override
    public int compareTo(Object o) {
        Word a = (Word) o;
        if (!word_target.toLowerCase().equals(a.getWordTarget().toLowerCase()))
            return word_target.toLowerCase().compareTo(a.getWordTarget().toLowerCase());
        else
            return word_explain.toLowerCase().compareTo(a.getWordExplain().toLowerCase());
    }
    @Override
    public String toString() {
        return this.word_target;
    }
}
