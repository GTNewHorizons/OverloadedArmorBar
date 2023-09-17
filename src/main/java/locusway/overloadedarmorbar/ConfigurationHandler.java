package locusway.overloadedarmorbar;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import locusway.overloadedarmorbar.overlay.ArmorBarRenderer;

public class ConfigurationHandler {

    public static Configuration config;
    public static String[] colorValues = new String[] { "#FFFFFF", "#FF5500", "#FFC747", "#27FFE3", "#00FF00",
            "#7F00FF" };
    public static int[] colorValuesI;
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

        fillColorValuesI();

        if (config.hasChanged()) {
            config.save();
            ArmorBarRenderer.forceUpdate();
        }
    }

    private static void fillColorValuesI() {
        colorValuesI = new int[colorValues.length];
        for (int i = 0; i < colorValues.length; i++) {
            colorValuesI[i] = parseColor(colorValues[i]);
        }
    }

    private static final Pattern colorPattern = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    private static int parseColor(String colorValue) {
        final Matcher matcher = colorPattern.matcher(colorValue);
        if (matcher.matches()) {
            return Integer.parseInt(colorValue.substring(1, 7), 16);
        } else {
            return 0xFFFFFF;
        }
    }

    @SubscribeEvent
    public void onConfigurationChangeEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(OverloadedArmorBar.MODID)) loadConfiguration();
    }

    public static Configuration getConfig() {
        return config;
    }

}
