package io.github.pikibanana.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.BlessingFinderData;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ChestBlockEntityRenderer.class)
public abstract class ChestRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    @Unique
    private static OutlineVertexConsumerProvider outlineVertexConsumerProvider;

    @Unique
    private static <T extends BlockEntity> boolean isValidChest(T entity) {
        if (DungeonDodgePlusConfig.get().features.blessingFinder.enabled && DungeonTracker.inDungeon()) {
            BlockPos pos = entity.getPos();
            return entity.getCachedState().isOf(Blocks.CHEST) &&
                    BlessingFinderData.isMarked(pos) &&
                    !BlessingFinderData.isClicked(pos);
        } else {
            return false;
        }
    }


    @Inject(
            method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("HEAD")
    )
    private void dungeondodgeplus$isValidChest(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci, @Share("dungeondodgeplus$validChest") LocalBooleanRef validChest) {
        validChest.set(isValidChest(entity));
        if (!BlessingFinderData.isClicked(entity.getPos())) {
            BlessingFinderData.mark(entity.getPos());
        }
        else {
            BlessingFinderData.remove(entity.getPos());
        }

    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("HEAD"),
            index = 4,
            argsOnly = true
    )
    private VertexConsumerProvider dungeondodgeplus$modifyVertexConsumer(VertexConsumerProvider value, @Share("dungeondodgeplus$validChest") LocalBooleanRef validChest) {
        if (outlineVertexConsumerProvider == null) {
            outlineVertexConsumerProvider = new OutlineVertexConsumerProvider((VertexConsumerProvider.Immediate) value);
        }

        if (validChest.get()) {
            Color color = new Color(DungeonDodgePlusConfig.get().features.blessingFinder.color);
            outlineVertexConsumerProvider.setColor(color.getRed(), color.getGreen(), color.getBlue(), 128);
            return outlineVertexConsumerProvider;
        }
        return value;
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(value = "STORE"),
            index = 17
    )
    private int dungeondodgeplus$modifyChestRenderLight(int light, @Share("dungeondodgeplus$validChest") LocalBooleanRef validChest) {
        if (validChest.get()) {
            return 15728880;
        }
        return light;
    }

    @Inject(
            method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V",
                    shift = At.Shift.BEFORE
            )
    )
    private void dungeondodgeplus$drawVertexConsumer(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci, @Share("dungeondodgeplus$validChest") LocalBooleanRef validChest) {
        if (validChest.get()) {
            outlineVertexConsumerProvider.draw();
        }
    }
}
