package durakonline.sk.durakonline;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class AminatedImageSmile
{
    private final float COUNT_STEPS = 5.0f;

    private Resources res;

    private float center_pos_x, center_pos_y, wait_before_hide;
    private float scale_end;

    private float _one_step = 0, _now_scale = 1.0f;

    private int counter_step = 0, img_res;

    private  RectF r;
    private long last_step = System.currentTimeMillis();

    private boolean end_animation_1 = false;
    private Bitmap tmp_bmp;
    private Paint paint;

    public AminatedImageSmile(
              Resources _res
            , float _center_pos_x
            , float _center_pos_y
            , int _wait_before_hide
            , float _scale_end
            , int _img_res
        )
    {
        center_pos_x = _center_pos_x;
        center_pos_y = _center_pos_y;
        wait_before_hide = _wait_before_hide;
        scale_end        = _scale_end;
        img_res          = _img_res;
        res              = _res;

        paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setFilterBitmap(true);

        float diff_scale = scale_end - _now_scale;

        _one_step = diff_scale / COUNT_STEPS;
    }

    private void get_img()
    {
        tmp_bmp = BitmapFactory.decodeResource( res, img_res );

        tmp_bmp = Bitmap.createScaledBitmap( tmp_bmp
                , (int) (tmp_bmp.getWidth() * _now_scale)
                , (int) (tmp_bmp.getHeight() * _now_scale), true);
    }

    public boolean step()
    {
        if( ! end_animation_1 && System.currentTimeMillis() - last_step > 20 )
        {
            last_step = System.currentTimeMillis();

            _now_scale += _one_step;

            counter_step += 1;

            if (counter_step >= COUNT_STEPS)
            {
                end_animation_1 = true;
                return false;
            }
        }

        get_img();

        if( System.currentTimeMillis() - last_step > wait_before_hide )
        {
            return true;
        }

        return false;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(
                       tmp_bmp
                , center_pos_x - tmp_bmp.getWidth() / 2
                , center_pos_y - tmp_bmp.getHeight() / 2
                , paint
        );
    }
}
