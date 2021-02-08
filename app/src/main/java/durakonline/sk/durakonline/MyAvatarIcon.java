package durakonline.sk.durakonline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyAvatarIcon extends SurfaceView implements SurfaceHolder.Callback
{
    private int res_id;
    private Bitmap _img;
    private boolean first_draw = true, stop_draw = false;
    int canvas_w, canvas_h;
    int canvas_w2, canvas_h2;
    private float angle = 0;
    private Object obj = new Object();
    private RectF rectF;
    private Paint p;
    private SurfaceHolder holder;

    public MyAvatarIcon(Context context)
    {
        super(context);

        holder = getHolder();
        holder.addCallback(this);

        _img = BitmapFactory.decodeResource( context.getResources(), R.drawable.default_avatar);

    }
    public MyAvatarIcon(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);

        _img = BitmapFactory.decodeResource( context.getResources(), R.drawable.default_avatar);
    }
    public MyAvatarIcon(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        holder = getHolder();
        holder.addCallback(this);

        _img = BitmapFactory.decodeResource( context.getResources(), R.drawable.default_avatar);
    }

    public void setImg(Bitmap img)
    {
        synchronized (obj)
        {
            _img = img;

            first_draw = true;
        }
    }

    public void setAngle(float _angle)
    {
        angle = _angle;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if( stop_draw ) { return; }

        if( first_draw )
        {
            first_draw = false;

            canvas_w = getWidth();
            canvas_h = getHeight();

            canvas_w2 = canvas_w / 2;
            canvas_h2 = canvas_h / 2;

            _img = Bitmap.createScaledBitmap(
                      _img
                    , (int) (canvas_w - 20)
                    , (int) (canvas_h - 20)
                    , true);

            rectF = new RectF(0, 0, canvas_w, canvas_h);

            p = new Paint();
            p.setFilterBitmap(true);
            p.setColor(Color.parseColor("#ff0000"));
        }

        canvas.drawColor( Color.parseColor("#373737") );

        canvas.drawArc (rectF, 180, angle, true, p);

        /*canvas.drawBitmap(
                   _img
                , canvas_w2 - _img.getWidth() / 2
                , canvas_h2 - _img.getHeight() / 2
                , null
              );*/
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}