package io.github.pikibanana.dungeonapi.essence;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.DungeonData;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class EssenceCounterScreen extends Screen {
    private static final Identifier ESSENCE_TEXTURE = Identifier.of(Main.MOD_ID, "textures/gui/essence.png");
    private static final Identifier SHEEP_TEXTURE = EssenceCounter.SHEEP_TEXTURE;
    private static final int MIN_ESSENCE_SIZE = 35;
    private static final int MAX_ESSENCE_SIZE = 120;
    private final DungeonData dungeonData = DungeonData.getInstance();
    private int essenceX;
    private int essenceY;
    private int essenceWidth;
    private int essenceHeight;
    private boolean dragging;
    private int dragOffsetX;
    private int dragOffsetY;

    protected EssenceCounterScreen() {
        super(Text.of("Essence Counter Configuration Screen"));
        essenceX = dungeonData.getInt("essenceX", 10);
        essenceY = dungeonData.getInt("essenceY", 10);
        essenceWidth = dungeonData.getInt("essenceWidth", 50);
        essenceHeight = dungeonData.getInt("essenceHeight", 50);
    }

    public static void register() {
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.dungeondodgeplus.essenceScreen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.dungeondodgeplus"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                if (client.currentScreen != null) client.currentScreen.close();
                client.execute(() -> {
                    EssenceCounterScreen screen = new EssenceCounterScreen();
                    client.setScreen(screen);
                    ScreenMouseEvents.afterMouseScroll(screen).register(screen::afterMouseScroll);
                });
            }
        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        String displayText = EssenceCounter.getInstance().getDisplayText();

        if (dungeonData.getBoolean("sheepMode", false)) {
            client.getTextureManager().bindTexture(SHEEP_TEXTURE);
            int originalSheepWidth = 750;
            int originalSheepHeight = 875;
            float aspectRatio = (float) originalSheepHeight / originalSheepWidth;
            essenceHeight = (int) (essenceWidth * aspectRatio);
            context.drawTexture(SHEEP_TEXTURE, essenceX, essenceY, 0, 0, essenceWidth, essenceHeight, essenceWidth, essenceHeight);
        } else {
            client.getTextureManager().bindTexture(ESSENCE_TEXTURE);
            context.drawTexture(ESSENCE_TEXTURE, essenceX, essenceY, 0, 0, essenceWidth, essenceHeight, essenceWidth, essenceHeight);
        }

        int textX = essenceX + essenceWidth + 5;

        if (textX + client.textRenderer.getWidth(displayText) > width) {
            textX = width - client.textRenderer.getWidth(displayText);
        }

        String[] lines = displayText.split("\n");
        int lineCount = lines.length;
        int totalTextHeight = lineCount * client.textRenderer.fontHeight + (lineCount - 1) * 2;

        int textY = essenceY + (essenceHeight - totalTextHeight) / 2;

        for (String line : lines) {
            context.drawText(client.inGameHud.getTextRenderer(), line, textX, textY, DungeonDodgePlusConfig.get().features.essenceCounter.color, false);
            textY += client.textRenderer.fontHeight + 2;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isMouseOverSheep(mouseX, mouseY)) {
            dragging = true;
            dragOffsetX = (int) mouseX - essenceX;
            dragOffsetY = (int) mouseY - essenceY;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = false;
            saveEssenceData();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            essenceX = (int) mouseX - dragOffsetX;
            essenceY = (int) mouseY - dragOffsetY;

            essenceX = Math.max(0, Math.min(width - essenceWidth, essenceX));
            essenceY = Math.max(0, Math.min(height - essenceHeight, essenceY));

            saveEssenceData();
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private boolean isMouseOverSheep(double mouseX, double mouseY) {
        return mouseX >= essenceX && mouseX <= essenceX + essenceWidth && mouseY >= essenceY && mouseY <= essenceY + essenceHeight;
    }

    private void saveEssenceData() {
        dungeonData.setInt("essenceX", essenceX);
        dungeonData.setInt("essenceY", essenceY);
        dungeonData.setInt("essenceWidth", essenceWidth);
        dungeonData.setInt("essenceHeight", essenceHeight);
    }

    private void afterMouseScroll(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (isMouseOverSheep(mouseX, mouseY)) {
            int scaleAmount = (int) verticalAmount;
            if (scaleAmount != 0) {
                int newWidth = essenceWidth + scaleAmount;
                int newHeight = essenceHeight + scaleAmount;

                newWidth = Math.max(MIN_ESSENCE_SIZE, Math.min(MAX_ESSENCE_SIZE, newWidth));
                newHeight = Math.max(MIN_ESSENCE_SIZE, Math.min(MAX_ESSENCE_SIZE, newHeight));

                float ratio = (float) essenceWidth / (float) essenceHeight;
                essenceWidth = newWidth;
                essenceHeight = (int) (essenceWidth / ratio);

                essenceX = (int) mouseX - (essenceWidth / 2);
                essenceY = (int) mouseY - (essenceHeight / 2);

                essenceX = Math.max(0, Math.min(width - essenceWidth, essenceX));
                essenceY = Math.max(0, Math.min(height - essenceHeight, essenceY));

                saveEssenceData();
            }
        }
    }
}
