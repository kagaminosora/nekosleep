package xjtlu.eevee.nekosleep.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import xjtlu.eevee.nekosleep.Pet.FloatWindowManagerService;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.ui.ItemScreenSlideActivity;
import xjtlu.eevee.nekosleep.collections.ui.PetScreenSlideActivity;
import xjtlu.eevee.nekosleep.home.DetectService;
import xjtlu.eevee.nekosleep.home.DigitalClock;
import xjtlu.eevee.nekosleep.home.ForegroundCallbacks;
import xjtlu.eevee.nekosleep.home.ServiceNotification;
import xjtlu.eevee.nekosleep.result.SleepResultActivity;
import xjtlu.eevee.nekosleep.settings.UserSettingsActivity;

import static xjtlu.eevee.nekosleep.settings.UserSettingsActivity.getTime;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private static SharedPreferences mSharedPreferences = null;
    private String sleep_time, wake_time;
    Button sleeporwake;
    boolean issleeped = false;
    Intent serviceForegroundIntent;

    private DrawerLayout homeLayout;
    private AnimationDrawable backgrouond;

    private ImageView hamburger;
    private ImageView fastResult;

    private ImageView petView;
    private String petImgName;
    private String itemImgName;

    private ProgressBar pb;
    private int progress=0;
    private Timer timer;
    private TimerTask timerTask;

    int OVERLAY_PERMISSION_REQ_CODE = 0;
    boolean petStarted = false;
    boolean sleep_result = false;
    boolean wake_result = false;
    boolean monitor_result = true;
    static Intent petIntent;

    private DetectService mDetectService;
    LockScreenBroadcastReceiver lockScreenBroadcastReceiver;
    boolean screen_off = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBackground();
        initPetView();
        initProgressBar();
        initNavigationView();
        initScreenListener();
        mDetectService = new DetectService();

        ForegroundCallbacks.init(this.getApplication()).addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {

            }

            @Override
            public void onBecameBackground() {
                System.out.println("screen: "+mSharedPreferences.getBoolean("SCREEN_STATE",false));
                System.out.println("sleep: "+issleeped);
                if ((mSharedPreferences.getBoolean("SCREEN_STATE",false)) && (issleeped)) {
                    monitor_result = false;
                    issleeped = false;
                    Toast toast = Toast.makeText(MainActivity.this,"Leave the APP and slepp fail!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    Intent it = new Intent(MainActivity.this, MainActivity.class);
                    finish();
                    startActivity(it);
                }
            }
        });
    }

    public void initNavigationView(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_pets:
                        Intent it1 = new Intent(MainActivity.this, PetScreenSlideActivity.class);
                        startActivity(it1);
                        drawer.closeDrawer(GravityCompat.START);
                        if(petStarted){
                            stopService(petIntent);
                            petStarted = false;
                        }
                        return true;
                    case R.id.nav_items:
                        Intent it2 = new Intent(MainActivity.this, ItemScreenSlideActivity.class);
                        startActivity(it2);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_settings:
                        Intent it3 = new Intent(MainActivity.this, UserSettingsActivity.class);
                        startActivity(it3);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });

        DigitalClock clock = findViewById(R.id.home_clock);
        clock.setOnTouchListener(new View.OnTouchListener() {
            Drawable hamburgerIcon = clock.getCompoundDrawables()[0];
            Drawable confirmIcon = clock.getCompoundDrawables()[2];
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getX() < view.getWidth() - view.getPaddingRight()
                        && motionEvent.getX() > view.getWidth() - view.getPaddingRight() - confirmIcon.getBounds().width()
                        && motionEvent.getY() < confirmIcon.getBounds().height() + view.getPaddingBottom()
                        && motionEvent.getY() > view.getPaddingTop()){
                    gotoResult();
                }

                if (motionEvent.getX() < hamburgerIcon.getBounds().width()+ view.getPaddingLeft()
                        && motionEvent.getX() > view.getPaddingLeft()
                        && motionEvent.getY() < hamburgerIcon.getBounds().height() + view.getPaddingBottom()
                        && motionEvent.getY() > view.getPaddingBottom()){
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer((int)Gravity.START);
                    }
                }
                return false;
            }
        });
    }

    public void initProgressBar(){
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setMax(getSleepLength());
        pb.setProgress(0);
        pb.setVisibility(View.INVISIBLE);
        sleeporwake = findViewById(R.id.sleeporwake);
        sleeporwake.setText(R.string.home_sleep);
        sleeporwake.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Date date = new Date(System.currentTimeMillis());
                long time_current = date.getTime()/1000;
                System.out.println(time_current);
                if(!issleeped) {
                    int length = getSleepLength();
                    if(length<14400 || length>360000){
                        Toast.makeText(getApplicationContext(),"Please reset sleep and wake up time to sleep 4-10 hours", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, UserSettingsActivity.class);
                        startActivity(intent);
                    }else {
                        monitor_result = true;
                        issleeped = true;
                        pb.setVisibility(View.VISIBLE);
                        setImg(petView, petImgName + "_sleep", "home");
                        if(petStarted){
                            stopService(petIntent);
                            petStarted = false;
                        }
                        petView.setClickable(false);
                        sleeporwake.setText(R.string.home_cancel);
                        StartTimer(length);
                    }
                }
                else{
                    Calendar calendar = Calendar. getInstance();
                    calendar.add( Calendar. DATE, +1);
                    int cHour = calendar.get(Calendar.HOUR);
                    int cMin = calendar.get(Calendar.MINUTE);
                    int wHour = Integer.valueOf(wake_time.split(":")[0]);
                    int wMin = Integer.valueOf(wake_time.split(":")[1]);
                    wake_result = false;
                    issleeped = false;
                    pb.setVisibility(View.INVISIBLE);
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("pet", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("sleepy", true);
                    editor.commit();
                    initPetView();
                    if(monitor_result) sleeporwake.setText(R.string.home_sleep);
                    if(within1h(cHour,cMin,wHour,wMin)) wake_result=true;
                    EndTimer();
                    if (wake_result&&sleep_result&&monitor_result){
                        System.out.println("success");gotoResult();
                    }else{
                        System.out.println("fail");
                        Toast toast = Toast.makeText(MainActivity.this,"Not follow the set time and sleep fail!",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                    progress=0;
                    pb.setProgress(0);
                }
            }
        });
    }

    // Sleep length measured by seconds
    public int getSleepLength(){
        mSharedPreferences = getSharedPreferences("SLEEP_WAKE_TIME", Context.MODE_PRIVATE);
        sleep_time = (mSharedPreferences.getString("SLEEP_TIME",""));
        wake_time = (mSharedPreferences.getString("WAKE_TIME",""));
        //System.out.println("sleep_time: "+sleep_time);
        //System.out.println("wake_time: "+wake_time);

        int period;
        if(sleep_time.equals("")||wake_time.equals("")){
            period = 0;
        }else {
            int sHour = Integer.valueOf(sleep_time.split(":")[0]);
            int sMin = Integer.valueOf(sleep_time.split(":")[1]);
            int wHour = Integer.valueOf(wake_time.split(":")[0]);
            int wMin = Integer.valueOf(wake_time.split(":")[1]);
            period = getTimeLength(sHour, sMin, wHour, wMin);
        }
        return period;
    }

    public int getTimeLength(int hourStart, int minStart, int hourEnd, int minEnd){
        int length;
        if (hourStart > hourEnd) {
            length = (24 - hourStart + hourEnd) * 3600 + (minEnd - minStart) * 60;
        } else {
            length = (hourEnd - hourStart) * 3600 + (minEnd - minStart) * 60;
        }
        if(length<0) length=+24*3600;
        return length;
    }

    public boolean within1h(int hourCurrent, int minCurrent, int hourSet, int minSet){
        if (hourSet==23){
            if (hourCurrent==0){
                if(minSet>=minCurrent){return true;}
                else return false;
            }else{
                if(getTimeLength(hourCurrent,minCurrent,hourSet,minSet)<=60){return true;}
                else return false;
            }
        }
        else if (hourSet==0){
            if (hourCurrent==23){
                if(minCurrent>=minSet){
                    return true;
                }else return false;
            }else{
                if(getTimeLength(hourSet,minSet,hourCurrent,minCurrent)<=60){return true;}
                else return false;
            }
        }
        else if(getTimeLength(hourCurrent,minCurrent,hourSet,minSet)<=60||getTimeLength(hourSet,minSet,hourCurrent,minCurrent)<=60){
            return true;
        }else return false;
    }


    public void initBackground(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        homeLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        backgrouond = (AnimationDrawable) homeLayout.getBackground();
        backgrouond.start();
    }

    public static Intent getPetIntent(){
        return petIntent;
    }

    public void initPetView(){
        petView = findViewById(R.id.home_pet);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("pet", MODE_PRIVATE);
        String petId = sp.getString("petId", "empty");
        String itemId = sp.getString("itemId", "empty");
        petImgName = sp.getString("petImgName", "pikachu");
        itemImgName = sp.getString("itemImgName", "empty");
        boolean sleepy = sp.getBoolean("sleepy", true);
        petView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequirePermission();
            }
        });
        if(sleepy){
            setImg(petView, petImgName + "_sleep", "home");
            petView.setClickable(false);
        }else {
            setImg(petView, petImgName + "_" + itemImgName, "home");
            petView.setClickable(true);
        }
    }

    public void setImg(ImageView view, String name, String type){
        if(name.equals("empty_empty")) {
            view.setImageDrawable(null);
        }else {
            Bitmap item_img = AssetReader.loadImageFromAssets(
                    getApplicationContext(), type + "_img/" + name + ".png");
            Drawable item_dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(item_img, 100, 100, true));
            view.setImageDrawable(item_dr);
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
//        System.out.println(screen_off);
//        System.out.println(initScreenListener());
//        System.out.println(mSharedPreferences.getBoolean("SCREEN_STATE",false));
        if (issleeped && (!mSharedPreferences.getBoolean("SCREEN_STATE",false))) {
            EndTimer();
            monitor_result = false;
            issleeped = false;
            System.out.println("fail");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
//        long sleep_time = sp.getLong("SLEEP_TIME_LONG",0)/1000;
//        long wake_time = sp.getLong("WAKE_TIME_LONG",0)/1000;
//        long sleep_last = wake_time - sleep_time;
//        long period = sleep_last*60;
//        System.out.println(mSharedPreferences.getBoolean("SCREEN_STATE",false));
        if (serviceForegroundIntent != null) {
            stopService(serviceForegroundIntent);
            serviceForegroundIntent = null;
        }
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

    public void StartTimer(int period) {
        pb.setMax(getSleepLength());
        Date time_current = new Date(System.currentTimeMillis());
        String time_c = getTime(time_current);
        Calendar calendar = Calendar. getInstance();
        calendar.add( Calendar. DATE, +1);
        Date date= calendar.getTime();
        int cHour = calendar.get(Calendar.HOUR_OF_DAY);
        int cMin = calendar.get(Calendar.MINUTE);
        int sHour = Integer.valueOf(sleep_time.split(":")[0]);
        int sMin = Integer.valueOf(sleep_time.split(":")[1]);
        int wHour = Integer.valueOf(wake_time.split(":")[0]);
        int wMin = Integer.valueOf(wake_time.split(":")[1]);

        if(within1h(cHour,cMin,sHour,sMin)){
            sleep_result = true;//can sleep
        }else sleep_result = false;

        //if timer and timerTask are null
        if (timer == null&&timerTask==null) {
            //新建timer和timerTask
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Date time_current = new Date(System.currentTimeMillis());
                    String time_c = getTime(time_current);
                    Calendar calendar = Calendar. getInstance();
                    int cHour = calendar.get(Calendar.HOUR_OF_DAY);
                    int cMin = calendar.get(Calendar.MINUTE);
                    int len1 = getTimeLength(sHour,sMin,cHour,cMin);
                    int len2 = getTimeLength(wHour,wMin,cHour,cMin);
                    if(len1>36000) len1=0;
                    pb.setProgress(len1);
                    Log.d("timer",String.valueOf(len2));
                    if(len2<120) {
                        timer.cancel();
                    }
                }
            };
        }
        timer.schedule(timerTask, time_current, 60000);
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
        if (Build.VERSION.SDK_INT >= 23) {//> 6.0
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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, OVERLAY_PERMISSION_REQ_CODE);
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
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
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
        initProgressBar();
    }

    public void gotoResult(){
            Intent intent = new Intent(MainActivity.this, SleepResultActivity.class);
            Bundle bundle = new Bundle();
            SharedPreferences sp = getApplicationContext().getSharedPreferences("pet", MODE_PRIVATE);
            String type = sp.getString("nextType", "empty");
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("sleepy", false);
            editor.commit();
            if(type.equals("empty")){
                type = "pet";
                bundle.putString("petId", "00000001");
            }else if(type.equals("pet")){
                bundle.putString("petId", sp.getString("nextItemId", "empty"));
            }else if(type.equals("item")){
                bundle.putString("itemId", sp.getString("nextItemId", "empty"));
            }
            bundle.putString("type", type);
            intent.putExtras(bundle);
            startActivity(intent);
    }

    public void initScreenListener() {
        IntentFilter filter = new IntentFilter("xjtlu.eevee.nekosleep");
        registerReceiver(mDetectService, filter);
        lockScreenBroadcastReceiver = new LockScreenBroadcastReceiver(new LockScreenListener() {
                    @Override
                    public void onUserPresent() {
                        screen_off = true;
                        SharedPreferences.Editor edit = mSharedPreferences.edit();
                        edit.putBoolean("SCREEN_STATE", screen_off);
                        edit.commit();
                        Log.i(Tag,"onUserPresent");
                    }

                    @Override
                    public void onScreenOn() {
                        Log.i(Tag,"onScreenOn");
                    }

                    @Override
                    public void onScreenOff() {
                        screen_off = false;
                        SharedPreferences.Editor edit = mSharedPreferences.edit();
                        edit.putBoolean("SCREEN_STATE", screen_off);
                        edit.commit();
                        Log.i(Tag,"onScreenOff");
                    }
                });
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(lockScreenBroadcastReceiver, intentFilter);
    }
}
