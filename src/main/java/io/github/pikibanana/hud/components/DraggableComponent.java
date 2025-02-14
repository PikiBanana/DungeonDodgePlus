package io.github.pikibanana.hud.components;

import io.github.pikibanana.data.DungeonData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class DraggableComponent {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected final String configPrefix;
    protected boolean dragging = false;
    protected int dragOffsetX;
    protected int dragOffsetY;

    protected final MinecraftClient client;

    public DraggableComponent(String configPrefix) {
        this.configPrefix = configPrefix;
        this.client = MinecraftClient.getInstance();
        loadPosition(DungeonData.getInstance());
    }

    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
    }

    public void startDragging(double mouseX, double mouseY) {
        dragging = true;
        dragOffsetX = (int) (mouseX - x);
        dragOffsetY = (int) (mouseY - y);
    }

    public void stopDragging() {
        dragging = false;
        savePosition(DungeonData.getInstance());
    }

    public void updatePosition(double mouseX, double mouseY) {
        if (dragging) {
            x = (int) Math.max(0, Math.min(client.getWindow().getScaledWidth() - width, mouseX - dragOffsetX));
            y = (int) Math.max(0, Math.min(client.getWindow().getScaledHeight() - height, mouseY - dragOffsetY));
            savePosition(DungeonData.getInstance());
        }
    }

    protected void loadPosition(DungeonData data) {
        x = data.getInt(configPrefix + "X", 10);
        y = data.getInt(configPrefix + "Y", 10);
    }

    protected void savePosition(DungeonData data) {
        data.setInt(configPrefix + "X", x);
        data.setInt(configPrefix + "Y", y);
    }
}