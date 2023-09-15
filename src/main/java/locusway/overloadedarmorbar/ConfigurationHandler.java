package locusway.overloadedarmorbar;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigurationHandler {

    public static Configuration config;
    public static String[] colorValues = new String[] { "#FFFFFF", "#FF5500", "#FFC747", "#27FFE3", "#00FF00",
            "#7F00FF" };
    public static boolean alwaysShowArmorBar = false;
    public static boolean showEmptyArmorIcons = false;

    public ConfigurationHandler(File configDir) {
        if (config == null) {
            config = new Configuration(configDir);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        alwaysShowArmorBar = config
                .getBoolean("Always Show armor bar?", Configuration.CATEGORY_GENERAL, false, "Always show armor bar");
        colorValues = config.getStringList(
                "Armor Icon Colors",
                Configuration.CATEGORY_GENERAL,
                new String[] { "#FFFFFF", "#FF5500", "#FFC747", "#27FFE3", "#00FF00", "#7F00FF" },
                "Colors must be specified in #RRGGBB format");
        showEmptyArmorIcons = config
                .getBoolean("Show empty armor icons?", Configuration.CATEGORY_GENERAL, false, "Show empty armor icons");

        if (config.hasChanged()) config.save();
    }

    @SubscribeEvent
    public void onConfigurationChangeEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(OverloadedArmorBar.MODID)) loadConfiguration();
    }

    public static Configuration getConfig() {
        return config;
    }
}
