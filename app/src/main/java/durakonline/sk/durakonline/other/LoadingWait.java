package durakonline.sk.durakonline.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LoadingWait
{
    public Bitmap loading = null;
    private Paint paint_drawBitmap = null;

    float x_r, y_r, R0, R1, step_angle, i, j, ADD_R;

    private float interpolate(float a, float b, float proportion) {
        return (a + ((b - a) * proportion));
    }

    /**
     * Returns an interpolated color, between <code>a</code> and <code>b</code>
     * proportion = 0, results in color a
     * proportion = 1, results in color b
     */
    private int interpolateColor(int a, int b, float proportion) {

        if (proportion > 1 || proportion < 0) {
            throw new IllegalArgumentException("proportion must be [0 - 1]: " + String.valueOf(proportion) );
        }
        float[] hsva = new float[3];
        float[] hsvb = new float[3];
        float[] hsv_output = new float[3];

        Color.colorToHSV(a, hsva);
        Color.colorToHSV(b, hsvb);
        for (int i = 0; i < 3; i++) {
            hsv_output[i] = interpolate(hsva[i], hsvb[i], proportion);
        }

        int alpha_a = Color.alpha(a);
        int alpha_b = Color.alpha(b);
        float alpha_output = interpolate(alpha_a, alpha_b, proportion);

        return Color.HSVToColor((int) alpha_output, hsv_output);
    }

    public LoadingWait(Context ctx)
    {
        //loading  = BitmapFactory.decodeResource( ctx.getResources(), R.drawable.loading_game);

        paint_drawBitmap = new Paint();
        paint_drawBitmap.setAntiAlias(true);
        paint_drawBitmap.setStyle(Paint.Style.FILL);
        paint_drawBitmap.setColor(Color.parseColor("#cccccc"));

        R1 = Utils.dipToPixels(ctx, 2);
        R0 = Utils.dipToPixels(ctx, 80);

        step_angle = 360.0f / 28.0f;

        j = 0;
    }

    public void step(Canvas canvas, int center_x, int center_y)
    {
        for(i = 0; i < 360.0f; i+= step_angle )
        {
            x_r = (float) (center_x + R0 * Math.sin(Math.toRadians( i )));
            y_r = (float) (center_y + R0 * Math.cos(Math.toRadians( i )));

            ADD_R = ( ( i + j ) % 360.0f ) / 360.0f;

            paint_drawBitmap.setColor( interpolateColor(Color.WHITE, Color.BLACK, ADD_R ));

            canvas.drawCircle(
                      x_r
                    , y_r
                    , R1 + ( 1.0f - ADD_R ) * 5.4f
                    , paint_drawBitmap
            );
        }

        j += 1.2f;
    }
}
