package durakonline.sk.durakonline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FireworksSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	private DrawManagerVideoAdBefore drawLoopThread;
	private SurfaceHolder holder;
    public boolean _stop_draw            = false;
    private boolean is_first_draw        = true;

    Bitmap card_bk_SPADE    = null;
    Bitmap card_bk_CLUB     = null;
    Bitmap card_bk_HEART    = null;
    Bitmap card_bk_DIAMOND  = null;

    //Paint paint_drawBitmap  = null;
    int bk_color;

    int canvas_w, canvas_h;
    private Object lock_list_firewords = new Object();
    private List<AnimationFireworks> af = new ArrayList<>();

    public boolean eneble_sound   = true;
    public boolean eneble_vibrate = true;
    private boolean exec_callback_end = false;
    private long timebefore_callbackend = 0;

    MainActivity _ma;

    List<AnimationTextAddCoints> list_anumation_text_coints  = new ArrayList<>();
    Object lock_list_anumation_text_coints                   = new Object();

    private MediaPlayer firework;

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

	public FireworksSurfaceView(Context context)
	{
		super(context);
		_ma = (MainActivity)context;

        holder = getHolder();
        holder.addCallback(this);

        firework = MediaPlayer.create(context, R.raw.firework);
        firework.setVolume( 0.5f, 0.5f );

        //float scale_pr = 90f;

        bk_color = Color.parseColor("#373737");

        //paint_drawBitmap = new Paint();
        //paint_drawBitmap.setAntiAlias(true);
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


        this.drawLoopThread = new DrawManagerVideoAdBefore(this);
	}

	@Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if( firework != null )
        {
            firework.stop();
        }
        this._stop_thread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this._run_thread();
    }
    
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
            this.drawLoopThread = new DrawManagerVideoAdBefore(this);
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


    public void addNewFireWork(AnimationFireworks v)
    {
        synchronized (lock_list_firewords)
        {
            af.add(v);
        }
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

            if(_ma.getAppSettings().opt_sound_in_game)
            {
                firework.start();
            }

            callback_init();



        } /// - first draw


        canvas.drawColor(bk_color);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        synchronized (lock_list_firewords)
        {
            for (Iterator<AnimationFireworks> it = af.iterator(); it.hasNext(); )
            {
                AnimationFireworks item = it.next();

                if (item != null && System.currentTimeMillis() > item.timestampStart)
                {

                    if ( item.step() )
                    {
                        item.draw(canvas);
                    }
                    else
                    {
                        item.draw(canvas);
                        it.remove();
                        continue;
                    }
                }
            }
        }

        synchronized (lock_list_anumation_text_coints)
        {
            for(int i = 0, _len = list_anumation_text_coints.size(); i < _len; i++)
            {
                AnimationTextAddCoints item = list_anumation_text_coints.get(i);

                if(item == null)
                {
                    continue;
                }
                else if (item != null && System.currentTimeMillis() > item.timestampStart)
                {
                    if( ! item.step() )
                    {
                        item.draw(canvas);
                    }
                }
            }

            list_anumation_text_coints.removeAll(Collections.singleton(null));
        }

        if( ! exec_callback_end && af.size() == 0 )
        {
            if( timebefore_callbackend == 0 )
            {
                timebefore_callbackend = System.currentTimeMillis() + 600;
            }
            else if( System.currentTimeMillis() > timebefore_callbackend )
            {
                exec_callback_end = true;
                callback_end();
            }
        }

    }

    /////////////////////////////////////////////////////////////////////////////////
    /// CALLBACKS ///
    public void callback_init() {}
    public void callback_end() {}
}
