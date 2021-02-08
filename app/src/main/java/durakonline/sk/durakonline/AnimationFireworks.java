package durakonline.sk.durakonline;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class AnimationFireworks
{
    private float start_x;
    private float start_y;
    private float count_steps_step_1;
    private float pos_x_start_fire;
    private float pos_y_start_fire;
    private float count_steps_step_2;
    private float scale_start_fire;
    private float scale_end_fire;
    private float step_scale;
    private float step_animation_x;
    private float step_animation_y;

    private long delayHide = 0, step3_end;
    private int current_step = 1;
    private int index_draw_step_1 = 0;
    private int index_draw_step_2 = 0;
    private Paint paint_drawBitmap  = null;
    private Bitmap step_1_img;
    private Bitmap tmp_bmp;
    private int res_img_step_2;
    private Resources res;
    private float alpha = 255.0f;
    private float step_alpha;

    public long timestampStart = 0;

    public AnimationFireworks(
              Context ctx
            , float start_x
            , float start_y
            , float count_steps_step_1
            , float pos_x_start_fire
            , float pos_y_start_fire
            , float count_steps_step_2
            , float scale_start_fire
            , float scale_end_fire
            , long _timestampStart
            , long _delayHide
            )
    {
        this.start_x = start_x;
        this.start_y = start_y;
        this.count_steps_step_1  = count_steps_step_1;
        this.pos_x_start_fire    = pos_x_start_fire;
        this.pos_y_start_fire    = pos_y_start_fire;
        this.count_steps_step_2  = count_steps_step_2;
        this.scale_start_fire    = scale_start_fire;
        this.scale_end_fire      = scale_end_fire;
        this.delayHide           = _delayHide;

        float _dx = pos_x_start_fire - start_x;
        float _dy = pos_y_start_fire - start_y;

        double _dd = RoomSurfaceView.distance_point( start_x, start_y, pos_x_start_fire, pos_y_start_fire );

        _dx /= _dd;
        _dy /= _dd;

        _dx *= _dd / (double) count_steps_step_1;
        _dy *= _dd / (double) count_steps_step_1;

        step_scale = ( scale_end_fire - scale_start_fire ) / (float) count_steps_step_2;

        step_animation_x = _dx;
        step_animation_y = _dy;

        timestampStart = System.currentTimeMillis() + _timestampStart;

        paint_drawBitmap = new Paint();
        //paint_drawBitmap.setFilterBitmap(true);

        int[] t1 = {
                      R.drawable.star_1
                    , R.drawable.star_2
                    , R.drawable.star_3
                    , R.drawable.star_4
                    , R.drawable.star_5
                    , R.drawable.star_6
        };

        int[] t2 = {
                      R.drawable.fireworks_1
                    , R.drawable.fireworks_2
                    , R.drawable.fireworks_3
                    , R.drawable.fireworks_4
                    , R.drawable.fireworks_5
        };

        res = ctx.getResources();

        step_alpha = 15.0f * 255.0f / (float) delayHide;

        step_1_img = BitmapFactory.decodeResource( res, t1[ (new Random()).nextInt(t1.length) ] );
        res_img_step_2 = t2[ (new Random()).nextInt(t2.length) ];
    }

    public boolean step()
    {
        if( current_step == 1 && index_draw_step_1 < count_steps_step_1 )
        {
            index_draw_step_1 += 1;
            start_x += step_animation_x;
            start_y += step_animation_y;

            if( index_draw_step_1 == count_steps_step_1 - 1 )
            {
                current_step = 2;
            }
        }
        else if( current_step == 2 && index_draw_step_2 < count_steps_step_2 )
        {
            index_draw_step_2 += 1;

            scale_start_fire += step_scale;

            tmp_bmp = BitmapFactory.decodeResource( res, res_img_step_2 );

            tmp_bmp = tmp_bmp.createScaledBitmap(
                       tmp_bmp
                    , (int) (tmp_bmp.getWidth() * scale_start_fire)
                    , (int) (tmp_bmp.getHeight() * scale_start_fire)
                    , false
            );

            if( index_draw_step_2 == count_steps_step_2 - 1 )
            {
                step3_end = System.currentTimeMillis() + delayHide;
                current_step = 3;

            }
        }
        else if( current_step == 3 && System.currentTimeMillis() < step3_end )
        {
            alpha -= step_alpha;

            if( alpha < 0 ) { alpha = 0; }

            paint_drawBitmap.setAlpha((int) alpha);
        }
        else
        {
            return false;
        }

        return true;
    }

    public void draw(Canvas canvas)
    {
        if( current_step == 1 )
        {
            canvas.drawBitmap( step_1_img, start_x, start_y, paint_drawBitmap );
        }
        else if( current_step == 2 && tmp_bmp != null )
        {
            canvas.drawBitmap( tmp_bmp, start_x - tmp_bmp.getWidth() / 2, start_y - tmp_bmp.getHeight() / 2, paint_drawBitmap );
        }
        else if( current_step == 3 && tmp_bmp != null )
        {
            canvas.drawBitmap( tmp_bmp, start_x - tmp_bmp.getWidth() / 2, start_y - tmp_bmp.getHeight() / 2, paint_drawBitmap );
        }
    }
}
