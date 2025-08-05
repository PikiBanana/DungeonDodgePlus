package io.github.pikibanana.hud.components;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.essence.EssenceCounter;
import io.github.pikibanana.misc.SheepRandomizer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class EssenceComponent extends DraggableComponent {
    private static final Identifier ESSENCE_TEXTURE = Identifier.of(Main.MOD_ID, "textures/gui/essence.png");
    private static final int MIN_SIZE = 35;
    private static final int MAX_SIZE = 120;
    private static final int SCROLL_SENSITIVITY = 5;
    private static final float SHEEP_ASPECT_RATIO = 875f / 750f;
    private static final int TEXT_OFFSET = 5;

    private final MinecraftClient client;
    private float aspectRatio = 1.0f;

    public EssenceComponent() {
        super("essence");
        this.client = MinecraftClient.getInstance();
        loadComponentData();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!DungeonDodgePlusConfig.get().features.essenceCounter.isEnabled()) return;

        final boolean sheepMode = DungeonData.getInstance().getBoolean("sheepMode", false);
        final Identifier texture = getCurrentTexture(sheepMode);

        updateDimensions(sheepMode);
        renderTexture(context, texture);
        renderText(context);
        loadPosition(DungeonData.getInstance());
    }

    private Identifier getCurrentTexture(boolean sheepMode) {
        if (!sheepMode) return ESSENCE_TEXTURE;
        if (SheepRandomizer.getCurrentSheep().isEmpty()) return ESSENCE_TEXTURE;
        return Identifier.of(Main.MOD_ID, "textures/gui/sheep/" + SheepRandomizer.getCurrentSheep() + ".png");
    }

    private void updateDimensions(boolean sheepMode) {
        aspectRatio = sheepMode ? SHEEP_ASPECT_RATIO : 1f;
        height = (int) (width * aspectRatio);
    }


    private void renderTexture(DrawContext context, Identifier texture) {
        context.drawTexture(identifier -> RenderLayer.getGuiTextured(texture), texture, x, y, 0, 0, width, height, width, height);
    }

    private void renderText(DrawContext context) {
        final TextRenderer textRenderer = client.textRenderer;
        final String displayText = EssenceCounter.getInstance().getDisplayText();
        final int textColor = DungeonDodgePlusConfig.get().features.essenceCounter.color;

        final TextLayout layout = calculateTextLayout(textRenderer, displayText);

        for (int i = 0; i < layout.lines.length; i++) {
            context.drawText(textRenderer, layout.lines[i],
                    layout.x, layout.y + (i * (textRenderer.fontHeight + 2)),
                    textColor, false);
        }
    }

    private TextLayout calculateTextLayout(TextRenderer renderer, String text) {
        final int screenWidth = client.getWindow().getScaledWidth();
        final String[] lines = text.split("\n");
        final int textWidth = renderer.getWidth(text);

        int textX = x + width + TEXT_OFFSET;
        textX = Math.min(textX, screenWidth - textWidth);

        final int totalHeight = (renderer.fontHeight + 2) * lines.length - 2;
        final int textY = y + (height - totalHeight) / 2;

        return new TextLayout(lines, textX, textY);
    }

    public void handleScroll(double delta) {
        width = clampSize(width + (int) (delta * SCROLL_SENSITIVITY));
        height = (int) (width * aspectRatio);
        saveComponentData();
    }

    private int clampSize(int size) {
        return Math.max(MIN_SIZE, Math.min(MAX_SIZE, size));
    }

    private void saveComponentData() {
        DungeonData data = DungeonData.getInstance();
        data.setInt(configPrefix + "Width", width);
        data.setInt(configPrefix + "Height", height);
    }

    private void loadComponentData() {
        DungeonData data = DungeonData.getInstance();
        width = data.getInt(configPrefix + "Width", 50);
        height = data.getInt(configPrefix + "Height", 50);
    }

    @Override
    protected void loadPosition(DungeonData data) {
        super.loadPosition(data);
        loadComponentData();
    }

    private record TextLayout(String[] lines, int x, int y) {}
}