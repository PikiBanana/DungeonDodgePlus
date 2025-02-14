package io.github.pikibanana.hud.components;

import io.github.pikibanana.Main;
import net.minecraft.util.Identifier;
import static io.github.pikibanana.dungeonapi.PlayerStats.*;

public class ManaBarComponent extends ProgressBarComponent {
    private static final Identifier FILLED = Identifier.of(Main.MOD_ID, "textures/gui/mana_bar_filled.png");
    private static final Identifier EMPTY = Identifier.of(Main.MOD_ID, "textures/gui/mana_bar.png");

    public ManaBarComponent() {
        super("manaBar",
                FILLED,
                EMPTY,
                ProgressDirection.HORIZONTAL,
                68,
                18,
                34,
                136,
                5,
                1.0f
        );
    }

    @Override
    protected double getCurrentValue() {
        return getManaValue();
    }

    @Override
    protected double getMaxValue() {
        return getManaMax();
    }

    @Override
    protected int getDefaultX() {
        return (client.currentScreen.width / 2) + 17;
    }

    @Override
    protected int getDefaultY() {
        return client.currentScreen.height - 50;
    }
}