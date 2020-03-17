package locusway.overloadedarmorbar.proxy;

import locusway.overloadedarmorbar.overlay.OverlayEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy{

    @Override
    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(OverlayEventHandler.INSTANCE);
    }
}
