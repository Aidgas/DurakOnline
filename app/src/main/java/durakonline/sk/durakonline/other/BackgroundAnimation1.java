package durakonline.sk.durakonline.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import durakonline.sk.durakonline.R;

public class BackgroundAnimation1 extends SurfaceView implements SurfaceHolder.Callback
{
    private static final int PADDING_CELL_W = 1;
    private static final int PADDING_CELL_H = 1;

    private Bitmap _bk              = null;
    private boolean is_first_draw   = true;
    public int canvas_w, canvas_h;
    public int bk_w, bk_h;
    Paint paint_bk  = new Paint();

    private int offset_x = 0;
    private int offset_y = 0;

    private boolean _stop_draw     = false;
    private DrawManager2 drawLoopThread = null;
    private long last_draw;

    private SurfaceHolder holder;
    public BackgroundAnimation1(Context context)
    {
        super(context);

        holder = getHolder();
        holder.addCallback(this);

        this.drawLoopThread = new DrawManager2(this);
    }

    public BackgroundAnimation1(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);

        this.drawLoopThread = new DrawManager2(this);
    }

    public BackgroundAnimation1(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        holder = getHolder();
        holder.addCallback(this);

        this.drawLoopThread = new DrawManager2(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        this._stop_thread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        this._run_thread();
    }

    //**********************************************************************************************

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        canvas_w = getWidth();
        canvas_h = getHeight();


    }

    private void _stop_thread()
    {
        boolean retry = true;
        this._stop_draw = true;
        this.drawLoopThread.setRunning(false);
        while (retry)
        {
            try
            {
                this.drawLoopThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            { }
        }
    }

    private void _run_thread()
    {
        this._stop_draw = false;
        this.drawLoopThread.setRunning(true);

        if(this.drawLoopThread.getState() == Thread.State.TERMINATED)
        {
            this.drawLoopThread = new DrawManager2(this);
            this.drawLoopThread.setRunning(true);
            this.drawLoopThread.start();
        }
        else if(this.drawLoopThread.getState() != Thread.State.RUNNABLE)
        {
            this.drawLoopThread.start();
        }
    }

    public void OnDestroy()
    {
        this.drawLoopThread.setRunning(false);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if (this.is_first_draw)
        {
            is_first_draw = false;

            _bk = BitmapFactory.decodeResource(getResources(), R.drawable.bk_88);

            bk_w = _bk.getWidth();
            bk_h = _bk.getHeight();

            canvas_w = getWidth();
            canvas_h = getHeight();

            //paint_bk.setColor(Color.parseColor("#F1F1F1"));
            paint_bk.setColor(Color.parseColor("#414141"));

            last_draw = System.currentTimeMillis();
        }

        if(canvas == null)
        {
            return;
        }

        if( System.currentTimeMillis() - last_draw > 20 )
        {
            last_draw = System.currentTimeMillis();

            offset_x += 1;
            offset_y += 1;

            while (offset_x > bk_w)
            {
                offset_x -= bk_w;
            }

            while (offset_y > bk_h)
            {
                offset_y -= bk_h;
            }
        }



        canvas.drawRect(0, 0, canvas_w, canvas_h, paint_bk);

        for(int i = offset_x - (bk_w + PADDING_CELL_W); i < canvas_w; i += bk_w + PADDING_CELL_W)
        {
            for(int j = offset_y - (bk_h + PADDING_CELL_H); j < canvas_h; j += bk_h + PADDING_CELL_H)
            {
                canvas.drawBitmap(_bk, i, j, null);
            }
        }
    }
}
