package io.github.pikibanana.mixin;

import io.github.pikibanana.gui.PinRecipe;
import io.github.pikibanana.gui.screens.ScreenManager;
import io.github.pikibanana.gui.screens.UpdateScreen;
import io.github.pikibanana.gui.widgets.RecipeWidget;
import io.github.pikibanana.gui.widgets.TexturedButtonWidget;
import io.github.pikibanana.gui.widgets.TexturedMenuWidgets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Unique
    private static final List<Integer> RECIPE_SLOTS = List.of(11, 12, 13, 20, 21, 22, 29, 30, 31);
    @Unique
    private static final int RESULT_SLOT = 24;
    @Unique
    private static final int PIN_BUTTON_SIZE = 12;
    @Unique
    private static final int PIN_BUTTON_PADDING = 4;
    @Shadow
    public int width;
    @Shadow
    @Final
    protected Text title;
    @Unique
    TexturedButtonWidget pinButton;
    @Unique
    private RecipeWidget recipeWidget;
    @Unique
    private int pinButtonX;
    @Unique
    private int pinButtonY;

    @Shadow
    protected abstract <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement);

    @Shadow
    public abstract Text getTitle();

    @Shadow @Nullable protected MinecraftClient client;

    @Shadow public int height;

    @Shadow public abstract void init(MinecraftClient client, int width, int height);

    @Shadow protected abstract void init();

    @Inject(method = "handleTextClick", at = @At("HEAD"), cancellable = true)
    public void handleTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        ClickEvent clickEvent = style.getClickEvent();
        if (clickEvent instanceof ClickEvent.RunCommand(String command)) {
            if (command.equals("custom:openUpdateScreen")) {
                ScreenManager.pushScreen(new UpdateScreen());
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "init*", at = @At("TAIL"))
    private void initScreen(CallbackInfo ci) {
        if (!((Object) this instanceof HandledScreen<?> screen)) return;

        if (this.recipeWidget == null) {
            this.recipeWidget = new RecipeWidget();
        }
        this.addDrawableChild(recipeWidget);


        if (screen.getScreenHandler() instanceof GenericContainerScreenHandler container && this.getTitle().getString().toLowerCase().contains("recipe browser")) {
            int backgroundWidth = 176;
            int backgroundHeight = 114 + container.getRows() * 18;

            pinButtonX = (width / 2) + (backgroundWidth / 2) - (PIN_BUTTON_SIZE + PIN_BUTTON_PADDING);
            pinButtonY = (height / 2) - (backgroundHeight / 2) + PIN_BUTTON_PADDING;

            TexturedMenuWidgets texturedMenuWidgets = new TexturedMenuWidgets();
            pinButton = texturedMenuWidgets.getPinButton(pinButtonX, pinButtonY, PIN_BUTTON_SIZE, () -> {
                DefaultedList<ItemStack> recipe = getRecipeFromContainer(container);
                PinRecipe.pin(recipe);
                recipeWidget.markDirty();

                MinecraftClient client = MinecraftClient.getInstance();
                client.getSoundManager().play(
                        PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                );
            }, "Pin Recipe");
            this.addDrawableChild(pinButton);
        }
    }

    @Unique
    private DefaultedList<ItemStack> getRecipeFromContainer(GenericContainerScreenHandler container) {
        DefaultedList<ItemStack> recipe = DefaultedList.ofSize(10, ItemStack.EMPTY);

        for (int i = 0; i < RECIPE_SLOTS.size(); i++) {
            int slotIndex = RECIPE_SLOTS.get(i);
            if (slotIndex < container.slots.size()) {
                recipe.set(i, container.getSlot(slotIndex).getStack().copy());
            }
        }

        if (RESULT_SLOT < container.slots.size()) {
            recipe.set(9, container.getSlot(RESULT_SLOT).getStack().copy());
        }

        return recipe;
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (pinButton != null && isHoveringPinButton(mouseX, mouseY)) {
            context.drawTooltip(MinecraftClient.getInstance().textRenderer,
                    pinButton.getMessage(), mouseX, mouseY);
        }
    }


    @Unique
    private boolean isHoveringPinButton(int mouseX, int mouseY) {
        return mouseX >= pinButtonX && mouseX < pinButtonX + PIN_BUTTON_SIZE &&
               mouseY >= pinButtonY && mouseY < pinButtonY + PIN_BUTTON_SIZE;
    }


}