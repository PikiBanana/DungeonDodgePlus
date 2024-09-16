package io.github.pikibanana.misc;

import net.minecraft.util.Formatting;

public enum Color {
    WHITE(255, 255, 255, net.minecraft.util.Formatting.WHITE),
    BLACK(0, 0, 0, net.minecraft.util.Formatting.BLACK),
    RED(255, 0, 0, net.minecraft.util.Formatting.RED),
    GOLD(255, 127, 0, net.minecraft.util.Formatting.GOLD),
    YELLOW(255, 255, 0, net.minecraft.util.Formatting.YELLOW),
    GREEN(0, 255, 0, net.minecraft.util.Formatting.GREEN),
    BLUE(0, 0, 255, net.minecraft.util.Formatting.BLUE),
    PURPLE(127, 0, 127, Formatting.DARK_PURPLE),
    PINK(255, 155, 182, Formatting.LIGHT_PURPLE),
    ;

    public int red;
    public int green;
    public int blue;
    public final Formatting color;

    private static Color[] colors = Color.values();

    public Formatting getColor() {
        return color;
    }

    Color(int red, int green, int blue, Formatting color) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.color = color;
    }


    public static Color[] getColors() {
        return colors;
    }
}