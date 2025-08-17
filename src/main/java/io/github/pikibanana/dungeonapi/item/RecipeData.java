package io.github.pikibanana.dungeonapi.item;

import net.minecraft.recipe.RecipeDisplayEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecipeData {

    private static List<RecipeDisplayEntry> recipes = new ArrayList<>();

    public static List<RecipeDisplayEntry> getRecipes() {
        return recipes;
    }

    public static void setRecipes(List<RecipeDisplayEntry> recipes) {
        RecipeData.recipes = new ArrayList<>(recipes);
        RecipeData.recipes.sort(Comparator.comparingInt(r -> {
            if (r.group().isEmpty()) return 0;
            return r.group().getAsInt();
        }));
    }
}
