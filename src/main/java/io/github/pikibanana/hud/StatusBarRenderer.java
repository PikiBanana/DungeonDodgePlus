package io.github.pikibanana.hud;

import io.github.pikibanana.Main;
import io.github.pikibanana.dungeonapi.PlayerStats;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.Identifier;

/**
 * Status bar GUI renderer.
 * @author BasicallyIAmFox
 */
@Environment(value = EnvType.CLIENT)
public final class StatusBarRenderer {
    private static final Identifier MANA_BUBBLE_RESOURCES = Identifier.of(Main.MOD_ID, "textures/gui/mana_bubble_resources.png");

    public static void renderManaBar(DrawContext ctx, int x, int y) {
        float manaRatio = PlayerStats.getManaValue() / PlayerStats.getManaMax() * 10f;
        int manaLast = 7 - ((int) Math.floor((manaRatio - Math.floor(manaRatio)) * 8f) % 8);

        for (int i = 0; i < 10; i++) {
            int posX = x - i * 8 - 9;
            ctx.drawTexture(identifier -> RenderLayer.getGuiTextured(MANA_BUBBLE_RESOURCES), MANA_BUBBLE_RESOURCES, posX, y, 9, 0, 9, 9, 256, 256);

            int offset = 7; // Always empty
            if (manaRatio >= i && manaRatio < (i + 1)) {
                offset = manaLast;
            }
            else if (manaRatio >= (i + 1)) {
                offset = 0;
            }

            ctx.drawTexture(identifier -> RenderLayer.getGuiTextured(MANA_BUBBLE_RESOURCES), MANA_BUBBLE_RESOURCES, posX + offset + 1, y, offset + 1, 0, 7 - offset, 9, 256, 256);
        }
    }

    /*
    public static void renderExperienceOrb(DrawContext ctx, int scaledWidth, int scaledHeight) {
        int x = (scaledWidth - 12) / 2;
        int y = scaledHeight - 48 - 3;

        ctx.drawTexture(RESOURCES, x, y, 18, 0, 12, 12);

        float offset;
        if (ExperienceTracker.getCurrentLevel() != null && ExperienceTracker.getNextLevel() != null) {
            float current = ExperienceTracker.getExperience() - ExperienceTracker.getCurrentLevel().getTotalExperience();
            float required = ExperienceTracker.getNextLevel().getTotalExperience() - ExperienceTracker.getCurrentLevel().getTotalExperience();

            if (ExperienceTracker.getNextLevel() == Level.COUNT) {
                offset = 1;
            }
            else {
                offset = current / required;
            }
        } else {
            offset = 1f;
        }

        float innerHeight = 12 * offset;
        float innerUvY = 12 - innerHeight;
        float innerY = y + innerUvY;

        ctx.drawTexture(RESOURCES, x, (int) innerY, 30, (int) innerUvY, 12, (int) innerHeight);
    }
     */
}
