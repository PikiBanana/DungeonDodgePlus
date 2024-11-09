package io.github.pikibanana.dungeonapi;

public enum DungeonType {

    UNKNOWN(-1),
    CASTLE(0),
    ICE(1),
    JUNGLE(2),
    DESERT(3);

    private final int id;

    DungeonType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}