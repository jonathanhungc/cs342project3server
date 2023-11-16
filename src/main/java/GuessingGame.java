import com.sun.imageio.plugins.common.SingleTileRenderedImage;
import javafx.beans.binding.StringBinding;

import java.util.ArrayList;

public class GuessingGame {
    ArrayList<Category> categories;
    int categoriesPassed;
    int consecutiveMisses;
    GuessingGame() {
        categories = new ArrayList<>(3);
        fillCategories();
    }

    public Category getCategory(String name) {
        for (Category e : categories) {
            if (e.name.equals(name))
                return e;
        }

        return null;
    }

    public void removeCategory(String name) {
        categories.removeIf(e -> e.name.equals(name));
    }

    public boolean wonGame() {return categoriesPassed == 3;}

    public boolean lostGame() {return consecutiveMisses == 3;}

    public String[] getCategoriesNames() {
        String[] names = new String[categories.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = categories.get(i).name;
        }
        return names;
    }

    public int[] getWordsInCategories() {
        int[] counts = new int[3];
        for (int i = 0; i < categories.size(); i++) {
            counts[i] = categories.get(i).getWordsRemaining();
        }
        return counts;
    }

    private void fillCategories() {
        Category animals = new Category("Animals", "Chameleon", "Penguin", "Axolotl");
        Category food = new Category("Food", "Risotto", "Tiramisu", "Sorbet");
        Category usStates = new Category("US States", "Delaware", "Wyoming", "Georgia");

        categories.add(animals);
        categories.add(food);
        categories.add(usStates);
    }


}
