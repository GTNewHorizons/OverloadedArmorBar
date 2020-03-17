package locusway.overloadedarmorbar;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import locusway.overloadedarmorbar.overlay.OverlayEventHandler;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = OverloadedArmorBar.MODID, name = OverloadedArmorBar.MODNAME, version = OverloadedArmorBar.MODVERSION, useMetadata = true)
public class OverloadedArmorBar {

  public static final String MODID = "overloadedarmorbar";
  public static final String MODNAME = "Overloaded Armor Bar";
  public static final String MODVERSION = "@VERSION@";
  public static org.apache.logging.log4j.Logger logger;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();

    String configDir = event.getModConfigurationDirectory().toString();
    ConfigurationHandler.init(configDir);
    FMLCommonHandler.instance().bus().register(new ConfigurationHandler());

    MinecraftForge.EVENT_BUS.register(OverlayEventHandler.INSTANCE);
  }
}