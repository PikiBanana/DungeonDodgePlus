package io.github.pikibanana.mixin;

import io.github.pikibanana.gui.screens.DungeonDodgePlusMenu;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends Screen {

    protected InventoryScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void dungeondodgeplusButton(CallbackInfo ci) {
        addDrawableChild(DungeonDodgePlusMenu.getDungeonDodgePlusButton(this.width,this.height));
    }

}
