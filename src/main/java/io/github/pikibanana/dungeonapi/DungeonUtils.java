package io.github.pikibanana.dungeonapi;

import io.github.pikibanana.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonUtils {

    MinecraftClient client = MinecraftClient.getInstance();

    public List<String> getDungeonMembers() {
        if (DungeonTracker.inDungeon()) {
            List<Team> teams = getScoreboardTeams();
            if (teams != null) {
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
        try {
            Scoreboard scoreboardObj = client.getNetworkHandler().getScoreboard();
            ScoreboardObjective sidebarObjective = null;

            for (ScoreboardObjective objective : scoreboardObj.getObjectives()) {
                if (scoreboardObj.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR) == objective) {
                    sidebarObjective = objective;
                    break;
                }
            }

            Scoreboard scoreboard = sidebarObjective.getScoreboard();
            List<Team> teams = scoreboard.getTeams().stream()
                    .filter(team -> team.getName().startsWith("TAB-Sidebar-"))
                    .sorted(Comparator.comparingInt(team -> Integer.parseInt(team.getName().substring("TAB-Sidebar-".length()))))
                    .toList();
            return teams != null ? teams : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static String getDungeonDodgeItemIDFrom(ItemStack itemStack) {
        NbtComponent customData = itemStack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData != null) {
            return customData.copyNbt().getString("dd_item_id", null);
        }
        return null;
    }

    @Nullable
    public static String getDungeonDodgeItemRarityFrom(ItemStack itemStack) {
        NbtComponent customData = itemStack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData != null) {
            return customData.copyNbt().getString("rarity", null);
        }
        return null;
    }

    public static boolean drawItemRaritySlotOverlay(DrawContext context, int x, int y, ItemStack stack) {
        String rarity = DungeonUtils.getDungeonDodgeItemRarityFrom(stack);
        int color = -1;
        if (rarity == null) {
            if (DungeonUtils.getDungeonDodgeItemIDFrom(stack) == null) return false; //only show for dungeon dodge items
            //try fetching through lore
            ComponentMap components = stack.getComponents();
            if (components.contains(DataComponentTypes.LORE)) {
                for (Component<?> component : components) {
                    if (component.type().equals(DataComponentTypes.LORE)) {
                        LoreComponent lore = (LoreComponent) component.value();
                        if (lore.styledLines().isEmpty()) break;
                        Text last = lore.styledLines().getLast();
                        TextColor textColor = last.getStyle().getColor();
                        if (textColor != null) color = textColor.getRgb();
                        break;
                    }
                }
            }
            if (color == -1) return false;
        } else {
            color = Main.features.showItemRarityBackgrounds.getRarityColorFor(rarity);
        }

        int alpha = Main.features.showItemRarityBackgrounds.transparency;
        int translucentColor = (alpha << 24) | (color & 0x00FFFFFF);

        context.fill(x, y, x + 16, y + 16, translucentColor);
        return true;
    }
}
