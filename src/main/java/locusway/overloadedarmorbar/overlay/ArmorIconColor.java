package locusway.overloadedarmorbar.overlay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Class representing the color of the armor icon
 */
public class ArmorIconColor {

    private static final Pattern pattern = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    public float red;
    public float blue;
    public float green;
    public final float alpha;

    public ArmorIconColor() {
        red = blue = green = alpha = 1.0f;
    }

    /**
     * Converts from #RRGGBB format. If string is not in correct format this function will set the color to white.
     */
    public void setColorFromHex(String colorHex) {
        Matcher matcher = pattern.matcher(colorHex);
        if (matcher.matches()) {
            final int color = Integer.parseInt(colorHex.substring(1, 7), 16);
            red = (float) (color >> 16 & 0xFF) / 255F;
            green = (float) (color >> 8 & 0xFF) / 255F;
            blue = (float) (color & 0xFF) / 255F;
        } else {
            red = green = blue = 1F;
        }
    }
}
