package durakonline.sk.durakonline.other;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import durakonline.sk.durakonline.RoomSurfaceView;

public class AnimationImgToUsersEndZoom
{
    private int COUNT_STEPS = 20;

    private int x_start;
    private int y_start;

    private int x_end;
    private int y_end;

    private float step_animation_x = 0;
    private float step_animation_y = 0;

    private Paint paint_drawBitmap  = null;

    private int index_draw_step = 0;

    public double center_x, center_y;

    private Bitmap img;
    private long start_tms;
    private long delay;
    private long tms2, delay_wait_before;

    public AnimationImgToUsersEndZoom(    Bitmap img
                                        , int x_start
                                        , int y_start
                                        , int x_end
                                        , int y_end
                                        , long delay_step
                                        , int _COUNT_STEPS
                                        , long _delay_wait_before
                                     )
    {
        paint_drawBitmap = new Paint();
        paint_drawBitmap.setAntiAlias(true);
        //paint_drawBitmap.setFilterBitmap(true);
        //paint_drawBitmap.setDither(true);

        this.x_start = x_start;
        this.y_start = y_start;

        center_x = x_start;
        center_y = y_start;


        this.x_end = x_end;
        this.y_end = y_end;

        this.img = img;

        COUNT_STEPS = _COUNT_STEPS;

        double _dx = x_end - x_start;
        double _dy = y_end - y_start;

        double _dd = RoomSurfaceView.distance_point( x_start, y_start, x_end, y_end );

        _dx /= _dd;
        _dy /= _dd;

        _dx *= _dd / (double) COUNT_STEPS;
        _dy *= _dd / (double) COUNT_STEPS;

        step_animation_x += _dx;
        step_animation_y += _dy;

        start_tms = System.currentTimeMillis();

        this.delay               = delay_step;
        this.delay_wait_before   = _delay_wait_before;
    }

    public boolean step()
    {
        if( System.currentTimeMillis() - start_tms < delay )
        {
            return true;
        }

        if( index_draw_step < COUNT_STEPS )
        {
            index_draw_step += 1;
            center_x += step_animation_x;
            center_y += step_animation_y;

            tms2 = System.currentTimeMillis();

            return true;
        }
        else
        {
            if( System.currentTimeMillis() - tms2 < delay_wait_before )
            {
                return true;
            }
        }

        return false;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(img, (float) center_x, (float) center_y, null);
        //canvas.drawBitmap(img, (float) center_x, (float) center_y, paint_drawBitmap);
    }

}
