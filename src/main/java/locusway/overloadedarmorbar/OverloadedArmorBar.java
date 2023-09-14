package locusway.overloadedarmorbar;

import locusway.overloadedarmorbar.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = OverloadedArmorBar.MODID,
        name = OverloadedArmorBar.MODNAME,
        version = OverloadedArmorBar.VERSION,
        useMetadata = true,
        guiFactory = OverloadedArmorBar.GUI_FACTORY_CLASS)
public class OverloadedArmorBar {

    public static final String MODID = "overloadedarmorbar";
    public static final String MODNAME = "Overloaded Armor Bar";
    public static final String VERSION = "GRADLETOKEN_VERSION";
    public static final String GUI_FACTORY_CLASS = "locusway.overloadedarmorbar.client.gui.GuiFactory";

    @SidedProxy(
            modId = MODID,
            clientSide = "locusway.overloadedarmorbar.proxy.ClientProxy",
            serverSide = "locusway.overloadedarmorbar.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.registerEvents();
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
    }

}
