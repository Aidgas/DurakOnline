package durakonline.sk.durakonline.other;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class DrawManager2 extends Thread
{
    private BackgroundAnimation1 view;
    private boolean running = false;
    private boolean stopping = false;

    public DrawManager2(BackgroundAnimation1 view)
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
			  { Thread.sleep(30); }
			  catch (InterruptedException e) 
			  { e.printStackTrace(); }
          }
    }
}
