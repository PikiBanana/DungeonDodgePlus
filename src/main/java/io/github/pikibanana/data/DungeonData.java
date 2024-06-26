package io.github.pikibanana.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DungeonData {
    private static final String CONFIG_DIRECTORY = "config" + File.separator + "dungeondodge";
    private static final String FILE_NAME = "dungeonData.json";
    private static final String FILE_PATH = CONFIG_DIRECTORY + File.separator + FILE_NAME;

    private int totalEssence = 0;

    private static DungeonData instance;

    private DungeonData() {
    }

    static {
        instance = new DungeonData();
        instance.init();
    }

    public static DungeonData getInstance() {
        return instance;
    }

    private void init() {
        File dir = new File(CONFIG_DIRECTORY);
        if (!dir.exists()) dir.mkdirs();
        loadTotalEssence();
    }

    public int getTotalEssence() {
        return totalEssence;
    }

    public void setTotalEssence(int totalEssence) {
        this.totalEssence = totalEssence;
        saveTotalEssence();
    }

    public void addTotalEssence(int essence) {
        this.totalEssence += essence;
        saveTotalEssence();
    }

    private void saveTotalEssence() {
        DataManager.saveDungeonData(this, FILE_PATH);
    }

    private void loadTotalEssence() {
        DungeonData loadedData = DataManager.loadDungeonData(FILE_PATH);
        if (loadedData != null) {
            this.totalEssence = loadedData.getTotalEssence();
        }
    }

    private static class DataManager {
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

        public static void saveDungeonData(DungeonData data, String filePath) {
            try (FileWriter writer = new FileWriter(filePath)) {
                GSON.toJson(data, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static DungeonData loadDungeonData(String filePath) {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }

            try (FileReader reader = new FileReader(filePath)) {
                return GSON.fromJson(reader, DungeonData.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
