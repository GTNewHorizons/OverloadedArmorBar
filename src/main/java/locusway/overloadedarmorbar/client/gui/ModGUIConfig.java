package locusway.overloadedarmorbar.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import locusway.overloadedarmorbar.OverloadedArmorBar;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import static locusway.overloadedarmorbar.ConfigurationHandler.getConfig;

public class ModGUIConfig extends GuiConfig {

    public ModGUIConfig(GuiScreen guiScreen){
        super(guiScreen,
                new ConfigElement(getConfig().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                OverloadedArmorBar.MODID,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(getConfig().toString()));
    }

}
