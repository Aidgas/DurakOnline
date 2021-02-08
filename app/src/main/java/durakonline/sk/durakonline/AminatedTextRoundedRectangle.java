package durakonline.sk.durakonline;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class AminatedTextRoundedRectangle
{
    private final float COUNT_STEPS = 6.0f;

    private String text;
    private float center_pos_x, center_pos_y, rect_w, rect_h, wait_before_hide;
    private float text_size, scale_end;

    private float _one_step = 0, _now_scale = 1.0f;
    private float _one_step_font_size;
    private float _one_step_size_w;
    private float _one_step_size_h;

    private int counter_step = 0;
    private Typeface font;
    private Paint text_p, rectagle_p1, rectagle_p2;
    private Rect text_bounds = new Rect();

    private  RectF r;
    private long last_step = System.currentTimeMillis();

    private boolean end_animation_1 = false;

    public AminatedTextRoundedRectangle(
              Typeface _font
            , String _text
            , float _center_pos_x
            , float _center_pos_y
            , float _text_size
            , int _wait_before_hide
            , float _scale_end
        )
    {
        font = _font;
        text = _text;

        text_p = new Paint();
        text_p.setTypeface(font);
        text_p.setTextSize(_text_size);

        text_p.getTextBounds(text, 0, text.length(), text_bounds);
        text_p.setColor(Color.WHITE);

        rectagle_p1 = new Paint();
        rectagle_p1.setStyle(Paint.Style.FILL);
        rectagle_p1.setColor( Color.parseColor("#fbc700") );

        rectagle_p2 = new Paint();
        rectagle_p2.setStyle(Paint.Style.STROKE);
        rectagle_p2.setColor( Color.parseColor("#999999") );
        rectagle_p2.setStrokeWidth(2);


        center_pos_x = _center_pos_x;
        center_pos_y = _center_pos_y;
        rect_w = text_bounds.width() + 30;
        rect_h = text_bounds.height() + 25;
        text_size = _text_size;
        wait_before_hide = _wait_before_hide;
        scale_end = _scale_end;

        r = new RectF(
                center_pos_x - rect_w / 2
                , center_pos_y - rect_h / 2
                , center_pos_x + rect_w / 2
                , center_pos_y + rect_h / 2
        );

        float diff_scale = scale_end - _now_scale;

        _one_step = diff_scale / COUNT_STEPS;

        if(_one_step != 0)
        {
            float t1             = text_size * scale_end / _now_scale;
            _one_step_font_size  = ( t1 - text_size ) / COUNT_STEPS;

            t1             = rect_w * scale_end / _now_scale;
            _one_step_size_w  = ( t1 - rect_w ) / COUNT_STEPS;

            t1             = rect_h * scale_end / _now_scale;
            _one_step_size_h  = ( t1 - rect_h ) / COUNT_STEPS;

        }
    }

    public boolean step()
    {
        if( ! end_animation_1 && System.currentTimeMillis() - last_step > 200 )
        {
            last_step = System.currentTimeMillis();

            _now_scale += _one_step;
            text_size += _one_step_font_size;
            rect_w += _one_step_size_w;
            rect_h += _one_step_size_h;

            r = new RectF(
                    center_pos_x - rect_w / 2
                    , center_pos_y - rect_h / 2
                    , center_pos_x + rect_w / 2
                    , center_pos_y + rect_h / 2
            );

            text_p.setTextSize(text_size);
            text_p.getTextBounds(text, 0, text.length(), text_bounds);

            counter_step += 1;

            if (counter_step >= COUNT_STEPS)
            {
                end_animation_1 = true;
                return false;
            }
        }

        if( System.currentTimeMillis() - last_step > wait_before_hide )
        {
            return true;
        }

        return false;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawRoundRect(r, 7.0f, 7.0f, rectagle_p1);    // fill
        canvas.drawRoundRect(r, 7.0f, 7.0f, rectagle_p2);  // stroke

        canvas.drawText(
                     text
                , center_pos_x - text_bounds.width() / 2
                , center_pos_y + text_bounds.height() / 2
                , text_p);
    }
}
