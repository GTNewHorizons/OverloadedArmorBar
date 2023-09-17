package locusway.overloadedarmorbar.overlay;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.ARMOR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import locusway.overloadedarmorbar.ConfigurationHandler;

/**
 * Class which handles the render event and hides the vanilla armor bar
 */
public class ArmorBarRenderer extends Gui {

    private static final int UNKNOWN_ARMOR_VALUE = -1;
    private static final int ARMOR_ICON_SIZE = 9;
    private static final int ARMOR_FIRST_HALF_WIDTH = 5;
    private static final int ARMOR_SECOND_HALF_WIDTH = 4;
    private static final ArmorIcon[] armorIcons = new ArmorIcon[10];
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static int previousArmorValue = UNKNOWN_ARMOR_VALUE;

    public static void forceUpdate() {
        previousArmorValue = UNKNOWN_ARMOR_VALUE;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {

        if (event.type != ARMOR) return;
        mc.mcProfiler.startSection("armor");
        GL11.glEnable(GL11.GL_BLEND);

        // uncomment to debug
        // final int currentArmorValue = (int) ((System.currentTimeMillis() / 200L) % 140);
        final int currentArmorValue = ForgeHooks.getTotalArmorValue(mc.thePlayer);
        final int left = event.resolution.getScaledWidth() / 2 - 91;
        final int top = event.resolution.getScaledHeight() - GuiIngameForge.left_height;

        // Save some CPU cycles by caching the armor bars to render
        if (currentArmorValue != previousArmorValue) {
            updateArmorIcons(currentArmorValue);
            previousArmorValue = currentArmorValue;
        }

        int colorState = 0xFFFFFF;
        for (int i = 0; i < armorIcons.length; i++) {
            final ArmorIcon icon = armorIcons[i];
            final int xPosition = left + i * 8;
            switch (icon.iconType) {
                case EMPTY:
                    colorState = setColor(icon.mainColor, colorState);
                    drawTexturedModalRect(xPosition, top, 16, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                    break;
                case FULL:
                    colorState = setColor(icon.mainColor, colorState);
                    drawTexturedModalRect(xPosition, top, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                    break;
                case HALF:
                    colorState = setColor(icon.mainColor, colorState);
                    drawTexturedModalRect(xPosition, top, 25, 9, ARMOR_FIRST_HALF_WIDTH, ARMOR_ICON_SIZE);
                    colorState = setColor(icon.secondaryColor, colorState);
                    // Draw the second half as full if the player has more than one bar of armor, empty otherwise
                    final int textureX = currentArmorValue > 20 ? 39 : 30;
                    drawTexturedModalRect(xPosition + 5, top, textureX, 9, ARMOR_SECOND_HALF_WIDTH, ARMOR_ICON_SIZE);
                    break;
                case NONE:
                default:
                    break;
            }
        }
        if (colorState != 0xFFFFFF) {
            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
        GuiIngameForge.left_height += 10;
        GL11.glDisable(GL11.GL_BLEND);
        mc.mcProfiler.endSection();
        event.setCanceled(true);

    }

    private static int setColor(int color, int colorState) {
        if (colorState == color) return colorState;
        final float red = (float) (color >> 16 & 0xFF) / 255F;
        final float green = (float) (color >> 8 & 0xFF) / 255F;
        final float blue = (float) (color & 0xFF) / 255F;
        GL11.glColor4f(red, green, blue, 1F);
        return color;
    }

    private static void updateArmorIcons(int armorPoints) {
        // Calculate which color scale to use
        int scale = armorPoints / 20;
        // Scale the value down for each position
        int counter = armorPoints - (scale * 20);
        // Handle exact wrap around situation
        if (scale > 0 && counter == 0) {
            // Show we are maxed out at previous scale
            scale -= 1;
            counter = 20;
        }
        for (int i = 0; i < armorIcons.length; i++) {
            if (armorIcons[i] == null) armorIcons[i] = new ArmorIcon();
            armorIcons[i].updateColors(scale, counter);
            if (counter >= 2) {
                armorIcons[i].iconType = ArmorIcon.Type.FULL;
                counter -= 2;
            } else if (counter == 1) {
                armorIcons[i].iconType = ArmorIcon.Type.HALF;
                counter -= 1;
            } else {
                if (armorPoints > 20) {
                    armorIcons[i].iconType = ArmorIcon.Type.FULL;
                } else if (ConfigurationHandler.showEmptyArmorIcons && armorPoints > 0
                        || ConfigurationHandler.alwaysShowArmorBar) {
                            armorIcons[i].iconType = ArmorIcon.Type.EMPTY;
                        } else {
                            armorIcons[i].iconType = ArmorIcon.Type.NONE;
                        }
            }
        }
    }

    static class ArmorIcon {

        public Type iconType;
        /** The main color of the icon */
        public int mainColor = 0xFFFFFF;
        /** When of Type = HALF, this is the color of the right-hand side of the icon */
        public int secondaryColor = 0xFFFFFF;

        public void updateColors(int scale, int armorValue) {
            int currentScale = scale;
            int previousScale = scale - 1;
            // Prevent array out of bounds exception
            final int arrayLength = ConfigurationHandler.colorValuesI.length;
            currentScale = Math.min(arrayLength - 1, currentScale);
            previousScale = Math.min(arrayLength - 1, previousScale);
            previousScale = Math.max(0, previousScale);
            // Covers 2 (FULL) and 1 (HALF) - Primary Color
            if (armorValue >= 1) {
                // Icon should be of current tier color
                this.mainColor = ConfigurationHandler.colorValuesI[currentScale];
            }
            // Covers 1 (HALF) - Secondary Color
            if (armorValue == 1) {
                // Only right side of icon should be of previous tier color
                this.secondaryColor = ConfigurationHandler.colorValuesI[previousScale];
            }
            if (armorValue == 0) {
                // Icon should be of previous tier color
                this.mainColor = ConfigurationHandler.colorValuesI[previousScale];
            }
        }

        /** The type of armor icon to render */
        public enum Type {
            EMPTY,
            FULL,
            HALF,
            NONE
        }

    }

}
