package xjtlu.eevee.nekosleep.alarm_clock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AlarmManagerUtils {

    private static final long TIME_INTERVAL = 10 * 1000;
    private Context context;
    public static AlarmManager am;
    public static PendingIntent pendingIntent;

    private Calendar calendar;

    private AlarmManagerUtils(Context aContext){
        this.context = aContext;
    }

    //singleton
    private static AlarmManagerUtils instance = null;

    public static AlarmManagerUtils getInstance(Context aContext){
        if(instance == null){
            synchronized (AlarmManagerUtils.class){
                if(instance == null){
                    instance = new AlarmManagerUtils(aContext);
                }
            }
        }
        return instance;
    }

    public void createGetUpAlarmManager(){
//        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmClockService.class);
//        pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    }

    @SuppressLint("NewApi")
    public void getUpAlarmManagerStartWork(Context context, int flag, int hour, int minute, int id, int
            week, String tips, int soundOrVibrator){

        calendar = Calendar.getInstance();
        long intervalMillis = 0;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 10);
        if (flag == 0) {
            intervalMillis = 0;
        } else if (flag == 1) {
            intervalMillis = 24 * 3600 * 1000;
        } else if (flag == 2) {
            intervalMillis = 24 * 3600 * 1000 * 7;
        }
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmClockService.class);
        intent.putExtra("intervalMillis", intervalMillis);
        intent.putExtra("msg", tips);
        intent.putExtra("id", id);
        intent.putExtra("soundOrVibrator", soundOrVibrator);
        pendingIntent = PendingIntent.getService(context, id, intent, 0);

        //API adaption
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calMethod(week, calendar.getTimeInMillis()), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pendingIntent);
        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), TIME_INTERVAL, pendingIntent);
        }
    }

    private static long calMethod(int weekflag, long dateTime) {
        long time = 0;
        //weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekflag非0时表示每周几的闹钟并以周为时间间隔
        if (weekflag != 0) {
            Calendar c = Calendar.getInstance();
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (1 == week) {
                week = 7;
            } else if (2 == week) {
                week = 1;
            } else if (3 == week) {
                week = 2;
            } else if (4 == week) {
                week = 3;
            } else if (5 == week) {
                week = 4;
            } else if (6 == week) {
                week = 5;
            } else if (7 == week) {
                week = 6;
            }

            if (weekflag == week) {
                if (dateTime > System.currentTimeMillis()) {
                    time = dateTime;
                } else {
                    time = dateTime + 7 * 24 * 3600 * 1000;
                }
            } else if (weekflag > week) {
                time = dateTime + (weekflag - week) * 24 * 3600 * 1000;
            } else if (weekflag < week) {
                time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000;
            }
        } else {
            if (dateTime > System.currentTimeMillis()) {
                time = dateTime;
            } else {
                time = dateTime + 24 * 3600 * 1000;
            }
        }
        return time;
    }

    @SuppressLint("NewApi")
    public void getUpAlarmManagerWorkOnOthers(int week){
        //set repeatedly in high API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calMethod(week, calendar.getTimeInMillis()), pendingIntent);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                    + TIME_INTERVAL, pendingIntent);
        }
    }

    public void cancelAlarm(int id){
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmClockService.class);
        pendingIntent = PendingIntent.getService(context, id, intent, 0);
        am.cancel(pendingIntent);
    }

}
