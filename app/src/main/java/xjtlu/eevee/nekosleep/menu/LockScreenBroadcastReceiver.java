package xjtlu.eevee.nekosleep.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LockScreenBroadcastReceiver extends BroadcastReceiver{
    LockScreenListener lockScreenListener;

    public LockScreenBroadcastReceiver(LockScreenListener lockScreenListener) {
        super();
        this.lockScreenListener = lockScreenListener;
    }

    @Override
    public void onReceive(Context content, Intent intent) {
        // TODO Auto-generated method stub
        String action=intent.getAction();
        if(action.equals(Intent.ACTION_SCREEN_ON)){
            lockScreenListener.onScreenOn();
        }else if(action.equals(Intent.ACTION_SCREEN_OFF)){
            lockScreenListener.onScreenOff();
        }else if(action.equals(Intent.ACTION_USER_PRESENT)){
            lockScreenListener.onUserPresent();
        }
    }
}
