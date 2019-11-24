package xjtlu.eevee.nekosleep.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.settings.UserSettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button settingButton = findViewById(R.id.button2);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        UserSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
