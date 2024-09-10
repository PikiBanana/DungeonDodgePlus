package io.github.pikibanana.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.regex.Pattern;

/**
 * Utility formatting methods for strings.
 * @author BasicallyIAmFox
 */
@Environment(EnvType.CLIENT)
public final class FormattingUtils {
    private static final Pattern FORMATTING_REGEX = Pattern.compile("§[0-9a-fk-or]");

    public static String removeFormatting(String value) {
        return value.replaceAll(FORMATTING_REGEX.pattern(), "");
    }
}
