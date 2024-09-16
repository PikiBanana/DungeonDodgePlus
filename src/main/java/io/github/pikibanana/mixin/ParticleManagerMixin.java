package io.github.pikibanana.mixin;

import io.github.pikibanana.Main;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Queue;

@Mixin(net.minecraft.client.particle.ParticleManager.class)
public abstract class ParticleManagerMixin {

    @Shadow
    @Final
    private Map<ParticleTextureSheet, Queue<Particle>> particles;

    @Inject(method = "renderParticles", at = @At("HEAD"))
    public void onParticleRender(LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
        if (Main.features.hideOtherFishingBobbers.enabled) {
            for (Queue<Particle> queue : particles.values()) {
                queue.removeIf(particle ->
                        particle.getClass() == FishingParticle.class ||
                                particle.getClass() == WaterSplashParticle.class ||
                                particle.getClass() == WaterBubbleParticle.class
                );
            }
        }
    }

}
