package io.github.pikibanana.dungeonapi.essence;

import io.github.pikibanana.data.DungeonData;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EssenceTracker {

    private static final Map<Pattern, Void> MESSAGE_MAP = new HashMap<>();

    static {
        MESSAGE_MAP.put(Pattern.compile("TREASURE! You have found (a|\\d+) Dungeon Essence!"), null);
        MESSAGE_MAP.put(Pattern.compile("TREASURE! (\\w+) has found (\\d+) Dungeon Essence!"), null);
        MESSAGE_MAP.put(Pattern.compile("Treasure: You have found an Essence Pile! \\((\\d+)\\)"), null);
        MESSAGE_MAP.put(Pattern.compile("BONUS ESSENCE! You received (\\d+) bonus dungeon essence for going through the dungeon!"), null);
    }

    private final EssenceCounter essenceCounter = EssenceCounter.getInstance();

    public void handleMessage(Text text, boolean b) {
        String message = text.getString();
        for (Map.Entry<Pattern, Void> entry : MESSAGE_MAP.entrySet()) {
            Pattern pattern = entry.getKey();
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches()) {
                if (pattern.pattern().equals("TREASURE! You have found (a|\\d+) Dungeon Essence!")) {
                    handleEssenceFound(matcher.group(1));
                } else if (pattern.pattern().equals("TREASURE! (\\w+) has found (\\d+) Dungeon Essence!")) {
                    handlePlayerEssenceFound(matcher.group(1), Integer.parseInt(matcher.group(2)));
                } else if (pattern.pattern().equals("Treasure: You have found a Essence Pile! (\\d+)")) {
                    handleEssencePileFound(Integer.parseInt(matcher.group(1)));
                } else if (pattern.pattern().equals("BONUS ESSENCE! You received (\\d+) bonus dungeon essence for going through the dungeon!")) {
                    handleBonusEssenceFound(Integer.parseInt(matcher.group(1)));
                }
            }
        }
    }

    private void handleEssenceFound(String essenceAmount) {
        int amount = essenceAmount.equals("a") ? 1 : Integer.parseInt(essenceAmount);
        essenceCounter.addEssence(amount);
    }

    private void handlePlayerEssenceFound(String playerName, int essenceAmount) {
        DungeonData dungeonData = DungeonData.getInstance();
        dungeonData.addInt("totalEssence", essenceAmount);
    }

    private void handleEssencePileFound(int essenceAmount) {
        essenceCounter.addEssence(essenceAmount);
    }

    private void handleBonusEssenceFound(int essenceAmount) {
        essenceCounter.addEssence(essenceAmount);
    }
}
