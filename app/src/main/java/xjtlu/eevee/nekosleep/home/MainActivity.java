package xjtlu.eevee.nekosleep.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import xjtlu.eevee.nekosleep.Pet.FloatWindowManagerService;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.ui.ChooseItemActivity;
import xjtlu.eevee.nekosleep.collections.ui.ItemScreenSlideActivity;
import xjtlu.eevee.nekosleep.collections.ui.PetScreenSlideActivity;
import xjtlu.eevee.nekosleep.result.SleepResultActivity;
import xjtlu.eevee.nekosleep.settings.UserSettingsActivity;

public class MainActivity extends AppCompatActivity {

    Button btPetBook;
    Button btSettings;
    Button btFloatWindow;
    Button btResult;
    Button btItems;

    CardView cvPet;
    private final CompositeDisposable disposable = new CompositeDisposable();

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
        btResult = findViewById(R.id.result_button);
        btPetBook = findViewById(R.id.pb_button);
        btItems = findViewById(R.id.item_button);
        cvPet = findViewById(R.id.cv_pet);

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

        btResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        btItems.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ItemScreenSlideActivity.class);
                startActivity(intent);
            }
        });

        cvPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        initPetView();
    }

    public void initPetView(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("pet", MODE_PRIVATE);
        String petId = sp.getString("petId", "empty");
        String itemId = sp.getString("itemId", "empty");
        String petImgName = sp.getString("petImgName", "empty");
        String itemImgName = sp.getString("itemImgName", "empty");
        setImg((ImageView) cvPet.getChildAt(0), petImgName, "pet");
        setImg((ImageView) cvPet.getChildAt(1), itemImgName, "item");
    }

    public void setImg(ImageView view, String name, String type){
        if(name.equals("empty")) {
            view.setImageDrawable(null);
        }else {
            Drawable item_img = AssetReader.getDrawableFromAssets(
                    getApplicationContext(), type + "book_img/" + name + ".png");
            item_img.setBounds(0, 0, item_img.getIntrinsicWidth(), item_img.getIntrinsicHeight());
            view.setImageDrawable(item_img);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initPetView();
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
