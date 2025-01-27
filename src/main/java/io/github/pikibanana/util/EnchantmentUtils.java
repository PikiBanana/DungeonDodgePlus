package io.github.pikibanana.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.github.pikibanana.Main;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * DungeonDodge enchantment data.
 * This should be moved to the API eventually.
 */
public class EnchantmentUtils {

    private static final String ENCHANTMENTS_URL = "https://raw.githubusercontent.com/PikiBanana/DungeonDodgePlus/refs/heads/master/src/main/resources/enchantments.json";
    private static final Gson gson = new Gson();
    private static final long UPDATE_INTERVAL = 5 * 60 * 1000;
    private static Map<String, String> maxLevelMap;
    private static long lastFetchTime = 0;

    /**
     * Returns the loaded max level map, fetching data if not already loaded.
     *
     * @return A map of enchantment names to their corresponding max levels.
     */
    public static Map<String, String> getMaxLevelMap() {
        long currentTime = System.currentTimeMillis();

        if (maxLevelMap == null || (currentTime - lastFetchTime) > UPDATE_INTERVAL) {
            maxLevelMap = fetchEnchantments();
            lastFetchTime = currentTime;
        }

        return maxLevelMap;
    }


    /**
     * Fetches enchantment data from the given URL and parses it into a Map.
     *
     * @return A Map where keys are enchantment names and values are their corresponding max levels.
     */
    private static Map<String, String> fetchEnchantments() {
        try {
            String json = sendGetRequest();
            return parseJsonToMap(json);
        } catch (Exception e) {
            Main.LOGGER.error("Failed to fetch enchantments: {}", e.getMessage());
            e.printStackTrace();
            return Map.of();
        }
    }

    /**
     * Sends a GET request to the specified URL and returns the response body as a String.
     *
     * @return The response body as a String, or null if an error occurs.
     */
    private static String sendGetRequest() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENCHANTMENTS_URL))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.err.println("Error fetching data: HTTP status code " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Exception while sending GET request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parses the JSON string into a Map using Gson.
     *
     * @param json The JSON string to parse.
     * @return A Map representing the parsed JSON, or an empty map if parsing fails.
     */
    private static Map<String, String> parseJsonToMap(String json) {
        if (json == null || json.isEmpty()) {
            System.err.println("Received null or empty JSON string.");
            return Map.of();
        }

        try {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
            e.printStackTrace();
            return Map.of();
        }
    }


    /**
     * Generates an array of colors that form a rainbow gradient.
     * The colors are distributed evenly across the HSV spectrum.
     *
     * @param n The number of colors to be generated. A higher number results in a smoother color transition.
     * @return An array of integers where each integer represents a color in RGB format.
     */
    public static int[] generateRainbowGradient(int n) {
        int[] colors = new int[n];
        for (int i = 0; i < n; i++) {
            float hue = (float) i / n * 360;
            colors[i] = hsvToRgb(hue, 1.0f, 1.0f);
        }
        return colors;
    }

    /**
     * Converts HSV (Hue, Saturation, Value) color values to RGB format.
     * The RGB color is returned as an integer where the red, green, and blue
     * components are packed into a single 24-bit value.
     *
     * @param h The hue component, ranging from 0 to 360 degrees. Hue represents the color type.
     * @param s The saturation component, ranging from 0.0 to 1.0. Saturation represents the intensity of the color.
     * @param v The value component, ranging from 0.0 to 1.0. Value represents the brightness of the color.
     * @return An integer representing the RGB color, with red in the highest byte, green in the middle byte,
     * and blue in the lowest byte.
     */
    public static int hsvToRgb(float h, float s, float v) {
        float c = v * s;
        float x = c * (1 - Math.abs((h / 60) % 2 - 1));
        float m = v - c;
        float rPrime = 0, gPrime = 0, bPrime = 0;

        if (0 <= h && h < 60) {
            rPrime = c;
            gPrime = x;
            bPrime = 0;
        } else if (60 <= h && h < 120) {
            rPrime = x;
            gPrime = c;
            bPrime = 0;
        } else if (120 <= h && h < 180) {
            rPrime = 0;
            gPrime = c;
            bPrime = x;
        } else if (180 <= h && h < 240) {
            rPrime = 0;
            gPrime = x;
            bPrime = c;
        } else if (240 <= h && h < 300) {
            rPrime = x;
            gPrime = 0;
            bPrime = c;
        } else if (300 <= h && h < 360) {
            rPrime = c;
            gPrime = 0;
            bPrime = x;
        }

        int r = (int) ((rPrime + m) * 255);
        int g = (int) ((gPrime + m) * 255);
        int b = (int) ((bPrime + m) * 255);

        return (r << 16) | (g << 8) | b;
    }
}
