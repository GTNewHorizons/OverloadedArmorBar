package locusway.overloadedarmorbar;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler {

    public static Configuration config;
    public static String[] colorValues = new String[]{ "#FFFFFF", "#FF5500", "#FFC747", "#27FFE3", "#00FF00", "#7F00FF"};
    public static boolean alwaysShowArmorBar = false;
    public static boolean showEmptyArmorIcons = false;
    public static boolean offset = false;

    public static void init(String configDir) {
        if(config == null){
            File path = new File(configDir + "/" + OverloadedArmorBar.MODID + ".cfg");
            config = new Configuration(path);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        alwaysShowArmorBar=config.getBoolean("Always Show armor bar?", Configuration.CATEGORY_GENERAL, false, "Always show armor bar" );
        colorValues=config.getStringList("Armor Icon Colors", Configuration.CATEGORY_GENERAL, new String[]{"#FFFFFF", "#FF5500", "#FFC747", "#27FFE3", "#00FF00", "#7F00FF"} , "Colors must be specified in #RRGGBB format");
        offset=config.getBoolean("Override for Armor shift", Configuration.CATEGORY_GENERAL, false,"Set to true if the armor bar display's incorrectly");
        showEmptyArmorIcons=config.getBoolean("Show empty armor icons?", Configuration.CATEGORY_GENERAL, false, "Show empty armor icons");

        if(config.hasChanged())
            config.save();
    }

    @SubscribeEvent
    public void onConfigurationChangeEvent (ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.modID.equalsIgnoreCase(OverloadedArmorBar.MODID))
            loadConfiguration();
    }

    public static Configuration getConfig(){
        return config;
    }
}