package io.github.pikibanana.util;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class GUIUtils {

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;

    public static ButtonWidget createButton(String label, int x, int y, int width, int height, ButtonWidget.PressAction action) {
        return ButtonWidget.builder(Text.of(label), action)
                .position(x, y)
                .dimensions(x, y, width, height)
                .tooltip(Tooltip.of(Text.of(label)))
                .build();
    }
}
