package io.github.pikibanana.gui.widgets;

import io.github.pikibanana.Main;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class TexturedMenuWidgets {

    public final Identifier DISCORD_ENABLED = Identifier.of(Main.MOD_ID, "widgets/discord_enabled");
    public final Identifier DISCORD_DISABLED = Identifier.of(Main.MOD_ID, "widgets/discord_disabled");
    public final Identifier MODRINTH_ENABLED = Identifier.of(Main.MOD_ID, "widgets/modrinth_enabled");
    public final Identifier MODRINTH_DISABLED = Identifier.of(Main.MOD_ID, "widgets/modrinth_disabled");
    public final Identifier GITHUB_ENABLED = Identifier.of(Main.MOD_ID, "widgets/github_enabled");
    public final Identifier GITHUB_DISABLED = Identifier.of(Main.MOD_ID, "widgets/github_disabled");
    public final Identifier KOFI_ENABLED = Identifier.of(Main.MOD_ID, "widgets/ko-fi_enabled");
    public final Identifier KOFI_DISABLED = Identifier.of(Main.MOD_ID, "widgets/ko-fi_disabled");

    public static ButtonWidget.PressAction openDiscord() {
        return (button) -> Util.getOperatingSystem().open("https://discord.com/invite/WcpuRfTfEZ");
    }

    public static ButtonWidget.PressAction openModrinth() {
        return (button) -> {
            Util.getOperatingSystem().open("https://modrinth.com/mod/dungeondodge+");
        };
    }

    public static ButtonWidget.PressAction openGithub() {
        return (button) -> Util.getOperatingSystem().open("https://github.com/PikiBanana/DungeonDodgePlus");
    }

    public static ButtonWidget.PressAction openKofi() {
        return (button) -> Util.getOperatingSystem().open("https://ko-fi.com/pikibanana");
    }

    public TexturedButtonWidget getDiscordButton(int x, int y) {
        return new TexturedButtonWidget(
                x, y,
                24, 24,
                new ButtonTextures(DISCORD_DISABLED, DISCORD_DISABLED, DISCORD_ENABLED, DISCORD_ENABLED),
                openDiscord(),
                Text.of("Discord")
        );
    }

    public TexturedButtonWidget getModrinthButton(int x, int y) {
        return new TexturedButtonWidget(
                x, y,
                24, 24,
                new ButtonTextures(MODRINTH_DISABLED, MODRINTH_DISABLED, MODRINTH_ENABLED, MODRINTH_ENABLED),
                openModrinth(),
                Text.of("Modrinth"));
    }

    public TexturedButtonWidget getGithubButton(int x, int y) {
        return new TexturedButtonWidget(
                x, y,
                24, 24,
                new ButtonTextures(GITHUB_DISABLED, GITHUB_DISABLED, GITHUB_ENABLED, GITHUB_ENABLED),
                openGithub(),
                Text.of("GitHub")
        );
    }

    public TexturedButtonWidget getKofiButton(int x, int y) {
        return new TexturedButtonWidget(
                x, y,
                24, 24,
                new ButtonTextures(KOFI_DISABLED, KOFI_DISABLED, KOFI_ENABLED, KOFI_ENABLED),
                openKofi(),
                Text.of("Ko-fi")
        );
    }
}
