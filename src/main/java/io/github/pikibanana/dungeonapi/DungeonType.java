package io.github.pikibanana.dungeonapi;

public enum DungeonType {

    UNKNOWN(-1, "unknown"),
    THE_RUINED_CASTLE(0, "castle"),
    THE_FROSTED_PASSAGE(1, "ice"),
    THE_UNDERGROUND_JUNGLE(2, "jungle"),
    THE_SCORCHING_CANYON(3, "desert");

    private final int id;
    private final String commandID;

    DungeonType(int id, String commandID){
        this.commandID = commandID;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * @return The string used to represent this dungeon type in a command such as the quick dungeon command.
     */
    public String getCommandID() {
        return commandID;
    }
}