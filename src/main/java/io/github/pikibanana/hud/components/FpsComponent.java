package io.github.pikibanana.hud.components;

import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class FpsComponent extends DraggableComponent {
    private static final int PADDING = 5;
    private static final int BACKGROUND_COLOR = 0x66000000;

    private final MinecraftClient client;
    private boolean showBackground;

    public FpsComponent() {
        super("fps");
        this.client = MinecraftClient.getInstance();
        loadConfig();
    }

    private void loadConfig() {
        showBackground = DungeonDodgePlusConfig.get().features.showFPSCounter.backgroundColor;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!DungeonDodgePlusConfig.get().features.showFPSCounter.enabled) return;

        TextRenderer renderer = client.textRenderer;
        String fpsText = client.getCurrentFps() + " FPS";

        updateDimensions(renderer, fpsText);
        renderBackground(context);
        renderText(context, renderer, fpsText);
        loadPosition(DungeonData.getInstance());
    }

    private void updateDimensions(TextRenderer renderer, String text) {
        width = renderer.getWidth(text) + PADDING * 2;
        height = renderer.fontHeight + PADDING * 2;
    }

    private void renderBackground(DrawContext context) {
        if (showBackground) {
            context.fill(x, y, x + width, y + height, BACKGROUND_COLOR);
        }
    }

    private void renderText(DrawContext context, TextRenderer renderer, String text) {
        int color = DungeonDodgePlusConfig.get().features.showFPSCounter.textColor;
        context.drawText(renderer, text, x + PADDING, y + PADDING, color, false);
    }
}