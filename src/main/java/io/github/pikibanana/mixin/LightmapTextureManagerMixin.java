package io.github.pikibanana.mixin;

import io.github.pikibanana.Main;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {

    @Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
    private static void getBrightness(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> cir) {
        if (Main.features.visual.fullBright) cir.setReturnValue(1f);
    }

}
