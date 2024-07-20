package io.github.pikibanana.dungeonapi;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public enum DungeonDifficulty {

    UNKNOWN(-1,Text.of("")),
    NORMAL(0, Text.literal("You have entered the ").setStyle(Style.EMPTY.withColor(Formatting.GREEN))
            .append(Text.literal("NORMAL").setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true)))
            .append(Text.literal(" difficulty!").setStyle(Style.EMPTY.withColor(Formatting.GREEN)))),
    HARD(1, Text.literal("You have entered the ").setStyle(Style.EMPTY.withColor(Formatting.GREEN))
            .append(Text.literal("HARD").setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true)))
            .append(Text.literal(" difficulty!").setStyle(Style.EMPTY.withColor(Formatting.GREEN)))),
    INSANE(2, Text.literal("You have entered the ").setStyle(Style.EMPTY.withColor(Formatting.YELLOW))
            .append(Text.literal("INSANE").setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withBold(true)))
            .append(Text.literal(" difficulty!").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))),
    EXTREME(3, Text.literal("You have entered the ").setStyle(Style.EMPTY.withColor(Formatting.YELLOW))
            .append(Text.literal("EXTREME").setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withBold(true)))
            .append(Text.literal(" difficulty!").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))),
    DEMONIC(4, Text.literal("You have entered the ").setStyle(Style.EMPTY.withColor(Formatting.YELLOW))
            .append(Text.literal("DEMONIC").setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withBold(true)))
            .append(Text.literal(" difficulty!").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)))),
    HELLISH(5, Text.literal("You have entered the ").setStyle(Style.EMPTY.withColor(Formatting.GOLD))
            .append(Text.literal("HELLISH").setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true)))
            .append(Text.literal(" difficulty!").setStyle(Style.EMPTY.withColor(Formatting.GOLD)))),
    DEATH(6, Text.literal("You have entered the ").setStyle(Style.EMPTY.withColor(Formatting.GOLD))
            .append(Text.literal("DEATH").setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true)))
            .append(Text.literal(" difficulty!").setStyle(Style.EMPTY.withColor(Formatting.GOLD)))),
    VOID(7, Text.literal("You have entered the ").setStyle(Style.EMPTY.withColor(Formatting.RED))
            .append(Text.literal("VOID").setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)))
            .append(Text.literal(" difficulty!").setStyle(Style.EMPTY.withColor(Formatting.RED)))),
    HARDCORE(8, Text.literal("You have entered the ").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED))
            .append(Text.literal("HARDCORE").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED).withBold(true)))
            .append(Text.literal(" difficulty!").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)))),
    IMPOSSIBLE(9, Text.literal("You have entered the ").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED))
            .append(Text.literal("IMPOSSIBLE").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED).withBold(true)))
            .append(Text.literal(" difficulty!").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED))));


    private final int id;
    private final Text announcementText;

    DungeonDifficulty(int id, Text announcementText){
        this.id = id;
        this.announcementText = announcementText;
    }

    public int getId() {
        return id;
    }

    public Text getAnnouncementText() {
        return announcementText;
    }
}
