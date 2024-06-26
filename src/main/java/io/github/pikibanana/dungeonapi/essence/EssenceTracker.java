package io.github.pikibanana.dungeonapi.essence;

import io.github.pikibanana.Main;
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
    }

    private final EssenceCounter essenceCounter = EssenceCounter.getInstance();

    public void handleMessage(Text text, boolean b) {
        String message = text.getString();
        for (Map.Entry<Pattern, Void> entry : MESSAGE_MAP.entrySet()) {
            Pattern pattern = entry.getKey();
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches()) {
                if (matcher.groupCount() == 1) {
                    handleEssenceFound(matcher.group(1));
                } else if (matcher.groupCount() == 2) {
                    handlePlayerEssenceFound(matcher.group(1), Integer.parseInt(matcher.group(2)));
                }
            }
        }
    }

    private void handleEssenceFound(String essenceAmount) {
        int amount = essenceAmount.equals("a") ? 1 : Integer.parseInt(essenceAmount);
        essenceCounter.addEssence(amount);
    }

    private void handlePlayerEssenceFound(String playerName, int essenceAmount) {
        essenceCounter.addEssence(essenceAmount);
    }
}
