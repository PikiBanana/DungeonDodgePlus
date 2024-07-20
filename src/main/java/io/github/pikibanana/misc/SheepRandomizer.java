package io.github.pikibanana.misc;

import io.github.pikibanana.data.DungeonData;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import java.util.List;
import java.util.Random;

public class SheepRandomizer {

    public static List<String> sheepTypes = List.of(
            "white_sheep", "white_sheep_lamb",
            "orange_sheep", "orange_sheep_lamb",
            "magenta_sheep", "magenta_sheep_lamb",
            "light_blue_sheep", "light_blue_sheep_lamb",
            "yellow_sheep", "yellow_sheep_lamb",
            "lime_sheep", "lime_sheep_lamb",
            "pink_sheep", "pink_sheep_lamb",
            "gray_sheep", "gray_sheep_lamb",
            "light_gray_sheep", "light_gray_sheep_lamb",
            "cyan_sheep", "cyan_sheep_lamb",
            "purple_sheep", "purple_sheep_lamb",
            "blue_sheep", "blue_sheep_lamb",
            "brown_sheep", "brown_sheep_lamb",
            "green_sheep", "green_sheep_lamb",
            "red_sheep", "red_sheep_lamb",
            "black_sheep", "black_sheep_lamb"
    );

    public static String getRandomSheepType() {
        Random random = new Random();
        return sheepTypes.get(random.nextInt(sheepTypes.size()));
    }

    public static void registerSheepCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("sheep")
                    .executes(context -> {
                        DungeonData dungeonData = DungeonData.getInstance();
                        dungeonData.setBoolean("sheepMode", !dungeonData.getBoolean("sheepMode", false));
                        return 1;
                    }));
        });
    }
}
