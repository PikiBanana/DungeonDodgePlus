package io.github.pikibanana.dungeonapi;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;

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
        }
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
