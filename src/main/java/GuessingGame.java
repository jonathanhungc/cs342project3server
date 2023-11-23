/**
 * FILE: GuessingGame
 *
 * Contains logic for a game of the user. It's used to keep track if the user won or lost, and to
 * fill the categories for the user.
 */

import java.util.ArrayList;
import java.util.Random;

public class GuessingGame {
    private ArrayList<Category> categories; // the categories of the game
    private int categoriesPassed; // number of categories passed
    private int consecutiveMisses; // number of consecutive round misses
    private final String[] foodsArr = {"mango", "sushi", "sorbet", "tofu", "apple",
            "noodles", "quinoa", "salmon", "blueberries", "avocado",
            "pancakes", "falafel", "broccoli", "chocolate", "tacos",
            "grapes", "yogurt", "tiramisu", "shrimp", "risotto"};
    private final String[] animalsArr = {"elephant", "giraffe", "zebra", "lion", "tiger",
            "kangaroo", "penguin", "koala", "jaguar", "cheetah",
            "lemur", "panda", "armadillo", "crocodile", "seagull",
            "albatross", "chameleon", "hedgehog", "penguin", "ocelot"};
    private final String[] USstatesArr = {"alabama", "alaska", "arkansas", "delaware", "hawaii",
            "idaho", "iowa", "kansas", "kentucky", "maine",
            "mississippi", "montana", "nebraska", "nevada", "wyoming",
            "utah", "vermont", "florida", "georgia", "oklahoma"};



    // generate categories for the game
    GuessingGame() {
        categories = new ArrayList<>(3);
        fillCategories();
    }

    // getter for number of categories passed
    public int getCategoriesPassed() { return categoriesPassed;}

    // setter for number of categories passed
    public void setCategoriesPassed(int num) { categoriesPassed = num;}

    // getter for number of consecutive misses
    public int getConsecutiveMisses() {return consecutiveMisses;}

    // setter for number of consecutive misses
    public void setConsecutiveMisses(int num) { consecutiveMisses = num;}


    // getter for a Category by its name
    public Category getCategory(String name) {
        for (Category e : categories) {
            if (e.getName().equals(name))
                return e;
        }

        return null;
    }

    // remove a category from the game by its name
    public void removeCategory(String name) {
        categories.removeIf(e -> e.getName().equals(name));
    }

    // check if game is won
    public boolean wonGame() {return categoriesPassed == 3;}

    // check if game is lost
    public boolean lostGame() {

        for (Category e : categories) {
            if (e.getWordsRemaining() == 0)
                return true;
        }
        return consecutiveMisses == 3;
    }

    // getter for the names of the categories
    public String[] getCategoriesNames() {
        String[] names = new String[categories.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = categories.get(i).getName();
        }
        return names;
    }

    // getter for the number of words in each category
    public int[] getWordsInCategories() {
        int[] counts = new int[3];
        for (int i = 0; i < categories.size(); i++) {
            counts[i] = categories.get(i).getWordsRemaining();
        }
        return counts;
    }

    // method to fill the categories
    private void fillCategories() {
        Category animals = fillSingleCategory("animals", animalsArr);
        Category food = fillSingleCategory("foods", foodsArr);
        Category usStates = fillSingleCategory("us states", USstatesArr);

        categories.add(animals);
        categories.add(food);
        categories.add(usStates);
    }

    // method to get a Category with 3 words chosen randomly from a pool of strings
    private Category fillSingleCategory(String name, String[] arr) {
        Random random = new Random();

        // array to store 3 random indexes
        int[] indexes = new int[3];
        for (int i = 0; i < indexes.length; i++) {

            int randomIndex;
            boolean isUnique;

            do {
                randomIndex = random.nextInt(20);
                isUnique = true;

                // Check if the generated number is already in the array
                for (int j = 0; j < i; j++) {
                    if (randomIndex == indexes[j]) {
                        isUnique = false;
                        break;
                    }
                }
            } while (!isUnique);

            indexes[i] = randomIndex;
        }

        return new Category(name, arr[indexes[0]], arr[indexes[1]], arr[indexes[2]]);
    }
}
