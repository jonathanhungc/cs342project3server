public class GuessingRound {

    String currentCategoryName;
    private final char[] currentWord;
    char[] userGuess;
    int numLettersGuessed;
    int numMisses;

    GuessingRound(Category selection) {
        currentCategoryName = selection.name;
        currentWord = selection.getCurrentWord().toCharArray();
        userGuess = new char[currentWord.length];
        numLettersGuessed = 0;
        numMisses = 0;
    }

    // check if the letter from the user is in the current word
    public void checkLetterInWord(char letter) {

        if (!validLetter(letter)) {
            numMisses++;
        }

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
