package io.github.pikibanana.data.config;

import io.github.pikibanana.CustomModelDataFormats;
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
        public AutoTogglePet autoTogglePet = new AutoTogglePet();

        @ConfigEntry.Gui.CollapsibleObject
        public HideCooldownMessages hideCooldownMessages = new HideCooldownMessages();

        @ConfigEntry.Gui.CollapsibleObject
        public EssenceCounter essenceCounter = new EssenceCounter();

        @ConfigEntry.Gui.CollapsibleObject
        public TeammateHighlighter teammateHighlighter = new TeammateHighlighter();

        @ConfigEntry.Gui.CollapsibleObject
        public EnemyHighlighter enemyHighlighter = new EnemyHighlighter();

        @ConfigEntry.Gui.CollapsibleObject
        public DifficultyAnnouncer difficultyAnnouncer = new DifficultyAnnouncer();


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

        public static class AutoTogglePet {
            @ConfigEntry.Gui.Excluded
            public String label = "Auto Toggle Pet";

            public boolean enabled = true;
        }

        public static class HideCooldownMessages {
            @ConfigEntry.Gui.Excluded
            public String label = "Hide Cooldown Messages";

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

        public static class EssenceCounter {
            @ConfigEntry.Gui.Excluded
            public String label = "Count Collected Essence";

            public String text = "Essence";

            public String totalText = "Essence";

            public boolean showTotal = true;

            @ConfigEntry.ColorPicker
            public int color = 0xffffff;

            public boolean enabled = true;
        }

        public static class TeammateHighlighter {
            @ConfigEntry.Gui.Excluded
            public String label = "Teammate Highlighter";

            @ConfigEntry.ColorPicker
            public int color = 0x7CFC00;

            public boolean enabled = false;

            @ConfigEntry.Gui.CollapsibleObject
            public TeammateHealthDisplay teammateHealthDisplay = new TeammateHealthDisplay();

            public static class TeammateHealthDisplay {
                @ConfigEntry.Gui.Excluded
                public String label = "Teammate Health Display";

                @ConfigEntry.ColorPicker
                public int color = 0xAA0000;

                public boolean enabled = false;
            }

        }

        public static class EnemyHighlighter {
            @ConfigEntry.Gui.Excluded
            public String label = "Enemy Highlighter";

            public boolean enabled = false;
        }

        public static class DifficultyAnnouncer {
            @ConfigEntry.Gui.Excluded
            public String label = "Difficulty Announcement";

            public boolean enabled = false;
        }

    }
}
