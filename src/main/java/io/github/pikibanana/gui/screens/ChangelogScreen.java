package io.github.pikibanana.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.pikibanana.util.UpdateChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class ChangelogScreen extends BaseReturnableScreen {
    private static final int RECTANGLE_HEIGHT = 400;
    private static final int LINE_HEIGHT = 12;
    private static final int PADDING = 10;
    private static final int RECTANGLE_WIDTH = 300;

    private final List<String> changelogs = UpdateChecker.changelogs;
    private final List<Text> formattedChangelogs = new ArrayList<>();
    private int scrollOffset = 0;

    public ChangelogScreen() {
        super(Text.of("Changelog"));
        if (changelogs.isEmpty()) {
            formattedChangelogs.add(Text.literal("No changelogs available.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
        } else {
            parseMarkdown();
        }
    }

    private void parseMarkdown() {
        for (String changelog : changelogs) {
            String[] lines = changelog.split("\n");

            for (String line : lines) {
                Text styledText = styleLine(line);
                List<Text> wrappedLines = manuallyWrapText(styledText);
                formattedChangelogs.addAll(wrappedLines);
            }
            // Add an empty line after each changelog
            formattedChangelogs.add(Text.literal(""));
        }
    }

    private Text styleLine(String line) {
        if (line.startsWith("Version")) {
            return Text.literal(line).setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true));
        } else if (line.startsWith("##")) {
            return Text.literal(line.substring(2).trim()).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.YELLOW));
        } else if (line.startsWith("#")) {
            return Text.literal(line.substring(1).trim()).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.AQUA));
        } else if (line.startsWith("- ") || line.startsWith(" - ")) {
            return Text.literal("• " + line.substring(2).trim()).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
        } else {
            return Text.literal(line).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        }
    }

    private List<Text> manuallyWrapText(Text text) {
        List<Text> wrappedLines = new ArrayList<>();
        String[] words = text.getString().split(" ");
        StringBuilder currentLine = new StringBuilder();
        Style currentStyle = text.getStyle();

        for (String word : words) {
            if (MinecraftClient.getInstance().textRenderer.getWidth(currentLine + word + " ") > 280) {
                wrappedLines.add(Text.literal(currentLine.toString()).setStyle(currentStyle));
                currentLine = new StringBuilder();
            }
            currentLine.append(word).append(" ");
        }

        if (!currentLine.isEmpty()) {
            wrappedLines.add(Text.literal(currentLine.toString()).setStyle(currentStyle));
        }

        return wrappedLines;
    }

    @Override
    public void init() {
        super.init();
        ButtonWidget backButton = ButtonWidget.builder(Text.of("Back"), action -> ScreenManager.popScreen())
                .dimensions(this.width / 2 - 150, this.height - 50, 300, 20)
                .build();
        this.addDrawableChild(backButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int xStart = centerX - RECTANGLE_WIDTH / 2;
        int yStart = centerY - RECTANGLE_HEIGHT / 2;

        // Render background
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        context.fill(xStart, yStart, xStart + RECTANGLE_WIDTH, yStart + RECTANGLE_HEIGHT, 0x88000000);
        renderBorders(context, xStart, yStart);

        // Render title text
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal("DungeonDodge+ Changelog")
                        .setStyle(Style.EMPTY.withBold(true).withColor(Formatting.LIGHT_PURPLE)),
                centerX - 75, yStart - 20, 0xFFFFFF);

        renderChangelogText(context, xStart, yStart);
        RenderSystem.disableBlend();
    }

    private void renderBorders(DrawContext context, int xStart, int yStart) {
        context.fill(xStart, yStart, xStart + RECTANGLE_WIDTH, yStart + 2, 0xFFFFFFFF);
        context.fill(xStart, yStart + RECTANGLE_HEIGHT - 2, xStart + RECTANGLE_WIDTH, yStart + RECTANGLE_HEIGHT, 0xFFFFFFFF);
        context.fill(xStart, yStart, xStart + 2, yStart + RECTANGLE_HEIGHT, 0xFFFFFFFF);
        context.fill(xStart + RECTANGLE_WIDTH - 2, yStart, xStart + RECTANGLE_WIDTH, yStart + RECTANGLE_HEIGHT, 0xFFFFFFFF);
    }

    private void renderChangelogText(DrawContext context, int xStart, int yStart) {
        int yPosition = yStart + PADDING - scrollOffset;
        for (Text line : formattedChangelogs) {
            if (yPosition >= yStart + PADDING && yPosition < yStart + RECTANGLE_HEIGHT - PADDING) {
                context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, line, xStart + PADDING, yPosition, 0xFFFFFF);
            }
            yPosition += LINE_HEIGHT + 2;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int totalHeight = formattedChangelogs.size() * LINE_HEIGHT;
        scrollOffset = Math.max(0, Math.min(scrollOffset - (int) (verticalAmount * 10), Math.max(0, totalHeight - RECTANGLE_HEIGHT)));
        return true;
    }
}
