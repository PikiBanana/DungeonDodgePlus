package io.github.pikibanana.hud;

import io.github.pikibanana.gui.screens.BaseReturnableScreen;
import io.github.pikibanana.hud.components.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ModifyScreen extends BaseReturnableScreen {
    private static final int SCROLL_SENSITIVITY_MULTIPLIER = 5;

    private final EssenceComponent essenceComponent = new EssenceComponent();
    private final FpsComponent fpsComponent = new FpsComponent();
    private final ManaBarComponent manaBarComponent = new ManaBarComponent();
    private final HealthBarComponent healthBarComponent = new HealthBarComponent();
    private DraggableComponent draggedComponent;

    public ModifyScreen() {
        super(Text.of("DungeonDodge+ Configuration"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderHudComponents(context, mouseX, mouseY, delta);
        if (draggedComponent != null) {
            draggedComponent.updatePosition(mouseX, mouseY);
        }
    }

    private void renderHudComponents(DrawContext context, int mouseX, int mouseY, float delta) {
        essenceComponent.render(context, mouseX, mouseY, delta);
        fpsComponent.render(context, mouseX, mouseY, delta);
//        manaBarComponent.render(context, mouseX, mouseY, delta);
//        healthBarComponent.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            return handleComponentSelection(mouseX, mouseY);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean handleComponentSelection(double mouseX, double mouseY) {
        if (essenceComponent.isMouseOver(mouseX, mouseY)) {
            draggedComponent = essenceComponent;
            essenceComponent.startDragging(mouseX, mouseY);
            return true;
        }
        if (fpsComponent.isMouseOver(mouseX, mouseY)) {
            draggedComponent = fpsComponent;
            fpsComponent.startDragging(mouseX, mouseY);
            return true;
        }
//        if (manaBarComponent.isMouseOver(mouseX, mouseY)) {
//            draggedComponent = manaBarComponent;
//            manaBarComponent.startDragging(mouseX, mouseY);
//            return true;
//        }
//        if (healthBarComponent.isMouseOver(mouseX, mouseY)) {
//            draggedComponent = healthBarComponent;
//            healthBarComponent.startDragging(mouseX, mouseY);
//            return true;
//        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggedComponent != null) {
            draggedComponent.stopDragging();
            draggedComponent = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggedComponent != null) {
            draggedComponent.updatePosition(mouseX, mouseY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (essenceComponent.isMouseOver(mouseX, mouseY)) {
            essenceComponent.handleScroll(verticalAmount * SCROLL_SENSITIVITY_MULTIPLIER);
            return true;
        }
//        if (manaBarComponent.isMouseOver(mouseX, mouseY)) {
//            manaBarComponent.handleScroll(verticalAmount * SCROLL_SENSITIVITY_MULTIPLIER);
//            return true;
//        }
//        if (healthBarComponent.isMouseOver(mouseX, mouseY)) {
//            healthBarComponent.handleScroll(verticalAmount * SCROLL_SENSITIVITY_MULTIPLIER);
//            return true;
//        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}