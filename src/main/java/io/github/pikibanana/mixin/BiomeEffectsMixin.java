package io.github.pikibanana.mixin;

import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import io.github.pikibanana.dungeonapi.DungeonType;
import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeEffects.class)
public class BiomeEffectsMixin {

    @Unique
    DungeonDodgePlusConfig config = DungeonDodgePlusConfig.get();

    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    private void getWaterColor(CallbackInfoReturnable<Integer> cir) {
        if (config.features.dungeonWaterColors.enabled) {
            int color = getColorForDungeon(DungeonTracker.getDungeonType());
            if (color != -1) {
                cir.setReturnValue(color);
            }
        }
    }

    @Inject(method = "getWaterFogColor", at = @At("HEAD"), cancellable = true)
    private void getWaterFogColor(CallbackInfoReturnable<Integer> cir) {
        if (config.features.dungeonWaterColors.enabled) {
            int color = getColorForDungeon(DungeonTracker.getDungeonType());
            if (color != -1) {
                cir.setReturnValue(color);
            }
        }
    }

    @Unique
    private int getColorForDungeon(DungeonType dungeonType) {
        return switch (dungeonType) {
            case ICE -> 0xA4C1FE;
            case JUNGLE -> 0x478800;
            case DESERT -> 0x0092a3;
            default -> -1;
        };
    }
}
