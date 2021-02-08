package durakonline.sk.durakonline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by root on 18.07.15.
 */
public class BootServiceReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Intent myIntent = new Intent(context, MyService.class);
        //context.startService(myIntent);

        try
        {
            context.startService(new Intent(context, MyService.class));
        }
        catch (IllegalStateException e)
        {
            //catch the IllegalStateExeption
        }
        catch (Exception e)
        {
            //catch all the ones I didn't think of.
        }
    }
}
