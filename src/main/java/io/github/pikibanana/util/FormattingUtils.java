package io.github.pikibanana.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

/**
 * Utility formatting methods for strings.
 *
 * @author BasicallyIAmFox
 */
@Environment(EnvType.CLIENT)
public final class FormattingUtils {
    private static final Pattern FORMATTING_REGEX = Pattern.compile("§[0-9a-fk-or]");

    public static String removeFormatting(String value) {
        return value.replaceAll(FORMATTING_REGEX.pattern(), "");
    }

    /**
     * Sets the subtitle of the in-game HUD with a specified color.
     *
     * @param subtitleText The text to display as the subtitle.
     * @param rgbColor     The color of the subtitle.
     */
    public static void sendSubtitles(String subtitleText, int rgbColor, boolean bold) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.inGameHud != null) {
            client.inGameHud.setTitleTicks(10, 30, 20);
            client.inGameHud.setTitle(Text.of(""));
            Text text = Text.literal(subtitleText).setStyle(Style.EMPTY.withColor(rgbColor).withBold(bold));
            client.inGameHud.setSubtitle(text);
        }
    }
}
