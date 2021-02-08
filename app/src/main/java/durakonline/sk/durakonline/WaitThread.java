package durakonline.sk.durakonline;

class WaitThread extends Thread
{
    private long wait = 0;
    public WaitThread(long v)
    {
        super("WaitThread");
        wait = v;
    }

    @Override
    public void run()
    {
        if( wait > 0 )
        {
            try
            {
                Thread.sleep(wait);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        callback();
    }

    public void callback() {}
}