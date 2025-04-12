package io.github.pikibanana.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.Predicate;

import static net.minecraft.sound.SoundEvents.BLOCK_NOTE_BLOCK_BASS;

public class PinRecipe {
    private static final int MAX_PINNED_RECIPES = 5;
    private static final int RECIPE_SIZE = 10;

    private static final DefaultedList<DefaultedList<ItemStack>> pinnedRecipes =
            DefaultedList.ofSize(MAX_PINNED_RECIPES, DefaultedList.ofSize(RECIPE_SIZE, ItemStack.EMPTY));
    private static final Predicate<ItemStack> EXCLUSION_FILTER = stack ->
            stack.getItem() == Items.GRAY_STAINED_GLASS_PANE;
    private static int currentRecipeIndex = 0;

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

        pinnedRecipes.set(currentRecipeIndex, recipeCopy);
        currentRecipeIndex = (currentRecipeIndex + 1) % MAX_PINNED_RECIPES;
    }

    private static boolean isAlreadyPinned(DefaultedList<ItemStack> newRecipe) {
        for (DefaultedList<ItemStack> recipe : pinnedRecipes) {
            if (areRecipesEqual(recipe, newRecipe)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRecipeEmpty(DefaultedList<ItemStack> recipe) {
        for (ItemStack stack : recipe) {
            if (!stack.isEmpty()) return false;
        }
        return true;
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
        return pinnedRecipes.get((currentRecipeIndex - 1 + MAX_PINNED_RECIPES) % MAX_PINNED_RECIPES);
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
        int index = (currentRecipeIndex - 1 + MAX_PINNED_RECIPES) % MAX_PINNED_RECIPES;
        pinnedRecipes.set(index, DefaultedList.ofSize(RECIPE_SIZE, ItemStack.EMPTY));
    }

    public static void clearAll() {
        for (int i = 0; i < MAX_PINNED_RECIPES; i++) {
            pinnedRecipes.set(i, DefaultedList.ofSize(RECIPE_SIZE, ItemStack.EMPTY));
        }
        currentRecipeIndex = 0;
    }

    public static void cycleNext() {
        for (int i = 1; i < MAX_PINNED_RECIPES; i++) {
            int nextIndex = (currentRecipeIndex + i) % MAX_PINNED_RECIPES;
            if (!isRecipeEmpty(pinnedRecipes.get(nextIndex))) {
                currentRecipeIndex = nextIndex;
                return;
            }
        }
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.playSound(Registries.SOUND_EVENT.get(BLOCK_NOTE_BLOCK_BASS.registryKey()), 1.0F, 0.8F);
        }
    }

    public static void cyclePrevious() {
        for (int i = 1; i < MAX_PINNED_RECIPES; i++) {
            int prevIndex = (currentRecipeIndex - i + MAX_PINNED_RECIPES) % MAX_PINNED_RECIPES;
            if (!isRecipeEmpty(pinnedRecipes.get(prevIndex))) {
                currentRecipeIndex = prevIndex;
                return;
            }
        }
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.playSound(Registries.SOUND_EVENT.get(BLOCK_NOTE_BLOCK_BASS.registryKey()), 1.0F, 0.8F);
        }
    }

    public static int getCurrentIndex() {
        return (currentRecipeIndex - 1 + MAX_PINNED_RECIPES) % MAX_PINNED_RECIPES;
    }

    public static int getMaxRecipes() {
        return MAX_PINNED_RECIPES;
    }
}
