package io.github.pikibanana.hud;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class FPSRenderer {

    private static final DungeonData dungeonData = DungeonData.getInstance();

    public static void drawFPS(DrawContext context, TextRenderer renderer, String fpsText, int fpsX, int fpsY, int fpsWidth, int fpsHeight) {
        int color = DungeonDodgePlusConfig.get().features.showFPSCounter.textColor; //white (default)
        int bg = 0x66000000; //translucent black
        boolean showBackground = Main.features.showFPSCounter.backgroundColor;

        if (showBackground) context.fill(fpsX, fpsY + 1, fpsWidth, fpsHeight - 2, bg);
        context.drawText(renderer, fpsText, fpsX + 5, fpsY + 5, color, false);
    }

    public static void renderFPS(DrawContext context, RenderTickCounter tickCounter) {
        if (DungeonDodgePlusConfig.get().features.showFPSCounter.enabled) {

            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            String fpsText = MinecraftClient.getInstance().getCurrentFps() + " FPS";

            int fpsX = dungeonData.getInt("fpsX", 10);
            int fpsY = dungeonData.getInt("fpsY", 10);
            int fpsWidth = fpsX + 10 + renderer.getWidth(fpsText);
            int fpsHeight = fpsY + 10 + renderer.fontHeight;

            drawFPS(context, renderer, fpsText, fpsX, fpsY, fpsWidth, fpsHeight);
        }
    }

}
