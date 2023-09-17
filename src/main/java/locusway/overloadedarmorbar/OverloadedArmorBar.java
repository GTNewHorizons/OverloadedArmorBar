package locusway.overloadedarmorbar;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import locusway.overloadedarmorbar.overlay.ArmorBarRenderer;

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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new ArmorBarRenderer());
            FMLCommonHandler.instance().bus().register(new ConfigurationHandler(event.getSuggestedConfigurationFile()));
        }
    }

}
