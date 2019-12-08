package xjtlu.eevee.nekosleep.alarm_clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.loonggg.lib.alarmmanager.clock.ClockAlarmActivity;

public class AlarmClockService extends Service{
    private static SharedPreferences mSharedPreferences1 = null;
    private static final String TAG = "AlarmClockService";
    public AlarmClockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "received in AlarmClockService ");
                mSharedPreferences1 = getSharedPreferences("ALARM_PREF", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = mSharedPreferences1.edit();
                edit.putBoolean("ALARM_OR_NOT", false);
                edit.commit();
                Intent clockIntent = new Intent(getApplicationContext(), ClockAlarmActivity.class);
                String msg = intent.getStringExtra("msg");
                int flag = intent.getIntExtra("soundOrVibrator", 0);
                clockIntent.putExtra("msg", msg);
                clockIntent.putExtra("flag", flag);
                clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(clockIntent);
            }
        }).start();

        //AlarmManagerUtils.getInstance(getApplicationContext()).getUpAlarmManagerWorkOnOthers();

        return super.onStartCommand(intent, flags, startId);
    }

}
