package io.github.pikibanana.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.PlayerStats;
import io.github.pikibanana.dungeonapi.event.SentMessageEvents;
import io.github.pikibanana.hud.StatusBarRenderer;
import io.github.pikibanana.util.FormattingUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.profiler.Profilers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

/**
 * Mixin that handles interactions with the player's HUD.
 *
 * @author BasicallyIAmFox
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    /* Note that these variables do not exist in the latest InGameHud class
    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;
     */

    @Shadow
    protected abstract void renderFood(DrawContext context, PlayerEntity player, int top, int right);

//    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
//    //TODO: ADD TOGGLES FOR EACH OF THESE
//    private void renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
//        ci.cancel();
//    }
//
//    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
//    private void renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
//        ci.cancel();
//    }

    @Inject(
            method = "setOverlayMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void dungeondodgeplus$setOverlayMessage(Text message, boolean tinted, CallbackInfo ci) {
        if (client.player == null)
            return;

        String literalMessage = FormattingUtils.removeFormatting(message.getString());

        var value = SentMessageEvents.OVERLAY_ON.invoker().onSentMessage(client, literalMessage);
        if (value == SentMessageEvents.ReturnState.CANCEL) {
            ci.cancel();
        }
    }



//    @ModifyVariable(
//            method = "setOverlayMessage(Lnet/minecraft/text/Text;Z)V",
//            at = @At("HEAD"),
//            argsOnly = true
//    )
//    private Text injectedSetOverlayMessage(Text original, Text text, boolean animate) {
//        String rawMessage = original.getString();
//
//        if (PlayerStats.STAT_OVERLAY_MESSAGE_REGEX.matcher(rawMessage).matches()) {
//            Text statsText = Text.literal("")
//                    .append(Text.literal(PlayerStats.getDefense() + "🛡 Defense")
//                            .styled(style -> style.withColor(Formatting.GREEN)))
//                    .append(Text.literal(" | ")
//                            .styled(style -> style.withColor(Formatting.GRAY).withBold(true)))
//                    .append(Text.literal(PlayerStats.getSpeed() + "✦ Speed")
//                            .styled(style -> style.withColor(Formatting.WHITE)));
//
//            return Text.empty()
//                    .append(statsText);
//        }
//
//        return original;
//    }

    @ModifyVariable(
            method = "renderStatusBars",
            at = @At("HEAD"),
            index = 1,
            argsOnly = true
    )
    private DrawContext dungeondodgeplus$renderStatusBars_holdContextRef(DrawContext ctx, @Share("dungeondodgeplus$ctx") LocalRef<DrawContext> ref) {
        ref.set(ctx);
        return ctx;
    }

    @ModifyVariable(
            method = "renderStatusBars",
            at = @At("STORE"),
            ordinal = 3
    )
    private int dungeondodgeplus$renderStatusBars_holdWidthRef(int width, @Share("dungeondodgeplus$width") LocalIntRef ref) {
        ref.set(width);
        return width;
    }

    @ModifyVariable(
            method = "renderStatusBars",
            at = @At("STORE"),
            ordinal = 4
    )
    private int dungeondodgeplus$renderStatusBars_holdHeightRef(int height, @Share("dungeondodgeplus$height") LocalIntRef ref) {
        ref.set(height);
        return height;
    }

    @ModifyVariable(
            method = "renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V",
            at = @At("STORE"),
            ordinal = 10
    )
    private int dungeondodgeplus$renderStatusBars(int i,
                                                  @Share("dungeondodgeplus$width") LocalIntRef widthRef,
                                                  @Share("dungeondodgeplus$height") LocalIntRef heightRef,
                                                  @Share("dungeondodgeplus$ctx") LocalRef<DrawContext> ctxRef) {
        //only display if enabled in config
        if (!DungeonDodgePlusConfig.get().features.showManaBar.enabled) return i;

        Profilers.get().swap("dungeondodgeplus$manaBar");
        StatusBarRenderer.renderManaBar(ctxRef.get(), widthRef.get(), heightRef.get());

        return Integer.MAX_VALUE;
    }

    /*
    @Inject(
            method = "renderExperienceBar",
            at = @At("TAIL")
    )
    private void dungeondodgeplus$renderExpOrb(DrawContext context, int x, CallbackInfo ci) {
        if (Features.DISPLAY_EXPERIENCE_ORB.getValue()) {
            Profilers.get().push("dungeondodgeplus$experienceOrb");
            StatusBarRenderer.renderExperienceOrb(context, scaledWidth, scaledHeight);
            Profilers.get().pop();
        }
    }
     */


}
