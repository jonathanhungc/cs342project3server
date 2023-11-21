/**
 * FILE: Category.java
 *
 * Used to represent a category of the game.
 */
public class Category {

    private String name; // name of the category
    private String[] words; // words in the category
    private int wordsRemaining; // number of words remaining

    // constructor that takes the category name and 3 words to add
    Category(String categoryName, String word1, String word2, String word3) {

        name = categoryName;

        words = new String[3];
        words[0] = word1;
        words[1] = word2;
        words[2] = word3;

        wordsRemaining = 3;
    }

    public String getName() { return name;}

    // get current word of the category
    public String getCurrentWord() {
        if (wordsRemaining == 0)
            return "None";

        return words[3 - wordsRemaining];
    }

    // get remaining number of words
    public int getWordsRemaining() { return wordsRemaining; }

    // move to the next word
    public void nextWord() {
        if (wordsRemaining == 0)
            return;

        wordsRemaining -= 1;
    }
}
