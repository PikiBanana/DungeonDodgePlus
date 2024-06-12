package io.github.pikibanana.mixin;

import io.github.pikibanana.dungeonapi.BlessingFinderData;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.s2c.play.BlockEventS2CPacket;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEventS2CPacket.class)
public abstract class BlockEventS2CPacketMixin {
    @Shadow @Final
    private int type;

    @Shadow @Final
    private int data;

    @Shadow @Final
    private Block block;

    @Shadow @Final
    private BlockPos pos;

    @Inject(
            method = "apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V",
            at = @At("TAIL")
    )
    private void dungeondodgeplus$apply(ClientPlayPacketListener clientPlayPacketListener, CallbackInfo ci) {
        if (DungeonTracker.inDungeon() && type == 1 && data == 1 && block == Blocks.CHEST) {
            if (BlessingFinderData.isMarked(pos)) {
                BlessingFinderData.remove(pos);
            } else {
                BlessingFinderData.mark(pos);
                if (!BlessingFinderData.isClicked(pos)) {
                    BlessingFinderData.markClicked(pos);
                }
            }
        }
    }
}
