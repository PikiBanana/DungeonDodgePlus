package io.github.pikibanana.dungeonapi.essence;

import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.misc.SheepRandomizer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class EssenceCounter {

    public static final Identifier ESSENCE_TEXTURE = Identifier.of("dungeondodgeplus", "textures/gui/essence.png");
    public static final Identifier SHEEP_TEXTURE = Identifier.of("dungeondodgeplus", "textures/gui/sheep/" + SheepRandomizer.getRandomSheepType() + ".png");
    private static final EssenceCounter INSTANCE = new EssenceCounter();
    private final DungeonData dungeonData = DungeonData.getInstance();
    private String essenceText = DungeonDodgePlusConfig.get().features.essenceCounter.text;
    private String totalEssenceText = DungeonDodgePlusConfig.get().features.essenceCounter.totalText;
    private int essence = 0;
    private boolean showTotalEssence = DungeonDodgePlusConfig.get().features.essenceCounter.showTotal;
    private String displayText = essenceText + ": " + essence + "\nTotal " + totalEssenceText + ": " + dungeonData.getInt("totalEssence");

    private EssenceCounter() {
    }

    public static EssenceCounter getInstance() {
        return INSTANCE;
    }

    public void addEssence(int amount) {
        this.essence += amount;
        updateDisplayText();
    }

    private void updateDisplayText() {
        this.essenceText = DungeonDodgePlusConfig.get().features.essenceCounter.text;
        this.totalEssenceText = DungeonDodgePlusConfig.get().features.essenceCounter.totalText;
        this.showTotalEssence = DungeonDodgePlusConfig.get().features.essenceCounter.showTotal;
        int totalEssenceValue = dungeonData.getInt("totalEssence");
        if (showTotalEssence) {
            this.displayText = essenceText + ": " + essence + "\nTotal " + totalEssenceText + ": " + totalEssenceValue;
        } else {
            this.displayText = essenceText + ": " + essence;
        }
    }


    public String getDisplayText() {
        return displayText;
    }

    public int getEssence() {
        return essence;
    }

    public void setEssence(int amount) {
        this.essence = amount;
        updateDisplayText();
    }

    public void render(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        this.showTotalEssence = DungeonDodgePlusConfig.get().features.essenceCounter.showTotal;
        updateDisplayText();
        if (client.player != null && DungeonDodgePlusConfig.get().features.essenceCounter.enabled) {
            int textureX = dungeonData.getInt("essenceX", 10);
            int textureY = dungeonData.getInt("essenceY", 10);
            int textureWidth = dungeonData.getInt("essenceWidth", 50);
            int textureHeight = dungeonData.getInt("essenceHeight", 50);

            if (dungeonData.getBoolean("sheepMode", false)) {
                client.getTextureManager().bindTexture(SHEEP_TEXTURE);
                int originalSheepWidth = 750;
                int originalSheepHeight = 875;
                float aspectRatio = (float) originalSheepHeight / originalSheepWidth;
                textureHeight = (int) (textureWidth * aspectRatio);
                drawContext.drawTexture(SHEEP_TEXTURE, textureX, textureY, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
            } else {
                client.getTextureManager().bindTexture(ESSENCE_TEXTURE);
                drawContext.drawTexture(ESSENCE_TEXTURE, textureX, textureY, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
            }
            int textX = textureX + textureWidth + 5;
            int width = MinecraftClient.getInstance().getWindow().getScaledWidth();

            if (textX + client.textRenderer.getWidth(displayText) > width) {
                textX = width - client.textRenderer.getWidth(displayText);
            }

            String[] lines = displayText.split("\n");
            int lineCount = lines.length;
            int totalTextHeight = lineCount * client.textRenderer.fontHeight + (lineCount - 1) * 2;

            int textY = textureY + (textureHeight - totalTextHeight) / 2;

            for (String line : lines) {
                drawContext.drawText(client.inGameHud.getTextRenderer(), line, textX, textY,
                        DungeonDodgePlusConfig.get().features.essenceCounter.color, false);
                textY += client.textRenderer.fontHeight + 2;
            }
        }
    }
}
