package io.github.pikibanana.dungeonapi;

public enum DungeonType {

    UNKNOWN(-1),
    THE_RUINED_CASTLE(0),
    THE_FROSTED_PASSAGE(1),
    THE_UNDERGROUND_JUNGLE(2),
    THE_SCORCHING_CANYON(3);

    private final int id;

    DungeonType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}