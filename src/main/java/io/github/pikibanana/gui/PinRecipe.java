package io.github.pikibanana.gui;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.PinRecipeStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.Predicate;

public class PinRecipe {
    // The size of each pinned recipe, which is a 3x3 grid plus a result slot,
    // hence 10 slots in total (9 for the grid + 1 for
    private static final int RECIPE_SIZE = 10;

    private static final DefaultedList<DefaultedList<ItemStack>> pinnedRecipes =
            DefaultedList.ofSize(Main.features.recipePinning.maxPinnedRecipes, DefaultedList.ofSize(RECIPE_SIZE, ItemStack.EMPTY));
    private static final Predicate<ItemStack> EXCLUSION_FILTER = stack ->
            stack.getItem() == Items.GRAY_STAINED_GLASS_PANE;
    private static int currentRecipeIndex = 0;

    static {
        if (Main.features.recipePinning.savePinsAcrossInstances) {
            var loaded = PinRecipeStorage.load(Main.features.recipePinning.maxPinnedRecipes, RECIPE_SIZE);

            for (int i = 0; i < Main.features.recipePinning.maxPinnedRecipes; i++) {
                DefaultedList<ItemStack> recipe;
                if (i < loaded.size()) {
                    recipe = loaded.get(i);
                } else {
                    recipe = DefaultedList.ofSize(RECIPE_SIZE, ItemStack.EMPTY);
                }
                pinnedRecipes.set(i, recipe);
            }

            for (int i = loaded.size() - 1; i >= 0; i--) {
                if (!isRecipeEmpty(loaded.get(i))) {
                    currentRecipeIndex = i;
                    break;
                }
            }
        }
    }

    private static boolean isRecipeEmpty(DefaultedList<ItemStack> recipe) {
        for (ItemStack stack : recipe) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    public static void pin(DefaultedList<ItemStack> items) {
        if (items == null || items.isEmpty()) return;

        DefaultedList<ItemStack> recipeCopy = DefaultedList.ofSize(RECIPE_SIZE, ItemStack.EMPTY);
        int copyIndex = 0;

        for (ItemStack stack : items) {
            if (copyIndex >= RECIPE_SIZE) break;
            if (!EXCLUSION_FILTER.test(stack)) {
                recipeCopy.set(copyIndex, stack.copy());
                copyIndex++;
            }
        }

        if (isAlreadyPinned(recipeCopy)) return;

        // Pin to the current index
        pinnedRecipes.set(currentRecipeIndex, recipeCopy);

        if (Main.features.recipePinning.savePinsAcrossInstances) {
            PinRecipeStorage.save(pinnedRecipes);
        }
    }


    private static boolean isAlreadyPinned(DefaultedList<ItemStack> newRecipe) {
        for (DefaultedList<ItemStack> recipe : pinnedRecipes) {
            if (areRecipesEqual(recipe, newRecipe)) {
                return true;
            }
        }
        return false;
    }

    private static boolean areRecipesEqual(DefaultedList<ItemStack> a, DefaultedList<ItemStack> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!ItemStack.areItemsAndComponentsEqual(a.get(i), b.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static DefaultedList<ItemStack> getCurrentPinned() {
        if (pinnedRecipes.isEmpty()) {
            return DefaultedList.ofSize(RECIPE_SIZE, ItemStack.EMPTY);
        }

        int safeIndex = currentRecipeIndex % pinnedRecipes.size();
        return pinnedRecipes.get(safeIndex);
    }


    public static boolean hasPinned() {
        for (DefaultedList<ItemStack> recipe : pinnedRecipes) {
            for (ItemStack stack : recipe) {
                if (!stack.isEmpty()) return true;
            }
        }
        return false;
    }

    public static void clearCurrent() {
        int index = (currentRecipeIndex - 1 + Main.features.recipePinning.maxPinnedRecipes) % io.github.pikibanana.Main.features.recipePinning.maxPinnedRecipes;
        pinnedRecipes.set(index, DefaultedList.ofSize(RECIPE_SIZE, ItemStack.EMPTY));

        if (Main.features.recipePinning.savePinsAcrossInstances) {
            PinRecipeStorage.save(pinnedRecipes);
        }

    }

    public static void clearAll() {
        for (int i = 0; i < Main.features.recipePinning.maxPinnedRecipes; i++) {
            pinnedRecipes.set(i, DefaultedList.ofSize(RECIPE_SIZE, ItemStack.EMPTY));
        }
        currentRecipeIndex = 0;

        if (Main.features.recipePinning.savePinsAcrossInstances) {
            PinRecipeStorage.save(pinnedRecipes);
        }
    }

    public static void cycleNext() {
        currentRecipeIndex = (currentRecipeIndex + 1) % Main.features.recipePinning.maxPinnedRecipes;
    }

    public static void cyclePrevious() {
        currentRecipeIndex = (currentRecipeIndex - 1 + Main.features.recipePinning.maxPinnedRecipes) % io.github.pikibanana.Main.features.recipePinning.maxPinnedRecipes;
    }

    public static int getCurrentIndex() {
        return (currentRecipeIndex - 1 + Main.features.recipePinning.maxPinnedRecipes) % io.github.pikibanana.Main.features.recipePinning.maxPinnedRecipes;
    }

    public static int getMaxRecipes() {
        return Main.features.recipePinning.maxPinnedRecipes;
    }
}