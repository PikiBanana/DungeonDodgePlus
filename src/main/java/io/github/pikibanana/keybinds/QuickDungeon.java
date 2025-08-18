package io.github.pikibanana.keybinds;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.DungeonDifficulty;
import io.github.pikibanana.dungeonapi.DungeonDodgeConnection;
import io.github.pikibanana.dungeonapi.DungeonType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class QuickDungeon {

    private static final KeyBinding[] quickDungeonKeyBindings = new KeyBinding[3];

    public static void register() {
        for (int i = 0; i < 3; i++) {
            final int slotIndex = i + 1;
            quickDungeonKeyBindings[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.dungeondodgeplus.quickDungeon." + slotIndex,
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_UNKNOWN,
                    "category.dungeondodgeplus"
            ));
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (int i = 0; i < 3; i++) {
                if (quickDungeonKeyBindings[i].wasPressed()) {
                    handleQuickDungeonKeyPress(i);
                }
            }
        });
    }

    private static void handleQuickDungeonKeyPress(int slotIndex) {
        DungeonDodgePlusConfig.Features.QuickDungeonHotkeys quickDungeon = Main.features.quickDungeon;

        DungeonType dungeonType = DungeonType.UNKNOWN;
        DungeonDifficulty dungeonDifficulty = DungeonDifficulty.NORMAL;

        switch (slotIndex) {
            case 0:
                dungeonType = quickDungeon.dungeonType;
                dungeonDifficulty = quickDungeon.dungeonDifficulty;
                break;
            case 1:
                dungeonType = quickDungeon.dungeonType2;
                dungeonDifficulty = quickDungeon.dungeonDifficulty2;
                break;
            case 2:
                dungeonType = quickDungeon.dungeonType3;
                dungeonDifficulty = quickDungeon.dungeonDifficulty3;
                break;
            default:
                break;
        }

        if (dungeonType != DungeonType.UNKNOWN && DungeonDodgeConnection.isConnected()) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                String quickDungeonCommand = "quickdungeon  " + dungeonType.getCommandID() + " " + dungeonDifficulty.name().toLowerCase();
                player.networkHandler.sendCommand(quickDungeonCommand);
            }
        }
    }
}
