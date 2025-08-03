package io.github.pikibanana.gui.widgets;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.gui.PinRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class RecipeWidget extends ClickableWidget {
    private static final Identifier TEXTURE = Identifier.of(Main.MOD_ID, "textures/gui/recipe_container.png");
    private static final int SLOT_SIZE = 18;
    private static final int TEXTURE_WIDTH = 140;
    private static final int TEXTURE_HEIGHT = 68;
    private static final int[][] SLOT_POSITIONS = {
            {26, 8}, {44, 8}, {62, 8},
            {26, 26}, {44, 26}, {62, 26},
            {26, 44}, {44, 44}, {62, 44}
    };
    private static final int[] RESULT_SLOT_POS = {98, 26};
    private final DungeonData data = DungeonData.getInstance();
    TexturedMenuWidgets texturedMenuWidgets = new TexturedMenuWidgets();
    private boolean dragging = false;
    private int dragOffsetX;
    private int dragOffsetY;
    private TexturedButtonWidget prevButton;
    private TexturedButtonWidget nextButton;
    private TexturedButtonWidget removeButton;
    private boolean needsUpdate = true;

    public RecipeWidget() {
        super(loadX(), loadY(), TEXTURE_WIDTH, TEXTURE_HEIGHT + 20, Text.empty());
        initializeControls();
    }

    private static int loadX() {
        return DungeonData.getInstance().getInt("recipeWidgetX", 10);
    }

    private static int loadY() {
        return DungeonData.getInstance().getInt("recipeWidgetY", 10);
    }

    private void savePosition() {
        String configPrefix = "recipeWidget";
        data.setInt(configPrefix + "X", getX());
        data.setInt(configPrefix + "Y", getY());
    }

    private void initializeControls() {
        this.prevButton = texturedMenuWidgets.getLeftArrowButton(getX() + TEXTURE_WIDTH - 38, getY() + 2, 12, () -> {
            PinRecipe.cyclePrevious();
            markDirty();
        }, "Previous Recipe");

        this.nextButton = texturedMenuWidgets.getRightArrowButton(getX() + TEXTURE_WIDTH - 26, getY() + 2, 12, () -> {
            PinRecipe.cycleNext();
            markDirty();
        }, "Next Recipe");

        this.removeButton = texturedMenuWidgets.getCrossButton(getX() + TEXTURE_WIDTH - 14, getY() + 2, 12, () -> {
            PinRecipe.clearCurrent();
            markDirty();
        }, "Unpin Recipe");
    }

    public void markDirty() {
        this.needsUpdate = true;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!PinRecipe.hasPinned()) return;

        if (needsUpdate) {
            updateButtonPositions();
            needsUpdate = false;
        }

        context.drawTexture(identifier -> RenderLayer.getGuiTextured(TEXTURE), TEXTURE, getX(), getY(), 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        DefaultedList<ItemStack> pinned = PinRecipe.getCurrentPinned();
        for (int i = 0; i < Math.min(SLOT_POSITIONS.length, pinned.size()); i++) {
            ItemStack stack = pinned.get(i);
            int slotX = getX() + SLOT_POSITIONS[i][0];
            int slotY = getY() + SLOT_POSITIONS[i][1];

            context.drawItem(stack, slotX, slotY);
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, stack, slotX, slotY);

            if (isHovered(mouseX, mouseY, slotX, slotY) && !stack.isEmpty()) {
                context.drawTooltip(MinecraftClient.getInstance().textRenderer,
                        Screen.getTooltipFromItem(MinecraftClient.getInstance(), stack), mouseX, mouseY);
            }
        }

        if (pinned.size() > SLOT_POSITIONS.length) {
            ItemStack resultStack = pinned.get(SLOT_POSITIONS.length);
            int resultX = getX() + RESULT_SLOT_POS[0];
            int resultY = getY() + RESULT_SLOT_POS[1];

            context.drawItem(resultStack, resultX, resultY);
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, resultStack, resultX, resultY);

            if (isHovered(mouseX, mouseY, resultX, resultY)) {
                context.drawTooltip(MinecraftClient.getInstance().textRenderer,
                        Screen.getTooltipFromItem(MinecraftClient.getInstance(), resultStack), mouseX, mouseY);
            }
        }


        prevButton.render(context, mouseX, mouseY, delta);
        nextButton.render(context, mouseX, mouseY, delta);
        removeButton.render(context, mouseX, mouseY, delta);
        renderButtonTooltips(context, mouseX, mouseY);
    }

    private void renderButtonTooltips(DrawContext context, int mouseX, int mouseY) {
        TexturedButtonWidget[] buttons = {prevButton, nextButton, removeButton};

        for (TexturedButtonWidget button : buttons) {
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

    private void updateButtonPositions() {
        prevButton.setX(getX() + TEXTURE_WIDTH - 38);
        prevButton.setY(getY() + 2);

        nextButton.setX(getX() + TEXTURE_WIDTH - 26);
        nextButton.setY(getY() + 2);

        removeButton.setX(getX() + TEXTURE_WIDTH - 14);
        removeButton.setY(getY() + 2);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.dragging = false;
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= getX() && mouseX < getX() + width &&
               mouseY >= getY() && mouseY < getY() + height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            if (prevButton.mouseClicked(mouseX, mouseY, button)) return true;
            if (nextButton.mouseClicked(mouseX, mouseY, button)) return true;
            if (removeButton.mouseClicked(mouseX, mouseY, button)) return true;

            this.setFocused(true);
            this.dragging = true;
            this.dragOffsetX = (int) (mouseX - getX());
            this.dragOffsetY = (int) (mouseY - getY());
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.dragging) {
            int newX = (int) (mouseX - this.dragOffsetX);
            int newY = (int) (mouseY - this.dragOffsetY);

            MinecraftClient client = MinecraftClient.getInstance();
            newX = Math.max(0, Math.min(client.getWindow().getScaledWidth() - width, newX));
            newY = Math.max(0, Math.min(client.getWindow().getScaledHeight() - height, newY));

            setX(newX);
            setY(newY);
            updateButtonPositions();
            savePosition();
            return true;
        }
        return false;
    }

    private boolean isHovered(int mouseX, int mouseY, int slotX, int slotY) {
        return mouseX >= slotX && mouseX < slotX + RecipeWidget.SLOT_SIZE &&
               mouseY >= slotY && mouseY < slotY + RecipeWidget.SLOT_SIZE;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}