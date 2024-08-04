package io.github.pikibanana.mixin;

import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeEffects.class)
public class BiomeMixin {

    DungeonDodgePlusConfig config = DungeonDodgePlusConfig.get();

    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    private void getWaterColor(CallbackInfoReturnable<Integer> cir) {
        if (config.features.dungeonWaterColors.enabled) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world != null && client.player != null) {
                BlockPos pos = client.player.getBlockPos();
                RegistryEntry<Biome> biomeEntry = client.world.getBiome(pos);
                Biome biome = biomeEntry.value();

                int color = getColorForBiome(biomeEntry);
                if (color != -1) {
                    cir.setReturnValue(color);
                }
            }
        }
    }

    @Inject(method = "getWaterFogColor", at = @At("HEAD"), cancellable = true)
    private void getWaterFogColor(CallbackInfoReturnable<Integer> cir) {
        if (config.features.dungeonWaterColors.enabled) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world != null && client.player != null) {
                BlockPos pos = client.player.getBlockPos();
                RegistryEntry<Biome> biomeEntry = client.world.getBiome(pos);
                Biome biome = biomeEntry.value();

                int color = getColorForBiome(biomeEntry);
                if (color != -1) {
                    cir.setReturnValue(color);
                }
            }
        }
    }

    @Unique
    private int getColorForBiome(RegistryEntry<Biome> biomeEntry) {
        if (biomeEntry.getKey().isEmpty()) return -1;
        return switch (biomeEntry.getKey().get().getValue().toString()) {
            case "minecraft:ice_spikes" -> 0xAECBFE;
            case "minecraft:jungle" -> 0x478800;
            case "minecraft:desert" -> 0x0092a3;
            default -> -1;
        };
    }
}
