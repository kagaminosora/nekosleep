package xjtlu.eevee.nekosleep.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.ActivityManager;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import xjtlu.eevee.nekosleep.Pet.FloatWindowManagerService;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.ui.ChooseItemActivity;
import xjtlu.eevee.nekosleep.collections.ui.ItemScreenSlideActivity;
import xjtlu.eevee.nekosleep.collections.ui.PetScreenSlideActivity;
import xjtlu.eevee.nekosleep.home.DigitalClock;
import xjtlu.eevee.nekosleep.home.ServiceNotification;
import xjtlu.eevee.nekosleep.settings.UserSettingsActivity;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    Button sleeporwake;
    boolean isChanged = false;
    Intent serviceForegroundIntent;

//    Button btPetBook;
//    Button btSettings;
//    Button btFloatWindow;
//    Button btResult;
//
    int OVERLAY_PERMISSION_REQ_CODE = 0;

//    String m_sMonitorAppName = "xjtlu.eevee.nekosleep";
//    class MonitorThread implements Runnable {
//        public void run() {
//            while (true) {
//                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//                List<ActivityManager.RunningAppProcessInfo> runningTasks = manager.getRunningAppProcesses();
//
//                // 获得当前最顶端的任务栈，即前台任务栈
//                ActivityManager.RunningAppProcessInfo runningTaskInfo = runningTasks.get(0);
//                String packageName = runningTaskInfo.processName.toString();
//
//                if (!packageName.equals(m_sMonitorAppName)) {
//
//                    PackageManager packageManager = getPackageManager();
//                    PackageInfo packageInfo = null;
//                    //在这里，该App虽然没在前台运行，也有可能在后台运行（未被结束），
//                    //为了更合理，应该先结束掉，但是注释的方法总是崩溃..........
//                    //android.os.Process.killProcess(runningTaskInfo.pid); //结束进程
//
//                    try {
//                        packageInfo = getPackageManager().getPackageInfo(packageName, 0);
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (packageInfo != null) {
//                        Intent intent = packageManager.getLaunchIntentForPackage(m_sMonitorAppName);
//                        startActivity(intent);//启动App
//                    }
//
//                }
//
//                try {
//                    Thread.sleep(3000); //延时3s
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView petView = (ImageView)findViewById(R.id.home_pet);
        sleeporwake = (Button)findViewById(R.id.sleeporwake);
        sleeporwake.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isChanged) {
                    petView.setImageDrawable(getResources().getDrawable(R.drawable.default_cat));
                    sleeporwake.setText(R.string.home_wake);
                }
                else{
                    petView.setImageDrawable(getResources().getDrawable(R.drawable.fou_riyo));
                    sleeporwake.setText(R.string.home_sleep);
                }
                isChanged = !isChanged;
            }

        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequirePermission();
                //requirePermission();
                Intent intent = new Intent(MainActivity.this, FloatWindowManagerService.class);
                startService(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,0,0)
//        {
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                getActionBar().setTitle("Nekosleep");
//                invalidateOptionsMenu(); // creates call to
//                // onPrepareOptionsMenu()
//            }
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                getActionBar().setTitle("Menu");
//                invalidateOptionsMenu(); // creates call to
//                // onPrepareOptionsMenu()
//            }
//        }
        ;
        drawer.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setItemIconTintList(null);
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
    }

    @Override
    public void onResume(){
        super.onResume();
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

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        init();
//    }
//
//    public void init(){
//        btPetBook = findViewById(R.id.pb_button);
//        btSettings = findViewById(R.id.setting_button);
//        btFloatWindow = findViewById(R.id.button);
//        btResult = findViewById(R.id.result_button);
//        btPetBook = findViewById(R.id.pb_button);
//
//        btPetBook.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, PetScreenSlideActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btSettings.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, UserSettingsActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btFloatWindow.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                checkAndRequirePermission();
//                //requirePermission();
//                Intent intent = new Intent(MainActivity.this, FloatWindowManagerService.class);
//                startService(intent);
//            }
//        });
//
//        btResult.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, SleepResultActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void requirePermission () {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 23) {//6.0以上
                try{
                    Intent  intent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkAndRequirePermission () {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.SYSTEM_ALERT_WINDOW }, 0);
                System.out.println("Success?");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            System.out.println("Success!");
        }
    }
}
