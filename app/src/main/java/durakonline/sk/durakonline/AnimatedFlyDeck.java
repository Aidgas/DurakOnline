package durakonline.sk.durakonline;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class AnimatedFlyDeck
{
    private final int COUNT_STEPS = 6;

    //public int /*card,*/ img_res;
    private Resources res;
    public Bitmap img, tmp_bmp;

    private float scale = 1.0f;
    private float step_scale = 0;

    public double
              center_x
            , center_y
            , end_center_x
            , end_center_y;

    private float step_animation_x = 0;
    private float step_animation_y = 0;
    private Matrix rotate_matrix = null;
    private Paint paint_drawBitmap  = null;

    private int index_draw_step = 0;
    private float rotate_angle_default = 7.0f;
    public long timestampStart = 0;

    public AnimatedFlyDeck(
              Resources _res
            //, int _card
            , int _img_res
            , double _center_x
            , double _center_y
            , double _end_center_x
            , double _end_center_y
            , float start_scale
            , float end_scale
            , long _timestampStart
    )
    {
        //card      = _card;
        //img_res   = _img_res;
        res       = _res;

        scale = start_scale;

        center_x  = _center_x;
        center_y  = _center_y;

        end_center_x = _end_center_x;
        end_center_y = _end_center_y;

        double _dx = end_center_x - center_x;
        double _dy = end_center_y - center_y;

        double _dd = RoomSurfaceView.distance_point( center_x, center_y, end_center_x, end_center_y );

        _dx /= _dd;
        _dy /= _dd;

        _dx *= _dd / (double) COUNT_STEPS;
        _dy *= _dd / (double) COUNT_STEPS;

        step_scale = ( end_scale - start_scale ) / (float) COUNT_STEPS;

        step_animation_x += _dx;
        step_animation_y += _dy;

        tmp_bmp = BitmapFactory.decodeResource( res, _img_res );
        img = get_img();

        index_draw_step = 0;

        rotate_matrix = new Matrix();
        update_matrix();

        timestampStart = _timestampStart;

        paint_drawBitmap = new Paint();
        paint_drawBitmap.setAntiAlias(true);
        //paint_drawBitmap.setFilterBitmap(true);
        //paint_drawBitmap.setDither(true);
    }

    public boolean step()
    {
        if( index_draw_step < COUNT_STEPS )
        {
            index_draw_step += 1;
            center_x += step_animation_x;
            center_y += step_animation_y;

            rotate_angle_default += 25.0f;

            scale += step_scale;

            update_matrix();
            img = get_img();

            return false;
        }

        return true;
    }

    private void update_matrix()
    {
        //rotate_matrix = new Matrix();
        rotate_matrix.reset();
        rotate_matrix.setRotate(rotate_angle_default, img.getWidth()/2, img.getHeight()/2);
        rotate_matrix.postTranslate((float)center_x, (float)center_y);
    }

    private Bitmap get_img()
    {
        return Bitmap.createScaledBitmap( tmp_bmp
                , (int) (tmp_bmp.getWidth() * scale)
                , (int) (tmp_bmp.getHeight() * scale), false);
    }

    public void draw(Canvas canvas)
    {
        //canvas.drawBitmap(img, rotate_matrix, paint_drawBitmap);
        canvas.drawBitmap(img, rotate_matrix, null);
    }


}
