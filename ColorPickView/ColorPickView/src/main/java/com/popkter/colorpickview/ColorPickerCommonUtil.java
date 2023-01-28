package com.popkter.colorpickview;

public class ColorPickerCommonUtil {

    /**
     * HSB转RGB
     * @param hsb
     * @return
     */
    public static String hsbToRgb(float[] hsb) {
        StringBuffer rgb = new StringBuffer();
        float hsv = hsb[0];
        float sat = hsb[1];
        float val = hsb[2];
        float hi = hsv / 60 % 6;
        float f = hsv / 60 - hi;
        float p = val * (1 - sat);
        float q = val * (1 - sat * f);
        float t = val * (1 - sat * (1 - f));
        String V = String.format("%02x", (int) (val * 255));
        String T = String.format("%02x", (int) (t * 255));
        String P = String.format("%02x", (int) (p * 255));
        String Q = String.format("%02x", (int) (q * 255));
        switch ((int) hi) {
            case 0:
                rgb.append(V);
                rgb.append(T);
                rgb.append(P);
                break;
            case 1:
                rgb.append(Q);
                rgb.append(V);
                rgb.append(P);
                break;
            case 2:
                rgb.append(P);
                rgb.append(V);
                rgb.append(T);
                break;
            case 3:
                rgb.append(P);
                rgb.append(Q);
                rgb.append(V);
                break;
            case 4:
                rgb.append(T);
                rgb.append(P);
                rgb.append(V);
                break;
            case 5:
                rgb.append(V);
                rgb.append(P);
                rgb.append(Q);
                break;
        }
        return rgb.toString();
    }
}
