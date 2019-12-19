package xjtlu.eevee.nekosleep.collections.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.Injection;
import xjtlu.eevee.nekosleep.collections.persistence.Item;
import xjtlu.eevee.nekosleep.collections.persistence.Pet;
import xjtlu.eevee.nekosleep.share.ShareUtil;

public class ChooseItemActivity extends AppCompatActivity {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private Context appContext;
    private static final String TAG = PetScreenSlideActivity.class.getSimpleName();
    private ImageView previousBgView;

    private ViewModelFactory petViewModelFactory;
    private PetViewModel petViewModel;

    private Pet chosenPet;
    private Item chosenItem;
    private String petId;
    private String itemId;
    private ImageView chosenPetImg;
    private ImageView chosenItemImg;
    private GridLayout itemGrid;
    private TextView cItemTV;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Bundle bundle = this.getIntent().getExtras();
        petId = bundle.getString("petId");
        setContentView(R.layout.choose_item);
        init();
    }

    public void init(){
        Bundle bundle = this.getIntent().getExtras();
        petId = bundle.getString("petId");
        setContentView(R.layout.choose_item);
        appContext = getApplicationContext();
        initViews();
        initDatabase();
        initObjects();
        initGridLayout();
        initChooseItemTV();
    }

    public void initViews(){
        chosenPetImg = findViewById(R.id.img_cip);
        chosenItemImg = findViewById(R.id.img_cii);
        cItemTV = findViewById(R.id.tv_choose_item);
    }

    public void initDatabase(){
        petViewModelFactory = Injection.provideViewModelFactory(this);
        petViewModel = new ViewModelProvider(this, petViewModelFactory).get(PetViewModel.class);
    }

    public void initObjects(){
        disposable.add(petViewModel.getPet(petId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pet -> {
                            chosenPet = pet;
                            setPetImg();
                        },
                        throwable -> Log.e(TAG, "Unable to load image", throwable)));
    }

    public void setPetImg(){
        Bitmap pet_img = AssetReader.loadImageFromAssets(
                appContext, "petbook_img/" + chosenPet.getImgName() + ".png");
        Drawable pet_dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(pet_img, 100, 100, true));
        chosenPetImg.setImageDrawable(pet_dr);
    }

    public void setItemImg(String itemName, ImageView itemView){
        Bitmap item_img = AssetReader.loadImageFromAssets(
                appContext, "itembook_img/" + itemName + ".png");
        Drawable item_dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(item_img, 100, 100, true));
        itemView.setImageDrawable(item_dr);
    }

    public void initGridLayout(){
        itemGrid = findViewById(R.id.gl_ch_items);

        disposable.add(petViewModel.getPetItems(petId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemList -> {
                        for (int i=0; i<itemList.size(); i++){
                            Item item = itemList.get(i);
                            if(item.isActive()) {
                                CardView itemCardView = (CardView) itemGrid.getChildAt(i);
                                ImageView bgView = (ImageView) itemCardView.getChildAt(0);
                                ImageView itemView = (ImageView) itemCardView.getChildAt(1);
                                setItemImg(item.getImgName(), itemView);
                                itemCardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Drawable bg = getResources().getDrawable(R.drawable.crv_cii_bg);
                                        if (previousBgView != null) {
                                            previousBgView.setImageDrawable(null);
                                        }
                                        previousBgView = bgView;
                                        bgView.setImageDrawable(bg);
                                        chosenItem = item;
                                        setItemImg(item.getImgName(), chosenItemImg);
                                    }
                                });
                            }
                        }
                        },
                        throwable -> Log.e(TAG, "Unable to load image", throwable)));
    }

    public void initChooseItemTV(){
        Drawable closeIcon = cItemTV.getCompoundDrawables()[0];
        Drawable confirmIcon = cItemTV.getCompoundDrawables()[2];
        cItemTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getX() < view.getWidth() - view.getPaddingRight()
                        && motionEvent.getX() > view.getWidth() - view.getPaddingRight() - confirmIcon.getBounds().width()
                        && motionEvent.getY() < confirmIcon.getBounds().height() + view.getPaddingBottom()
                        && motionEvent.getY() > view.getPaddingTop()){
                    savePreferences();
                    finish();
                }

                if (motionEvent.getX() < closeIcon.getBounds().width()+ view.getPaddingLeft()
                        && motionEvent.getX() > view.getPaddingLeft()
                        && motionEvent.getY() < closeIcon.getBounds().height() + view.getPaddingBottom()
                        && motionEvent.getY() > view.getPaddingBottom()){
                    Intent intent = new Intent(ChooseItemActivity.this, PetScreenSlideActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }

            public boolean performClick(){
                return false;
            }
        });
    }

    public void savePreferences(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("pet", MODE_PRIVATE);
        SharedPreferences.Editor editor =  sp.edit();
        editor.putString("petId", petId);
        editor.putString("petImgName", chosenPet.getImgName());
        if(chosenItem == null){
            editor.putString("itemId", "empty");
            editor.putString("itemImgName", "empty");
        }else {
            editor.putString("itemId", itemId);
            editor.putString("itemImgName", chosenItem.getImgName());
        }
        editor.commit();
    }

    public void setItemId(String itemId){
        this.itemId = itemId;
    }
}
