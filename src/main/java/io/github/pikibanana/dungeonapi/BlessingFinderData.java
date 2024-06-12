package io.github.pikibanana.dungeonapi;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class BlessingFinderData {

    private static final Set<BlockPos> foundBlessings = new HashSet<>();
    private static final Set<BlockPos> clickedChests = new HashSet<>();

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!DungeonTracker.inDungeon()) {
                clear();
            }
        });
    }

    public static void remove(BlockPos blockPos) {
        foundBlessings.remove(blockPos);
        clickedChests.remove(blockPos);
    }

    public static boolean isMarked(BlockPos blockPos) {
        return foundBlessings.contains(blockPos);
    }

    public static boolean isClicked(BlockPos blockPos) {
        return clickedChests.contains(blockPos);
    }

    public static void mark(BlockPos blockPos) {
        foundBlessings.add(blockPos);
    }

    public static void markClicked(BlockPos blockPos) {
        clickedChests.add(blockPos);
    }

    public static void clear() {
        foundBlessings.clear();
        clickedChests.clear();
    }
}
