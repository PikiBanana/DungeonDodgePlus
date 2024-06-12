package io.github.pikibanana.dungeonapi;

import com.mojang.authlib.GameProfile;
import io.github.pikibanana.Main;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonTracker {

    private static final Pattern dungeonEntryRegex = Pattern.compile("You have entered the (\\w*) dungeon!");
    private static final Pattern dungeonDifficultyRegex = Pattern.compile("[!] Notice! Your dungeon difficulty is set to (\\w*)");
    private static final Logger LOGGER = Main.LOGGER;
    private static final Map<Pattern, DungeonMessage> MESSAGE_MAP = new HashMap<>();
    private static boolean isInDungeon = false;
    private static DungeonType dungeonType = DungeonType.UNKNOWN;
    private static DungeonDifficulty dungeonDifficulty = DungeonDifficulty.UNKNOWN;

    static {
        MESSAGE_MAP.put(Pattern.compile("You have entered the ([\\w\\s]+) dungeon.*"), DungeonMessage.ENTER);
        MESSAGE_MAP.put(Pattern.compile("Dungeon failed! The whole team died!"), DungeonMessage.DEATH);
        MESSAGE_MAP.put(Pattern.compile("Teleported you to spawn!"), DungeonMessage.LEAVE);
        MESSAGE_MAP.put(Pattern.compile("The boss has been defeated! The dungeon will end in 10 seconds!"), DungeonMessage.LEAVE);
        MESSAGE_MAP.put(Pattern.compile("Teleporting..."), DungeonMessage.TELEPORTING);
    }

    public static boolean inDungeon() {
        return isInDungeon;
    }

    public static DungeonType getDungeonType() {
        return dungeonType;
    }

    public static void handleEntry(Text message) {
        Matcher typeMatcher = dungeonEntryRegex.matcher(message.getContent().toString());
        if (typeMatcher.find()) {
            isInDungeon = true;
            dungeonType = DungeonType.valueOf(typeMatcher.group(1).toUpperCase());
        }
        Matcher difficultyMatcher = dungeonDifficultyRegex.matcher(message.getContent().toString());
        if (difficultyMatcher.find()) {
            dungeonDifficulty = DungeonDifficulty.valueOf(difficultyMatcher.group(1).toUpperCase());
        }
    }

    public static void handleDeath(Text message) {
        isInDungeon = false;
    }

    public static void handleLeave(Text message) {
        isInDungeon = false;
    }

    public void handleMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {

        // Iterate over the patterns in the MESSAGE_MAP and check if the message content matches any of them
        for (Map.Entry<Pattern, DungeonMessage> entry : MESSAGE_MAP.entrySet()) {
            if (entry.getKey().matcher(message.getString()).find()) {
                DungeonMessage msgType = entry.getValue();

                // Handle the message based on its type
                switch (msgType) {
                    case ENTER:
                        handleEntry(message);
                        break;
                    case DEATH:
                        handleDeath(message);
                        break;
                    case LEAVE:
                    case TELEPORTING:
                        handleLeave(message);
                        break;
                    default:
                        break;
                }
                return; // Exit the loop after handling the message
            }
        }
    }


    private enum DungeonMessage {
        ENTER, DEATH, LEAVE, TELEPORTING
    }
}
