package io.github.pikibanana.mixin;

import io.github.pikibanana.Main;
import io.github.pikibanana.util.FormattingUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {

    @Shadow
    @Final
    private static TrackedData<Boolean> CAUGHT_FISH;

    @Shadow
    @Nullable
    public abstract PlayerEntity getPlayerOwner();

    @Inject(method = "onTrackedDataSet", at = @At("HEAD"))
    public void onTrackedDataSet(TrackedData<?> data, CallbackInfo ci) {
        if (CAUGHT_FISH.equals(data) && getPlayerOwner() == MinecraftClient.getInstance().player) {
            FormattingUtils.sendSubtitles(
                    Main.features.fishingAnnouncement.text,
                    Main.features.fishingAnnouncement.announcementColor,
                    Main.features.fishingAnnouncement.bold
            );
        }
    }
}
