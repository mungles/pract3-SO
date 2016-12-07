package com.lamejorcompaiadeluniberso.so;

import android.graphics.Color;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by sergio on 7/12/16.
 */

public class ColorGenerator {
    private static String lastColor;
    private static String colors[] = {"9C27B0", "3F51B5", "673AB7", "2196F3", "F44336", "00BCD4", "009688", "9CCC65"};
    private static HashMap<Integer, Integer> colors_assigned = new HashMap<>();

    public static int getColor(int i) {
        if (colors_assigned.get(i) != null) {
            return colors_assigned.get(i);
        } else {
            colors_assigned.put(i, generateColor());
            return colors_assigned.get(i);
        }
    }

    public static int generateColor() {
        int i = ThreadLocalRandom.current().nextInt(0, colors.length);
        if (colors[i].equals(lastColor)) {
            return generateColor();
        } else {
            lastColor = colors[i];
            return Color.parseColor("#" + colors[i]);
        }
    }
}