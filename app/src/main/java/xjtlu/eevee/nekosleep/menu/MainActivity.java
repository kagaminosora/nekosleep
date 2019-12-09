package xjtlu.eevee.nekosleep.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.ActivityManager;
import android.content.Intent;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import xjtlu.eevee.nekosleep.Pet.FloatWindowManagerService;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.ui.ChooseItemActivity;
import xjtlu.eevee.nekosleep.collections.ui.ItemScreenSlideActivity;
import xjtlu.eevee.nekosleep.collections.ui.PetScreenSlideActivity;
import xjtlu.eevee.nekosleep.home.DigitalClock;
import xjtlu.eevee.nekosleep.home.ServiceNotification;
import xjtlu.eevee.nekosleep.home.TestActivity;
import xjtlu.eevee.nekosleep.result.SleepResultActivity;
import xjtlu.eevee.nekosleep.settings.UserSettingsActivity;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    Button sleeporwake;
    boolean isChanged = true;
    Intent serviceForegroundIntent;
    ImageView petView;
    ImageView itemView;
    LinearLayout petLL;

    private ProgressBar pb;
    private int progress=0;
    private Timer timer;
    private TimerTask timerTask;

    int OVERLAY_PERMISSION_REQ_CODE = 0;
    boolean petStarted = false;
    boolean sleep_result = false;
    boolean wake_result = false;
    static Intent petIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
        long sleep_time = sp.getLong("SLEEP_TIME_LONG",0)/1000;
        long wake_time = sp.getLong("WAKE_TIME_LONG",0)/1000;
        long sleep_last = wake_time - sleep_time;
        long period = sleep_last*60;
        System.out.println(period);
        petView = findViewById(R.id.home_pet);
        itemView = findViewById(R.id.home_item);
        initPetView();

        petLL = findViewById(R.id.home_pet_item);

        petLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequirePermission();
            }
        });

        sleeporwake = findViewById(R.id.sleeporwake);
        sleeporwake.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Date date = new Date(System.currentTimeMillis());
                long time_current = date.getTime()/1000;
                if(isChanged) {
                    petView.setImageDrawable(null);
                    sleeporwake.setText(R.string.home_wake);
                    StartTimer(period);
                    if (Math.abs(time_current-sleep_time)<=3600){
                        sleep_result = true;
                    }
                }
                else{
                    sleeporwake.setText(R.string.home_sleep);
                    if (Math.abs(time_current-wake_time)<=3600){
                        wake_result = true;
                    }
                    if (wake_result&&sleep_result){
                        System.out.println("success");
                    }else{
                        System.out.println("fail");
                    }
                    sleepResult();
                }
                isChanged = !isChanged;
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
        mDrawerToggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer((int)Gravity.START);
                }
            }
        });

        drawer.addDrawerListener(mDrawerToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent it = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(it);
                        return true;
                    case R.id.nav_pets:
                        Intent it1 = new Intent(MainActivity.this, PetScreenSlideActivity.class);
                        startActivity(it1);
                        return true;
                    case R.id.nav_items:
                        Intent it2 = new Intent(MainActivity.this, ItemScreenSlideActivity.class);
                        startActivity(it2);
                        return true;
                    case R.id.nav_settings:
                        Intent it3 = new Intent(MainActivity.this, UserSettingsActivity.class);
                        startActivity(it3);
                        return true;
                }
                return false;
            }
        });

        pb = (ProgressBar) findViewById(R.id.progressBar);
        //设置进度条的最大数值
        pb.setMax(600);
        //一开始进度条的进度是0
        pb.setProgress(0);

    }

    public void sleepResult(){
        Intent intent = new Intent(MainActivity.this, SleepResultActivity.class);
        Bundle bundle = new Bundle();
        SharedPreferences sp = getApplicationContext().getSharedPreferences("pet", MODE_PRIVATE);
        String type = sp.getString("nextType", "empty");
        if(type.equals("empty")){
            bundle.putString("type", "pet");
            bundle.putString("petId", "00000000");
        }else if(type.equals("pet")){
            bundle.putString("type", "pet");
            bundle.putString("petId", sp.getString("nextItemId", "empty"));
        }else if(type.equals("item")){
            bundle.putString("type", "item");
            bundle.putString("itemId", sp.getString("nextItemId", "empty"));
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public static Intent getPetIntent(){
        return petIntent;
    }

    public void initPetView(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("pet", MODE_PRIVATE);
        String petId = sp.getString("petId", "empty");
        String itemId = sp.getString("itemId", "empty");
        String petImgName = sp.getString("petImgName", "empty");
        String itemImgName = sp.getString("itemImgName", "empty");
        setImg(petView, petImgName, "home");
        setImg(itemView, itemImgName, "itembook");
    }

    public void setImg(ImageView view, String name, String type){
        if(name.equals("empty")) {
            view.setImageDrawable(null);
        }else {
            Drawable item_img = AssetReader.getDrawableFromAssets(
                    getApplicationContext(), type + "_img/" + name + ".png");
            item_img.setBounds(0, 0, item_img.getIntrinsicWidth(), item_img.getIntrinsicHeight());
            view.setImageDrawable(item_img);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        serviceForegroundIntent = new Intent(this, ServiceNotification.class);
        serviceForegroundIntent.putExtra(ServiceNotification.EXTRA_NOTIFICATION_CONTENT, "running");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceForegroundIntent);
        } else {
            startService(serviceForegroundIntent);
        }
        EndTimer();
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
        long sleep_time = sp.getLong("SLEEP_TIME_LONG",0)/1000;
        long wake_time = sp.getLong("WAKE_TIME_LONG",0)/1000;
        long sleep_last = wake_time - sleep_time;
        long period = sleep_last*60;
        if (serviceForegroundIntent != null) {
            stopService(serviceForegroundIntent);
            serviceForegroundIntent = null;
        }
        StartTimer(period);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (serviceForegroundIntent != null) {
            stopService(serviceForegroundIntent);
            serviceForegroundIntent = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void StartTimer(long period) {
        Date time_current = new Date(System.currentTimeMillis());
        //如果timer和timerTask已经被置null了
        if (timer == null&&timerTask==null) {
            //新建timer和timerTask
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    //每次progress加一
                    progress++;
                    //如果进度条满了的话
                    if (progress == 601) {
                        progress = 600;
                    }
                    //设置进度条进度
                    pb.setProgress(progress);
                }
            };
            if (period>0) {
                timer.schedule(timerTask, time_current, period);
            }
            /*开始执行timer,第一个参数是要执行的任务，
            第二个参数是开始的延迟时间（单位毫秒）或者是Date类型的日期，代表开始执行时的系统时间
            第三个参数是计时器两次计时之间的间隔（单位毫秒）*/
        }
    }


    public void EndTimer() {
        timer.cancel();
        timerTask.cancel();
        timer=null;
        timerTask=null;
    }

    public void launchPet () {
        if (!petStarted) {
            petView.setImageDrawable(null);
            itemView.setImageDrawable(null);
            petIntent = new Intent(getApplicationContext(), FloatWindowManagerService.class);
            startService(petIntent);
            petStarted = true;
            System.out.println("Service started.");
        } else {
            initPetView();
            stopService(petIntent);
            petStarted = false;
            System.out.println("Service stopped.");
        }
    }

    private void checkAndRequirePermission () {
        if (Build.VERSION.SDK_INT >= 23) {//6.0以上
            if (Settings.canDrawOverlays(this)) {
                launchPet();
            }else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SYSTEM_ALERT_WINDOW)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 0);
                System.out.println("Success?");
            }else {
                launchPet();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == OVERLAY_PERMISSION_REQ_CODE){
            if(Settings.canDrawOverlays(this)) {
                launchPet();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SYSTEM_ALERT_WINDOW)
                    != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Success!");
                launchPet();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!petStarted) {
            initPetView();
        }
    }
}
