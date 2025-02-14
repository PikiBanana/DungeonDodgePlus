package io.github.pikibanana.dungeonapi;

import io.github.pikibanana.Main;
import io.github.pikibanana.dungeonapi.event.SentMessageEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.regex.Pattern;

/**
 * Class that stores information sent by DungeonDodge about a player, such as the defense, max health, mana, etc.<br>
 * Note that this is a PRE-API implementation.
 * @author BasicallyIAmFox
 */
@Environment(EnvType.CLIENT)
public class PlayerStats {

    //15/15? Health  |  56? Defense  |  56.67/563? Mana  |  35? Speed
    public static final Pattern STAT_OVERLAY_MESSAGE_REGEX = Pattern.compile("(\\d*\\.?\\d*?)/(\\d*\\.?\\d*?). Health.{5}(\\d*\\.?\\d*?). Defense.{5}(\\d*\\.?\\d*?)/(\\d*\\.?\\d*?). Mana.{5}(\\d*\\.?\\d*?). Speed");

    private static float healthValue;
    private static float healthMax;
    private static float defense;
    private static float manaValue;
    private static float manaMax;
    private static float speed;

    public static float getHealthValue() {
        return healthValue;
    }
    public static float getHealthMax() {
        return healthMax;
    }
    public static float getDefense() {
        return defense;
    }
    public static float getManaValue() {
        return manaValue;
    }
    public static float getManaMax() {
        return manaMax;
    }
    public static float getSpeed() {
        return speed;
    }

    public static void init() {
        SentMessageEvents.OVERLAY_ON.register((client, message) -> {
            tryReceive(message);
            return null;
        });
    }

    public static void tryReceive(String literalMessage) {
        try {
            var matches = STAT_OVERLAY_MESSAGE_REGEX.matcher(literalMessage);
            if (matches.find()) {
                healthValue = Float.parseFloat(matches.group(1));
                healthMax = Float.parseFloat(matches.group(2));
                defense = Float.parseFloat(matches.group(3));
                manaValue = Float.parseFloat(matches.group(4));
                manaMax = Float.parseFloat(matches.group(5));
                speed = Float.parseFloat(matches.group(6));
            }
        }
        catch (Exception e) {
            Main.LOGGER.error(String.format("Unable to parse message for stats: %s", literalMessage));
        }
    }

}
