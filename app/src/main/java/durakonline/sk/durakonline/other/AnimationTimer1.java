package durakonline.sk.durakonline.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class AnimationTimer1
{
    Bitmap timer, arrow;
    int timer_w, timer_h;

    float a = 0;
    Matrix matrix1 = null;
    Matrix matrix2 = null;
    Paint paint_drawBitmap;

    int timer_w2, timer_h2;
    int arrow_w2, arrow_h2;

    int last_pos_x  = 0;
    int last_pos_y  = 0;
    long step_tms   = 0;

    public AnimationTimer1(  Context c
                           , int img_timer
                           , int img_arrow
    )
    {
        timer  = BitmapFactory.decodeResource(c.getResources(), img_timer);
        arrow  = BitmapFactory.decodeResource(c.getResources(), img_arrow);

        timer_w = timer.getWidth();
        timer_h = timer.getHeight();

        paint_drawBitmap = new Paint();
        paint_drawBitmap.setAntiAlias(true);
        //paint_drawBitmap.setFilterBitmap(true);
        //paint_drawBitmap.setDither(true);

        timer_w2 = timer.getWidth() / 2;
        timer_h2 = timer.getHeight() / 2;

        arrow_w2 = arrow.getWidth() / 2;
        arrow_h2 = arrow.getHeight() / 2;
    }

    public void step()
    {
        if(System.currentTimeMillis() - step_tms > 100)
        {
            step_tms = System.currentTimeMillis();
            a += 2;
        }
    }

    public void Draw(Canvas canvas, int pos_x, int pos_y)
    {
        matrix2 = new Matrix();
        matrix2.setRotate(-10, timer_w2, timer_h2);
        matrix2.postTranslate(
                pos_x - timer_w2
                , pos_y - timer_h2
        );

        canvas.drawBitmap(timer, matrix2, paint_drawBitmap);

        matrix1 = new Matrix();
        matrix1.setRotate(a, arrow_w2, arrow_h2);
        matrix1.postTranslate(
                pos_x - arrow_w2
                , pos_y - arrow_h2
        );

        canvas.drawBitmap(arrow, matrix1, paint_drawBitmap);
    }
}
