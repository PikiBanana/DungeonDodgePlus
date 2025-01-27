package io.github.pikibanana.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigationType;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

@Environment(value = EnvType.CLIENT)
public abstract class VerticalSliderWidget extends ClickableWidget {
    private static final Identifier TEXTURE = Identifier.ofVanilla("widget/slider");
    private static final Identifier HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/slider_highlighted");
    private static final Identifier HANDLE_TEXTURE = Identifier.ofVanilla("widget/slider_handle");
    private static final Identifier HANDLE_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/slider_handle_highlighted");

    protected double value;
    private boolean sliderFocused;

    public VerticalSliderWidget(int x, int y, int width, int height, Text text, double value) {
        super(x, y, width, height, text);
        this.value = value;
    }

    private Identifier getTexture() {
        if (this.isFocused() && !this.sliderFocused) {
            return HIGHLIGHTED_TEXTURE;
        }
        return TEXTURE;
    }

    private Identifier getHandleTexture() {
        if (this.hovered || this.sliderFocused) {
            return HANDLE_HIGHLIGHTED_TEXTURE;
        }
        return HANDLE_TEXTURE;
    }

    @Override
    protected MutableText getNarrationMessage() {
        return Text.translatable("gui.narrate.slider", this.getMessage());
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, this.getNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                builder.put(NarrationPart.USAGE, Text.translatable("narration.slider.usage.focused"));
            } else {
                builder.put(NarrationPart.USAGE, Text.translatable("narration.slider.usage.hovered"));
            }
        }
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        context.drawGuiTexture(this.getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());

        context.drawGuiTexture(this.getHandleTexture(), this.getX(), this.getY() + (int) (this.value * (double) (this.height - 8)), this.getWidth(), 8);

        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int i = this.active ? 0xFFFFFF : 0xA0A0A0;
        this.drawScrollableText(context, minecraftClient.textRenderer, 2, i | MathHelper.ceil(this.alpha * 255.0f) << 24);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseY);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.sliderFocused = false;
            return;
        }
        GuiNavigationType guiNavigationType = MinecraftClient.getInstance().getNavigationType();
        if (guiNavigationType == GuiNavigationType.MOUSE || guiNavigationType == GuiNavigationType.KEYBOARD_TAB) {
            this.sliderFocused = true;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (KeyCodes.isToggle(keyCode)) {
            this.sliderFocused = !this.sliderFocused;
            return true;
        }
        if (this.sliderFocused) {
            boolean bl;
            boolean bl2 = bl = keyCode == GLFW.GLFW_KEY_UP;
            if (bl || keyCode == GLFW.GLFW_KEY_DOWN) {
                float f = bl ? -1.0f : 1.0f;
                this.setValue(this.value + (double) (f / (float) (this.height - 8)));
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the value from mouse position.
     *
     * <p>The value will be calculated from the position and the height of this
     * slider.
     *
     * @see #setValue
     */
    private void setValueFromMouse(double mouseY) {
        this.setValue((mouseY - (double) (this.getY() + 4)) / (double) (this.height - 8));
    }

    /**
     * @param value the new value; will be clamped to {@code [0, 1]}
     */
    private void setValue(double value) {
        double d = this.value;
        this.value = MathHelper.clamp(value, 0.0, 1.0);
        if (d != this.value) {
            this.applyValue();
        }
        this.updateMessage();
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.setValueFromMouse(mouseY);
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.playDownSound(MinecraftClient.getInstance().getSoundManager());
    }

    protected abstract void updateMessage();

    protected abstract void applyValue();
}
