package io.github.pikibanana.gui.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public abstract class BaseReturnableScreen extends Screen {

    public BaseReturnableScreen(Text title) {
        super(title);
    }

    @Override
    public void close() {
        ScreenManager.popScreen();
    }

    @Override
    public boolean keyPressed(int key, int scancode, int modifiers) {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            ScreenManager.popScreen();
            return true;
        }
        return super.keyPressed(key, scancode, modifiers);
    }
}
