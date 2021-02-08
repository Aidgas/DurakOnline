package durakonline.sk.durakonline;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawManagerRoom extends Thread
{
    private RoomSurfaceView view;
    private boolean running = false;
    private boolean stopping = false;
    private SurfaceHolder surfaceHolder;

    public DrawManagerRoom(RoomSurfaceView view)
    {
          this.view = view;
          this.surfaceHolder = view.getHolder();
    }

    public void setRunning(boolean run) 
    {
          running = run;
    }
    
    public void setStopping(boolean stop) 
    {
    	stopping = stop;
    } 
    
    public boolean getRunning() 
    {
       return running;
    }
    
    public boolean getStopping() 
    {
    	return stopping;
    }


    @SuppressLint("WrongCall")
	@Override
    public void run()
    {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        try
        { Thread.sleep(200); }
        catch (InterruptedException e)
        { e.printStackTrace(); }

        Canvas c = null;
        int _f = 0;
        while (running)
        {
            if(this.stopping)
            {
                 try
                 { Thread.sleep(1000); }
                 catch (InterruptedException e)
                 { e.printStackTrace(); }

                 continue;
            }

            try
            {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder)
                {
                       view.onDraw(c);
                }
            }
            finally
            {
                if (c != null)
                {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }

            if(
                     ! view.animation_my_animated_card
                  && ! view.animation_fly_card
                  //&& ! view.animation_fly_card_other_user
                  //&& ! view.need_move_cards_on_board
                  && ! view.animation_fly_card_off_board
                  && ! view.animation_fly_desc_card
                  && ! view.flag_animation_fly_img
                  && ! view.flag_list_animation_image_smile
                  && ! view.flag_list_animation_number
                  && ! view.flag_list_animation_text_coints
                  && ! view.flag_adraw_animated_texts
                  && ! view.flag_card_fly_user_xod
                  && ! view.show_wondow_user_info
            )
            {
                if(_f > 10)
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        //e.printStackTrace();
                    }
                }
            }
            else
            {
                _f = 0;
            }

            _f += 1;
        }
    }
}
