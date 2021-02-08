package durakonline.sk.durakonline;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;

public class DrawTextRect
{
    private String text;
    public float rect_w, rect_h;
    private Rect text_bounds = new Rect();
    private Typeface font;
    private Paint text_p, rectagle_p1, rectagle_p2;
    private  RectF r;
    private Paint p1;
    private Paint p2;

    public DrawTextRect(
              Typeface _font
            , String _text
            , float _text_size
            , String text_color
            , String color_fill
            , String color_stroke
            , int stroke_width
            , float padding_h
            , float padding_v
    )
    {
        font = _font;
        text = _text;

        text_p = new Paint();
        text_p.setTypeface(font);
        text_p.setTextSize(_text_size);

        text_p.getTextBounds(text, 0, text.length(), text_bounds);
        text_p.setColor(Color.parseColor(text_color));

        rect_w = text_bounds.width() + padding_h / 2;
        rect_h = text_bounds.height() + padding_v / 2;

        rectagle_p1 = new Paint();
        rectagle_p1.setStyle(Paint.Style.FILL);
        rectagle_p1.setColor( Color.parseColor(color_fill) );

        rectagle_p2 = new Paint();
        rectagle_p2.setStyle(Paint.Style.STROKE);
        rectagle_p2.setColor( Color.parseColor(color_stroke) );
        rectagle_p2.setStrokeWidth(stroke_width);

        p1 = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        p1.setColor(0xff800000);
        p1.setShader(new LinearGradient(0,0,0, rect_h,
                new int[]
                        {
                                Color.parseColor("#e7f3fc"),
                                Color.parseColor("#dceefc"),
                                Color.parseColor("#e9f5ff"),
                                Color.parseColor("#e0ebf3"),
                        },
                new float[] { 0, 0.35f, 0.55f, 1 },
                Shader.TileMode.REPEAT));

        p2 = new Paint();
        p2.setStyle(Paint.Style.STROKE);
        p2.setStrokeCap(Paint.Cap.ROUND);
        p2.setStrokeWidth(2);
        p2.setAntiAlias(true);
        p2.setColor(Color.parseColor("#d4d4d4"));
    }

    public void draw(float x, float y, Canvas canvas)
    {
        r = new RectF(
                x - rect_w / 2
                , y - rect_h / 2
                , x + rect_w / 2
                , y + rect_h / 2
        );

        //canvas.drawRoundRect(r, 7.0f, 7.0f, rectagle_p1);    // fill
        //canvas.drawRoundRect(r, 7.0f, 7.0f, rectagle_p2);  // stroke

        float R = 20, offset_x = r.left, offset_y = r.top;
        float w = rect_w
                , h = rect_h
                , w20 = rect_w * 15.0f / 100.0f
                , w10 = rect_w * 10.0f / 100.0f;

        {
            Path pth = new Path();
            pth.moveTo(offset_x, offset_y + R);

            pth.arcTo(new RectF(offset_x, offset_y, offset_x + R, offset_y + R), 180, 90);
            pth.lineTo(offset_x + w - R, offset_y);

            pth.arcTo(new RectF(offset_x + w - R, offset_y, offset_x + w, offset_y + R), 270, 90);
            pth.lineTo(offset_x + w, offset_y + h- R);

            pth.arcTo(new RectF(offset_x + w - R, offset_y + h- R, offset_x + w, offset_y + h), 0, 90);

            pth.lineTo(offset_x + R + w20, offset_y + h);
            pth.lineTo(offset_x + R + w20 - w10, offset_y + h + w10);
            pth.lineTo(offset_x + R + w20 - w10, offset_y + h);

            pth.lineTo(offset_x + R, offset_y + h);

            pth.arcTo(new RectF(offset_x, offset_y + h - R, offset_x + R, offset_y + h), 90, 90);
            pth.lineTo(offset_x, offset_y + R);


            canvas.drawPath(pth,p1);

            /*p = new Paint();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(2);
            p.setAntiAlias(true);
            p.setColor(Color.parseColor("#d4d4d4"));*/

            canvas.drawPath(pth, p2);
        }

        canvas.drawText(
                text
                , x - text_bounds.width() / 2
                , y + text_bounds.height() / 2
                , text_p);
    }
}
