package io.github.pikibanana.dungeonapi;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.essence.EssenceCounter;
import io.github.pikibanana.util.FormattingUtils;
import io.github.pikibanana.util.TaskScheduler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonTracker {

    public static final EssenceCounter essenceCounter = EssenceCounter.getInstance();
    private static final Pattern dungeonEntryRegex = Pattern.compile("You have entered the ([\\w\\s]+) dungeon.*");
    private static final Pattern dungeonDifficultyRegex = Pattern.compile("\\[!] Notice! Your dungeon difficulty is set to (\\w*)!");
    private static final Map<Pattern, DungeonMessage> MESSAGE_MAP = new HashMap<>();
    private static final DungeonData dungeonData = DungeonData.getInstance();
    private static boolean isInDungeon = false;
    private static DungeonType dungeonType = DungeonType.UNKNOWN;
    private static DungeonDifficulty dungeonDifficulty = DungeonDifficulty.UNKNOWN;

    static {
        MESSAGE_MAP.put(Pattern.compile("You have entered the ([\\w\\s]+) dungeon.*"), DungeonMessage.ENTER);
        MESSAGE_MAP.put(Pattern.compile("Dungeon failed! The whole team died!"), DungeonMessage.DEATH);
        MESSAGE_MAP.put(Pattern.compile("Teleported you to spawn!"), DungeonMessage.LEAVE);
        MESSAGE_MAP.put(Pattern.compile("The boss has been defeated! The dungeon will close in (\\d+) seconds!"), DungeonMessage.END);
        MESSAGE_MAP.put(Pattern.compile("Teleporting..."), DungeonMessage.TELEPORTING);
        MESSAGE_MAP.put(dungeonDifficultyRegex, DungeonMessage.DIFFICULTY);
        MESSAGE_MAP.put(Pattern.compile("The current room has been completed!*"), DungeonMessage.CLEAR);
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
                String dungeonName = typeMatcher.group(1).toUpperCase();
                try {
                    dungeonType = DungeonType.valueOf(dungeonName);
                } catch (IllegalArgumentException e) {
                    dungeonType = DungeonType.UNKNOWN;
                }
            }
            if (MinecraftClient.getInstance().player != null && DungeonDodgeConnection.isConnected() && DungeonDodgePlusConfig.get().features.autoTogglePet.enabled) {
                MinecraftClient.getInstance().player.networkHandler.sendCommand("togglepet");
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
                    if (DungeonDodgePlusConfig.get().features.difficultyAnnouncer.enabled) {
                        MinecraftClient.getInstance().inGameHud.setTitleTicks(10, 30, 20);
                        MinecraftClient.getInstance().inGameHud.setTitle(Text.of(""));
                        MinecraftClient.getInstance().inGameHud.setSubtitle(dungeonDifficulty.getAnnouncementText());
                    }
                } catch (IllegalArgumentException e) {
                    dungeonDifficulty = DungeonDifficulty.UNKNOWN;
                }
            }
        }
    }

    public static void handleDeath(Text message) {
        isInDungeon = false;
    }

    public static void handleClear(Text message) {
        if (Main.features.roomCleared.enabled) {
            FormattingUtils.sendSubtitles(
                    Main.features.roomCleared.text,
                    Main.features.roomCleared.announcementColor,
                    Main.features.roomCleared.bold
            );
        }
    }

    public static void handleEnd(Text message) {
        DungeonDodgePlusConfig.Features.AutoDungeon autoDungeon = Main.features.autoDungeon;

        if (autoDungeon.autoLeave) {
            TaskScheduler.scheduleDelayedTask(autoDungeon.amountOfTimeBeforeLeave * 20,
                    () -> {
                        MinecraftClient client = MinecraftClient.getInstance();
                        if (client.player != null) {
                            client.player.networkHandler.sendCommand("spawn");
                        }
                    });

            if (autoDungeon.autoQuickDungeon) {
                TaskScheduler.scheduleDelayedTask((autoDungeon.amountOfTimeBeforeAutoDungeon * 20) + 5,
                        () -> {
                            MinecraftClient client = MinecraftClient.getInstance();
                            if (client.player != null && autoDungeon.dungeonType != DungeonType.UNKNOWN) {
                                String quickDungeonCommand = "quickdungeon" + " " + autoDungeon.dungeonType.name().toLowerCase() + " "
                                        + autoDungeon.dungeonDifficulty.name().toLowerCase();
                                Main.LOGGER.info("Command String {}", quickDungeonCommand);
                                client.player.networkHandler.sendCommand(quickDungeonCommand);
                            }
                        });
            }
        }
    }


    public static void handleLeave(Text message) {
        isInDungeon = false;
        int essence = essenceCounter.getEssence();
        dungeonData.addInt("totalEssence", essence);
        essenceCounter.setEssence(0);
        dungeonType = DungeonType.UNKNOWN;
        if (MinecraftClient.getInstance().player != null && DungeonDodgeConnection.isConnected() && DungeonDodgePlusConfig.get().features.autoTogglePet.enabled) {
            MinecraftClient.getInstance().player.networkHandler.sendCommand("togglepet");
        }
    }

    public static void handleTeleport(Text message) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null && client.player.getWorld() != null) {
            String worldName = client.player.getWorld().getRegistryKey().getValue().getPath();

            if (!worldName.contains("dungeon")) {
                isInDungeon = false;
                int essence = essenceCounter.getEssence();
                dungeonData.addInt("totalEssence", essence);
                essenceCounter.setEssence(0);
                dungeonType = DungeonType.UNKNOWN;

                if (DungeonDodgeConnection.isConnected() && DungeonDodgePlusConfig.get().features.autoTogglePet.enabled) {
                    client.player.networkHandler.sendCommand("togglepet");
                }
            }
        }
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
                        handleLeave(text);
                        break;
                    case TELEPORTING:
                        handleTeleport(text);
                        break;
                    case END:
                        handleEnd(text);
                    case DIFFICULTY:
                        handleDifficulty(text);
                        break;
                    case CLEAR:
                        handleClear(text);
                        break;
                    default:
                        break;
                }
                return;
            }
        }
    }

    private enum DungeonMessage {
        ENTER, DEATH, LEAVE, TELEPORTING, DIFFICULTY, CLEAR, END
    }
}
