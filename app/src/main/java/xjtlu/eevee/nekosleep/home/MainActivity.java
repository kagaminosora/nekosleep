package xjtlu.eevee.nekosleep.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import xjtlu.eevee.nekosleep.Pet.FloatWindowManagerService;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.ui.PetScreenSlideActivity;
import xjtlu.eevee.nekosleep.settings.UserSettingsActivity;

public class MainActivity extends AppCompatActivity {

    Button btPetBook;
    Button btSettings;
    Button btFloatWindow;
    int OVERLAY_PERMISSION_REQ_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        btPetBook = findViewById(R.id.pb_button);
        btSettings = findViewById(R.id.setting_button);
        btFloatWindow = findViewById(R.id.button);
        btPetBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PetScreenSlideActivity.class);
                startActivity(intent);
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserSettingsActivity.class);
                startActivity(intent);
            }
        });

        btFloatWindow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                checkAndRequirePermission();
                //requirePermission();
                Intent intent = new Intent(MainActivity.this, FloatWindowManagerService.class);
                startService(intent);
            }
        });
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
