/**
 * FILE: GuessingGame
 *
 * Contains logic for a game of the user.
 */

import java.util.ArrayList;

public class GuessingGame {
    private ArrayList<Category> categories; // the categories of the game
    private int categoriesPassed; // number of categories passed
    private int consecutiveMisses; // number of consecutive round misses

    // generate categories for the game
    GuessingGame() {
        categories = new ArrayList<>(3);
        fillCategories();
    }

    public int getCategoriesPassed() { return categoriesPassed;}

    public void setCategoriesPassed(int num) { categoriesPassed = num;}

    public int getConsecutiveMisses() {return consecutiveMisses;}

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

    // method to fill the categories of the names
    private void fillCategories() {
        Category animals = new Category("Animals", "Chameleon", "Penguin", "Axolotl");
        Category food = new Category("Food", "Risotto", "Tiramisu", "Sorbet");
        Category usStates = new Category("US States", "Delaware", "Wyoming", "Georgia");

        categories.add(animals);
        categories.add(food);
        categories.add(usStates);
    }


}
