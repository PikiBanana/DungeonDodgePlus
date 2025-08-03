package io.github.pikibanana.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.pikibanana.Main;
import io.github.pikibanana.util.UpdateChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class UpdateScreen extends BaseReturnableScreen {
    private static final int RECTANGLE_HEIGHT = 350;
    private static final int LINE_HEIGHT = 12;
    private static final int RECTANGLE_WIDTH = 300;
    private static final int PADDING = 10;
    private static final int VERTICAL_PADDING = 10;

    private final UpdateChecker updateChecker = new UpdateChecker();
    private final String updateChangelog = updateChecker.getChangelogForVersion(UpdateChecker.latestVersionNumber);
    private final List<Text> formattedUpdateChangelog = new ArrayList<>();
    private int scrollOffset = 0;

    public UpdateScreen() {
        super(Text.of("Update"));
        Main.LOGGER.info("Update Changelog: {}", updateChangelog);
        if (updateChangelog == null || updateChangelog.isEmpty()) {
            Main.LOGGER.warn("No changelog found for the update!");
        }
        parseMarkdown();
    }

    private void parseMarkdown() {
        if (updateChangelog == null || updateChangelog.isEmpty()) {
            formattedUpdateChangelog.add(Text.literal("No changelog available for this update.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            return;
        }

        String[] lines = updateChangelog.split("\n");

        for (String line : lines) {
            Text styledText = createStyledText(line);
            List<Text> wrappedLines = manuallyWrapText(styledText);
            formattedUpdateChangelog.addAll(wrappedLines);
        }
    }

    private Text createStyledText(String line) {
        Text styledText;
        if (line.startsWith("Version")) {
            styledText = Text.literal(line).setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true));
        } else if (line.startsWith("##")) {
            styledText = Text.literal(line.substring(2).trim()).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.YELLOW));
        } else if (line.startsWith("#")) {
            styledText = Text.literal(line.substring(1).trim()).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.AQUA));
        } else if (line.startsWith("- ") || line.startsWith(" - ")) {
            styledText = Text.literal("• " + line.substring(2).trim()).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
        } else {
            styledText = Text.literal(line).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        }
        return styledText;
    }

    private List<Text> manuallyWrapText(Text text) {
        List<Text> wrappedLines = new ArrayList<>();
        String originalText = text.getString();
        String[] words = originalText.split(" ");

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

        ButtonWidget updateButton = ButtonWidget.builder(Text.of("Update"),
                        action -> updateChecker.downloadAndReplaceMod())
                .dimensions(this.width / 2 - 150, this.height - 80, 150, 20)
                .build();
        this.addDrawableChild(updateButton);

        ButtonWidget backButton = ButtonWidget.builder(Text.of("Back"),
                        action -> ScreenManager.popScreen())
                .dimensions(this.width / 2, this.height - 80, 150, 20)
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
        //RenderSystem.enableBlend();
        //RenderSystem.defaultBlendFunc();
        context.fill(xStart, yStart, xStart + RECTANGLE_WIDTH, yStart + RECTANGLE_HEIGHT, 0x88000000);

        context.fill(xStart, yStart, xStart + RECTANGLE_WIDTH, yStart + 2, 0xFFFFFFFF);
        context.fill(xStart, yStart + RECTANGLE_HEIGHT - 2, xStart + RECTANGLE_WIDTH, yStart + RECTANGLE_HEIGHT, 0xFFFFFFFF);
        context.fill(xStart, yStart, xStart + 2, yStart + RECTANGLE_HEIGHT, 0xFFFFFFFF);
        context.fill(xStart + RECTANGLE_WIDTH - 2, yStart, xStart + RECTANGLE_WIDTH, yStart + RECTANGLE_HEIGHT, 0xFFFFFFFF);

        // Render title text
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal("DungeonDodge+ Update")
                        .setStyle(Style.EMPTY.withBold(true).withColor(Formatting.LIGHT_PURPLE)),
                centerX - 75, yStart - 20, 0xFFFFFF);

        int yPosition = yStart + VERTICAL_PADDING - scrollOffset;
        for (Text line : formattedUpdateChangelog) {
            if (yPosition >= yStart + VERTICAL_PADDING && yPosition < yStart + RECTANGLE_HEIGHT - VERTICAL_PADDING) {
                context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, line, xStart + PADDING, yPosition, 0xFFFFFF);
            }
            yPosition += LINE_HEIGHT + 2;
        }
        //RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int totalHeight = formattedUpdateChangelog.size() * LINE_HEIGHT;
        scrollOffset = Math.max(0, Math.min(scrollOffset - (int) (verticalAmount * 10), Math.max(0, totalHeight - RECTANGLE_HEIGHT)));
        return true;
    }
}
