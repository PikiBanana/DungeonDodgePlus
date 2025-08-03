package io.github.pikibanana.mixin;

import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntityRenderer.class)
public abstract class FishingBobberEntityRendererMixin {

    @Inject(method = "shouldRender(Lnet/minecraft/entity/projectile/FishingBobberEntity;Lnet/minecraft/client/render/Frustum;DDD)Z", at = @At("HEAD"), cancellable = true)
    public void onRender(FishingBobberEntity fishingBobberEntity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> cir) {
        if (fishingBobberEntity.getPlayerOwner() != MinecraftClient.getInstance().player) {
            if (DungeonDodgePlusConfig.get().features.hideOtherFishingBobbers.enabled) {
                cir.setReturnValue(false);
            }
        }
    }

}
