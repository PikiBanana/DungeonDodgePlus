package io.github.pikibanana.mixin;

import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void outlineEnemies(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (DungeonTracker.inDungeon()) {
            if (entity instanceof MobEntity && DungeonDodgePlusConfig.get().features.enemyHighlighter.enabled) {
                cir.setReturnValue(true);
            }
        }
    }
}
