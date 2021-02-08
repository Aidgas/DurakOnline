package durakonline.sk.durakonline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import durakonline.sk.durakonline.other.Log;

public class RuletSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int ORIENTATION_PORTRAIT      =  0x01;
    public static final int ORIENTATION_LANDSCAPE     =  0x02;

    //private final Typeface _fontApp_bold, _fontApp;
    public int canvas_w, canvas_h;
    public int canvas_w2, canvas_h2;

    public Bitmap rulet = null;
    public Bitmap rulet_arrow = null;
    public Bitmap rulet_arrow_selected = null;
    private boolean is_first_draw = true;
    public boolean _stop_draw = false;
    private boolean draw_selected_arrow = false;
    private int orietration_screen = -1;

    public Paint paint_drawBitmap = null;
    public Paint paint_drawFilterBitmap = null;

    private DrawManagerRulet drawLoopThread = null;
    private SurfaceHolder holder;

    private int bk_color = Color.parseColor("#333333");

    private Matrix matrix_rulet = new Matrix();
    private float tmp_angle_timer = 0;
    private long time_last_step = 0;
    private boolean work_rulet = true;

    private float time_stop_rotate = (float) (3000 + 50000 * Math.random());
    private long time_start_stop = 0;


    public boolean eneble_sound   = true;
    public boolean eneble_vibrate = true;

    private MainActivity _ma;
    private long TNOW = 0;

    public RuletSurfaceView(
              Context context
    )
    {
        super(context);
        _ma = (MainActivity)context;

        //_fontApp = Typeface.createFromAsset(((MainActivity) context).getAssets(), "fonts/Roboto-Regular.ttf");
        //_fontApp_bold = Typeface.createFromAsset(((MainActivity) context).getAssets(), "fonts/Monitorca-Bd.ttf");
        //_fontApp_bold2 = Typeface.createFromAsset(((MainActivity) context).getAssets(), "fonts/RobotoCondensed-Bold.ttf");

        holder = getHolder();
        holder.addCallback(this);

        paint_drawFilterBitmap = new Paint();
        paint_drawFilterBitmap.setAntiAlias(true);
        paint_drawFilterBitmap.setFilterBitmap(true);

        paint_drawBitmap = new Paint();
        paint_drawBitmap.setAntiAlias(true);
        //paint_drawBitmap.setFilterBitmap(true);
        //paint_drawBitmap.setDither(false);
        //paint_drawBitmap.setColor(Color.parseColor("#3dc12f"));

        rulet                 = BitmapFactory.decodeResource(getResources(), R.drawable.rulet);
        rulet_arrow           = BitmapFactory.decodeResource(getResources(), R.drawable.rulet_arrow);
        rulet_arrow_selected  = BitmapFactory.decodeResource(getResources(), R.drawable.rulet_arrow_select);

        this.drawLoopThread = new DrawManagerRulet(this);
    }

    public static int randInt(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static double distance_point(double x0, double y0, double x1, double y1)
    {
        return Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
    }

    //Фо́рмула Герона позволяет вычислить площадь треугольника (S) по его сторонам a, b, c:
    public static double s_tr(double x1, double y1, double x2, double y2, double x3, double y3)
    {
        double da = RuletSurfaceView.distance_point(x1, y1, x2, y2);
        double db = RuletSurfaceView.distance_point(x2, y2, x3, y3);
        double dc = RuletSurfaceView.distance_point(x1, y1, x3, y3);

        double p = (da + db + dc) / 2;

        return Math.sqrt(p * (p - da) * (p - db) * (p - dc));
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

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        canvas_w = getWidth();
        canvas_h = getHeight();

        canvas_w2 = canvas_w / 2;
        canvas_h2 = canvas_h / 2;

        initFidrstDraw();
    }

    public void startRuletAnain()
    {
        draw_selected_arrow = false;

        time_stop_rotate = (float) (3000 + 50000 * Math.random());
        time_start_stop = System.currentTimeMillis();
        work_rulet = true;
    }

    public void _stop_thread()
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

        if (this.drawLoopThread.getState() == Thread.State.TERMINATED)
        {
            this.drawLoopThread = new DrawManagerRulet(this);
            this.drawLoopThread.setRunning(true);
            this.drawLoopThread.start();
        }
        else if (this.drawLoopThread.getState() != Thread.State.RUNNABLE)
        {
            this.drawLoopThread.start();
        }
    }

    //==============================================================================================
    public boolean onTouchEvent(final MotionEvent event)
    {
        final float ex = event.getX();
        final float ey = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: // нажатие на экран

                return true;

            case MotionEvent.ACTION_MOVE: //движение по экрану

                return true;

            case MotionEvent.ACTION_UP: // отжатие

                break;

            case MotionEvent.ACTION_OUTSIDE:

                //Log.i("TAG", "MotionEvent.ACTION_OUTSIDE");
                break;

            case MotionEvent.ACTION_CANCEL:

                //Log.i("TAG", "MotionEvent.ACTION_CANCEL");
                break;

            default:

                //Log.i("TAG", "onTouchEvent: default");
                return false; // событие не обработано
        }

        return true; // событие обработано
    }

    private void initFidrstDraw()
    {
        if( canvas_w > canvas_h )
        {
            orietration_screen = ORIENTATION_LANDSCAPE;
        }
        else
        {
            orietration_screen = ORIENTATION_PORTRAIT;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (this._stop_draw)
        {
            callback_off();
            return;
        }

        super.onDraw(canvas);

        TNOW = System.currentTimeMillis();

        if (this.is_first_draw)
        {
            //_bk = BitmapFactory.decodeResource(getResources(), R.drawable._bk31);
            //_bk = BitmapFactory.decodeResource(getResources(), R.drawable._bk3);

            canvas_w = getWidth();
            canvas_h = getHeight();

            canvas_w2 = canvas_w / 2;
            canvas_h2 = canvas_h / 2;

            initFidrstDraw();

            is_first_draw = false;

            time_start_stop = TNOW;

            canvas.drawColor(bk_color);

        } /// - first draw

        /// очистка
        //canvas.drawColor(Color.GRAY, PorterDuff.Mode.CLEAR);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //canvas.drawColor(bk_color);
        //canvas.drawColor(bk_color);
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        matrix_rulet.reset();
        //matrix_rulet.postRotate(tmp_angle_timer,  rulet.getWidth() / 2,  rulet.getHeight() / 2);
        matrix_rulet.postRotate(tmp_angle_timer,  rulet.getWidth() / 2,  rulet.getHeight() / 2);
        matrix_rulet.postTranslate(canvas_w2 - rulet.getWidth() / 2, rulet.getHeight() / 6 - rulet.getHeight() / 2);

        //******************************************************************************************

        if( ! work_rulet )
        {
            canvas.drawBitmap(
                    rulet
                    , matrix_rulet
                    , paint_drawFilterBitmap
            );
        }
        else
        {
            canvas.drawBitmap(
                    rulet
                    , matrix_rulet
                    , paint_drawBitmap
            );
        }

        //******************************************************************************************

        if( draw_selected_arrow )
        {
            canvas.drawBitmap(
                      rulet_arrow_selected
                    , canvas_w2 - rulet_arrow.getWidth() / 2
                    , rulet.getHeight() / 6 + rulet.getHeight() / 2 - rulet_arrow.getHeight() / 2.5f
                    , paint_drawBitmap);
        }
        else
        {
            canvas.drawBitmap(
                      rulet_arrow
                    , canvas_w2 - rulet_arrow.getWidth() / 2
                    , rulet.getHeight() / 6 + rulet.getHeight() / 2 - rulet_arrow.getHeight() / 2.5f
                    , paint_drawBitmap);
        }

        if (TNOW - time_last_step > 10)
        {
            time_last_step = TNOW;

            //tmp_angle_timer += 3;

            if( TNOW - time_start_stop < time_stop_rotate )
            {
                tmp_angle_timer += (time_stop_rotate - (TNOW - time_start_stop)) * 3 / time_stop_rotate;
            }
            else
            {
                if( work_rulet )
                {
                    work_rulet = false;
                    float tk = tmp_angle_timer % 360.0f;

                    draw_selected_arrow = false;

                    if( tk >= 0.9f && tk <= 11.25f )
                    {
                        draw_selected_arrow = true;
                        callback_money_rulet( 50 );
                    }
                    else if( tk >= 11.25f * 13.0f && tk <= 11.25f * 13.0f + 11.25f )
                    {
                        draw_selected_arrow = true;
                        callback_money_rulet( 20 );
                    }
                    else if( tk >= 11.25f * 18.0f && tk <= 11.25f * 18.0f + 11.25f )
                    {
                        draw_selected_arrow = true;
                        callback_money_rulet( 10 );
                    }
                    else if( tk >= 11.25f * 23.0f && tk <= 11.25f * 23.0f + 11.25f )
                    {
                        draw_selected_arrow = true;
                        callback_money_rulet( 5 );
                    }
                    else
                    {
                        callback_money_rulet( 0 );
                    }

                    if( draw_selected_arrow )
                    {
                        if( eneble_sound && _ma.cachbox != null )
                        {
                            _ma.cachbox.start();
                        }

                        if( eneble_vibrate )
                        {
                            Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(350);
                        }

                        callback_end_rotate();
                    }
                    else
                    {
                        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(40);
                    }
                }
            }
        }

        //canvas.drawLine(0, canvas_h2, canvas_w, canvas_h2, paint_drawBitmap);
        //canvas.drawLine(canvas_w2, 0, canvas_w2, canvas_h, paint_drawBitmap);
    }

    /////////////////////////////////////////////////////////////////////////////////
    /// CALLBACKS ///
    public void callback_money_rulet(int count_money) {}
    public void callback_off() {}
    public void callback_end_rotate() {}
}
