package io.github.pikibanana.gui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.function.Consumer;

public class ColorPickerScreen extends BaseReturnableScreen {
    private static final int BOX_SIZE = 200;
    private static final int HUE_BAR_HEIGHT = 20;
    private static final int HEX_INPUT_HEIGHT = 20;
    private static final int PREVIEW_BOX_HEIGHT = 20;

    private final Consumer<Integer> onColorSelected;
    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private int currentColor = 0xFFFF0000; // Default to red color
    private float hue = 0.0f;
    private TextFieldWidget hexInputField;

    private boolean isUpdatingHexInput = false; // Flag to track programmatic updates

    public ColorPickerScreen(Consumer<Integer> onColorSelected) {
        super(Text.literal("Color Picker"));
        this.onColorSelected = onColorSelected;
    }

    @Override
    protected void init() {
        super.init();

        // Initialize the hex input field
        hexInputField = new TextFieldWidget(
                textRenderer,
                this.width / 2 - 50,
                this.height / 2 + BOX_SIZE / 2 + 80,
                100,
                HEX_INPUT_HEIGHT,
                Text.literal("")
        );
        hexInputField.setMaxLength(7); // Limit to 7 characters for hex code (#RRGGBB)
        hexInputField.setText(toHex(currentColor)); // Display the current color in hex format
        hexInputField.setChangedListener(this::hexInputListener);

        this.addSelectableChild(hexInputField);

        // Sync everything with the initial color
        updateColor();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int boxX = this.width / 2 - BOX_SIZE / 2;
        int boxY = this.height / 2 - BOX_SIZE / 2;

        // Render the "Color Picker" label
        context.drawText(
                textRenderer,
                Text.of("Color Picker"),
                this.width / 2 - 30,
                this.height / 2 - BOX_SIZE / 2 - 30 ,
                0xFFFFFFFF,
                true
        );

        // Render color picker box (Saturation & Brightness) with border
        context.fill(boxX - 2, boxY - 2, boxX + BOX_SIZE + 2, boxY + BOX_SIZE + 2, 0xFF000000);
        for (int x = 0; x < BOX_SIZE; x++) {
            for (int y = 0; y < BOX_SIZE; y++) {
                int color = Color.HSBtoRGB(hue, (float) x / BOX_SIZE, 1.0f - (float) y / BOX_SIZE);
                context.fill(boxX + x, boxY + y, boxX + x + 1, boxY + y + 1, color);
            }
        }

        // Render color preview box
        int previewY = this.height / 2 + 150;
        context.fill(this.width / 2 - 50 - 1, previewY - 1, this.width / 2 + 50 + 1, previewY + PREVIEW_BOX_HEIGHT + 1, 0xFF000000);
        context.fill(this.width / 2 - 50, previewY, this.width / 2 + 50, previewY + PREVIEW_BOX_HEIGHT, currentColor);

        // Render the hue bar
        int hueBarY = this.height / 2 + BOX_SIZE / 2 + 10;
        context.fill(this.width / 2 - BOX_SIZE / 2 - 2, hueBarY - 2, this.width / 2 + BOX_SIZE / 2 + 2, hueBarY + HUE_BAR_HEIGHT + 2, 0xFF000000);
        for (int x = 0; x < BOX_SIZE; x++) {
            int color = Color.HSBtoRGB(x / (float) BOX_SIZE, 1.0f, 1.0f);
            context.fill(this.width / 2 - BOX_SIZE / 2 + x, hueBarY, this.width / 2 - BOX_SIZE / 2 + x + 1, hueBarY + HUE_BAR_HEIGHT, color);
        }

        // Render the hex input field
        hexInputField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int boxX = this.width / 2 - BOX_SIZE / 2;
        int boxY = this.height / 2 - BOX_SIZE / 2;

        if (mouseX >= boxX && mouseX <= boxX + BOX_SIZE && mouseY >= boxY && mouseY <= boxY + BOX_SIZE) {
            float saturation = (float) (mouseX - boxX) / BOX_SIZE;
            float brightness = 1.0f - (float) (mouseY - boxY) / BOX_SIZE;
            currentColor = Color.HSBtoRGB(hue, saturation, brightness);
            updateColor();
            return true;
        }

        int hueBarY = this.height / 2 + BOX_SIZE / 2 + 10;
        if (mouseY >= hueBarY && mouseY <= hueBarY + HUE_BAR_HEIGHT) {
            hue = Math.max(0, Math.min(1, (float) (mouseX - (this.width / 2.0 - BOX_SIZE / 2.0)) / BOX_SIZE));
            currentColor = Color.HSBtoRGB(hue, 1.0f, 1.0f); // Use full saturation and brightness for hue preview
            updateColor();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void hexInputListener(String text) {
        if (isUpdatingHexInput) return; // Prevent recursion during programmatic updates

        String inputText = hexInputField.getText();
        if (inputText.length() == 7 && inputText.charAt(0) == '#') {
            try {
                int parsedColor = Integer.parseInt(inputText.substring(1), 16);
                currentColor = 0xFF000000 | parsedColor; // Ensure alpha is preserved

                // Extract RGB values
                int r = (parsedColor >> 16) & 0xFF;
                int g = (parsedColor >> 8) & 0xFF;
                int b = parsedColor & 0xFF;

                // Convert RGB to HSB and update the hue
                float[] hsbValues = Color.RGBtoHSB(r, g, b, null);
                hue = hsbValues[0];

                updateColor();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


    private void updateColor() {
        onColorSelected.accept(currentColor);

        // Set the flag to true to indicate a programmatic update
        isUpdatingHexInput = true;
        hexInputField.setText(toHex(currentColor));
        isUpdatingHexInput = false; // Reset the flag after the update
    }

    private String toHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
