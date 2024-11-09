package io.github.pikibanana.data.config;

import io.github.pikibanana.CustomModelDataFormats;
import io.github.pikibanana.dungeonapi.DungeonDifficulty;
import io.github.pikibanana.dungeonapi.DungeonType;
import io.github.pikibanana.misc.Color;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

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

        @ConfigEntry.Gui.CollapsibleObject
        public DungeonWaterColors dungeonWaterColors = new DungeonWaterColors();

        @ConfigEntry.Gui.CollapsibleObject
        public HideOtherFishingBobbers hideOtherFishingBobbers = new HideOtherFishingBobbers();

        @ConfigEntry.Gui.CollapsibleObject
        public ShowManaBar showManaBar = new ShowManaBar();

        @ConfigEntry.Gui.CollapsibleObject
        public ShowFPSCounter showFPSCounter = new ShowFPSCounter();

        @ConfigEntry.Gui.CollapsibleObject
        public ColorMaxEnchantments colorMaxEnchantments = new ColorMaxEnchantments();

        @ConfigEntry.Gui.CollapsibleObject
        public FishingAnnouncement fishingAnnouncement = new FishingAnnouncement();

        @ConfigEntry.Gui.CollapsibleObject
        public RoomCleared roomCleared = new RoomCleared();

        @ConfigEntry.Gui.CollapsibleObject
        public AutoDungeon autoDungeon = new AutoDungeon();

        @ConfigEntry.Gui.CollapsibleObject
        public QuickDungeonHotkeys quickDungeon = new QuickDungeonHotkeys();


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

            public boolean enabled = false;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public Color color = Color.GREEN;

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

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public Color color = Color.RED;
        }

        public static class DifficultyAnnouncer {
            @ConfigEntry.Gui.Excluded
            public String label = "Difficulty Announcement";

            public boolean enabled = false;
        }

        public static class DungeonWaterColors {
            @ConfigEntry.Gui.Excluded
            public String label = "Dungeon Water Colors";

            public boolean enabled = true;
        }

        public static class HideOtherFishingBobbers {
            @ConfigEntry.Gui.Excluded
            public String label = "Hide Other Fishing Bobbers";

            public boolean enabled = true;
        }

        public static class ShowManaBar {
            @ConfigEntry.Gui.Excluded
            public String label = "Show Mana Bar";

            public boolean enabled = true;
        }

        public static class ShowFPSCounter {
            @ConfigEntry.Gui.Excluded
            public String label = "Show FPS Counter";

            public boolean enabled = false;

            @ConfigEntry.ColorPicker
            public int textColor = 0xFFFFFF;

            public boolean backgroundColor = true;
        }

        public static class ColorMaxEnchantments {
            @ConfigEntry.Gui.Excluded
            public String label = "Color Max Enchantments";

            public boolean enabled = true;

            public boolean isRainbow = true;

            @ConfigEntry.ColorPicker
            public int enchantmentColor = 0xFFD500;

            @ConfigEntry.Gui.PrefixText
            @ConfigEntry.BoundedDiscrete(min = 10, max = 100)
            public int animationSpeed = 50;

            @ConfigEntry.BoundedDiscrete(min = 32, max = 256)
            public int animationSmoothness = 256;
        }

        public static class FishingAnnouncement {
            @ConfigEntry.Gui.Excluded
            public String label = "Fish Caught Announcement";

            public boolean enabled = true;

            public boolean bold = false;

            public String text = "Fish Caught!";

            @ConfigEntry.ColorPicker
            public int announcementColor = 0x87CEFA;
        }

        public static class RoomCleared {
            @ConfigEntry.Gui.Excluded
            public String label = "Room Cleared Announcement";

            public boolean enabled = true;

            public boolean bold = false;

            public String text = "Room Cleared!";

            @ConfigEntry.ColorPicker
            public int announcementColor = 0xDC143C;
        }

        public static class AutoDungeon {

            public boolean autoLeave = false;

            @ConfigEntry.BoundedDiscrete(min = 0, max = 20) // In Seconds
            public int amountOfTimeBeforeLeave = 0;

            public boolean autoQuickDungeon = false;

            @ConfigEntry.BoundedDiscrete(min = 0, max = 20) // In Seconds
            public int amountOfTimeBeforeAutoDungeon = 0;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public DungeonType dungeonType = DungeonType.UNKNOWN;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public DungeonDifficulty dungeonDifficulty = DungeonDifficulty.NORMAL;
        }

        public static class QuickDungeonHotkeys {

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public DungeonType dungeonType = DungeonType.UNKNOWN;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public DungeonDifficulty dungeonDifficulty = DungeonDifficulty.NORMAL;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public DungeonType dungeonType2 = DungeonType.UNKNOWN;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public DungeonDifficulty dungeonDifficulty2 = DungeonDifficulty.NORMAL;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public DungeonType dungeonType3 = DungeonType.UNKNOWN;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public DungeonDifficulty dungeonDifficulty3 = DungeonDifficulty.NORMAL;
        }


    }
}
