package durakonline.sk.durakonline.graphic_ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class GButton
{
    public Matrix btn_matrix = new Matrix();

    public RectF btn_rect;

    private float width;
    private float height;
    private Bitmap bg, bg_select;
    private int status_now = GUiStatus.STATUS_DEFAULT;
    private float margin_left = 0;
    private float margin_top = 0;
    private float margin_right = 0;
    private float margin_bottom = 0;
    private Object data;

    public GButton(Resources res, Object data, int btn_default, int btn_select)
    {
        bg         = BitmapFactory.decodeResource(res, btn_default);

        if( btn_select != 0 )
        {
            bg_select  = BitmapFactory.decodeResource(res, btn_select);
        }

        width  = bg.getWidth();
        height = bg.getHeight();

        this.data = data;

        btn_rect = new RectF(0, 0, width, height);
    }

    public void setStatus(int v)
    {
        status_now = v;
    }

    public int getStatus()
    {
        return status_now;
    }

    public void setMargin(float left, float top, float right, float bottom)
    {
        margin_left = left;
        margin_top = top;
        margin_right = right;
        margin_bottom = bottom;

        width  = bg.getWidth() + left + right;
        height = bg.getHeight() + top + bottom;

        btn_rect = new RectF(0, 0, width, height);
    }

    public float getW()
    {
        return width;
    }

    public float getH()
    {
        return height;
    }

    static public Bitmap generateButton(
              String text_btn
            , Typeface font
            , float font_size
            , float padding_left
            , float padding_top
            , float padding_right
            , float padding_bottom
            , int color_bk
            , int color_border
            , int color_text
            , float round_corner
            , float border_size
    )
    {
        Bitmap result;

        Paint paint = new Paint();
        paint.setTypeface(font);
        paint.setTextSize(font_size);
        paint.setColor(color_text);

        Rect bounds = new Rect();
        paint.getTextBounds(text_btn, 0, text_btn.length(), bounds);

        RectF rect = new RectF(
                border_size / 2
                , border_size / 2
                , bounds.width() + padding_left + padding_right + border_size * 2 - border_size / 2
                , bounds.height() + padding_top + padding_bottom + border_size * 2 - border_size / 2
        );

        RectF rect2 = new RectF(
                0
                , 0
                , bounds.width() + padding_left + padding_right + border_size * 2
                , bounds.height() + padding_top + padding_bottom + border_size * 2
        );

        result = Bitmap.createBitmap(
                  (int) Math.ceil( bounds.width() + padding_left + padding_right + border_size * 2 )
                , (int) Math.ceil( bounds.height() + padding_top + padding_bottom + border_size * 2 )
                , Bitmap.Config.ARGB_8888
        );

        Canvas _canvas = new Canvas(result);
        _canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);

        Paint paint1;


        paint1 = new Paint();
        paint1.setStyle( Paint.Style.FILL );
        paint1.setColor(color_bk);
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(0);
        paint1.setDither(true);

        _canvas.drawRoundRect(rect, round_corner, round_corner, paint1);

        paint1 = new Paint();
        paint1.setStyle( Paint.Style.STROKE );
        paint1.setStrokeWidth(border_size);
        paint1.setColor(color_border);
        paint1.setAntiAlias(true);
        paint1.setDither(true);

        _canvas.drawRoundRect(rect, round_corner, round_corner, paint1);

        _canvas.drawText(text_btn, padding_left + border_size, padding_top + bounds.height() + border_size, paint);

        return result;
    }

    public GButton(Object data, float width, float height, Bitmap bg, Bitmap bg_select)
    {
        this.width = width;
        this.height = height;
        this.bg = bg;
        this.bg_select = bg_select;
        this.data = data;

        btn_rect = new RectF(0, 0, width, height);
    }

    public GButton(Object data, Bitmap bg, Bitmap bg_select)
    {
        this.width = bg.getWidth();
        this.height = bg.getHeight();
        this.bg = bg;
        this.bg_select = bg_select;
        this.data = data;

        btn_rect = new RectF(0, 0, width, height);
    }

    public Object getData()
    {
        return data;
    }

    public void setPosition(float x, float y)
    {
        btn_matrix.setTranslate(x + margin_left, y + margin_top);
        btn_matrix.mapRect(btn_rect);
    }

    public void draw(Canvas canvas)
    {
        if( status_now == GUiStatus.STATUS_DEFAULT )
        {
            canvas.drawBitmap(bg, btn_matrix, null);
        }
        else if( bg_select != null && status_now == GUiStatus.STATUS_PRESSED )
        {
            canvas.drawBitmap(bg_select, btn_matrix, null);
        }
        else
        {
            canvas.drawBitmap(bg, btn_matrix, null);
        }
    }

    public void execPress()
    {
        callbackPressed();
    }

    public void callbackPressed() {}
}
