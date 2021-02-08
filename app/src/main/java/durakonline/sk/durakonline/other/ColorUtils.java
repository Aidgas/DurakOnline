package durakonline.sk.durakonline.other;

import android.graphics.Color;

public class ColorUtils {

    private static class Yuv {
        public float y;
        public float u;
        public float v;

        public Yuv(int c) {
            int r = Color.red(c);
            int g = Color.green(c);
            int b = Color.blue(c);
            this.y = 0.299f * r + 0.587f * g + 0.114f * b;
            this.u = (b - y) * 0.493f;
            this.v = (r - y) * 0.877f;
        }
    }



    public static int getColor(int color0, int color1, float p) {
        Yuv c0 = new Yuv(color0);
        Yuv c1 = new Yuv(color1);
        float y = ave(c0.y, c1.y, p);
        float u = ave(c0.u, c1.u, p);
        float v = ave(c0.v, c1.v, p);

        int b = (int) (y + u / 0.493f);
        int r = (int) (y + v / 0.877f);
        int g = (int) (1.7f * y - 0.509f * r - 0.194f * b);

        return Color.rgb(r, g, b);
    }

    private static float ave(float src, float dst, float p) {
        return src + Math.round(p * (dst - src));
    }
}
