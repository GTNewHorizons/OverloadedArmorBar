package locusway.overloadedarmorbar;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import locusway.overloadedarmorbar.overlay.OverlayEventHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

@Mod(modid = OverloadedArmorBar.MODID, name = OverloadedArmorBar.MODNAME, version = OverloadedArmorBar.MODVERSION, useMetadata = true)
public class OverloadedArmorBar {

  public static final String MODID = "overloadedarmorbar";
  public static final String MODNAME = "Overloaded Armor Bar";
  public static final String MODVERSION = "@VERSION@";

  //@SidedProxy(clientSide = "locusway.overloadedarmorbar.proxy.ClientProxy")

  @Mod.Instance
  public static OverloadedArmorBar instance;

  public static Logger logger;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    MinecraftForge.EVENT_BUS.register(OverlayEventHandler.INSTANCE);
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
  }
}