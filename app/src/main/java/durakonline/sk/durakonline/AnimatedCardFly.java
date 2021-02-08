package durakonline.sk.durakonline;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class AnimatedCardFly
{
    private final int COUNT_STEPS = 5;

    private Matrix matrix = new Matrix();
    //private Paint paint_drawBitmap;
    private double scale, pos_x, pos_y;
    //private double end_pos_x, end_pos_y;

    private int /*img_res,*/ card;

    private Resources res;

    private float step_move_up_animation_x   = 0;
    private float step_move_up_animation_y   = 0;

    private float step_move_down_animation_x = 0;
    private float step_move_down_animation_y = 0;

    private float step_scale_up   = 0;
    private float step_scale_down = 0;
    //private double destination_scale_up;
    //private double destination_scale_down;

    public boolean stop_up   = false;
    public boolean stop_down = false;

    private int count_exec_step_up    = 0;
    private int count_exec_step_down  = 0;

    //private Paint paint_drawLine;

    public int card_hovered = 0;
    private Bitmap tmp_bmp, img;

    public AnimatedCardFly(Resources _res, int _card, int _img_res, float _scale, float _pos_x, float _pos_y)
    {
        //paint_drawBitmap = new Paint();
        //paint_drawBitmap.setAntiAlias(true);
        //paint_drawBitmap.setFilterBitmap(true);
        //paint_drawBitmap.setDither(false);

        //paint_drawLine = new Paint();
        //paint_drawLine.setAntiAlias(true);
        //paint_drawLine.setFilterBitmap(true);
        //paint_drawLine.setDither(false);

        scale  = _scale;
        pos_x  = _pos_x;
        pos_y  = _pos_y;

        card = _card;

        res = _res;

        //img_res = _img_res;
        tmp_bmp = BitmapFactory.decodeResource( res, _img_res );
        img     = get_img();

        update_matrix();
    }

    public int getCard()
    {
        return card;
    }

    public void setDestinationXYUpDownAnimation(
              double destination_scale_up
            , double final_up_pos_x
            , double final_up_pos_y

            , double destination_scale_down
            , double final_down_pos_x
            , double final_down_pos_y
    )
    {
        int count_steps = COUNT_STEPS;
        double _scale = scale;

        //destination_scale_up   = destination_scale_up;
        //destination_scale_down = destination_scale_down;

        step_scale_up   = (float) (( destination_scale_up - _scale ) / (double) count_steps);
        step_scale_down = (float) (( destination_scale_down - destination_scale_up ) / (double) count_steps);

        double _dx = final_up_pos_x - pos_x;
        double _dy = final_up_pos_y - pos_y;

        double _dd = RoomSurfaceView.distance_point( pos_x, pos_y, final_up_pos_x, final_up_pos_y );

        _dx /= _dd;
        _dy /= _dd;

        _dx *= _dd / (double) count_steps;
        _dy *= _dd / (double) count_steps;

        step_move_up_animation_x += _dx;
        step_move_up_animation_y += _dy;

        //end_pos_x = final_down_pos_x;
        //end_pos_y = final_down_pos_y;

        _dx = final_down_pos_x - final_up_pos_x;
        _dy = final_down_pos_y - final_up_pos_y;

        _dd = RoomSurfaceView.distance_point( final_up_pos_x, final_up_pos_y, final_down_pos_x, final_down_pos_y );

        _dx /= _dd;
        _dy /= _dd;

        _dx *= _dd / (double) count_steps;
        _dy *= _dd / (double) count_steps;

        step_move_down_animation_x += _dx;
        step_move_down_animation_y += _dy;
    }

    public void stepDown()
    {
        if(    step_move_down_animation_x != 0
            || step_move_down_animation_y != 0  )
        {
            pos_x += step_move_down_animation_x;
            pos_y += step_move_down_animation_y;
            update_matrix();
        }

        scale += step_scale_down;

        /*if(
                   ( step_scale_up < 0 && scale < destination_scale_down )
                || ( step_scale_up > 0 && scale > destination_scale_down )
          )
        {
            scale = destination_scale_down;
        }*/

        img = get_img();

        count_exec_step_down += 1;

        if( count_exec_step_down >= COUNT_STEPS )
        {
            stop_down = true;
        }
    }

    public void stepUp()
    {
        if(    step_move_up_animation_x != 0
            || step_move_up_animation_y != 0  )
        {
            pos_x += step_move_up_animation_x;
            pos_y += step_move_up_animation_y;
            update_matrix();
        }

        scale += step_scale_up;

        /*if(
               ( step_scale_up < 0 && scale < destination_scale_up )
            || ( step_scale_up > 0 && scale > destination_scale_up )
          )
        {
            scale = destination_scale_up;
        }*/

        img = get_img();

        count_exec_step_up += 1;

        if( count_exec_step_up >= COUNT_STEPS )
        {
            stop_up = true;
        }
    }

    private void update_matrix()
    {
        matrix.reset();
        //matrix.setRotate( (float) rotate_angle, (float) rotate_angle_x, (float) rotate_angle_y );
        matrix.postTranslate(
                  (float) (pos_x - img.getWidth() / 2)
                , (float) (pos_y - img.getHeight() )
        );
    }

    private Bitmap get_img()
    {
        return Bitmap.createScaledBitmap( tmp_bmp
                , (int) (tmp_bmp.getWidth() * scale / 100f)
                , (int) (tmp_bmp.getHeight() * scale / 100f), false);
    }

    public void draw(Canvas canvas)
    {
        //canvas.drawBitmap(img, matrix, paint_drawBitmap);
        canvas.drawBitmap(img, matrix, null);

        //canvas.drawLine((float)pos_x, (float)pos_y, (float)end_pos_x, (float)end_pos_y, paint_drawLine);
    }
}
