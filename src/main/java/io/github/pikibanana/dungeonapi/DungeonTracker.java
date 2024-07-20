package io.github.pikibanana.dungeonapi;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.essence.EssenceCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonTracker {

    private static final Pattern dungeonEntryRegex = Pattern.compile("You have entered the ([\\w\\s]+) dungeon.*");
    private static final Pattern dungeonDifficultyRegex = Pattern.compile("\\[!] Notice! Your dungeon difficulty is set to (\\w*)!");
    private static final Logger LOGGER = Main.LOGGER;
    private static final Map<Pattern, DungeonMessage> MESSAGE_MAP = new HashMap<>();
    private static boolean isInDungeon = false;
    private static DungeonType dungeonType = DungeonType.UNKNOWN;
    private static DungeonDifficulty dungeonDifficulty = DungeonDifficulty.UNKNOWN;
    public static final EssenceCounter essenceCounter = EssenceCounter.getInstance();
    private static final DungeonData dungeonData = DungeonData.getInstance();

    static {
        MESSAGE_MAP.put(Pattern.compile("You have entered the ([\\w\\s]+) dungeon.*"), DungeonMessage.ENTER);
        MESSAGE_MAP.put(Pattern.compile("Dungeon failed! The whole team died!"), DungeonMessage.DEATH);
        MESSAGE_MAP.put(Pattern.compile("Teleported you to spawn!"), DungeonMessage.LEAVE);
        MESSAGE_MAP.put(Pattern.compile("The boss has been defeated! The dungeon will end in (\\d+) seconds!"), DungeonMessage.LEAVE);
        MESSAGE_MAP.put(Pattern.compile("Teleporting..."), DungeonMessage.TELEPORTING);
        MESSAGE_MAP.put(dungeonDifficultyRegex, DungeonMessage.DIFFICULTY);
    }

    public static boolean inDungeon() {
        return isInDungeon;
    }

    public static DungeonType getDungeonType() {
        return dungeonType;
    }

    public static void handleEntry(Text text) {
        if (DungeonDodgeConnection.isConnected()) {
            String message = text.getString();
            Matcher typeMatcher = dungeonEntryRegex.matcher(message);
            if (typeMatcher.find()) {
                isInDungeon = true;
                String dungeonName = typeMatcher.group(1);
                try {
                    dungeonType = DungeonType.valueOf(dungeonName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    dungeonType = DungeonType.UNKNOWN;
                }
            }
        }
    }

    public static void handleDifficulty(Text text) {
        if (DungeonDodgeConnection.isConnected()) {
            String message = text.getString();

            Matcher difficultyMatcher = dungeonDifficultyRegex.matcher(message);
            if (difficultyMatcher.find()) {
                String difficultyName = difficultyMatcher.group(1);
                try {
                    dungeonDifficulty = DungeonDifficulty.valueOf(difficultyName.toUpperCase());
                    if (DungeonDodgePlusConfig.get().features.difficultyAnnouncer.enabled) MinecraftClient.getInstance().inGameHud.setTitle(dungeonDifficulty.getAnnouncementText());
                } catch (IllegalArgumentException e) {
                    dungeonDifficulty = DungeonDifficulty.UNKNOWN;
                }
            }
        }
    }

    public static void handleDeath(Text message) {
        isInDungeon = false;
    }

    public static void handleLeave(Text message) {
        isInDungeon = false;
        int essence = essenceCounter.getEssence();
        dungeonData.addInt("totalEssence", essence);
        essenceCounter.setEssence(0);
    }

    public static DungeonDifficulty getDungeonDifficulty() {
        return dungeonDifficulty;
    }

    public void handleMessage(Text text, boolean b) {
        String message = text.getString();

        for (Map.Entry<Pattern, DungeonMessage> entry : MESSAGE_MAP.entrySet()) {
            Matcher matcher = entry.getKey().matcher(message);
            if (matcher.find()) {
                DungeonMessage msgType = entry.getValue();

                switch (msgType) {
                    case ENTER:
                        handleEntry(text);
                        break;
                    case DEATH:
                        handleDeath(text);
                        break;
                    case LEAVE:
                    case TELEPORTING:
                        handleLeave(text);
                        break;
                    case DIFFICULTY:
                        handleDifficulty(text);
                        break;
                    default:
                        break;
                }
                return;
            }
        }
    }

    private enum DungeonMessage {
        ENTER, DEATH, LEAVE, TELEPORTING, DIFFICULTY
    }
}
