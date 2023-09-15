package locusway.overloadedarmorbar.overlay;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.ARMOR;

import locusway.overloadedarmorbar.ConfigurationHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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

    private static int previousArmorValue = UNKNOWN_ARMOR_VALUE;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static ArmorIcon[] armorIcons;

    public static void forceUpdate() {
        previousArmorValue = UNKNOWN_ARMOR_VALUE;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
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
            armorIcons = calculateArmorIcons(currentArmorValue);
            previousArmorValue = currentArmorValue;
        }

        int armorIconCounter = 0;
        for (ArmorIcon icon : armorIcons) {
            int xPosition = left + armorIconCounter * 8;
            armorIconCounter++;
            switch (icon.iconType) {
                case EMPTY:
                    ArmorIconColor color = icon.mainColor;
                    GL11.glColor4f(color.red, color.green, color.blue, color.alpha);
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
                    ArmorIconColor firstHalfColor = icon.mainColor;
                    ArmorIconColor secondHalfColor = icon.secondaryColor;

                    GL11.glColor4f(firstHalfColor.red, firstHalfColor.green, firstHalfColor.blue, firstHalfColor.alpha);
                    this.drawTexturedModalRect(xPosition, top, 25, 9, 5, ARMOR_ICON_SIZE);

                    GL11.glColor4f(
                            secondHalfColor.red,
                            secondHalfColor.green,
                            secondHalfColor.blue,
                            secondHalfColor.alpha);
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
                    ArmorIconColor fullColor = icon.mainColor;
                    GL11.glColor4f(fullColor.red, fullColor.green, fullColor.blue, fullColor.alpha);
                    this.drawTexturedModalRect(xPosition, top, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                    break;
                default:
                    break;
            }
        }
        // Revert our state back
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GuiIngameForge.left_height += 10;
        GL11.glDisable(GL11.GL_BLEND);
        mc.mcProfiler.endSection();
        event.setCanceled(true);

    }

    private static ArmorIcon[] calculateArmorIcons(int armorPoints) {
        ArmorIcon[] armorIcons = new ArmorIcon[10];

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

        for (int i = 0; i < 10; i++) {
            armorIcons[i] = new ArmorIcon();
            setArmorIconColor(armorIcons[i], ConfigurationHandler.colorValues, scale, counter);
            if (counter >= 2) {
                // We have at least a full icon to show
                armorIcons[i].iconType = ArmorIcon.Type.FULL;
                counter -= 2;
            } else if (counter == 1) {
                // We have a half icon to show
                armorIcons[i].iconType = ArmorIcon.Type.HALF;
                counter -= 1;
            } else {
                // Empty icon
                armorIcons[i].iconType = ArmorIcon.Type.EMPTY;
            }
        }

        return armorIcons;

    }

    private static void setArmorIconColor(ArmorIcon icon, String[] colors, int scale, int armorValue) {
        int currentScale = scale;
        int previousScale = scale - 1;

        // Force last color if we have run out of colors on the list
        if (currentScale > colors.length - 1) {
            currentScale = colors.length - 1;
        }
        if (previousScale > colors.length - 1) {
            previousScale = colors.length - 1;
        }

        // Previous scale is -1 between 0 and 20 points of armor, so reset to 0 for sane value
        if (previousScale < 0) {
            previousScale = 0;
        }

        // Covers 2 (FULL) and 1 (HALF) - Primary Color
        if (armorValue >= 1) {
            // Should be current tier color
            icon.mainColor.setColorFromHex(colors[currentScale]);
        }

        // Covers 1 (HALF) - Secondary Color
        if (armorValue == 1) {
            // Should be previous tier color
            icon.secondaryColor.setColorFromHex(colors[previousScale]);
        }

        if (armorValue == 0) {
            // Should be previous tier color
            icon.mainColor.setColorFromHex(colors[previousScale]);
        }
    }

    static class ArmorIcon {

        public Type iconType;
        /** The main color of the icon */
        public final ArmorIconColor mainColor;
        /** When of Type = HALF, this is the color of the right-hand side of the icon */
        public final ArmorIconColor secondaryColor;

        public ArmorIcon() {
            iconType = Type.EMPTY;
            mainColor = new ArmorIconColor();
            secondaryColor = new ArmorIconColor();
        }

        /** The type of armor icon to render */
        public enum Type {
            EMPTY,
            HALF,
            FULL
        }

    }

}
