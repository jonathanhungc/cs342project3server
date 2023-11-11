import java.util.Arrays;

public class Category {

    String name;
    private String[] words;
    private int wordsRemaining;
    private boolean completed;

    Category(String categoryName, String word1, String word2, String word3) {

        name = categoryName;

        words = new String[3];
        words[0] = word1;
        words[1] = word2;
        words[2] = word3;

        wordsRemaining = 3;
    }

    // get current word of the category
    public String getCurrentWord() {
        if (wordsRemaining == 0)
            return "None";

        return words[3 - wordsRemaining];
    }

    // get remaining number of words
    public int getWordsRemaining() { return wordsRemaining; }

    public void setCompleted() { completed = true; }
    public boolean getCompleted() { return completed; }

    public void nextWord() {
        if (wordsRemaining == 0)
            return;

        wordsRemaining -= 1;
    }

}
