package io.github.pikibanana.mixin;

import io.github.pikibanana.Main;
import io.github.pikibanana.misc.Color;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void renderEntities(LivingEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int i, CallbackInfo ci) {
        if (vertexConsumers instanceof OutlineVertexConsumerProvider outlineVertexConsumers) {
            Color enemyColor = Main.features.enemyHighlighter.color;
            if (state.entityType.getBaseClass().isInstance(MobEntity.class) && Main.features.enemyHighlighter.enabled) {
                outlineVertexConsumers.setColor(enemyColor.red, enemyColor.green, enemyColor.blue, 255);
            }

        }
        if ((Main.features.visual.disableInvisibility && state.invisible && (state.entityType == EntityType.PLAYER)) || (state.entityType.getBaseClass().isInstance(MobEntity.class))) {
            state.invisible = false;
        }
    }
}
