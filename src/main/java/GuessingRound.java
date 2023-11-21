/**
 * FILE: GuessingRound.java
 *
 * Contains the logic for a round of guessing one word. A user's game will use multiple rounds.
 */
public class GuessingRound {

    private String currentCategoryName; // name of the category
    private final char[] currentWord; // word to guess
    private char[] userGuess; // user's current guess
    private int numLettersGuessed; // number of letters guessed
    private int numMisses; // number of misses

    // Takes a Category object and takes the name and the current word to guess in the category. It initializes the
    // userGuess to an empty array of chars, and initializes variables to 0.
    GuessingRound(Category selection) {
        currentCategoryName = selection.getName();
        currentWord = selection.getCurrentWord().toCharArray();
        userGuess = new char[currentWord.length];
        numLettersGuessed = 0;
        numMisses = 0;
    }

    // getter for the current word in the round
    public String getCurrentWord() {
        return new String(currentWord);
    }

    // getter for the current category name
    public String getCurrentCategoryName() {
        return currentCategoryName;
    }

    public char[] getUserGuess() { return userGuess; }

    public int getNumLettersGuessed() { return numLettersGuessed; }

    public int getNumMisses() { return numMisses; }

    // check if the letter from the user is in the current word
    public void checkLetterInWord(char letter) {

        // letter not valid, increment number of misses
        if (!validLetter(letter)) {
            numMisses++;
        }

        // check each letter in currentWord, and if there is a match, add letter to userGuess at specific position
        for (int i = 0; i < currentWord.length; i++) {

            if (letter == currentWord[i]) {
                userGuess[i] = letter;
                numLettersGuessed++;
            }
        }
    }

    // check if user won round
    public boolean wonRound() { return numLettersGuessed == currentWord.length; }

    // check if user lost round
    public boolean lostRound() { return numMisses == 6;}

    // check if letter is repeated, or not in currentWord
    private boolean validLetter(char letter) {

        int counter = 0;

        for (int i = 0; i < userGuess.length; i++) {
            if (letter == userGuess[i]) // letter is repeated
                return false;

            if (letter == currentWord[i]) // count the number of times letter is in current word
                counter++;
        }

        // letter is not in current word
        return counter != 0;
    }

}
