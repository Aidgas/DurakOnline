package durakonline.sk.durakonline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	private DrawManagerBk drawLoopThread = null;
	private SurfaceHolder holder;
    private boolean _stop_draw           = false;
    private boolean is_first_draw        = true;


    private int STEP_SIZE_W, STEP_SIZE_H;

    Bitmap card_bk_SPADE    = null;
    Bitmap card_bk_CLUB     = null;
    Bitmap card_bk_HEART    = null;
    Bitmap card_bk_DIAMOND  = null;

    Paint paint_drawBitmap  = null;
    int bk_color;

    boolean init = false;
    int canvas_w, canvas_h;
    int start_offset_y_draw = 25;

    private class DrawLineInfo
    {
        int offset;
        int index_start_1;
        int index_start_2;
        int step;
    }

    private List<DrawLineInfo> _lines_draw = new ArrayList<>();

    private Bitmap getIconByIndex(int index)
    {
        switch (index)
        {
            case 0:  return card_bk_CLUB;
            case 1:  return card_bk_SPADE;
            case 2:  return card_bk_DIAMOND;
            default: return card_bk_HEART;
        }
    }

	public MySurfaceView(Context context)
	{
		super(context);

        holder = getHolder();
        holder.addCallback(this);


        float scale_pr = 90f;

        STEP_SIZE_W = (int)(50.0f * ((MainActivity)context).get_scale_px());
        STEP_SIZE_H = (int)(150.0f * ((MainActivity)context).get_scale_px());

        bk_color = Color.parseColor("#00a2d3");

        paint_drawBitmap = new Paint();
        paint_drawBitmap.setAntiAlias(true);
        //paint_drawBitmap.setFilterBitmap(true);
        //paint_drawBitmap.setDither(true);

        /*Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bk_spade);
        card_bk_SPADE = bmp.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bk_club);
        card_bk_CLUB = bmp.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bk_heart);
        card_bk_HEART = bmp.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bk_diamond);
        card_bk_DIAMOND = bmp.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale_pr / 100f), (int) (bmp.getHeight() * scale_pr / 100f), true);
*/

        this.drawLoopThread = new DrawManagerBk(this);
	}

	@Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
          this._stop_thread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this._run_thread();
    }
    
    @Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
    {
        this.is_first_draw = true;
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
            this.drawLoopThread = new DrawManagerBk(this);
            this.drawLoopThread.setRunning(true);
            this.drawLoopThread.start();
        }
        else if(this.drawLoopThread.getState() != Thread.State.RUNNABLE)
        {
            this.drawLoopThread.start();
        }
    }
    //=========================================================================================================

    public void OnDestroy()
    {
    	this.drawLoopThread.setRunning(false);
    }


    public static int randInt(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (this._stop_draw)
        {
            return;
        }

        super.onDraw(canvas);

        if (this.is_first_draw)
        {
            this.is_first_draw = false;

            canvas_w = getWidth();
            canvas_h = getHeight();

            STEP_SIZE_H = canvas_h / 4;

            //while( STEP_SIZE_H -10 < card_bk_SPADE.getHeight() )
            while( STEP_SIZE_H -10 < 100 )
            {
                STEP_SIZE_H += 1;
            }

            int height_draw = 0;

            for(int j = 0; j < canvas_h; j += STEP_SIZE_H)
            {
                height_draw += STEP_SIZE_H;
            }

            start_offset_y_draw = STEP_SIZE_H/4;

            for(int j = start_offset_y_draw, start_index = 0; j < canvas_h; j += STEP_SIZE_H, start_index+= 1)
            {
                DrawLineInfo t = new DrawLineInfo();

                t.offset        = MySurfaceView.randInt(0, 200);
                t.index_start_1 = MySurfaceView.randInt(0, 3);
                t.index_start_2 = MySurfaceView.randInt(0, 3);
                t.step          = MySurfaceView.randInt(-1, 1);

                while(t.step == 0)
                {
                    t.step          = MySurfaceView.randInt(-1, 1);
                }

                _lines_draw.add(t);
            }

        } /// - first draw


        canvas.drawColor(bk_color);
        
        for(int j = start_offset_y_draw, start_index = 0, f = 0; j < canvas_h; j += STEP_SIZE_H, start_index+= 1, f++)
        {
            for (int i = _lines_draw.get(f).offset, k = _lines_draw.get(f).index_start_1; i < canvas_w; i += STEP_SIZE_W)
            {
                canvas.drawBitmap(getIconByIndex(k), i, j, paint_drawBitmap);
                k += 1;

                if (k > 3) {
                    k = 0;
                }
            }

            for (int i = _lines_draw.get(f).offset - STEP_SIZE_W, k = _lines_draw.get(f).index_start_2; i > -STEP_SIZE_W; i -= STEP_SIZE_W)
            {
                canvas.drawBitmap(getIconByIndex(k), i, j, paint_drawBitmap);
                k -= 1;

                if (k < 0) {
                    k = 3;
                }
            }

            if( start_index > 3)
            {
                start_index = 0;
            }

            _lines_draw.get(f).offset += _lines_draw.get(f).step;
        }

        /*for(int j = 5 + STEP_SIZE_H / 2; j < canvas_h; j += STEP_SIZE_H)
        {
            for (int i = offset_2, k = 0; i < canvas_w; i += STEP_SIZE_W)
            {
                canvas.drawBitmap(getIconByIndex(k), i, j, paint_drawBitmap);
                k += 1;

                if (k > 3) {
                    k = 0;
                }
            }

            for (int i = offset_2 - STEP_SIZE_W, k = 3; i > -STEP_SIZE_W; i -= STEP_SIZE_W)
            {
                canvas.drawBitmap(getIconByIndex(k), i, j, paint_drawBitmap);
                k -= 1;

                if (k < 0) {
                    k = 3;
                }
            }
        }

        offset_2 -= 2;*/

    }

    /////////////////////////////////////////////////////////////////////////////////
    /// CALLBACKS ///
    
}
