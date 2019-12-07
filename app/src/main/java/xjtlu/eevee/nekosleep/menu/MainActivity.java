package xjtlu.eevee.nekosleep.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import xjtlu.eevee.nekosleep.Pet.FloatWindowManagerService;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.ui.ChooseItemActivity;
import xjtlu.eevee.nekosleep.collections.ui.ItemScreenSlideActivity;
import xjtlu.eevee.nekosleep.collections.ui.PetScreenSlideActivity;
import xjtlu.eevee.nekosleep.settings.UserSettingsActivity;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

//    Button btPetBook;
//    Button btSettings;
//    Button btFloatWindow;
//    Button btResult;
//
    int OVERLAY_PERMISSION_REQ_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
        mDrawerToggle.syncState();
        setSupportActionBar(toolbar);

        drawer.addDrawerListener(mDrawerToggle);
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
