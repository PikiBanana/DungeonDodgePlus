package io.github.pikibanana.mixin;

import io.github.pikibanana.Main;
import io.github.pikibanana.gui.screens.DungeonDodgePlusMenu;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void dungeondodgeplusButton(CallbackInfo ci) {
        addDrawableChild(DungeonDodgePlusMenu.getDungeonDodgePlusButton(this.width, this.height));
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (Main.features.visual.playDungeonDodgeButtonReplaceSingleplayer) {
            assert client != null;

            for (int i = 0; i < this.children().size(); i++) {
                if (this.children().get(i) instanceof ButtonWidget button) {
                    if (button.getMessage().getString().equalsIgnoreCase("Singleplayer")) {
                        this.remove(button);

                        ServerAddress serverAddress = new ServerAddress("dungeondodge.net", 25565);
                        ServerInfo serverInfo = new ServerInfo("DungeonDodge", serverAddress.getAddress(), ServerInfo.ServerType.OTHER);
                        serverInfo.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.ENABLED);

                        Style style = Style.EMPTY.withColor(0xFFDE75).withBold(true);

                        addDrawableChild(ButtonWidget.builder(Text.literal("Play DungeonDodge").fillStyle(style), action -> {
                                    action.active = false;
                                    ConnectScreen.connect(this, client, serverAddress, serverInfo, false, null);
                                })
                                .dimensions(this.width / 2 - 100, this.height / 4 + 24, 200, 20)
                                .build());
                        break;
                    }
                }
            }
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        super.render(context, mouseX, mouseY, delta);
    }
}
