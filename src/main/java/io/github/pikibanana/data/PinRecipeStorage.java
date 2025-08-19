package io.github.pikibanana.data;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// this stuff caused a lot of pain

public class PinRecipeStorage {
    private static final Path SAVE_PATH = Path.of("config", "dungeondodge", "pinnedRecipes.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save(DefaultedList<DefaultedList<ItemStack>> recipes) {
        JsonArray outer = new JsonArray();
        for (var recipe : recipes) {
            JsonArray inner = new JsonArray();
            for (var stack : recipe) {
                if (stack.isEmpty() || stack.getItem() == Items.AIR) {
                    inner.add(JsonNull.INSTANCE); // empty slots stay null
                } else {
                    // safely encode stack, skip if it fails
                    stack = stack.copy(); // just in case
                    inner.add(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack)
                            .resultOrPartial(err -> System.err.println("Failed to encode stack: " + err))
                            .orElse(JsonNull.INSTANCE));
                }
            }
            outer.add(inner);
        }

        try {
            Files.createDirectories(SAVE_PATH.getParent());
            Files.writeString(SAVE_PATH, GSON.toJson(outer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DefaultedList<DefaultedList<ItemStack>> load(int max, int size) {
        DefaultedList<DefaultedList<ItemStack>> recipes =
                DefaultedList.ofSize(max, DefaultedList.ofSize(size, ItemStack.EMPTY));

        if (!Files.exists(SAVE_PATH)) return recipes;

        try {
            String content = Files.readString(SAVE_PATH);
            var json = JsonParser.parseString(content).getAsJsonArray();

            for (int i = 0; i < json.size() && i < max; i++) {
                var inner = json.get(i).getAsJsonArray();
                DefaultedList<ItemStack> recipe = DefaultedList.ofSize(size, ItemStack.EMPTY);

                for (int j = 0; j < inner.size() && j < size; j++) {
                    JsonElement elem = inner.get(j);
                    if (elem == null || elem.isJsonNull()) {
                        recipe.set(j, ItemStack.EMPTY);
                    } else {
                        ItemStack stack = ItemStack.CODEC.parse(JsonOps.INSTANCE, elem)
                                .result()
                                .orElse(ItemStack.EMPTY);
                        recipe.set(j, stack);
                    }
                }
                recipes.set(i, recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }
}
