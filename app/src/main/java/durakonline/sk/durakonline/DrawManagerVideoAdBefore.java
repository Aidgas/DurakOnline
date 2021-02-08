package durakonline.sk.durakonline;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class DrawManagerVideoAdBefore extends Thread
{
    private FireworksSurfaceView view;
    private boolean running = false;
    private boolean stopping = false;

    public DrawManagerVideoAdBefore(FireworksSurfaceView view)
    {
          this.view = view;
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
    	  Canvas c = null;
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
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder()) 
				{
				       view.onDraw(c);
				}
			  } 
			  finally 
			  {
		        if (c != null) 
		        {
		               view.getHolder().unlockCanvasAndPost(c);
		        }
			  }


			  try
			  { Thread.sleep(7); }
			  catch (InterruptedException e)
			  { e.printStackTrace(); }

          }
    }
}
