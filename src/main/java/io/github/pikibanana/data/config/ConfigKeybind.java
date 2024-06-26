package io.github.pikibanana.data.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ConfigKeybind {

    public static void register() {
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key.dungeondodgeplus.config",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "category.dungeondodgeplus"
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {while (keyBinding.wasPressed()) {
            if (client.currentScreen != null) client.currentScreen.close();
            client.setScreen(AutoConfig.getConfigScreen(DungeonDodgePlusConfig.class, client.currentScreen).get());
        }});
    }

}
