package xjtlu.eevee.nekosleep.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.alarm_clock.AlarmClockActivity;
import xjtlu.eevee.nekosleep.alarm_clock.AlarmManagerUtils;
import xjtlu.eevee.nekosleep.collections.ui.ItemScreenSlideActivity;
import xjtlu.eevee.nekosleep.collections.ui.PetScreenSlideActivity;
import xjtlu.eevee.nekosleep.menu.MainActivity;
import com.bigkoo.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserSettingsActivity extends AppCompatActivity {
    private static final String SLEEP_WAKE_TIME = "SLEEP_WAKE_TIME";
    private static SharedPreferences mSharedPreferences, mSharedPreferences1 = null;
    private TimePickerView pvSleepTime, pvWakeTime;
    private String alarmTime, sleepTime, wakeTime;
    private boolean alarmFlag;
    private AlarmManagerUtils alarmManagerUtils;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsv3);
        mSharedPreferences = getSharedPreferences(SLEEP_WAKE_TIME, Context.MODE_PRIVATE);
        mSharedPreferences1 = getSharedPreferences("ALARM_PREF", Context.MODE_PRIVATE);

        Switch alarmSwitch = findViewById(R.id.sw_alarm_switch);
        RelativeLayout setAlarm = findViewById(R.id.set_alarm3);
        TextView alarmRepeat = findViewById(R.id.tv_repeat_alarm_time);
        RelativeLayout setSleepTime = findViewById(R.id.set_sleep_time);
        TextView sleepTimeRepeat = findViewById(R.id.tv_repeat_sleep_time);
        RelativeLayout setWakeTime = findViewById(R.id.set_wake_time);
        TextView wakeTimeRepeat = findViewById(R.id.tv_repeat_wake_time);

        alarmFlag = mSharedPreferences1.getBoolean("ALARM_OR_NOT", false);
        alarmSwitch.setChecked(alarmFlag);
        alarmTime = mSharedPreferences1.getString("ALARM_TIME", "");
        alarmRepeat.setText(alarmTime);
        sleepTimeRepeat.setText(mSharedPreferences.getString("SLEEP_TIME", ""));
        wakeTimeRepeat.setText(mSharedPreferences.getString("WAKE_TIME", ""));

        alarmManagerUtils = AlarmManagerUtils.getInstance(this);
        alarmManagerUtils.createGetUpAlarmManager();


        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (alarmTime.length() > 1 && !alarmFlag) { // have saved alarmTime but no current alarm

                        String[] times = alarmTime.split(":");
                        alarmManagerUtils.getUpAlarmManagerStartWork(getApplicationContext(), 0, Integer.parseInt(times[0]), Integer.parseInt
                                (times[1]), 0, 0, "Time to get up!", mSharedPreferences1.getInt("RING", 0));

                        SharedPreferences.Editor edit = mSharedPreferences1.edit();
                        edit.putBoolean("ALARM_OR_NOT", true);
                        edit.commit();
                        Toast.makeText(getApplicationContext(), "Set once alarm at " + alarmTime, Toast.LENGTH_LONG).show();
                    } else if (alarmTime.length() == 0 && !alarmFlag) { //first time to use, no saved alarmTime
                        Intent intent = new Intent(UserSettingsActivity.this,
                                AlarmClockActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    if (alarmFlag) {
                        alarmManagerUtils.cancelAlarm(0);

                        SharedPreferences.Editor edit = mSharedPreferences1.edit();
                        edit.putBoolean("ALARM_OR_NOT", false);
                        edit.commit();
                        Toast.makeText(getApplicationContext(), "Current alarm has been canceled", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingsActivity.this,
                        AlarmClockActivity.class);
                startActivity(intent);
                finish();
            }
        });


        pvSleepTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvSleepTime.setTime(new Date());
        pvSleepTime.setCyclic(false);
        pvSleepTime.setCancelable(true);
        pvSleepTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                sleepTime = getTime(date);
                sleepTimeRepeat.setText(sleepTime);
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putString("SLEEP_TIME", sleepTime);
                edit.putLong("SLEEP_TIME_LONG",date.getTime());
                edit.commit();
                System.out.println("sleep_time: "+date.getTime());
            }
        });


        setSleepTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                pvSleepTime.show();
            }
        });



        pvWakeTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvWakeTime.setTime(new Date());
        pvWakeTime.setCyclic(false);
        pvWakeTime.setCancelable(true);
        pvWakeTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                wakeTime = getTime(date);
                wakeTimeRepeat.setText(wakeTime);
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putString("WAKE_TIME", wakeTime);
                edit.putLong("WAKE_TIME_LONG",date.getTime());
                edit.commit();
                System.out.println("wake_time: "+date.getTime());
            }
        });
        setWakeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvWakeTime.show();
            }
        });

        RelativeLayout aboutUs = findViewById(R.id.about_us3);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingsActivity.this,
                        AboutUsActivity.class);
                startActivity(intent);
            }
        });

    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }



}
