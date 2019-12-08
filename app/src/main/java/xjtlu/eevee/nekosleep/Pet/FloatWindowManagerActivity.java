package xjtlu.eevee.nekosleep.Pet;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import xjtlu.eevee.nekosleep.R;

public class FloatWindowManagerActivity extends AppCompatActivity {

    int OVERLAY_PERMISSION_REQ_CODE = 0;
    boolean petStarted = false;
    Intent petIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!petStarted) {
                    petIntent = new Intent(getApplicationContext(), FloatWindowManagerService.class);
                    startService(petIntent);
                    petStarted = true;
                    System.out.println("Service started.");
                } else {
                    stopService(petIntent);
                    petStarted = false;
                    System.out.println("Service stopped.");
                }
            }
        });
        requirePermission();
    }

    private void requirePermission () {
        if (Build.VERSION.SDK_INT >= 23) {//6.0以上
            if (!Settings.canDrawOverlays(this)) {
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

    public void launchPet () {
        if (!petStarted) {
            petIntent = new Intent(getApplicationContext(), FloatWindowManagerService.class);
            startService(petIntent);
            petStarted = true;
            System.out.println("Service started.");
        } else {
            stopService(petIntent);
            petStarted = false;
            System.out.println("Service stopped.");
        }
    }

}
