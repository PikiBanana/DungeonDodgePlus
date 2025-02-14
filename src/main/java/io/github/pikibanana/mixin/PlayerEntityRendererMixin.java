package io.github.pikibanana.mixin;

import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import io.github.pikibanana.dungeonapi.DungeonUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Unique
    private static final DungeonUtils DUNGEON_UTILS = new DungeonUtils();
    @Unique
    private static final float TEXT_SCALE = 0.025f;
    @Unique
    private static final float VERTICAL_OFFSET = 2.15f;
    @Unique
    private static final int BACKGROUND_COLOR = 0x40000000; // Semi-transparent black

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    private void onRender(AbstractClientPlayerEntity player, float entityYaw, float partialTicks,
                          MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light,
                          CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!validateRenderConditions(player, client)) return;

        renderHealthDisplay(player, client, matrixStack, vertexConsumers);
    }

    @Unique
    private boolean validateRenderConditions(AbstractClientPlayerEntity player, MinecraftClient client) {
        return player != null &&
                DungeonTracker.inDungeon() &&
                DUNGEON_UTILS.isParticipating(player.getName().getString()) &&
                DungeonDodgePlusConfig.get().features.teammateHighlighter.teammateHealthDisplay.enabled &&
                client.player != player;
    }

    @Unique
    private void renderHealthDisplay(AbstractClientPlayerEntity player, MinecraftClient client,
                                     MatrixStack matrixStack, VertexConsumerProvider vertexConsumers) {
        TextRenderer textRenderer = client.textRenderer;
        String healthText = getHealthText(player);
        int color = DungeonDodgePlusConfig.get().features.teammateHighlighter.teammateHealthDisplay.color;

        matrixStack.push();
        try {
            // Position above player's head
            matrixStack.translate(0, player.getHeight() + VERTICAL_OFFSET, 0);

            // Billboard effect - always face camera
            matrixStack.multiply(client.getEntityRenderDispatcher().getRotation());
            matrixStack.scale(-TEXT_SCALE, -TEXT_SCALE, TEXT_SCALE);

            // Center text
            float textWidth = textRenderer.getWidth(healthText);
            matrixStack.translate(-textWidth / 2, 0, 0);

            // Actual text rendering
            textRenderer.draw(healthText, 0, 0, color, false,
                    matrixStack.peek().getPositionMatrix(), vertexConsumers,
                    TextRenderer.TextLayerType.NORMAL, BACKGROUND_COLOR, 0xF000F0);
        } finally {
            matrixStack.pop();
        }
    }

    @Unique
    private String getHealthText(PlayerEntity player) {
        float healthPercent = (player.getHealth() / player.getMaxHealth()) * 100;
        return String.format("%.0f%%", healthPercent);
    }
}