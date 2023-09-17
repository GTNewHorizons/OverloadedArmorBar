package locusway.overloadedarmorbar.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.config.GuiConfig;
import locusway.overloadedarmorbar.ConfigurationHandler;
import locusway.overloadedarmorbar.OverloadedArmorBar;

public class ModGUIConfig extends GuiConfig {

    public ModGUIConfig(GuiScreen guiScreen) {
        super(
                guiScreen,
                new ConfigElement<>(ConfigurationHandler.getConfig().getCategory(Configuration.CATEGORY_GENERAL))
                        .getChildElements(),
                OverloadedArmorBar.MODID,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(ConfigurationHandler.getConfig().toString()));
    }

}
