package io.github.pikibanana.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.authlib.properties.Property;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.DungeonTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.*;
import java.util.Base64;
import java.util.Collection;

@Mixin(SkullBlockEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class SkullRenderer {

    @Unique
    private static OutlineVertexConsumerProvider outlineVertexConsumerProvider;

    @Unique
    private static final String textureURL = "http://textures.minecraft.net/texture/280d44ca15e303a1714d8d688bc3d0c4848af48bbe16b38893e64298ddcfe10e";

    @Inject(method = "render(Lnet/minecraft/block/entity/SkullBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("HEAD"))
    private void renderSkullValidation(SkullBlockEntity skullBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
                                       int i, int j, CallbackInfo ci, @Share("dungeondodgeplus$validSkull") LocalBooleanRef validSkull) {
        validSkull.set(isValidSkull(skullBlockEntity) && DungeonTracker.inDungeon());
    }

    @ModifyArgs(
            method = "render(Lnet/minecraft/block/entity/SkullBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;renderSkull(Lnet/minecraft/util/math/Direction;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/block/entity/SkullBlockEntityModel;Lnet/minecraft/client/render/RenderLayer;)V"
            )
    )
    private void modifyRender(Args args, @Share("dungeondodgeplus$validSkull") LocalBooleanRef validSkull) {
        if (outlineVertexConsumerProvider == null) {
            outlineVertexConsumerProvider = new OutlineVertexConsumerProvider(
                    (VertexConsumerProvider.Immediate) args.<VertexConsumerProvider>get(4)
            );
        }

        if (validSkull.get()) {
            Color originalColor = new Color(DungeonDodgePlusConfig.get().features.essenceFinder.color);
            int red = originalColor.getRed();
            int green = originalColor.getGreen();
            int blue = originalColor.getBlue();

            int invertedRed = 255 - red;
            int invertedGreen = 255 - green;
            int invertedBlue = 255 - blue;

            outlineVertexConsumerProvider.setColor(red, green, blue, 128);
            args.set(4, (VertexConsumerProvider) outlineVertexConsumerProvider);
        }

    }




    @Inject(method = "renderSkull",
    at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/render/block/entity/SkullBlockEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V")
    )
    private static void drawOutline(Direction direction, float yaw, float animationProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SkullBlockEntityModel model, RenderLayer renderLayer, CallbackInfo ci) {
        if (outlineVertexConsumerProvider != null) {
            outlineVertexConsumerProvider.draw();
        }
    }

    @Unique
    private boolean isValidSkull(SkullBlockEntity skullBlockEntity) {
        ProfileComponent profile = skullBlockEntity.getOwner();
        if (profile == null) {
            return false;
        }

        Collection<Property> textures = profile.properties().get("textures");
        if (textures.isEmpty()) {
            return false;
        }

        if (!DungeonDodgePlusConfig.get().features.essenceFinder.enabled) {
            return false;
        }

        Property textureProperty = textures.iterator().next();
        String texture = textureProperty.value();
        JsonObject json = JsonParser.parseString(new String(Base64.getDecoder().decode(texture))).getAsJsonObject();
        String url = json.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();

        return textureURL.equals(url);
    }
}

