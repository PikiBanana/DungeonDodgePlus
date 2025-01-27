package io.github.pikibanana.keybinds;

import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.gui.screens.ScreenManager;
import io.github.pikibanana.music.MusicManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybinds {

    public static void register() {

        KeyBinding hidePlayers = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key.dungeondodgeplus.hidePlayers",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_DELETE,
                        "category.dungeondodgeplus"
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (hidePlayers.wasPressed()) {
                DungeonData.getInstance().setBoolean("hidePlayers", !DungeonData.getInstance().getBoolean("hidePlayers", false));
            }
        });

        KeyBinding skipSongKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key.dungeondodgeplus.skipSong",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_COMMA,
                        "category.dungeondodgeplus.music"
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (skipSongKeybind.wasPressed()) {
                MusicManager.skip();
            }
        });

        KeyBinding stopSongKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key.dungeondodgeplus.stopSong",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_PERIOD,
                        "category.dungeondodgeplus.music"
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (stopSongKeybind.wasPressed()) {
                MusicManager.stopMusic();
            }
        });
    }
}

