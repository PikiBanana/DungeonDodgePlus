package io.github.pikibanana.dungeonapi;

public enum DungeonDifficulty {

    UNKNOWN(-1),
    NORMAL(0),
    HARD(1),
    INSANE(2),
    EXTREME(3),
    DEMONIC(4),
    HELLISH(5),
    DEATH(6),
    VOID(7),
    HARDCORE(8),
    IMPOSSIBLE(9),
    ;


    private final int id;

    DungeonDifficulty(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
