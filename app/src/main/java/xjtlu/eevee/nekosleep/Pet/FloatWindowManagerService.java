package xjtlu.eevee.nekosleep.Pet;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
        mLayoutParams =  new WindowManager.LayoutParams(PetView.height, PetView.width, 0, 0, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.RGBA_8888);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android 8.0
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            //其他版本
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mContext = this.getApplicationContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        linearLayout = (LinearLayout)inflater.inflate(R.layout.pet_frame, null, false);
        linearLayout.setBackgroundColor(Color.TRANSPARENT);
        PetView pet = (PetView)inflater.inflate(R.layout.pet_view, null,false);
        pet.container = linearLayout;
        pet.setPetNum(petNum);
        linearLayout.addView(pet);
        addFloatingWindow();
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            int x;
            int y;
            float touchedX;
            float touchedY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        x = mLayoutParams.x;
                        y = mLayoutParams.y;
                        touchedX = motionEvent.getRawX();
                        touchedY = motionEvent.getRawY();
                        pet.doRandomAction();
                    case MotionEvent.ACTION_MOVE:
                        mLayoutParams.x = (int)(x + motionEvent.getRawX() - touchedX);
                        mLayoutParams.y = (int)(y + motionEvent.getRawY() - touchedY);
                        mWindowManager.updateViewLayout(linearLayout, mLayoutParams);
                }
                return false;
            }
        });
        BroadcastReceiver tickReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    pet.doRandomAction();
                    pet.doRandomAction();
                }
            }
        };
        registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        return result;
    }

    public void addFloatingWindow () {
        mWindowManager.addView(linearLayout, mLayoutParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(linearLayout);
    }
}
