package durakonline.sk.durakonline;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class AnimationNumber
{
    private float COUNT_STEPS = 5.0f;

    private Rect text_bounds = new Rect();

    private boolean end_animation_1 = false;
    private boolean added_num = false;

    private Typeface font;
    private Paint text_p;
    private int current_num;
    private int end_num;
    private int step;
    private String str_current_num;

    private float text_size, scale_end;

    private float _one_step = 0, _now_scale = 1.0f;

    private float center_pos_x, center_pos_y, wait_before_hide;
    private long last_step = System.currentTimeMillis();
    private long last_step2 = System.currentTimeMillis();
    private float _one_step_font_size;

    private int counter_step = 0;
    private long wait_step = 0;
    private long wait_step2 = 0;
    public long timestampStart;

    private boolean direction_up;
    private boolean end_inc_dec = false;
    private String before_text;
    private String after_text;

    public AnimationNumber(
                                      Typeface _font
                                    , int _start_num
                                    , int _end_num
                                    , int _step
                                    , float _center_pos_x
                                    , float _center_pos_y
                                    , float _text_size
                                    , int _wait_before_hide
                                    , float _scale_end
                                    , int color
                                    , long wait_step
                                    , long wait_step2
                                    , int count_steps_scale
                                    , long _timestampStart
                                    , String before_text
                                    , String after_text
                                )
    {
        timestampStart = System.currentTimeMillis() + _timestampStart;
        COUNT_STEPS    = count_steps_scale;
        font           = _font;
        current_num    = _start_num;
        end_num        = _end_num;
        step           = _step;

        this.before_text = before_text;
        this.after_text  = after_text;

        direction_up = _start_num < end_num;

        str_current_num = before_text + String.valueOf( current_num ) + after_text;

        this.wait_step = wait_step;
        this.wait_step2 = wait_step2;

        text_p = new Paint();
        text_p.setTypeface(font);
        text_p.setTextSize(_text_size);
        text_p.setColor(color);
        text_p.getTextBounds( str_current_num, 0, str_current_num.length(), text_bounds);

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
        if( System.currentTimeMillis() - last_step2 > wait_step2 )
        {
            if( ! end_inc_dec )
            {
                last_step2 = System.currentTimeMillis();

                if (
                        (direction_up && current_num + step < end_num)
                                || (!direction_up && current_num + step > end_num)
                        )
                {
                    added_num = true;
                    current_num += step;
                    str_current_num = before_text + String.valueOf(current_num) + after_text;
                    text_p.getTextBounds(str_current_num, 0, str_current_num.length(), text_bounds);
                }
                else
                {
                    added_num = false;
                    current_num = end_num;
                    str_current_num = before_text + String.valueOf(current_num) + after_text;
                    end_inc_dec = true;
                    text_p.getTextBounds(str_current_num, 0, str_current_num.length(), text_bounds);
                }
            }
        }

        if(    ! end_animation_1  && System.currentTimeMillis() - last_step > wait_step )
        {
            if( ! end_animation_1 ) {
                last_step = System.currentTimeMillis();

                _now_scale += _one_step;
                text_size += _one_step_font_size;

                text_p.setTextSize(text_size);
                text_p.getTextBounds(str_current_num, 0, str_current_num.length(), text_bounds);

                counter_step += 1;
            }

            if (counter_step >= COUNT_STEPS)
            {
                end_animation_1 = true;
                return false;
            }
        }

        if(      end_inc_dec
              && System.currentTimeMillis() - last_step > wait_before_hide
              && System.currentTimeMillis() - last_step2 > wait_before_hide
          )
        {
            return true;
        }

        return false;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawText(
                     str_current_num
                , center_pos_x - text_bounds.width() / 2
                , center_pos_y + text_bounds.height() / 2
                , text_p);
    }
}
