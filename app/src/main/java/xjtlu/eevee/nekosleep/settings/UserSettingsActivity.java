package xjtlu.eevee.nekosleep.settings;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.alarm_clock.AlarmClockActivity;

public class UserSettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsv3);

        TextView setAlarm = findViewById(R.id.tv_set_alarm3);
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingsActivity.this,
                        AlarmClockActivity.class);
                startActivity(intent);
            }
        });

    }



}
