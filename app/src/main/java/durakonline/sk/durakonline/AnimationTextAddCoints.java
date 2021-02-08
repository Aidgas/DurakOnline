package durakonline.sk.durakonline;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class AnimationTextAddCoints
{
    private float COUNT_STEPS = 5.0f;

    private Rect text_bounds = new Rect();

    private boolean end_animation_1 = false;

    private Typeface font;
    private Paint text_p;
    private String text;

    private float text_size, scale_end;

    private float _one_step = 0, _now_scale = 1.0f;

    private float center_pos_x, center_pos_y, wait_before_hide;
    private long last_step = System.currentTimeMillis();
    private float _one_step_font_size;

    private int counter_step = 0;
    private long wait_step = 0;
    public long timestampStart;

    public AnimationTextAddCoints(
                                      Typeface _font
                                    , String _text
                                    , float _center_pos_x
                                    , float _center_pos_y
                                    , float _text_size
                                    , int _wait_before_hide
                                    , float _scale_end
                                    , int color
                                    , long wait_step
                                    , int count_steps
                                    , long _timestampStart
                                )
    {
        timestampStart = System.currentTimeMillis() + _timestampStart;
        COUNT_STEPS = count_steps;
        font = _font;
        text = _text;

        this.wait_step = wait_step;

        text_p = new Paint();
        text_p.setTypeface(font);
        text_p.setTextSize(_text_size);

        text_p.getTextBounds(text, 0, text.length(), text_bounds);
        text_p.setColor(color);

        center_pos_x = _center_pos_x;
        center_pos_y = _center_pos_y;

        text_size = _text_size;
        wait_before_hide = _wait_before_hide;
        scale_end = _scale_end;

        float diff_scale = scale_end - _now_scale;

        _one_step = diff_scale / COUNT_STEPS;

        if(_one_step != 0)
        {
            float t1             = text_size * scale_end / _now_scale;
            _one_step_font_size  = ( t1 - text_size ) / COUNT_STEPS;
        }
    }

    public boolean step()
    {
        if( ! end_animation_1 && System.currentTimeMillis() - last_step > wait_step )
        {
            last_step = System.currentTimeMillis();

            _now_scale += _one_step;
            text_size += _one_step_font_size;

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
        canvas.drawText(
                text
                , center_pos_x - text_bounds.width() / 2
                , center_pos_y + text_bounds.height() / 2
                , text_p);
    }
}
