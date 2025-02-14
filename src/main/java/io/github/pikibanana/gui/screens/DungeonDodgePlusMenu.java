package io.github.pikibanana.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.pikibanana.Main;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.gui.widgets.TexturedButtonWidget;
import io.github.pikibanana.gui.widgets.TexturedMenuWidgets;
import io.github.pikibanana.hud.ModifyScreen;
import io.github.pikibanana.util.UpdateChecker;
import io.github.pikibanana.util.GUIUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DungeonDodgePlusMenu extends BaseReturnableScreen {
    private static final Identifier NOTIFICATION_ICON = Identifier.of(Main.MOD_ID, "textures/gui/notification.png");
    private static final Identifier TITLE_IMAGE = Identifier.of(Main.MOD_ID, "textures/gui/title.png");
    private static final int ICON_SIZE = 16;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;

    private final UpdateChecker updateChecker = new UpdateChecker();
    private final boolean isUpdateAvailable = updateChecker.isNewVersionAvailable(UpdateChecker.latestVersionNumber);

    private ButtonWidget updateButton;

    public DungeonDodgePlusMenu() {
        super(Text.of("DungeonDodge+ Menu"));
    }

    public static ButtonWidget getDungeonDodgePlusButton(int width, int height) {
        final int BUTTON_WIDTH = 120;
        final int BUTTON_HEIGHT = 20;

        int updateButtonX = width - BUTTON_WIDTH - 10;
        int updateButtonY = height - BUTTON_HEIGHT - 10;

        return ButtonWidget.builder(Text.of("DungeonDodge+"), button -> ScreenManager.pushScreen(new DungeonDodgePlusMenu()))
                .tooltip(Tooltip.of(Text.of("Opens the DungeonDodge+ Menu!")))
                .dimensions(updateButtonX, updateButtonY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
    }

    public static void renderNotificationIcon(DrawContext context, ButtonWidget button) {
        int iconX = button.getX() + button.getWidth() - ICON_SIZE + 5;
        int iconY = button.getY() - ICON_SIZE + 12;

        MinecraftClient.getInstance().getTextureManager().bindTexture(NOTIFICATION_ICON);
        RenderSystem.enableBlend();
        RenderSystem.depthFunc(519);
        context.drawTexture(NOTIFICATION_ICON, iconX, iconY, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        RenderSystem.depthFunc(515);
        RenderSystem.disableBlend();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        updateButton.render(context, mouseX, mouseY, delta);
        if (isUpdateAvailable) {
            renderNotificationIcon(context, updateButton);
        }

        int titleWidth = 400;
        int titleHeight = 100;
        int titleX = (width - titleWidth) / 2;
        int titleY = 80;

        MinecraftClient.getInstance().getTextureManager().bindTexture(TITLE_IMAGE);
        RenderSystem.enableBlend();
        context.drawTexture(TITLE_IMAGE, titleX, titleY, 0, 0, titleWidth, titleHeight, titleWidth, titleHeight);
        RenderSystem.disableBlend();
    }


    @Override
    protected void init() {
        super.init();

        int centerX = width / 2;
        int centerY = height / 2;

        int widgetSpacing = 5;
        int widgetYLevel = centerY + 100;

        int totalWidth = (4 * 24) + (3 * widgetSpacing);

        int startX = centerX - totalWidth / 2;

        TexturedMenuWidgets texturedMenuWidgets = new TexturedMenuWidgets();
        TexturedButtonWidget discordButton = texturedMenuWidgets.getDiscordButton(startX, widgetYLevel);
        TexturedButtonWidget modrinthButton = texturedMenuWidgets.getModrinthButton(startX + 24 + widgetSpacing, widgetYLevel);
        TexturedButtonWidget githubButton = texturedMenuWidgets.getGithubButton(startX + 2 * (24 + widgetSpacing), widgetYLevel);
        TexturedButtonWidget kofiButton = texturedMenuWidgets.getKofiButton(startX + 3 * (24 + widgetSpacing), widgetYLevel);

        addDrawableChild(discordButton);
        addDrawableChild(modrinthButton);
        addDrawableChild(githubButton);
        addDrawableChild(kofiButton);

        ButtonWidget modifyScreenButton = GUIUtils.createButton("Modify Screen", centerX + 3, centerY, BUTTON_WIDTH, BUTTON_HEIGHT, action -> {
            assert client != null;
            ScreenManager.pushScreen(new ModifyScreen());
        });

        ButtonWidget changelogButton = GUIUtils.createButton("Changelog", centerX - BUTTON_WIDTH - 3, centerY, BUTTON_WIDTH, BUTTON_HEIGHT, action -> {
            assert client != null;
            ScreenManager.pushScreen(new ChangelogScreen());
        });

        updateButton = GUIUtils.createButton("Update", centerX - BUTTON_WIDTH - 3, centerY + BUTTON_HEIGHT + 5, BUTTON_WIDTH, BUTTON_HEIGHT, action -> {
            assert client != null;
            ScreenManager.pushScreen(new UpdateScreen());
        });
        updateButton.active = isUpdateAvailable;

        ButtonWidget configButton = GUIUtils.createButton("Config", centerX + 3, centerY + BUTTON_HEIGHT + 5, BUTTON_WIDTH, BUTTON_HEIGHT, action -> {
            assert client != null;
            ScreenManager.pushScreen(AutoConfig.getConfigScreen(DungeonDodgePlusConfig.class, client.currentScreen).get());
        });

        ButtonWidget backButton = GUIUtils.createButton("Back", centerX - 75, centerY + 55, 150, 20, action -> ScreenManager.popScreen());

        addDrawableChild(backButton);
        addDrawableChild(updateButton);
        addDrawableChild(changelogButton);
        addDrawableChild(modifyScreenButton);
        addDrawableChild(configButton);
    }




}
