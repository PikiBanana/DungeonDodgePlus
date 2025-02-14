package io.github.pikibanana.dungeonapi.essence;

import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.misc.SheepRandomizer;
import net.minecraft.util.Identifier;

public class EssenceCounter {
    private static final EssenceCounter INSTANCE = new EssenceCounter();
    private final DungeonData dungeonData = DungeonData.getInstance();
    private int currentEssence = 0;
    private int totalEssence = 0;

    private EssenceCounter() {
        loadPersistentData();
    }

    public static EssenceCounter getInstance() {
        return INSTANCE;
    }

    public void loadPersistentData() {
        totalEssence = dungeonData.getInt("totalEssence", 0);
    }

    public void savePersistentData() {
        dungeonData.setInt("totalEssence", totalEssence);
    }

    public void addEssence(int amount) {
        currentEssence += amount;
        totalEssence += amount;
        savePersistentData();
    }

    public void resetSession() {
        currentEssence = 0;
    }

    public String getDisplayText() {
        return buildDisplayText().toString();
    }

    private StringBuilder buildDisplayText() {
        StringBuilder sb = new StringBuilder(50);
        sb.append(DungeonDodgePlusConfig.get().features.essenceCounter.text)
                .append(": ").append(currentEssence);

        if (DungeonDodgePlusConfig.get().features.essenceCounter.showTotal) {
            sb.append("\nTotal ")
                    .append(DungeonDodgePlusConfig.get().features.essenceCounter.totalText)
                    .append(": ").append(totalEssence);
        }
        return sb;
    }

    // Getters for UI component
    public int getCurrentEssence() {
        return currentEssence;
    }

    public int getTotalEssence() {
        return totalEssence;
    }

    public void setCurrentEssence(int amount) {
        currentEssence = Math.max(0, amount);
    }
}