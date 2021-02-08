package durakonline.sk.durakonline.other;

import java.util.List;

public class ColorBetwheen
{
    public static class ColorBetwheenStep
    {
        float pct;
        int color_r, color_g, color_b;

        public ColorBetwheenStep(float _pct, int _color_r, int _color_g, int _color_b)
        {
            pct = _pct;
            color_r = _color_r;
            color_g = _color_g;
            color_b = _color_b;
        }
    }

    public static int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public static int getIntFromColor(float Red, float Green, float Blue){
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }

    public static int getColor(List<ColorBetwheenStep> percentColors, float pct)
    {
        int i = 1;
        for(; i < percentColors.size() - 1; i++)
        {
            if( pct < percentColors.get(i).pct )
            {
                break;
            }
        }

        ColorBetwheenStep lower = percentColors.get( i - 1 );
        ColorBetwheenStep upper = percentColors.get( i );

        float ragne = upper.pct - lower.pct;
        float rargePct = (pct - lower.pct) / ragne;

        float pctLower = 1 - rargePct;
        float pctUpper = rargePct;

        return getIntFromColor(
                  (int) Math.floor( lower.color_r * pctLower + upper.color_r * pctUpper )
                , (int) Math.floor( lower.color_g * pctLower + upper.color_g * pctUpper )
                , (int) Math.floor( lower.color_b * pctLower + upper.color_b * pctUpper )
        );
    }
}
