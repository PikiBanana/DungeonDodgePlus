package io.github.pikibanana.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.text.Text;
import net.minecraft.util.context.ContextParameterMap;

import java.util.ArrayList;
import java.util.List;

public class ItemDisplayWidget extends ClickableWidget {

    private final int TOP_PADDING = 2;
    private final int BUTTON_SIZE = 12;
    private final int PADDING = 1;
    private final int SLOT_SIZE = 16;
    private final int ITEMS_IN_ROW = 10;
    private final int FULL_SLOT_SIZE = PADDING * 2 + SLOT_SIZE;

    private final TexturedButtonWidget prevButton;
    private final TexturedButtonWidget nextButton;

    private int visiblePage = 0;
    private int maxPages = 1;

    public ItemDisplayWidget(int windowWidth, int windowHeight) {
        super(0, 0, windowWidth, windowHeight, Text.empty());
        this.setWidth(ITEMS_IN_ROW * (FULL_SLOT_SIZE));
        this.setX(windowWidth - this.width);

        TexturedMenuWidgets texturedMenuWidgets = new TexturedMenuWidgets();
        this.prevButton = texturedMenuWidgets.getLeftArrowButton(this.getX() + PADDING, this.getY() + TOP_PADDING, BUTTON_SIZE, () -> {
            if (visiblePage > 0) visiblePage--;
        }, "Previous Page");
        this.nextButton = texturedMenuWidgets.getRightArrowButton(this.getX() + this.width - PADDING - BUTTON_SIZE, this.getY() + TOP_PADDING, BUTTON_SIZE, () -> {
            if (visiblePage < maxPages) visiblePage++;
        }, "Next Page");
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null || client.player == null) return;
        ClientRecipeBook recipeBook = client.player.getRecipeBook();
        List<RecipeResultCollection> collection = recipeBook.getOrderedResults();
        List<RecipeDisplayEntry> entries = new ArrayList<>();
        collection.forEach(c -> entries.addAll(c.getAllRecipes()));

        int xPos = this.getX();
        int yPos = this.getY();

        context.fill(xPos, yPos, xPos + width, yPos + height, 0x55000000);
        Text text = Text.of("Items: Page " + (visiblePage + 1) + "/" + maxPages);
        int textX = xPos + (width / 2) - (client.textRenderer.getWidth(text) / 2);
        context.drawText(client.textRenderer, text, textX, yPos + TOP_PADDING, 0xFFFFFF, false);

        int topPos = yPos + TOP_PADDING + Math.max(client.textRenderer.fontHeight, BUTTON_SIZE) + TOP_PADDING;
        int itemsOnPage = ITEMS_IN_ROW * ((height - topPos - FULL_SLOT_SIZE) / FULL_SLOT_SIZE);
        maxPages = 1 + (entries.size() / itemsOnPage);

        ContextParameterMap contextParameterMap = SlotDisplayContexts.createParameters(client.world);

        int itemIndex = visiblePage * itemsOnPage;
        for (int y = topPos; y < yPos + height; y += FULL_SLOT_SIZE) {
            for (int x = xPos; x < xPos + width; x += FULL_SLOT_SIZE) {
                if (itemIndex >= entries.size()) break;

                RecipeDisplayEntry recipeDisplayEntry = entries.get(itemIndex);
                List<ItemStack> results = recipeDisplayEntry.display().result().getStacks(contextParameterMap);
                int index = (int) (Math.floor(deltaTicks / 100) % results.size());

                ItemStack itemStack = results.get(index);

                if (isHovered(mouseX, mouseY, x, y, FULL_SLOT_SIZE, FULL_SLOT_SIZE)) {
                    context.fill(x, y, x + FULL_SLOT_SIZE, y + FULL_SLOT_SIZE, 0x55FFFFFF);
                }
                context.drawItem(itemStack, x + PADDING, y + PADDING);
                if (isHovered(mouseX, mouseY, x, y, FULL_SLOT_SIZE, FULL_SLOT_SIZE)) {
                    context.drawItemTooltip(client.textRenderer, itemStack, mouseX, mouseY);
                }

                itemIndex++;
            }
        }

        prevButton.render(context, mouseX, mouseY, deltaTicks);
        nextButton.render(context, mouseX, mouseY, deltaTicks);
        renderButtonTooltips(context, mouseX, mouseY);
    }

    private void renderButtonTooltips(DrawContext context, int mouseX, int mouseY) {
        net.minecraft.client.gui.widget.TexturedButtonWidget[] buttons = {prevButton, nextButton};

        for (net.minecraft.client.gui.widget.TexturedButtonWidget button : buttons) {
            if (isHovered(mouseX, mouseY, button.getX(), button.getY(), button.getWidth(), button.getHeight())) {
                button.setFocused(true);
                context.drawTooltip(MinecraftClient.getInstance().textRenderer,
                        button.getMessage(), mouseX, mouseY);
            } else {
                button.setFocused(false);
            }
        }
    }

    private boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width &&
                mouseY >= y && mouseY < y + height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            if (prevButton.mouseClicked(mouseX, mouseY, button)) return true;
            if (nextButton.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return false;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}
