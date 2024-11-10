    package io.github.pikibanana.mixin;

    import io.github.pikibanana.gui.screens.ScreenManager;
    import io.github.pikibanana.gui.screens.UpdateScreen;
    import net.minecraft.client.gui.screen.Screen;
    import net.minecraft.text.ClickEvent;
    import net.minecraft.text.Style;
    import org.spongepowered.asm.mixin.Mixin;
    import org.spongepowered.asm.mixin.injection.At;
    import org.spongepowered.asm.mixin.injection.Inject;
    import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

    @Mixin(Screen.class)
    public abstract class ScreenMixin {

        @Inject(method = "handleTextClick", at = @At("HEAD"), cancellable = true)
        public void handleTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
            ClickEvent clickEvent = style.getClickEvent();
            if (clickEvent != null && clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                if (clickEvent.getValue().equals("custom:openUpdateScreen")) {
                    ScreenManager.pushScreen(new UpdateScreen());
                    cir.setReturnValue(false);
                }
            }
        }


    }
