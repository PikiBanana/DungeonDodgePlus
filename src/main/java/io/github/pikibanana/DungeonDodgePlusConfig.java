package io.github.pikibanana;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.annotation.ConfigEntry;


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
        public BlessingFinder blessingFinder = new BlessingFinder();

        @ConfigEntry.Gui.CollapsibleObject
        public CustomModelDataDisplay customModelDataDisplay = new CustomModelDataDisplay();

        @ConfigEntry.Gui.CollapsibleObject
        public autoTogglePet  autoTogglePet = new autoTogglePet();


//        @ConfigEntry.Gui.TransitiveObject
//        public Timestamp timestamp = new Timestamp();


        public static class EssenceFinder {
            @ConfigEntry.Gui.Excluded
            public String label = "Essence Finder Settings";

            public boolean enabled = true;
            @ConfigEntry.ColorPicker
            public int color = 0x4682B4;
        }

        public static class BlessingFinder {
            @ConfigEntry.Gui.Excluded
            public String label = "Blessing Finder Settings";

            public boolean enabled = true;
            @ConfigEntry.ColorPicker
            public int color = 0x228B22;
        }

        public static class autoTogglePet {
            @ConfigEntry.Gui.Excluded
            public String label = "Auto Toggle Pet";

            public boolean enabled = true;
        }

        public static class CustomModelDataDisplay {
            @ConfigEntry.Gui.Excluded
            public String label = "Custom Model Data Display Settings";

            public boolean enabled = false;

            @ConfigEntry.Gui.PrefixText
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
            public CustomModelDataFormats format = CustomModelDataFormats.PLAIN;
        }

//        public static class Timestamp {
//            public boolean enabled = false;
//        }
    }
}
