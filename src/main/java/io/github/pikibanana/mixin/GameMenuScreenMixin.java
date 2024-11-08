package io.github.pikibanana.mixin;

import io.github.pikibanana.gui.screens.DungeonDodgePlusMenu;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        assert client != null;
        addDrawableChild(DungeonDodgePlusMenu.getDungeonDodgePlusButton(width, height));
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        super.render(context, mouseX, mouseY, delta);
    }
}
