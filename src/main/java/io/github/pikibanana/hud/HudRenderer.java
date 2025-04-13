package io.github.pikibanana.hud;

import io.github.pikibanana.gui.widgets.RecipeWidget;
import io.github.pikibanana.hud.components.EssenceComponent;
import io.github.pikibanana.hud.components.FpsComponent;
import io.github.pikibanana.hud.components.HealthBarComponent;
import io.github.pikibanana.hud.components.ManaBarComponent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class HudRenderer {
    private final EssenceComponent essenceComponent = new EssenceComponent();
    private final FpsComponent fpsComponent = new FpsComponent();
    private final ManaBarComponent manaBarComponent = new ManaBarComponent();
    private final HealthBarComponent healthBarComponent = new HealthBarComponent();
    private final RecipeWidget recipeWidget = new RecipeWidget();

    public void render(DrawContext context, RenderTickCounter tickCounter) {
        essenceComponent.render(context, 0, 0, tickCounter.getTickDelta(true));
        fpsComponent.render(context, 0, 0, tickCounter.getTickDelta(true));
//        manaBarComponent.render(context, 0, 0, tickCounter.getTickDelta(true));
//        healthBarComponent.render(context, 0, 0, tickCounter.getTickDelta(true));
        recipeWidget.render(context, 0, 0, tickCounter.getTickDelta(true));
    }
}