package xjtlu.eevee.nekosleep.Pet;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import xjtlu.eevee.nekosleep.R;

public class FloatWindowManagerService extends Service {

    private WindowManager mWindowManager;
    private Context mContext;
    private WindowManager.LayoutParams mLayoutParams;
    private LinearLayout linearLayout;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        SharedPreferences sharedPreferences = this.getSharedPreferences("type", MODE_PRIVATE);
        int petNum = sharedPreferences.getInt("type", 0);
        PetView petView = new PetView(this);
        mLayoutParams =  new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android 8.0
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            //其他版本
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mContext = this.getApplicationContext();
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mLayoutParams.height = 400;
        mLayoutParams.width = 400;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(new PetView(this), mLayoutParams);
        return result;
    }
}
