package io.github.pikibanana.hud.components;

import io.github.pikibanana.Main;
import net.minecraft.util.Identifier;

import static io.github.pikibanana.dungeonapi.PlayerStats.*;

public class HealthBarComponent extends ProgressBarComponent {
    private static final Identifier FILLED = Identifier.of(Main.MOD_ID, "textures/gui/health_bar_filled.png");
    private static final Identifier EMPTY = Identifier.of(Main.MOD_ID, "textures/gui/health_bar.png");

    public HealthBarComponent() {
        super("healthBar",
                FILLED,
                EMPTY,
                ProgressDirection.HORIZONTAL,
                57,
                20,
                29,
                114,
                5,
                1.0f
        );
    }

    @Override
    protected double getCurrentValue() {
        return getHealthValue();
    }

    @Override
    protected double getMaxValue() {
        return getHealthMax();
    }

    @Override
    protected int getDefaultX() {
        return (client.currentScreen.width / 2) - width - 30;
    }

    @Override
    protected int getDefaultY() {
        return client.currentScreen.height - 50;
    }
}