package xjtlu.eevee.nekosleep.Pet;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
        mLayoutParams =  new WindowManager.LayoutParams(PetView.height, PetView.width, 0, 0, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.OPAQUE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android 8.0
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            //其他版本
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mContext = this.getApplicationContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        linearLayout = (LinearLayout)inflater.inflate(R.layout.pet_frame, null, false);
        linearLayout.setBackgroundColor(Color.TRANSPARENT);
        PetView pet = (PetView)inflater.inflate(R.layout.pet_view, null,false);
        pet.setBackgroundColor(Color.TRANSPARENT);
        linearLayout.addView(pet);
        pet.setImageResource(R.drawable.pikaqiu_walking);
        pet.container = linearLayout;
        mWindowManager.addView(linearLayout, mLayoutParams);
        pet.setOnTouchListener(new View.OnTouchListener() {
            int x;
            int y;
            float touchedX;
            float touchedY;
            WindowManager.LayoutParams updatedParams =  new WindowManager.LayoutParams(PetView.height, PetView.width, mLayoutParams.x, mLayoutParams.y , WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.OPAQUE);

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        x = updatedParams.x;
                        y = updatedParams.y;
                        touchedX = motionEvent.getRawX();
                        touchedY = motionEvent.getRawY();
                    case MotionEvent.ACTION_MOVE:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //Android 8.0
                            updatedParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                        } else {
                            //其他版本
                            updatedParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                        }
                        updatedParams.x = (int)(x + motionEvent.getRawX() - touchedX);
                        updatedParams.y = (int)(y + motionEvent.getRawY() - touchedY);
                        mWindowManager.updateViewLayout(linearLayout, updatedParams);
                }
                return false;
            }
        });
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(linearLayout);
    }
}
