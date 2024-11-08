package io.github.pikibanana.dungeonapi;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.util.TaskScheduler;
import io.github.pikibanana.util.UpdateChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Comparator;
import java.util.List;

public class DungeonDodgeConnection {

    private static boolean isConnected = false;
    private boolean isToggled = false;
    private int hiddenTogglePet = 0;

    public static boolean isConnected() {
        return isConnected;
    }

    public void handleMessage(Text text, boolean b) {
        if (isDungeonDodgeSidebar2())
            isConnected = true;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && isConnected && DungeonDodgePlusConfig.get().features.autoTogglePet.enabled && !isToggled) {
            player.networkHandler.sendCommand("togglepet");
            isToggled = true;

            UpdateChecker updateChecker = new UpdateChecker();
            boolean isUpdateAvailable = updateChecker.isNewVersionAvailable(UpdateChecker.latestVersionNumber);

            if (isUpdateAvailable) {
                TaskScheduler.scheduleDelayedTask(100, this::sendUpdateMessageToChat);
            }
        }
    }

    private void sendUpdateMessageToChat() {
        ChatHud chatHud = MinecraftClient.getInstance().inGameHud.getChatHud();

        chatHud.addMessage(Text.literal("🚀 DungeonDodge+ Update Available!")
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true)));

        chatHud.addMessage(Text.literal(" "));
        chatHud.addMessage(Text.literal("A fresh new update is here, featuring exciting new features and improvements!")
                .setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));

        chatHud.addMessage(Text.literal("Don't miss out on the latest enhancements—download it now! ")
                .append(Text.literal("Click here").setStyle(Style.EMPTY.withColor(Formatting.AQUA)
                        .withUnderline(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, UpdateChecker.downloadUrl))))
                .append(Text.literal(" or find the update button in the menu at the bottom right of the pause/title screen."))
                .setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

        chatHud.addMessage(Text.literal(" "));
        chatHud.addMessage(Text.literal("⚠ Update now to experience the newest features and improvements!")
                .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)));
    }

    public boolean isDungeonDodgeSidebar2() {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler == null) return false;

        Scoreboard scoreboardObJ = networkHandler.getScoreboard();
        ScoreboardObjective sidebarObjective = null;

        for (ScoreboardObjective objective : scoreboardObJ.getObjectives()) {
            if (scoreboardObJ.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR) == objective) {
                sidebarObjective = objective;
                break;
            }
        }

        if (sidebarObjective == null) return false;

        Scoreboard scoreboard = sidebarObjective.getScoreboard();

        List<Team> teams = scoreboard.getTeams().stream()
                .filter(team -> team.getName().startsWith("TAB-Sidebar-"))
                .sorted(Comparator.comparingInt(team -> Integer.parseInt(team.getName().substring("TAB-Sidebar-".length()))))
                .toList();

        return !teams.isEmpty() && teams.getLast().getPrefix().getString().replaceAll("§[0-9a-fk-or]", "").trim().contains("dungeondodge.net");
    }


    public boolean isDungeonDodgeSidebar() {
        if (!isConnected) {
            ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
            if (networkHandler == null) return false;

            Scoreboard scoreboard = networkHandler.getScoreboard();
            ScoreboardObjective sidebarObjective = null;

            for (ScoreboardObjective objective : scoreboard.getObjectives()) {
                if (scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR) == objective) {
                    sidebarObjective = objective;
                    break;
                }
            }
            if (sidebarObjective != null) {
                String displayName = sidebarObjective.getDisplayName().getString();
                Main.LOGGER.info("Display Name: {}", displayName);
                String displayNameColorless = displayName.replaceAll("§[0-9a-fk-or]", "").trim();
                Main.LOGGER.info("Colorless Display Name: {}", displayNameColorless);
                return displayNameColorless.contains("Dungeon Dodge");
            }
        }
        return false;
    }

    public boolean allowMessage(Text text, boolean b) {
        String message = text.getString();
        boolean blockCooldowns = DungeonDodgePlusConfig.get().features.hideCooldownMessages.enabled;
        if (message.contains("Your pet is now hidden!") && isToggled && hiddenTogglePet == 0) {
            hiddenTogglePet += 1;
            return false;
        } else if (message.contains("This ability is still on cooldown") && blockCooldowns) {
            return false;
        } else if (message.contains("You may use this ability again in") && blockCooldowns) {
            return false;
        }
        return true;
    }
}
