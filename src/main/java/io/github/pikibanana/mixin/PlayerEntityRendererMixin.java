package io.github.pikibanana.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import io.github.pikibanana.dungeonapi.DungeonUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

    @ModifyVariable(
            method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private AbstractClientPlayerEntity dungeondodgeplus$renderPlayer_holdPlayerRef(
            AbstractClientPlayerEntity player, @Share("dungeondodgeplus$playerRef") LocalRef<AbstractClientPlayerEntity> ref
    ) {
        ref.set(player);
        return player;
    }

    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    private void onRender(PlayerEntityRenderState playerEntityRenderState, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
                          int i, CallbackInfo ci, @Share("dungeondodgeplus$playerRef") LocalRef<AbstractClientPlayerEntity> ref) {
        MinecraftClient client = MinecraftClient.getInstance();
        AbstractClientPlayerEntity target = ref.get();

        if (!validateRenderConditions(target, client)) return;

        renderHealthDisplay(target, playerEntityRenderState, client, matrixStack, vertexConsumers);
    }

    @Unique
    private boolean validateRenderConditions(AbstractClientPlayerEntity player, MinecraftClient client) {
        return player != null && client.player != null &&
                DungeonTracker.inDungeon() &&
                DUNGEON_UTILS.isParticipating(player.getName().getString()) &&
                DungeonDodgePlusConfig.get().features.teammateHighlighter.teammateHealthDisplay.enabled &&
                !client.player.getName().equals(player.getName());
    }

    @Unique
    private void renderHealthDisplay(AbstractClientPlayerEntity target, PlayerEntityRenderState playerEntityRenderState,
                                     MinecraftClient client, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers) {
        TextRenderer textRenderer = client.textRenderer;
        String healthText = getHealthText(target);
        int color = DungeonDodgePlusConfig.get().features.teammateHighlighter.teammateHealthDisplay.color;

        matrixStack.push();
        try {
            // Position above player's head
            matrixStack.translate(0, playerEntityRenderState.height + VERTICAL_OFFSET, 0);

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