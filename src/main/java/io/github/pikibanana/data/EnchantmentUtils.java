package io.github.pikibanana.data;

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
            "III", List.of("knockback", "depth strider"),
            "IV", List.of("thunderlord", "fire aspect", "flaming", "flame"),
            "V", List.of("vampirism", "combo", "jungle protection", "desert protection", "sea creature protection", "vicious"),
            "VI", List.of("sea strike", "lifesteal", "freezing", "last life", "critical", "blessed strike", "last stand", "prosperity", "turtle overlord", "lure",
                    "barbed hook", "luck of the sea", "charm", "sharp hook", "blessed hook"),
            "VII", List.of("sharpness", "smite", "bane of arthropods", "nether slayer", "looting", "sparking", "power", "protection", "projectile protection",
                    "undead protection", "nether protection", "fortune"),
            "X", List.of("boss slayer", "mana saver", "infinite quiver", "piercing", "agility", "wisdom", "efficiency")
    );

}
