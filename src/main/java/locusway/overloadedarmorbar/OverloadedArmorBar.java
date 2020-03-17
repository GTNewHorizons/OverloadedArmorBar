package locusway.overloadedarmorbar;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import locusway.overloadedarmorbar.proxy.CommonProxy;

@Mod(modid = OverloadedArmorBar.MODID, name = OverloadedArmorBar.MODNAME, version = OverloadedArmorBar.MODVERSION, useMetadata = true, guiFactory = OverloadedArmorBar.GUI_FACTORY_CLASS)
public class OverloadedArmorBar {

  public static final String MODID = "overloadedarmorbar";
  public static final String MODNAME = "Overloaded Armor Bar";
  public static final String MODVERSION = "1.7.10-1.0.2";
  public static final String GUI_FACTORY_CLASS = "locusway.overloadedarmorbar.client.gui.GuiFactory";

  public static org.apache.logging.log4j.Logger logger;

  @SidedProxy(modId=MODID, clientSide = "locusway.overloadedarmorbar.proxy.ClientProxy", serverSide = "locusway.overloadedarmorbar.proxy.CommonProxy")
  public static CommonProxy proxy;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    proxy.registerEvents();
    String configDir = event.getModConfigurationDirectory().toString();
    ConfigurationHandler.init(configDir);
    FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
  }
}