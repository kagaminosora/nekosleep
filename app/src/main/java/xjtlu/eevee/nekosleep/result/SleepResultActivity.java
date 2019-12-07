package xjtlu.eevee.nekosleep.result;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Index;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.persistence.ItemDao;
import xjtlu.eevee.nekosleep.collections.persistence.PetBookDatabase;
import xjtlu.eevee.nekosleep.collections.persistence.PetDao;
import xjtlu.eevee.nekosleep.collections.ui.PetScreenSlideActivity;
import xjtlu.eevee.nekosleep.share.ShareUtil;

public class SleepResultActivity extends AppCompatActivity {
    private final CompositeDisposable disposable = new CompositeDisposable();
    static PetBookDatabase dbPet;
    static PetDao petDao;
    static ItemDao itemDao;
    private static final String TAG = PetScreenSlideActivity.class.getSimpleName();

    private TextView titleTV;
    private Drawable shareIcon;
    private Drawable backIcon;

    private ImageView resultImg;
    private TextView resultTV;

    private String type;
    private String petId;
    private String itemId;

    private Drawable itemImg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getString("type");
        if(type.equals("pet")){
            petId = bundle.getString("petId");
        }else if(type.equals("item")){
            itemId = bundle.getString("itemId");
        }
        setContentView(R.layout.sleep_result);
        init();
    }

    public void onCreate(Bundle savedInstanceState, PersistableBundle persistableState){
        super.onCreate(savedInstanceState, persistableState);
        setContentView(R.layout.sleep_result);
        init();
    }

    public void init(){
        initView();
        initIcon();
    }

    public void initView(){
        titleTV = findViewById(R.id.title_result);
        backIcon = titleTV.getCompoundDrawables()[0];
        shareIcon = titleTV.getCompoundDrawables()[2];

        resultImg = findViewById(R.id.img_result);
        resultTV = findViewById(R.id.tv_result);

        if(dbPet == null) {
            dbPet = PetBookDatabase.getInstance(getApplicationContext());
            if(petDao==null){
                petDao = dbPet.petDAO();
            }
            if(itemDao==null){
                itemDao = dbPet.itemDAO();
            }
        }
        editDatabase();
        setImg();
        setText();
    }

    public void editDatabase(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(type.equals("pet")) {
                    petDao.setActive(true, petId);
                }else if(type.equals("item")){
                    itemDao.setActive(true, itemId);
                }
            }
        });
        t.start();
    }

    public void initIcon(){
        titleTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getX() < view.getWidth() - view.getPaddingRight()
                        && motionEvent.getX() > view.getWidth() - view.getPaddingRight() - shareIcon.getBounds().width()
                        && motionEvent.getY() < shareIcon.getBounds().height() + view.getPaddingBottom()
                        && motionEvent.getY() > view.getPaddingTop()){
                    ShareUtil util = new ShareUtil(getApplicationContext(), itemImg, type);
                    Intent shareIntent = util.createShareImage();
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                }

                if (motionEvent.getX() < backIcon.getBounds().width()+ view.getPaddingLeft()
                        && motionEvent.getX() > view.getPaddingLeft()
                        && motionEvent.getY() < backIcon.getBounds().height() + view.getPaddingBottom()
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

    public void setText(){
        if(type.equals("pet")) {
            resultTV.setText(R.string.result_get_pet);
        }else if(type.equals("item")){
            resultTV.setText(R.string.result_get_item);
        }
    }

    public void setImg(){
        if(type.equals(("pet"))) {
            disposable.add(petDao.getPetById(petId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pet -> {
                                Drawable pet_img = AssetReader.getDrawableFromAssets(
                                        getApplicationContext(), "petbook_img/" + pet.getImageName() + ".png");
                                pet_img.setBounds(0, 0, pet_img.getMinimumWidth(), pet_img.getMinimumHeight());
                                itemImg = pet_img;
                                resultImg.setImageDrawable(itemImg);
                            },
                            throwable -> Log.e(TAG, "Unable to load image", throwable)));
        }else if(type.equals("item")){
            disposable.add(itemDao.findById(itemId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(item -> {
                                Drawable item_img = AssetReader.getDrawableFromAssets(
                                        getApplicationContext(), "itembook_img/" + item.getImageName() + ".png");
                                item_img.setBounds(0,0,item_img.getIntrinsicWidth(),item_img.getIntrinsicHeight());
                                itemImg = item_img;
                                resultImg.setImageDrawable(itemImg);
                            },
                            throwable -> Log.e(TAG, "Unable to load image", throwable)));
        }
    }

    public void savePreferences(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("pet", MODE_PRIVATE);
        SharedPreferences.Editor editor =  sp.edit();
        if(type.equals("pet")){
            int id = Integer.valueOf(petId)+1;
            String nextId = String.valueOf(id);
            if(id<10){
                nextId = "0000000"+nextId;
            }else {
                nextId = "000000"+nextId;
            }
            editor.putString("nextType", "pet");
            editor.putString("nextItemId", nextId);
            if(petId.equals("00000015")) {
                editor.putString("nextType", "item");
                editor.putString("nextItemId", "00000000");
            }
        }else{
            int id = Integer.valueOf(itemId)+1;
            String nextId = String.valueOf(id);
            if(id<10){
                nextId = "0000000"+nextId;
            }else if(id<100){
                nextId = "000000"+nextId;
            }else {
                nextId = "00000"+nextId;
            }
            editor.putString("nextType", "item");
            editor.putString("nextItemId", nextId);
        }
        editor.commit();
    }
}
