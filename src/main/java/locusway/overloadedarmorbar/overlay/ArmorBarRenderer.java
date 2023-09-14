package locusway.overloadedarmorbar.overlay;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.ARMOR;

import locusway.overloadedarmorbar.ConfigurationHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Class which handles the render event and hides the vanilla armor bar
 */
public class ArmorBarRenderer extends Gui {

    private final static int UNKNOWN_ARMOR_VALUE = -1;
    private final static int ARMOR_ICON_SIZE = 9;
    private final static int ARMOR_SECOND_HALF_ICON_SIZE = 4;

    private int previousArmorValue = UNKNOWN_ARMOR_VALUE;
    private final Minecraft mc = Minecraft.getMinecraft();
    private ArmorIcon[] armorIcons;

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {

        if (event.type != ARMOR) return;
        mc.mcProfiler.startSection("armor");
        GL11.glEnable(GL11.GL_BLEND);

        final EntityPlayer player = mc.thePlayer;
        final int currentArmorValue = ForgeHooks.getTotalArmorValue(player);
        final int left = event.resolution.getScaledWidth() / 2 - 91;
        final int top = event.resolution.getScaledHeight() - GuiIngameForge.left_height;

        // Save some CPU cycles by only recalculating armor when it changes
        if (currentArmorValue != previousArmorValue) {
            // Calculate here
            armorIcons = ArmorBar.calculateArmorIcons(currentArmorValue);
            // Save value for next cycle
            previousArmorValue = currentArmorValue;
        }

        int armorIconCounter = 0;
        for (ArmorIcon icon : armorIcons) {
            int xPosition = left + armorIconCounter * 8;
            switch (icon.armorIconType) {
                case NONE:
                    ArmorIconColor color = icon.primaryArmorIconColor;
                    GL11.glColor4f(color.Red, color.Green, color.Blue, color.Alpha);
                    if (currentArmorValue > 20) {
                        // Draw the full icon as we have wrapped
                        this.drawTexturedModalRect(xPosition, top, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                    } else {
                        if (ConfigurationHandler.showEmptyArmorIcons
                                && (ConfigurationHandler.alwaysShowArmorBar || currentArmorValue > 0)) {
                            // Draw the empty armor icon
                            this.drawTexturedModalRect(xPosition, top, 16, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                        }
                    }
                    break;
                case HALF:
                    ArmorIconColor firstHalfColor = icon.primaryArmorIconColor;
                    ArmorIconColor secondHalfColor = icon.secondaryArmorIconColor;

                    GL11.glColor4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, firstHalfColor.Alpha);
                    this.drawTexturedModalRect(xPosition, top, 25, 9, 5, ARMOR_ICON_SIZE);

                    GL11.glColor4f(
                            secondHalfColor.Red,
                            secondHalfColor.Green,
                            secondHalfColor.Blue,
                            secondHalfColor.Alpha);
                    if (currentArmorValue > 20) {
                        // Draw the second half as full as we have wrapped
                        this.drawTexturedModalRect(
                                xPosition + 5,
                                top,
                                39,
                                9,
                                ARMOR_SECOND_HALF_ICON_SIZE,
                                ARMOR_ICON_SIZE);
                    } else {
                        // Draw the second half as empty
                        this.drawTexturedModalRect(
                                xPosition + 5,
                                top,
                                30,
                                9,
                                ARMOR_SECOND_HALF_ICON_SIZE,
                                ARMOR_ICON_SIZE);
                    }
                    break;
                case FULL:
                    ArmorIconColor fullColor = icon.primaryArmorIconColor;
                    GL11.glColor4f(fullColor.Red, fullColor.Green, fullColor.Blue, fullColor.Alpha);
                    this.drawTexturedModalRect(xPosition, top, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                    break;
                default:
                    break;
            }
            armorIconCounter++;
        }
        // Revert our state back
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GuiIngameForge.left_height += 10;
        GL11.glDisable(GL11.GL_BLEND);
        mc.mcProfiler.endSection();
        event.setCanceled(true);

    }

    public void forceUpdate() {
        // Setting to unknown value will cause a refresh next render
        previousArmorValue = UNKNOWN_ARMOR_VALUE;
    }

}
