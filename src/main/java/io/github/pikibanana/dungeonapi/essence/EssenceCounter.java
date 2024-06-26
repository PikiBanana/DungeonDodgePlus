package io.github.pikibanana.dungeonapi.essence;

import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class EssenceCounter {

    public static final Identifier ESSENCE_TEXTURE = Identifier.of("dungeondodgeplus", "textures/gui/essence.png");
    private int essence = 0;
    private final DungeonData dungeonData = DungeonData.getInstance();
    private String displayText = "Essence: " + essence + "\nTotal Essence: " + dungeonData.getTotalEssence();

    private static final EssenceCounter INSTANCE = new EssenceCounter();

    private EssenceCounter() {
    }

    public static EssenceCounter getInstance() {
        return INSTANCE;
    }

    public void addEssence(int amount) {
        this.essence += amount;
        updateDisplayText();
    }

    public void setEssence(int amount) {
        this.essence = amount;
        updateDisplayText();
    }
    private void updateDisplayText() {
        this.displayText = "Essence: " + essence + "\nTotal Essence: " + dungeonData.getTotalEssence();
    }

    public int getEssence() {
        return essence;
    }

    public void render(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && DungeonDodgePlusConfig.get().features.essenceCounter.enabled) {
            int textureX = 10;
            int textureY = 10;
            int textureWidth = 50;
            int textureHeight = 50;

            client.getTextureManager().bindTexture(ESSENCE_TEXTURE);
            drawContext.drawTexture(ESSENCE_TEXTURE, textureX, textureY, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

            int textX = textureX + textureWidth + 5;

            String[] lines = displayText.split("\n");
            int lineCount = lines.length;
            int totalTextHeight = lineCount * client.textRenderer.fontHeight + (lineCount - 1) * 2;

            int textY = textureY + (textureHeight - totalTextHeight) / 2;

            for (String line : lines) {
                drawContext.drawText(client.inGameHud.getTextRenderer(), line, textX, textY, 0xFFFFFF, false);
                textY += client.textRenderer.fontHeight + 2;
            }
        }
    }
}
