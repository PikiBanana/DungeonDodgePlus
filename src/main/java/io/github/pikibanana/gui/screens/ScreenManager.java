package io.github.pikibanana.gui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.Stack;

public class ScreenManager {
    private static final Stack<Screen> screenStack = new Stack<>();
    private static final MinecraftClient client = MinecraftClient.getInstance();

    // Push the current screen onto the stack and switch to a new one
    public static void pushScreen(Screen newScreen) {
        if (client.currentScreen != null) {
            screenStack.push(client.currentScreen);
        }
        client.setScreen(newScreen);
    }

    // Return to the previous screen
    public static void popScreen() {
        if (!screenStack.isEmpty()) {
            client.setScreen(screenStack.pop());
        } else {
            client.setScreen(null);
        }
    }

    // Get the previous screen directly if needed
    public static Screen getPreviousScreen() {
        return screenStack.isEmpty() ? null : screenStack.peek();
    }
}
