package io.github.pikibanana.dungeonapi;

import io.github.pikibanana.Main;
import io.github.pikibanana.config.DungeonDodgePlusConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;

import java.util.Objects;

public class DungeonDodgeConnection {

    private static boolean isConnected = false;
    private boolean isToggled = false;
    private int hiddenTogglePet = 0;
    private int tickCounter = 0; // Counter to limit the frequency of connection checks

    public static boolean isConnected() {
        return isConnected;
    }

    public void handleMessage(Text text, boolean b) {
        if (isDungeonDodgeSidebar())
            isConnected = true;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && isConnected && DungeonDodgePlusConfig.get().features.autoTogglePet.enabled && !isToggled) {
            player.networkHandler.sendCommand("togglepet");
            isToggled = true;
        }
    }

    public boolean isDungeonDodgeSidebar() {
        if (!isConnected) {
            Scoreboard scoreboard = Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getScoreboard();
            ScoreboardObjective sidebarObjective = null;

            for (ScoreboardObjective objective : scoreboard.getObjectives()) {
                if (scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR) == objective) {
                    sidebarObjective = objective;
                    break;
                }
            }
            if (sidebarObjective != null) {
                String displayName = sidebarObjective.getDisplayName().getString();
                Main.LOGGER.info("Display Name: " + displayName);
                String displayNameColorless = displayName.replaceAll("§[0-9a-fk-or]", "").trim();
                Main.LOGGER.info("Colorless Display Name: " + displayNameColorless);
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

    public void handleLeave(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {
        Main.LOGGER.warn("Player disconnected: " + serverPlayNetworkHandler.player.getName().getString());
        if (isConnected) {
            isConnected = false;
            Main.LOGGER.info("Player is now disconnected, setting isConnected to false.");
        }
    } //TODO: Fix event not registering correctly
}
