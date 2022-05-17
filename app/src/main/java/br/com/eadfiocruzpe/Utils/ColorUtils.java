package br.com.eadfiocruzpe.Utils;

import android.graphics.Color;

import br.com.eadfiocruzpe.R;

public class ColorUtils {

    public static final int RGB_COLOR_GRAY = Color.rgb(155,155,155);
    public static final int RGB_COLOR_BLUE = Color.rgb(28,97,155);
    public static final int RGB_COLOR_GREEN = Color.rgb(19,188,195);
    public static final int RGB_COLOR_GREEN_1 = Color.rgb(24,218,192);
    public static final int RGB_COLOR_GREEN_2 = Color.rgb(115,180,154);
    public static final int RGB_COLOR_RED = Color.rgb(185,64,0);
    public static final int RGB_COLOR_RED_1 = Color.rgb(241,123,60);
    public static final int RGB_COLOR_ORANGE = Color.rgb(231,206,47);
    public static final int RGB_COLOR_YELLOW = Color.rgb(238,107,46);
    public static final int RGB_COLOR_YELLOW_1 = Color.rgb(231,206,47);
    public static final int RGB_COLOR_YELLOW_2 = Color.rgb(231,231,47);

    public static int [] getColorsInvestmentsForCityPH() {
        int [] colors = new int[3];

        colors[0] = R.color.color_strong_green;
        colors[1] = R.color.color_blue_5;
        colors[2] = R.color.color_green_blue_2;

        return colors;
    }

}