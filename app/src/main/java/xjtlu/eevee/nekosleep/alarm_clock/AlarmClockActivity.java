package xjtlu.eevee.nekosleep.alarm_clock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import xjtlu.eevee.nekosleep.alarm_clock.view.SelectRemindCyclePopup;
import xjtlu.eevee.nekosleep.alarm_clock.view.SelectRemindWayPopup;

import java.text.SimpleDateFormat;
import java.util.Date;

import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.settings.UserSettingsActivity;

public class AlarmClockActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView date_tv;
    private TimePickerView pvTime;
    private RelativeLayout repeat_rl, ring_rl;
    private TextView tv_repeat_value, tv_ring_value;
    private LinearLayout allLayout;
    private Button set_btn;
    private String time;
    private int cycle;
    private int ring;
    private static SharedPreferences mSharedPreferences1 = null;

    private AlarmManagerUtils alarmManagerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_main_activity);
        allLayout = (LinearLayout) findViewById(R.id.all_layout);
        set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(this);
        date_tv = (TextView) findViewById(R.id.date_tv);
        repeat_rl = (RelativeLayout) findViewById(R.id.repeat_rl);
        repeat_rl.setOnClickListener(this);
        ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        ring_rl.setOnClickListener(this);
        tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
        tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);
        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //callback after submit
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                time = getTime(date);
                date_tv.setText(time);
            }
        });

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });

    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repeat_rl:
                selectRemindCycle();
                break;
            case R.id.ring_rl:
                selectRingWay();
                break;
            case R.id.set_btn:
                setClock();
                break;
            default:
                break;
        }
    }

    private void setClock() {
        alarmManagerUtils = AlarmManagerUtils.getInstance(this);
        alarmManagerUtils.createGetUpAlarmManager();
        if (time != null && time.length() > 0) {
            String[] times = time.split(":");
            if (cycle == 0) {//everyday
                alarmManagerUtils.getUpAlarmManagerStartWork(this, 0, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), 0, 0, "Time to get up!", ring);
            } if(cycle == -1){//only once
                alarmManagerUtils.getUpAlarmManagerStartWork(this, 0, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), 0, 0, "Time to get up!", ring);
            }else {//multiple choices
                String weeksStr = parseRepeat(cycle, 1);
                String[] weeks = weeksStr.split(",");
                for (int i = 0; i < weeks.length; i++) {
                    alarmManagerUtils.getUpAlarmManagerStartWork(this, 2, Integer.parseInt(times[0]), Integer
                            .parseInt(times[1]), i, Integer.parseInt(weeks[i]), "Time to get up!", ring);
                }
            }
            mSharedPreferences1 = getSharedPreferences("ALARM_PREF", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = mSharedPreferences1.edit();
            edit.putBoolean("ALARM_OR_NOT", true);
            edit.putString("ALARM_TIME", time);
            edit.putInt("RING", ring);
            edit.commit();
            Toast.makeText(this, "Alarm clock set successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AlarmClockActivity.this,
                    UserSettingsActivity.class);
            startActivity(intent);
            this.finish();
        }
        //TEST CODE
        //Toast.makeText(this, "TEST", Toast.LENGTH_LONG).show();
    }


    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // Monday
                    case 0:

                        break;
                    // Tuesday
                    case 1:

                        break;
                    // Wednesday
                    case 2:

                        break;
                    // Thursday
                    case 3:

                        break;
                    // Friday
                    case 4:

                        break;
                    // Saturday
                    case 5:

                        break;
                    // Sunday
                    case 6:

                        break;
                    // submit
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        tv_repeat_value.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        tv_repeat_value.setText("Everyday");
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        tv_repeat_value.setText("Once");
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup
                .SelectRemindWayPopupOnClickListener() {

            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    //Vibration
                    case 0:
                        tv_ring_value.setText("Vibration");
                        ring = 0;
                        break;
                    // Ring
                    case 1:
                        tv_ring_value.setText("Ring");
                        ring = 1;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "Monday";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "Tuesday";
                weeks = "2";
            } else {
                cycle = cycle + "," + "Tuesday";
                weeks = weeks + "," + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "Wednesday";
                weeks = "3";
            } else {
                cycle = cycle + "," + "Wednesday";
                weeks = weeks + "," + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "Thursday";
                weeks = "4";
            } else {
                cycle = cycle + "," + "Thursday";
                weeks = weeks + "," + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "Friday";
                weeks = "5";
            } else {
                cycle = cycle + "," + "Friday";
                weeks = weeks + "," + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "Saturday";
                weeks = "6";
            } else {
                cycle = cycle + "," + "Saturday";
                weeks = weeks + "," + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "Sunday";
                weeks = "7";
            } else {
                cycle = cycle + "," + "Sunday";
                weeks = weeks + "," + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlarmClockActivity.this,
                UserSettingsActivity.class);
        startActivity(intent);
        this.finish();
        super.onBackPressed();
    }

}

