package durakonline.sk.durakonline;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by sk on 01.10.17.
 */

public class AnimatedCard
{
    public int card;//, img_res;
    public double
              center_x
            , center_y
            , end_center_x
            , end_center_y
            , rotate_angle
            , rotate_angle_x
            , rotate_angle_y
            , scale
            ;

    public boolean up;
    public boolean need_delete, play_sound_thrown;

    public double offset_move_x, offset_move_y;

    private Matrix matrix = new Matrix();
    private Paint paint_drawBitmap  = null;

    private double offset_x;
    private double offset_y;
    private double scale_img_start, scale_max_up, final_scale_dow;

    private double step_angle, step_offset_x, step_offset_y, step_scale_up, step_scale_down;
    private Resources res;

    private int count_step_up;
    private int count_step_down;

    private float step_move_down_animation_x = 0;
    private float step_move_down_animation_y = 0;
    private boolean calc_scale_up   = true;
    private boolean calc_scale_down = true;
    private Bitmap tmp_bmp, img;

    public AnimatedCard(
              Resources _res
            , int _card
            , double _center_x
            , double _center_y
            , double _end_center_x
            , double _end_center_y
            , double _rotate_angle
            , double _rotate_angle_x
            , double _rotate_angle_y
            , boolean _up
            , int _img_res
            , float _scale_img_start
            , float _scale_max_up
            , int _count_step_up
            , float _final_scale_dow
            , int _count_step_down
    )
    {
        card   = _card;
        scale  = _scale_img_start;

        scale_img_start  = _scale_img_start;
        scale_max_up     = _scale_max_up;
        final_scale_dow = _final_scale_dow;
        count_step_up    = _count_step_up;
        count_step_down  = _count_step_down;

        res = _res;

        offset_move_x = 0;
        offset_move_y = 0;

        offset_x = 0;
        offset_y = 0;

        center_x = _center_x;
        center_y = _center_y;

        end_center_x = _end_center_x;
        end_center_y = _end_center_y;

        rotate_angle   = _rotate_angle;
        rotate_angle_x = _rotate_angle_x;
        rotate_angle_y = _rotate_angle_y;

        double d1  = RoomSurfaceView.distance_point( center_x, center_y, end_center_x, end_center_y );
        double d   = ( d1 * Math.sin( Math.toRadians( Math.abs(rotate_angle) ) ) ) / 2;
        double d2  = d1 * Math.cos( Math.toRadians( Math.abs(rotate_angle) ) );

        step_angle     = -1 * ( rotate_angle / 3 );
        step_offset_x  = d / 3;
        step_offset_y  = ( d1 - d2 ) / 3;


        if( rotate_angle < 0)
        {
            step_offset_x *= -1;
        }

        need_delete  = false;
        up           = _up;
        tmp_bmp = BitmapFactory.decodeResource( res, _img_res );
        //img_res      = _img_res;

        img = get_img();

        //origin_width_img  = img.getWidth();
        //origin_height_img = img.getHeight();

        update_matrix();

        //paint_drawBitmap = new Paint();
        //paint_drawBitmap.setAntiAlias(true);
        //paint_drawBitmap.setFilterBitmap(true);
        //paint_drawBitmap.setDither(true);
    }

    public void setUp(boolean v)
    {
        up = v;
        calc_scale_up = true;
        calc_scale_down = true;
    }

    private Bitmap get_img()
    {
        return Bitmap.createScaledBitmap( tmp_bmp
                , (int) (tmp_bmp.getWidth() * (scale) / 100f)
                , (int) (tmp_bmp.getHeight() * (scale) / 100f), false);
    }

    private void update_matrix()
    {
        matrix.reset();
        matrix.setRotate( (float) rotate_angle, (float) rotate_angle_x, (float) rotate_angle_y );
        matrix.postTranslate( (float) (center_x + offset_x + offset_move_x - img.getWidth() / 2)
                , (float) (center_y + offset_y + offset_move_y - img.getHeight() ) );
    }

    public void setOffsetMove(double _offset_move_x, double _offset_move_y)
    {
        offset_move_x = _offset_move_x;
        offset_move_y = _offset_move_y;

        update_matrix();
    }

    public boolean stepUp()
    {
        if( calc_scale_up )
        {
            step_scale_up  = ( scale_max_up - scale ) / count_step_up;
            calc_scale_up = false;
        }

        if( rotate_angle < 0 )
        {
            rotate_angle += step_angle;

            if( rotate_angle > 0 )
            {
                rotate_angle = 0;
            }
            else
            {
                offset_x += step_offset_x;
                offset_y -= step_offset_y;

                scale += step_scale_up;
                img = get_img();
            }
        }
        else if( rotate_angle > 0 )
        {
            rotate_angle += step_angle;

            if( rotate_angle < 0 )
            {
                rotate_angle = 0;
            }
            else
            {
                offset_x += step_offset_x;
                offset_y -= step_offset_y;

                scale += step_scale_up;
                img = get_img();
            }
        }
        else if( scale < scale_max_up )
        {
            scale += step_scale_up;
            img = get_img();
        }

        /*matrix.setRotate( (float) rotate_angle, (float) rotate_angle_x, (float) rotate_angle_y );
        matrix.postTranslate(
                  (float) (center_x + offset_x - img.getWidth() / 2)
                , (float) (center_y + offset_y - img.getHeight() ) );*/

        update_matrix();

        return false;
    }

    public boolean stepDown()
    {
        if( calc_scale_down )
        {
            step_scale_down  = ( final_scale_dow - scale ) / count_step_down;
            calc_scale_down = false;
        }

        if( scale == final_scale_dow )
        {
            //Log.i("TAG", "need_delete = true");
            need_delete = true;
            return true;
        }
        else
        {
            scale += step_scale_down;

            //Log.i("TAG", "scale: " + String.valueOf(scale) + " " + String.valueOf(step_scale) );

            if(    step_move_down_animation_x != 0
                || step_move_down_animation_y != 0  )
            {
                offset_move_x += step_move_down_animation_x;
                offset_move_y += step_move_down_animation_y;
                update_matrix();
            }

            if(
                    ( step_scale_down < 0 && scale < final_scale_dow )
                 || ( step_scale_down > 0 && scale > final_scale_dow )
              )
            {
                scale = final_scale_dow;
            }
            else
            {
                img = get_img();
            }
        }

        return false;
    }

    public void setDestinationXYDownAnimation(double final_pos_x, double final_pos_y, double _final_scale_dow)
    {
        int count_steps = 0;
        double _scale = scale;

        final_scale_dow = _final_scale_dow;

        step_scale_down  = ( final_scale_dow - scale ) / count_step_down;

        while( ! (
                    ( step_scale_down < 0 && _scale < final_scale_dow )
                 || ( step_scale_down > 0 && _scale > final_scale_dow )
        ))
        {
            _scale       += step_scale_down;
            count_steps += 1;
        }

        if( count_steps == 0 )
        {
            return;
        }
        else if( count_steps < 2 )
        {
            count_steps = 2;
        }

        double __x = center_x + offset_x + offset_move_x;
        double __y = center_y + offset_y + offset_move_y;

        double _dx = final_pos_x - __x;
        double _dy = final_pos_y - __y;

        double _dd = RoomSurfaceView.distance_point( __x, __y, final_pos_x, final_pos_y );

        _dx /= _dd;
        _dy /= _dd;

        _dx *= _dd / (double) count_steps;
        _dy *= _dd / (double) count_steps;

        step_move_down_animation_x += _dx;
        step_move_down_animation_y += _dy;
    }

    public void draw(Canvas canvas)
    {
        //canvas.drawBitmap(img, matrix, paint_drawBitmap);
        canvas.drawBitmap(img, matrix, null);
    }
}
