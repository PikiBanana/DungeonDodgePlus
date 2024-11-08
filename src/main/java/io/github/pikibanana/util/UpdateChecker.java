package io.github.pikibanana.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.pikibanana.Main;
import io.github.pikibanana.gui.screens.ScreenManager;
import io.github.pikibanana.gui.screens.UpdateCompleteScreen;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class UpdateChecker {

    private static final String MODRINTH_API_BASE = "https://api.modrinth.com/v2/project/";
    private static final String MOD_ID = "dungeondodge+"; // Mod ID on Modrinth
    private static final String CURRENT_VERSION = Main.MOD_VERSION;
    private static final String MOD_NAME = "dungeondodgeplus"; // Mod name used in file paths
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    public static List<String> changelogs = new ArrayList<>();
    public static String downloadUrl;
    public static String latestVersionNumber;
    private final HttpClient httpClient;

    public UpdateChecker() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
    }

    /**
     * Checks for the latest version of the mod and retrieves changelogs for all versions.
     */
    public void checkForUpdates() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(MODRINTH_API_BASE + MOD_ID + "/version"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonArray versions = JsonParser.parseString(response.body()).getAsJsonArray();

            JsonObject latestVersion = versions.get(0).getAsJsonObject();
            latestVersionNumber = latestVersion.get("version_number").getAsString();

            changelogs.clear();
            for (var version : versions) {
                JsonObject versionObject = version.getAsJsonObject();
                String changelog = versionObject.has("changelog") && !versionObject.get("changelog").isJsonNull()
                        ? versionObject.get("changelog").getAsString()
                        : "No changelog available for this version.";
                String versionNumber = versionObject.get("version_number").getAsString();
                changelogs.add("Version " + versionNumber + ":\n" + changelog);
            }

            if (isNewVersionAvailable(latestVersionNumber)) {
                downloadUrl = latestVersion.getAsJsonArray("files")
                        .get(0)
                        .getAsJsonObject()
                        .get("url")
                        .getAsString();

            }
        } catch (Exception e) {
            Main.LOGGER.error("Failed to check for updates: {}", e.getMessage());
        }
    }

    /**
     * Compares the current version with the latest version to check if an update is needed.
     */
    public boolean isNewVersionAvailable(String latestVersion) {
        return !CURRENT_VERSION.equals(latestVersion);
    }

    /**
     * Downloads the latest version of the mod and replaces the old file in the mods folder.
     */
    public void downloadAndReplaceMod() {
        try {
            Path modsFolder = Paths.get(System.getProperty("user.dir"), "mods");
            Path newModFile = modsFolder.resolve(MOD_NAME + "-" + latestVersionNumber + ".jar");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(downloadUrl))
                    .GET()
                    .build();

            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            try (InputStream inputStream = response.body();
                 FileOutputStream outputStream = new FileOutputStream(newModFile.toFile())) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // Remove old mod files to prevent duplicates
            Files.list(modsFolder)
                    .filter(path -> path.getFileName().toString().startsWith(MOD_NAME + "-") &&
                            !path.getFileName().toString().contains(latestVersionNumber))
                    .forEach(oldFile -> {
                        try {
                            Files.delete(oldFile);
                        } catch (IOException e) {
                            Main.LOGGER.error("Failed to delete old mod file: {}", e.getMessage());
                        }
                    });

            ScreenManager.pushScreen(new UpdateCompleteScreen());
        } catch (IOException | InterruptedException e) {
            Main.LOGGER.error("Failed to download or replace mod file: {}", e.getMessage());
        }
    }

    /**
     * Returns the changelog for a specific version.
     */
    public String getChangelogForVersion(String version) {
        if (version == null) {
            return "Version is null!";
        }

        if (changelogs.isEmpty()) {
            Main.LOGGER.warn("Changelog list is empty.");
            return "No changelog data available.";
        }

        for (String changelogEntry : changelogs) {
            if (changelogEntry.contains("Version " + version + ":")) {
                return changelogEntry;
            }
        }

        Main.LOGGER.warn("No changelog found for version: {}", version);
        return "Changelog for version " + version + " not found.";
    }
}
