package io.github.pikibanana.mixin;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import io.github.pikibanana.dungeonapi.DungeonUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

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
}
