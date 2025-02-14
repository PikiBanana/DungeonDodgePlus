package io.github.pikibanana.hud.components;

import io.github.pikibanana.data.DungeonData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public abstract class ProgressBarComponent extends DraggableComponent {
    protected final Identifier filledTexture;
    protected final Identifier emptyTexture;
    protected final ProgressDirection direction;
    protected final MinecraftClient client;
    protected final int baseWidth;
    protected final int baseHeight;
    protected final int minSize;
    protected final int maxSize;
    protected final int scrollSensitivity;
    protected final float defaultScale;
    public ProgressBarComponent(String componentName,
                                Identifier filledTexture,
                                Identifier emptyTexture,
                                ProgressDirection direction,
                                int baseWidth,
                                int baseHeight,
                                int minSize,
                                int maxSize,
                                int scrollSensitivity,
                                float defaultScale) {
        super(componentName);
        this.filledTexture = filledTexture;
        this.emptyTexture = emptyTexture;
        this.direction = direction;
        this.client = MinecraftClient.getInstance();

        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.scrollSensitivity = scrollSensitivity;
        this.defaultScale = defaultScale;

        loadComponentData();
    }

    public void handleScroll(double delta) {
        if (direction == ProgressDirection.HORIZONTAL) {
            width = clampSize(width + (int) (delta * scrollSensitivity));
            height = (int) (width * ((float) baseHeight / baseWidth));
        } else {
            height = clampSize(height + (int) (delta * scrollSensitivity));
            width = (int) (height * ((float) baseWidth / baseHeight));
        }
        saveComponentData();
    }

    private int clampSize(int size) {
        return Math.max(minSize, Math.min(maxSize, size));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        updateDimensions();
        loadPosition(DungeonData.getInstance());

        // Render empty background
        renderTexture(context, emptyTexture);

        // Calculate filled portion
        double percentage = getCurrentValue() / getMaxValue();
        if (percentage > 1) percentage = 1;
        if (percentage < 0) percentage = 0;

        // Render filled portion with scissor
        if (percentage > 0) {
            if (direction == ProgressDirection.HORIZONTAL) {
                int filledWidth = (int) (width * percentage);
                context.enableScissor(x, y, x + filledWidth, y + height);
                renderTexture(context, filledTexture);
                context.disableScissor();
            } else {
                int filledHeight = (int) (height * percentage);
                int startY = y + (height - filledHeight);
                context.enableScissor(x, startY, x + width, y + height);
                renderTexture(context, filledTexture);
                context.disableScissor();
            }

        }
    }

    protected void renderTexture(DrawContext context, Identifier texture) {
        context.drawTexture(texture, x, y, 0, 0, width, height, width, height);
    }

    protected void updateDimensions() {
        if (direction == ProgressDirection.HORIZONTAL) {
            height = (int) (width * ((float) baseHeight / baseWidth));
        } else {
            width = (int) (height * ((float) baseWidth / baseHeight));
        }
    }

    protected void saveComponentData() {
        DungeonData data = DungeonData.getInstance();
        data.setInt(configPrefix + "Width", width);
        data.setInt(configPrefix + "Height", height);
    }

    protected void loadComponentData() {
        DungeonData data = DungeonData.getInstance();
        if (direction == ProgressDirection.HORIZONTAL) {
            width = data.getInt(configPrefix + "Width", (int) (baseWidth * defaultScale));
            height = (int) (width * ((float) baseHeight / baseWidth));
        } else {
            height = data.getInt(configPrefix + "Height", (int) (baseHeight * defaultScale));
            width = (int) (height * ((float) baseWidth / baseHeight));
        }
    }

    @Override
    protected void loadPosition(DungeonData data) {
        if (client != null && client.currentScreen != null) {
            x = data.getInt(configPrefix + "X", getDefaultX());
            y = data.getInt(configPrefix + "Y", getDefaultY());
            loadComponentData();
        }
    }

    protected abstract double getCurrentValue();

    protected abstract double getMaxValue();

    protected abstract int getDefaultX();

    protected abstract int getDefaultY();

    public enum ProgressDirection {
        HORIZONTAL,
        VERTICAL
    }
}