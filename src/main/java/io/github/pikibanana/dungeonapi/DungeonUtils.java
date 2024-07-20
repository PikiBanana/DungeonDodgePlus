package io.github.pikibanana.dungeonapi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonUtils {

    MinecraftClient client = MinecraftClient.getInstance();

    public List<String> getDungeonMembers() {
        if (DungeonTracker.inDungeon()) {
            List<Team> teams = getScoreboardTeams();
            List<String> dungeonMembers = new ArrayList<>();

            boolean playersSectionFound = false;
            Pattern playerPattern = Pattern.compile("- \\[\\d+] (\\S+) \\d+%");

            for (Team team : teams) {
                String prefix = team.getPrefix().getString().replaceAll("§[0-9a-fk-or]", "").trim();

                if (prefix.contains("Players:")) {
                    playersSectionFound = true;
                } else if (playersSectionFound) {
                    if (prefix.isEmpty()) {
                        break;
                    }
                    Matcher matcher = playerPattern.matcher(prefix);
                    if (matcher.matches()) {
                        String playerName = matcher.group(1).trim();
                        dungeonMembers.add(playerName);
                    }
                }
            }

            return dungeonMembers;
        }
        return List.of();
    }

    public boolean isParticipating(String playerName) {
        return getDungeonMembers().contains(playerName);
    }


    public String getRoom() {
        List<Team> teams = getScoreboardTeams();
        Pattern roomPattern = Pattern.compile("✥ (Entrance Room|Room \\d+|Boss Room)");

        for (Team team : teams) {
            String prefix = team.getPrefix().getString().replaceAll("§[0-9a-fk-or]", "").trim();
            Matcher matcher = roomPattern.matcher(prefix);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }

        return "Unknown Room";
    }

    public List<Team> getScoreboardTeams() {
        Scoreboard scoreboardObj = Objects.requireNonNull(client.getNetworkHandler()).getScoreboard();
        ScoreboardObjective sidebarObjective = null;

        for (ScoreboardObjective objective : scoreboardObj.getObjectives()) {
            if (scoreboardObj.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR) == objective) {
                sidebarObjective = objective;
                break;
            }
        }

        Scoreboard scoreboard = Objects.requireNonNull(sidebarObjective).getScoreboard();

        return scoreboard.getTeams().stream()
                .filter(team -> team.getName().startsWith("TAB-Sidebar-"))
                .sorted(Comparator.comparingInt(team -> Integer.parseInt(team.getName().substring("TAB-Sidebar-".length()))))
                .toList();
    }
}
