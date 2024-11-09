package io.github.pikibanana.mixin;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import io.github.pikibanana.dungeonapi.DungeonUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    public void setGlow(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        DungeonUtils dungeonUtils = new DungeonUtils();
        if (DungeonTracker.inDungeon()) {
            if (entity instanceof PlayerEntity player) {
                if (dungeonUtils.isParticipating(player.getName().getString()) && DungeonDodgePlusConfig.get().features.teammateHighlighter.enabled) {
                    Team team = entity.getScoreboardTeam();
                    if (team != null) {
                        team.setColor(Main.features.teammateHighlighter.color.color);
                    }
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @Inject(method = "hasStatusEffect", at = @At("HEAD"), cancellable = true)
    public void hasStatusEffects(RegistryEntry<StatusEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        DungeonDodgePlusConfig.Features.Visual visual = Main.features.visual;
        if (effect.getKey().isPresent()) {
            if (effect.getKey().get().getValue() == StatusEffects.DARKNESS.getKey().get().getValue() && visual.disableDarkness) {
                cir.setReturnValue(false);
            }
            if (effect.getKey().get().getValue() == StatusEffects.BLINDNESS.getKey().get().getValue() && visual.disableBlindness) {
                cir.setReturnValue(false);
            }
            if (effect.getKey().get().getValue() == StatusEffects.INVISIBILITY.getKey().get().getValue() && visual.disableInvisibility) {
                cir.setReturnValue(false);
            }
            if (effect.getKey().get().getValue() == StatusEffects.NAUSEA.getKey().get().getValue() && visual.disableNausea) {
                cir.setReturnValue(false);
            }
        }
    }


    @Inject(method = "getArmor", at = @At("HEAD"), cancellable = true)
    public void getArmor(CallbackInfoReturnable<Integer> cir) {
        if (Main.features.visual.disableArmorBar) cir.setReturnValue(0);
    }


}
