package io.github.pikibanana.dungeonapi.item;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemData {

    private static final List<DungeonDodgeItem> items = new ArrayList<>();

    public static List<DungeonDodgeItem> getItems() {
        return items;
    }

    public static void clearItems() {
        items.clear();
    }

    public static void addItem(String itemID, ItemStack itemStack) {
        items.add(new DungeonDodgeItem(itemID, itemStack));
    }
}
