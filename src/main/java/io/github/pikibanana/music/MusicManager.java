package io.github.pikibanana.music;

import io.github.pikibanana.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MusicManager {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Random RANDOM = new Random();
    private static final List<Genre> ACTIVE_GENRES = List.of(Genre.LOFI, Genre.JAZZ);
    private static final LinkedList<SoundEvent> musicQueue = new LinkedList<>();
    private static Identifier lastPlayedTrack;
    private static boolean isPlaying = false;
    private static MusicSound currentMusic;

    public static void playRandomMusic() {
        if (ACTIVE_GENRES.isEmpty()) {
            Main.LOGGER.warn("No active genres to play music from!");
            return;
        }

        Genre genre = ACTIVE_GENRES.get(RANDOM.nextInt(ACTIVE_GENRES.size()));

        // Get a random track, avoiding the last played track
        List<Identifier> soundIdentifiers = genre.getSoundIdentifiers();
        Identifier randomTrackId;

        do {
            randomTrackId = soundIdentifiers.get(RANDOM.nextInt(soundIdentifiers.size()));
        } while (randomTrackId.equals(lastPlayedTrack) && soundIdentifiers.size() > 1);

        SoundEvent currentTrack = SoundRegistry.getSoundEvent(genre, randomTrackId);

        if (currentTrack == null) {
            Main.LOGGER.error("SoundEvent for {} not found!", randomTrackId);
            return;
        }

        musicQueue.add(currentTrack);

        if (!isPlaying) {
            playNextTrack();
        } else {
//            Main.LOGGER.info("Added {} to the queue", randomTrackId);
        }
    }


    private static void playNextTrack() {
        if (musicQueue.isEmpty()) {
            Main.LOGGER.warn("No tracks in the queue to play!");
            playRandomMusic(); // Automatically queue a new song if the queue is empty
            return;
        }

        SoundEvent soundEvent = musicQueue.poll();
        Identifier trackId = soundEvent.getId();
        lastPlayedTrack = trackId; // Track the last played song

        RegistryEntry<SoundEvent> soundEventEntry = Registries.SOUND_EVENT.getEntry(soundEvent);

        if (soundEventEntry == null) {
            Main.LOGGER.error("RegistryEntry for {} not found!", soundEvent);
            return;
        }

        currentMusic = new MusicSound(soundEventEntry, 0, 0, true);

        stopAllSounds();

        CLIENT.getMusicTracker().play(currentMusic);
        isPlaying = true;
//        Main.LOGGER.info("Playing music track: {}", trackId);
    }


    public static void stopMusic() {
        if (!isPlaying) return;

        CLIENT.getMusicTracker().stop();
        isPlaying = false;
//        Main.LOGGER.info("Stopped playing music.");
    }

    private static void stopAllSounds() {
        CLIENT.getSoundManager().stopAll();
    }

    public static void skip() {
        stopMusic();
        playNextTrack();
    }

    public static void tick(MinecraftClient minecraftClient) {
        if (currentMusic != null) { // If music exists
            if (isPlaying && !CLIENT.getMusicTracker().isPlayingType(currentMusic)) { // If playing but currentMusic ended play next track.
                isPlaying = false;
                playNextTrack();
            }
        }
    }
}


