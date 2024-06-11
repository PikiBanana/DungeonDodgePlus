package io.github.pikibanana;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.math.Color;


@Config(name = "DungeonDodgePlus")
public class DungeonDodgePlusConfig implements ConfigData {

    @ConfigEntry.Category("features")
    @ConfigEntry.Gui.TransitiveObject
    public Features features = new Features();

    public static void register() {
        AutoConfig.register(DungeonDodgePlusConfig.class, GsonConfigSerializer::new);
    }

    public static DungeonDodgePlusConfig get() {
        return AutoConfig.getConfigHolder(DungeonDodgePlusConfig.class).getConfig();
    }

    public static class Features {
        @ConfigEntry.Gui.CollapsibleObject
        public EssenceFinder essenceFinder = new EssenceFinder();

        @ConfigEntry.Gui.CollapsibleObject
        public CookieFinder cookieFinder = new CookieFinder();

        @ConfigEntry.Gui.TransitiveObject
        public Timestamp timestamp = new Timestamp();


        public static class EssenceFinder {
            @ConfigEntry.Gui.Excluded
            public String label = "Essence Finder Settings";

            public boolean enabled = true;
            @ConfigEntry.ColorPicker
            public int color = 0x4682B4;
        }

        public static class CookieFinder {
            @ConfigEntry.Gui.Excluded
            public String label = "Cookie Finder Settings";

            public boolean enabled = true;
//            @ConfigEntry.ColorPicker
//            public Color color = Color.decode("#DB7093"); // Default color is coral
        }

        public static class Timestamp {
            public boolean enabled = false;
        }
    }
}
