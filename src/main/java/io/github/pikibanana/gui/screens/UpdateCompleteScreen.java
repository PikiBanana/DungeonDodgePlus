package io.github.pikibanana.gui.screens;

import io.github.pikibanana.util.GUIUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class UpdateCompleteScreen extends BaseReturnableScreen {

    public UpdateCompleteScreen() {
        super(Text.of("Update Complete"));
    }

    @Override
    protected void init() {
        ButtonWidget restartButton = GUIUtils.createButton("Restart", this.width / 2 - 75, this.height / 2 + 20, 150, 20, action -> MinecraftClient.getInstance().scheduleStop());
        this.addDrawableChild(restartButton);

        ButtonWidget backButton = GUIUtils.createButton("Back", this.width / 2 - 75, this.height / 2 + 40, 150, 20, action -> ScreenManager.popScreen());
        this.addDrawableChild(backButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int centerX = this.width / 2;
        int centerY = this.height / 2 - 20;

        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,
                Text.literal("Update Complete").formatted(Formatting.BOLD, Formatting.GREEN), centerX, centerY - 20, 0xFFFFFF);
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,
                Text.literal("Please restart the game to apply the update."), centerX, centerY, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
