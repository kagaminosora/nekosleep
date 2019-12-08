package com.loonggg.lib.alarmmanager.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;


import java.util.Calendar;

/**
 * Created by loonggg on 2016/3/21.
 */
public class AlarmManagerUtil {
    public static final String ALARM_ACTION = "com.loonggg.alarm.clock";
    private IntentFilter intentFilter;
    private LoongggAlarmReceiver loongggAlarmReceiver;
    private Calendar calendar;

    public static void setAlarmTime(Context context, long timeInMillis, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, intent.getIntExtra("id", 0),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int interval = (int) intent.getLongExtra("intervalMillis", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender);
        }
    }

    public static void cancelAlarm(Context context, String action, int id) {
        Intent intent = new Intent(action);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    /**
     * @param flag            周期性时间间隔的标志,flag = 0 表示一次性的闹钟, flag = 1 表示每天提醒的闹钟(1天的时间间隔),flag = 2
     *                        表示按周每周提醒的闹钟（一周的周期性时间间隔）
     * @param hour            时
     * @param minute          分
     * @param id              闹钟的id
     * @param week            week=0表示一次性闹钟或者按天的周期性闹钟，非0 的情况下是几就代表以周为周期性的周几的闹钟
     * @param tips            闹钟提示信息
     * @param soundOrVibrator 2表示声音和震动都执行，1表示只有铃声提醒，0表示只有震动提醒
     */
    public static void setAlarm(Context context, int flag, int hour, int minute, int id, int
            week, String tips, int soundOrVibrator) {
        System.out.println("=========================================================================");
        System.out.println("=========================================================================");
        System.out.println("check0.5");
        System.out.println("=========================================================================");
        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long intervalMillis = 0;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 2);
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.HOUR_OF_DAY, minute);
//        calendar.set(Calendar.SECOND,2);

        if (flag == 0) {
            intervalMillis = 0;
        } else if (flag == 1) {
            intervalMillis = 24 * 3600 * 1000;
        } else if (flag == 2) {
            intervalMillis = 24 * 3600 * 1000 * 7;
        }
        Intent intent = new Intent(ALARM_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putLong("intervalMillis", intervalMillis);
        //intent.putExtra("intervalMillis", intervalMillis);
        //intent.putExtra("msg", tips);
        //intent.putExtra("id", id);
        bundle.putInt("soundOrVibrator", soundOrVibrator);
        intent.putExtra("bundle", bundle);
        final PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    //calMethod(week, calendar.getTimeInMillis()), sender);
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis()+5000, sender);
            System.out.println("=========================================================================");
            System.out.println("week: "+ week+" currentTimeMillis: "+System.currentTimeMillis());
            System.out.println("=========================================================================");
        } else {
            if (flag == 0) {
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            } else {
                am.setRepeating(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis
                        ()), intervalMillis, sender);
            }
        }
        BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
//                            System.currentTimeMillis()+5000, sender);
//                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + TIME_INTERVAL, pendingIntent);
//                }
                System.out.println("=========================================================================");
                System.out.println("Received!");
                System.out.println("=========================================================================");
                String msg = intent.getStringExtra("msg");
                int flag = intent.getIntExtra("soundOrVibrator", 0);
                Intent clockIntent = new Intent(context, ClockAlarmActivity.class);
                clockIntent.putExtra("msg", msg);
                clockIntent.putExtra("flag", flag);
                clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(clockIntent);
            }
        };
    }


    /**
     * @param weekflag 传入的是周几
     * @param dateTime 传入的是时间戳（设置当天的年月日+从选择框拿来的时分秒）
     * @return 返回起始闹钟时间的时间戳
     */
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


}
