package io.github.pikibanana.gui.widgets;

import io.github.pikibanana.Main;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class TexturedMenuWidgets {
    public static final int DEFAULT_SOCIAL_SIZE = 24;
    public final Identifier DISCORD_ENABLED = Identifier.of(Main.MOD_ID, "widgets/discord_enabled");
    public final Identifier DISCORD_DISABLED = Identifier.of(Main.MOD_ID, "widgets/discord_disabled");
    public final Identifier MODRINTH_ENABLED = Identifier.of(Main.MOD_ID, "widgets/modrinth_enabled");
    public final Identifier MODRINTH_DISABLED = Identifier.of(Main.MOD_ID, "widgets/modrinth_disabled");
    public final Identifier GITHUB_ENABLED = Identifier.of(Main.MOD_ID, "widgets/github_enabled");
    public final Identifier GITHUB_DISABLED = Identifier.of(Main.MOD_ID, "widgets/github_disabled");
    public final Identifier KOFI_ENABLED = Identifier.of(Main.MOD_ID, "widgets/ko-fi_enabled");
    public final Identifier KOFI_DISABLED = Identifier.of(Main.MOD_ID, "widgets/ko-fi_disabled");
    public final Identifier ARROW_LEFT_ENABLED = Identifier.of(Main.MOD_ID, "widgets/arrow_left_enabled");
    public final Identifier ARROW_LEFT_DISABLED = Identifier.of(Main.MOD_ID, "widgets/arrow_left_disabled");
    public final Identifier ARROW_RIGHT_ENABLED = Identifier.of(Main.MOD_ID, "widgets/arrow_right_enabled");
    public final Identifier ARROW_RIGHT_DISABLED = Identifier.of(Main.MOD_ID, "widgets/arrow_right_disabled");
    public final Identifier CROSS_ICON_ENABLED = Identifier.of(Main.MOD_ID, "widgets/cross_enabled");
    public final Identifier CROSS_ICON_DISABLED = Identifier.of(Main.MOD_ID, "widgets/cross_disabled");
    public final Identifier PIN_ICON_ENABLED = Identifier.of(Main.MOD_ID, "widgets/pin_enabled");
    public final Identifier PIN_ICON_DISABLED = Identifier.of(Main.MOD_ID, "widgets/pin_disabled");

    public static ButtonWidget.PressAction openDiscord() {
        return (button) -> Util.getOperatingSystem().open("https://discord.com/invite/WcpuRfTfEZ");
    }

    public static ButtonWidget.PressAction openModrinth() {
        return (button) -> Util.getOperatingSystem().open("https://modrinth.com/mod/dungeondodge+");
    }

    public static ButtonWidget.PressAction openGithub() {
        return (button) -> Util.getOperatingSystem().open("https://github.com/PikiBanana/DungeonDodgePlus");
    }

    public static ButtonWidget.PressAction openKofi() {
        return (button) -> Util.getOperatingSystem().open("https://ko-fi.com/pikibanana");
    }

    public TexturedButtonWidget getDiscordButton(int x, int y) {
        return getDiscordButton(x, y, DEFAULT_SOCIAL_SIZE);
    }

    public TexturedButtonWidget getDiscordButton(int x, int y, int size) {
        return new TexturedButtonWidget(
                x, y,
                size, size,
                new ButtonTextures(DISCORD_DISABLED, DISCORD_DISABLED, DISCORD_ENABLED, DISCORD_ENABLED),
                openDiscord(),
                Text.of("Discord")
        );
    }

    public TexturedButtonWidget getModrinthButton(int x, int y) {
        return getModrinthButton(x, y, DEFAULT_SOCIAL_SIZE);
    }

    public TexturedButtonWidget getModrinthButton(int x, int y, int size) {
        return new TexturedButtonWidget(
                x, y,
                size, size,
                new ButtonTextures(MODRINTH_DISABLED, MODRINTH_DISABLED, MODRINTH_ENABLED, MODRINTH_ENABLED),
                openModrinth(),
                Text.of("Modrinth"));
    }

    public TexturedButtonWidget getGithubButton(int x, int y) {
        return getGithubButton(x, y, DEFAULT_SOCIAL_SIZE);
    }

    public TexturedButtonWidget getGithubButton(int x, int y, int size) {
        return new TexturedButtonWidget(
                x, y,
                size, size,
                new ButtonTextures(GITHUB_DISABLED, GITHUB_DISABLED, GITHUB_ENABLED, GITHUB_ENABLED),
                openGithub(),
                Text.of("GitHub")
        );
    }

    public TexturedButtonWidget getKofiButton(int x, int y) {
        return getKofiButton(x, y, DEFAULT_SOCIAL_SIZE);
    }

    public TexturedButtonWidget getKofiButton(int x, int y, int size) {
        return new TexturedButtonWidget(
                x, y,
                size, size,
                new ButtonTextures(KOFI_DISABLED, KOFI_DISABLED, KOFI_ENABLED, KOFI_ENABLED),
                openKofi(),
                Text.of("Ko-fi")
        );
    }

    public TexturedButtonWidget getLeftArrowButton(int x, int y, int size, Runnable action, String text) {
        return new TexturedButtonWidget(
                x, y,
                size, size,
                new ButtonTextures(ARROW_LEFT_DISABLED, ARROW_LEFT_DISABLED, ARROW_LEFT_ENABLED, ARROW_LEFT_ENABLED),
                (btn) -> action.run(),
                Text.of(text)
        );
    }

    public TexturedButtonWidget getRightArrowButton(int x, int y, int size, Runnable action, String text) {
        return new TexturedButtonWidget(
                x, y,
                size, size,
                new ButtonTextures(ARROW_RIGHT_DISABLED, ARROW_RIGHT_DISABLED, ARROW_RIGHT_ENABLED, ARROW_RIGHT_ENABLED),
                (btn) -> action.run(),
                Text.of(text)
        );
    }

    public TexturedButtonWidget getCrossButton(int x, int y, int size, Runnable action, String text) {
        return new TexturedButtonWidget(
                x, y,
                size, size,
                new ButtonTextures(CROSS_ICON_DISABLED, CROSS_ICON_DISABLED, CROSS_ICON_ENABLED, CROSS_ICON_ENABLED),
                (btn) -> action.run(),
                Text.of(text)
        );
    }

    public TexturedButtonWidget getPinButton(int x, int y, int size, Runnable action, String text) {
        return new TexturedButtonWidget(
                x, y,
                size, size,
                getButtonTextures(PIN_ICON_DISABLED, PIN_ICON_ENABLED),
                (btn) -> action.run(),
                Text.of(text)
        );
    }

    public ButtonTextures getButtonTextures(Identifier disabled, Identifier enabled) {
        return new ButtonTextures(disabled, disabled, enabled, enabled);
    }
}