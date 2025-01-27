package io.github.pikibanana.music;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class SoundRegistry {

    private static final Map<Genre, Map<Identifier, SoundEvent>> SOUND_EVENTS_BY_GENRE = new HashMap<>();

    public static void registerAll() {
        for (Genre genre : Genre.values()) {
            registerGenreSounds(genre);
        }
    }

    private static void registerGenreSounds(Genre genre) {
        Map<Identifier, SoundEvent> genreMap = SOUND_EVENTS_BY_GENRE.computeIfAbsent(genre, k -> new HashMap<>());
        for (Identifier id : genre.getSoundIdentifiers()) {
            SoundEvent soundEvent = registerSound(id);
            genreMap.put(id, soundEvent);
        }
    }

    private static SoundEvent registerSound(Identifier id) {
        SoundEvent soundEvent = SoundEvent.of(id);
        return Registry.register(Registries.SOUND_EVENT, id, soundEvent);
    }

    public static SoundEvent getSoundEvent(Genre genre, Identifier id) {
        Map<Identifier, SoundEvent> genreMap = SOUND_EVENTS_BY_GENRE.get(genre);
        if (genreMap != null) {
            return genreMap.get(id);
        }
        return null;
    }

    public static List<Identifier> getIdentifiersByGenre(Genre genre) {
        Map<Identifier, SoundEvent> genreMap = SOUND_EVENTS_BY_GENRE.get(genre);
        if (genreMap != null) {
            return new ArrayList<>(genreMap.keySet());
        }
        return List.of();
    }

    public static List<Genre> getActiveGenres() {
        return List.of(Genre.values());
    }
}
