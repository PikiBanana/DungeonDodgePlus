package io.github.pikibanana.misc;

import io.github.pikibanana.data.DungeonData;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import java.util.List;
import java.util.Random;

public class SheepRandomizer {
    private static final Random RANDOM = new Random();
    private static final List<String> COLORS = List.of(
            "white", "orange", "magenta", "light_blue", "yellow", "lime",
            "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown",
            "green", "red", "black"
    );

    private static boolean sheepEnabled = DungeonData.getInstance().getBoolean("sheepMode", false);;
    private static boolean isBaby = false;
    private static boolean isJeb = false;
    private static int currentColorIndex = 0;
    private static long lastUpdate;

    public static String getCurrentSheep() {
        if (!sheepEnabled) return "";

        if (isJeb && System.currentTimeMillis() - lastUpdate > 200) {
            currentColorIndex = (currentColorIndex + 1) % COLORS.size();
            lastUpdate = System.currentTimeMillis();
        }

        String base = COLORS.get(currentColorIndex) + "_sheep";
        return isBaby ? base + "_lamb" : base;
    }

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("sheep")
                    .executes(context -> {
                        DungeonData.getInstance().setBoolean("sheepMode", !sheepEnabled);
                        sheepEnabled = !sheepEnabled;
                        return 1;
                    })
                    .then(ClientCommandManager.literal("jeb")
                            .executes(context -> {
                                isJeb = !isJeb;
                                currentColorIndex = 0;
                                return 1;
                            }))
                    .then(ClientCommandManager.literal("baby")
                            .executes(context -> {
                                isBaby = !isBaby;
                                return 1;
                            }))
                    .then(ClientCommandManager.literal("reroll")
                            .executes(context -> {
                                if (!isJeb) {
                                    currentColorIndex = RANDOM.nextInt(COLORS.size());
                                }
                                return 1;
                            }))
            );
        });
    }
}