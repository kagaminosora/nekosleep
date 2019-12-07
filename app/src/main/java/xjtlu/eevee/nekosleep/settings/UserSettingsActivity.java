package xjtlu.eevee.nekosleep.settings;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.alarm_clock.AlarmClockActivity;
import xjtlu.eevee.nekosleep.alarm_clock.TimeSetActivity;
import xjtlu.eevee.nekosleep.collections.ui.ChooseItemActivity;
import xjtlu.eevee.nekosleep.collections.ui.ItemScreenSlideActivity;
import xjtlu.eevee.nekosleep.collections.ui.PetScreenSlideActivity;
import xjtlu.eevee.nekosleep.menu.MainActivity;

public class UserSettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    Intent it = new Intent(UserSettingsActivity.this, MainActivity.class);
                    startActivity(it);
                    return true;
                case R.id.nav_pets:
                    Intent it1 = new Intent(UserSettingsActivity.this, PetScreenSlideActivity.class);
                    startActivity(it1);
                    return true;
                case R.id.nav_items:
                    Intent it2 = new Intent(UserSettingsActivity.this, ItemScreenSlideActivity.class);
                    startActivity(it2);
                    return true;
                case R.id.nav_settings:
                    Intent it3 = new Intent(UserSettingsActivity.this, UserSettingsActivity.class);
                    startActivity(it3);
                    return true;
            }
            return false;
        });

        TextView setAlarm= findViewById(R.id.tv_set_alarm3);
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingsActivity.this,
                        AlarmClockActivity.class);
                startActivity(intent);
            }
        });

        TextView setTime = findViewById(R.id.tv_set_sleep_time3);
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingsActivity.this,
                        TimeSetActivity.class);
                startActivity(intent);
            }
        });

        TextView aboutUs = findViewById(R.id.tv_about_us3);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingsActivity.this,
                        AboutUsActivity.class);
                startActivity(intent);
            }
        });

    }



}
