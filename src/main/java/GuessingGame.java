import java.util.ArrayList;

public class GuessingGame {
    ArrayList<Category> categories;
    int categoriesPassed;
    Category currentCategory;
    char[] currentWord;
    char[] userGuess;
    int numLettersGuessed;
    int numMisses;

    GuessingGame() {
        categories = new ArrayList<>(3);
        fillCategories();
    }

    // method to set the current category and word to guess
    public void chooseCategory(Category choice) {
        currentCategory = choice;
        currentWord = currentCategory.getCurrentWord().toCharArray();
        userGuess = new char[currentWord.length];
    }

    public char[] getUserGuess() { return userGuess; }


    // check if the letter from the user is in the current word
    public boolean checkLetterInWord(char letter) {

        if (!validLetter(letter)) {
            numMisses++;
            return false;
        }

        for (int i = 0; i < currentWord.length; i++) {

            if (letter == currentWord[i]) {
                userGuess[i] = letter;
                numLettersGuessed++;
            }
        }

        return true;
    }

    // used to check if the user won/lost, or continue playing
    public String endCategory() {
        if (numLettersGuessed == currentWord.length) {
            currentCategory.setCompleted();
            categoriesPassed++;
            return "User wins";
        }
        else if (numMisses == 6) {
            currentCategory.nextWord();
            return "User loses";
        }

        return "Continue";
    }


    // get length of current word
    public int getCurrentWordLength() { return currentWord.length; }


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

    private void fillCategories() {
        Category animals = new Category("Animals", "Tiger", "Penguin", "Giraffe");
        Category food = new Category("Food", "Risotto", "Burger", "Pizza");
        Category usStates = new Category("US States", "Illinois", "Wyoming", "Georgia");

        categories.add(animals);
        categories.add(food);
        categories.add(usStates);
    }


}
