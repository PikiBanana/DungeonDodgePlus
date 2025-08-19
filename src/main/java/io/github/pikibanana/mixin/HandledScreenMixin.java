package io.github.pikibanana.mixin;

import io.github.pikibanana.Main;
import io.github.pikibanana.dungeonapi.DungeonUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> {

    @Final
    @Shadow protected T handler;

    @Inject(method = "drawSlotHighlightBack", at = @At("HEAD"), cancellable = true)
    private void onRenderItemSlot(DrawContext context, CallbackInfo ci) {
        if (!Main.features.showItemRarityBackgrounds.enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.currentScreen instanceof CreativeInventoryScreen) return;

        List<Slot> slots = handler.slots;

        for (Slot slot : slots) {
            if (!slot.isEnabled()) continue;
            ItemStack stack = slot.getStack();

            if (DungeonUtils.drawItemRaritySlotOverlay(context, slot.x, slot.y, stack, false)) ci.cancel();
        }
    }

}