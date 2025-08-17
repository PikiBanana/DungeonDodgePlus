package io.github.pikibanana.mixin;

import io.github.pikibanana.Main;
import io.github.pikibanana.dungeonapi.item.RecipeData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.RecipeBookAddS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientNetworkHandlerMixin {

    @Inject(
            method = "onRecipeBookAdd",
            at = @At("HEAD")
    )
    private void onRecipeBookAdd(RecipeBookAddS2CPacket packet, CallbackInfo ci) {
        if (packet == null || packet.entries() == null) return;
        Main.LOGGER.info("Recipe packet received with " + packet.entries().size() + " entries!");
        RecipeData.setRecipes(packet.entries().stream().map(RecipeBookAddS2CPacket.Entry::contents).toList());
    }

}
