package xjtlu.eevee.nekosleep.home;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import xjtlu.eevee.nekosleep.R;

public class ServiceNotification extends Service {
    public static final String EXTRA_NOTIFICATION_CONTENT = "notification_content";
    private static final String CHANNEL_ID = "com.xd.gps";
    private static final String CHANNEL_NAME = "Default Channel";

    private NotificationUtil notificationUtil;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }

        String content = intent.getStringExtra(EXTRA_NOTIFICATION_CONTENT);
        notificationUtil = new NotificationUtil(getApplicationContext(), R.mipmap.ic_launcher,
                getString(R.string.app_name), content,
                CHANNEL_ID, CHANNEL_NAME);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NotificationUtil.NOTIFICATION_ID, notificationUtil.getNotification());
        } else {
            notificationUtil.showNotification();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (notificationUtil != null) {
            notificationUtil.cancelNotification();
            notificationUtil = null;
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
