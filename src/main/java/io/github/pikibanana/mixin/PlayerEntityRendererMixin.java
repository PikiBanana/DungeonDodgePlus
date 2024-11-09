package io.github.pikibanana.mixin;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import io.github.pikibanana.dungeonapi.DungeonUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    private void renderText(AbstractClientPlayerEntity player, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (player != null && DungeonTracker.inDungeon()) {
            PlayerEntity playerEntity;
            playerEntity = player;

            DungeonUtils dungeonUtils = new DungeonUtils();

            if (dungeonUtils.isParticipating(player.getName().getString()) && DungeonDodgePlusConfig.get().features.teammateHighlighter.teammateHealthDisplay.enabled) {

                String health = Math.round((playerEntity.getHealth() / playerEntity.getMaxHealth()) * 100) + "%";

                float nameTagTextOffsetY = 2.75f;
                float nameTagTextOffsetX = -MinecraftClient.getInstance().textRenderer.getWidth(health) / 2.0f;

                matrixStack.push();

                matrixStack.translate(0.0D, nameTagTextOffsetY, 0.0D);

                matrixStack.scale(0.035f, -0.035f, 0.035f);

                Quaternionf playerRotation = MinecraftClient.getInstance().getEntityRenderDispatcher().getRotation().mul(-1);

                matrixStack.multiply(playerRotation);

                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
                textRenderer.draw(health, nameTagTextOffsetX, 0, DungeonDodgePlusConfig.get().features.teammateHighlighter.teammateHealthDisplay.color, false, matrixStack.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, 0x000000, 15);

                matrixStack.pop();
            }
        }

        if ((Main.features.visual.disableInvisibility)) {
            assert player != null;
            if (player.isInvisible()) {
                player.setInvisible(false);
            }
        }
    }
}
