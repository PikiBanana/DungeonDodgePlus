package io.github.pikibanana.gui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TexturedButtonWidget extends net.minecraft.client.gui.widget.TexturedButtonWidget {
    public TexturedButtonWidget(int x, int y, int width, int height, ButtonTextures textures, PressAction pressAction) {
        super(x, y, width, height, textures, pressAction);
    }

    public TexturedButtonWidget(int x, int y, int width, int height, ButtonTextures textures, PressAction pressAction, Text text) {
        super(x, y, width, height, textures, pressAction, text);
    }

    public TexturedButtonWidget(int width, int height, ButtonTextures textures, PressAction pressAction, Text text) {
        super(width, height, textures, pressAction, text);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier identifier = this.textures.get(this.isNarratable(), this.isHovered());
        context.drawGuiTexture(identifier, this.getX(), this.getY(), this.width, this.height);
    }

}
