package xjtlu.eevee.nekosleep.collections.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.persistence.Item;
import xjtlu.eevee.nekosleep.collections.persistence.ItemDao;
import xjtlu.eevee.nekosleep.collections.persistence.PetBookDatabase;
import xjtlu.eevee.nekosleep.collections.persistence.PetDao;

public class ChooseItemActivity extends AppCompatActivity {
    private final CompositeDisposable disposable = new CompositeDisposable();
    static PetDao petDao;
    static PetBookDatabase dbPet;
    static ItemDao itemDao;
    private Context appContext;
    private static final String TAG = PetScreenSlideActivity.class.getSimpleName();
    static ImageView previousBgView;

    String petId;
    String itemId;
    ImageView chosenPet;
    ImageView chosenItem;
    GridLayout itemGrid;
    TextView cItemTV;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        petId = bundle.getString("petId");
        setContentView(R.layout.choose_item);
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

    public void initPetDao(){
        if(dbPet == null) {
            dbPet = PetBookDatabase.getInstance(appContext);
            if(petDao==null){
                petDao = dbPet.petDAO();
            }
            if(itemDao==null){
                itemDao = dbPet.itemDAO();
            }
        }
            //mViewModelFactory = Injection.provideViewModelFactory(this);
            //mViewModel = new ViewModelProvider(this, mViewModelFactory).get(PetViewModel.class);        }
    }

    public void init(){
        appContext = getApplicationContext();
        initPetDao();
        chosenPet = findViewById(R.id.img_cip);
        setPetImg();
        chosenItem = findViewById(R.id.img_cii);
        initGridLayout();
        cItemTV = findViewById(R.id.tv_choose_item);
        initChooseItemTV();
    }

    public void setPetImg(){
        disposable.add(petDao.getPetById(petId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pet -> {
                            Drawable pet_img = AssetReader.getDrawableFromAssets(
                                    appContext, "petbook_img/" + pet.getImageName() + ".png");
                            pet_img.setBounds(0,0,pet_img.getMinimumWidth(),pet_img.getMinimumHeight());
                            chosenPet.setImageDrawable(pet_img);
                        },
                        throwable -> Log.e(TAG, "Unable to load image", throwable)));
    }

    public void setItemImg(String itemId, ImageView itemView){
        disposable.add(itemDao.findById(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                            Drawable item_img = AssetReader.getDrawableFromAssets(
                                    appContext, "itembook_img/" + item.getImageName() + ".png");
                            item_img.setBounds(0,0,item_img.getIntrinsicWidth(),item_img.getIntrinsicHeight());
                            itemView.setImageDrawable(item_img);
                        },
                        throwable -> Log.e(TAG, "Unable to load image", throwable)));
    }

    public void initGridLayout(){
        itemGrid = findViewById(R.id.gl_ch_items);
        int itemNum = itemGrid.getChildCount();

        disposable.add(itemDao.getItemByPetId(petId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemList -> {
                            for (int i=0; i<itemList.size(); i++){
                                CardView itemCardView = (CardView) itemGrid.getChildAt(i);
                                ImageView bgView = (ImageView) itemCardView.getChildAt(0);
                                ImageView itemView = (ImageView) itemCardView.getChildAt(1);

                                String itemId = itemList.get(i).getId();
                                setItemImg(itemId, itemView);
                                itemCardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Drawable bg = getResources().getDrawable(R.drawable.crv_cii_bg);
                                        bgView.setImageDrawable(bg);
                                        if(previousBgView!=null) {
                                            previousBgView.setImageDrawable(null);
                                        }
                                        previousBgView = bgView;
                                        setItemImg(itemId, chosenItem);
                                        setItemId(itemId);
                                    }
                                });
                            }
                        },
                        throwable -> Log.e(TAG, "Unable to load image", throwable)));
        itemDao.getItemByPetId(petId);
    }

    public void initChooseItemTV(){
        Drawable closeIcon = cItemTV.getCompoundDrawables()[0];
        cItemTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getX() < closeIcon.getBounds().width()+ view.getPaddingLeft()
                        && motionEvent.getX() > view.getPaddingLeft()
                        && motionEvent.getY() < closeIcon.getBounds().height() + view.getPaddingBottom()
                        && motionEvent.getY() > view.getPaddingBottom()){
                    savePreferences();

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
        editor.putString("itemId", itemId);
        editor.commit();
        //String petIdSP = sp.getString("petId","empty");
        //String itemIdSP = sp.getString("itemId","empty");
        //Toast.makeText(getApplicationContext(),petIdSP+", "+itemIdSP, Toast.LENGTH_SHORT).show();
    }

    public void setItemId(String itemId){
        this.itemId = itemId;
    }
}
