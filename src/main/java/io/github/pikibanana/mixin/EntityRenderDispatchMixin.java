package io.github.pikibanana.mixin;

import io.github.pikibanana.data.DungeonData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatchMixin {

    @Inject(method = "render*", at = @At("HEAD"), cancellable = true)
    public void hidePlayers(Entity entity,  double x, double y, double z, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof AbstractClientPlayerEntity player) {
            if (DungeonData.getInstance().getBoolean("hidePlayers", false) && !player.equals(MinecraftClient.getInstance().player)) {
                ci.cancel();
            }
        }
    }
}

