package io.github.pikibanana.util;

import java.util.List;
import java.util.Map;

/**
 * DungeonDodge enchantment data.
 * This should be moved to the API eventually.
 */
public class EnchantmentUtils {

    // I need to learn a better way to do this without hard-coding -Wyndev
    /**
     * Array of colors that are used to create the max-level enchantment rainbow text.
     */
    public static final int[] RAINBOW_GRADIENT = new int[]{
            0xFF0000, 0xFF001E, 0xFF002B, 0xFF0044, 0xFF0055, 0xFF0066, 0xFF007B, 0xFF0099,
            0xFF00BB, 0xFF00DD, 0xFF00FF, 0xEA00FF, 0xC800FF, 0xAE00FF, 0x9900FF, 0x8400FF,
            0x6F00FF, 0x5500FF, 0x4000FF, 0x3300FF, 0x1900FF, 0x0000FF, 0x001AFF, 0x003CFF,
            0x005EFF, 0x007BFF, 0x0099FF, 0x00B3FF, 0x00D5FF, 0x00F2FF, 0x00FFEA, 0x00FFCC,
            0x00FFA6, 0x00FF80, 0x00FF5E, 0x00FF2F, 0x00FF11, 0x08FF00, 0x26FF00, 0x44FF00,
            0x6AFF00, 0x8CFF00, 0xA6FF00, 0xBBFF00, 0xD4FF00, 0xEAFF00, 0xFFFB00, 0xFFE600,
            0xFFD000, 0xFFBB00, 0xFFA200, 0xFF8400, 0xFF6F00, 0xFF5E00, 0xFF4000, 0xFF2A00,
            0xFF1E00
    };

    /**
     * Map of all max levels corresponding to a list of enchantments that match that max level.
     * Note that the level keys are Strings corresponding to the roman numeral equivalent of the
     * max level.
     * This is currently hard-coded but should be moved to the API eventually.
     */
    public static final Map<String, List<String>> MAX_LEVEL_MAP = Map.of(
            "I", List.of("chicken slayer", "homing", "tough rod", "fish streak", "treasure streak"),
            "II", List.of("knockback"),
            "III", List.of("depth strider", "thunderlord"),
            "IV", List.of("fire aspect", "flaming", "flame"),
            "V", List.of("vampirism", "combo", "jungle protection", "desert protection", "sea creature protection", "vicious"
                    , "piercing", "freezing", "last life", "bane of arthropods", "nether slayer", "last stand", "nether protection", "projectile protection",
                    "undead protection", "turtle overlord","barbed hook", "luck of the sea", "charm", "sharp hook", "blessed hook","smite","sea strike"),
            "VI", List.of("lifesteal",
                    "critical", "blessed strike", "prosperity", "lure"),
            "VII", List.of("sharpness", "looting", "sparking", "power", "protection",
                    "fortune"),
            "X", List.of("boss slayer", "mana saver", "infinite quiver", "agility", "wisdom", "efficiency")
    );

    /**
     * Generates an array of colors that form a rainbow gradient.
     * The colors are distributed evenly across the HSV spectrum.
     *
     * @param n The number of colors to be generated. A higher number results in a smoother color transition.
     * @return An array of integers where each integer represents a color in RGB format.
     */
    public static int[] generateRainbowGradient(int n) {
        int[] colors = new int[n];
        for (int i = 0; i < n; i++) {
            float hue = (float) i / n * 360;
            colors[i] = hsvToRgb(hue, 1.0f, 1.0f);
        }
        return colors;
    }

    /**
     * Converts HSV (Hue, Saturation, Value) color values to RGB format.
     * The RGB color is returned as an integer where the red, green, and blue
     * components are packed into a single 24-bit value.
     *
     * @param h The hue component, ranging from 0 to 360 degrees. Hue represents the color type.
     * @param s The saturation component, ranging from 0.0 to 1.0. Saturation represents the intensity of the color.
     * @param v The value component, ranging from 0.0 to 1.0. Value represents the brightness of the color.
     * @return An integer representing the RGB color, with red in the highest byte, green in the middle byte,
     * and blue in the lowest byte.
     */
    public static int hsvToRgb(float h, float s, float v) {
        float c = v * s;
        float x = c * (1 - Math.abs((h / 60) % 2 - 1));
        float m = v - c;
        float rPrime = 0, gPrime = 0, bPrime = 0;

        if (0 <= h && h < 60) {
            rPrime = c;
            gPrime = x;
            bPrime = 0;
        } else if (60 <= h && h < 120) {
            rPrime = x;
            gPrime = c;
            bPrime = 0;
        } else if (120 <= h && h < 180) {
            rPrime = 0;
            gPrime = c;
            bPrime = x;
        } else if (180 <= h && h < 240) {
            rPrime = 0;
            gPrime = x;
            bPrime = c;
        } else if (240 <= h && h < 300) {
            rPrime = x;
            gPrime = 0;
            bPrime = c;
        } else if (300 <= h && h < 360) {
            rPrime = c;
            gPrime = 0;
            bPrime = x;
        }

        int r = (int) ((rPrime + m) * 255);
        int g = (int) ((gPrime + m) * 255);
        int b = (int) ((bPrime + m) * 255);

        return (r << 16) | (g << 8) | b;
    }


}
