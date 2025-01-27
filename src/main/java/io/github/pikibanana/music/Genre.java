package io.github.pikibanana.music;

import net.minecraft.util.Identifier;

import java.util.List;

public enum Genre {
    JAZZ(
            Identifier.of("dungeondodgeplus", "music.jazz.by_the_sea"),
            Identifier.of("dungeondodgeplus", "music.jazz.funk_groove"),
            Identifier.of("dungeondodgeplus", "music.jazz.mug_full_of_tunes"),
            Identifier.of("dungeondodgeplus", "music.jazz.relaxing"),
            Identifier.of("dungeondodgeplus", "music.jazz.theme")
    ),
    LOFI(
            Identifier.of("dungeondodgeplus", "music.lofi.tokyo_cafe"),
            Identifier.of("dungeondodgeplus", "music.lofi.backyard"),
            Identifier.of("dungeondodgeplus", "music.lofi.good_night"),
            Identifier.of("dungeondodgeplus", "music.lofi.hip_hop"),
            Identifier.of("dungeondodgeplus", "music.lofi.interior_lounge"),
            Identifier.of("dungeondodgeplus", "music.lofi.room"),
            Identifier.of("dungeondodgeplus", "music.lofi.whispering")
    );

    private final List<Identifier> soundIdentifiers;

    Genre(Identifier... soundIdentifiers) {
        this.soundIdentifiers = List.of(soundIdentifiers);
    }

    public List<Identifier> getSoundIdentifiers() {
        return soundIdentifiers;
    }
}
