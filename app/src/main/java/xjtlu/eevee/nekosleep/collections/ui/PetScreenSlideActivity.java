package xjtlu.eevee.nekosleep.collections.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.Injection;
import xjtlu.eevee.nekosleep.collections.persistence.Pet;
import xjtlu.eevee.nekosleep.share.ShareUtil;

public class PetScreenSlideActivity extends AppCompatActivity {
    Context appContext;

    private TextView titleTV;

    private static final String TAG = PetScreenSlideActivity.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private ViewModelFactory petViewModelFactory;
    private PetViewModel petViewModel;

    private static int NUM_PAGE = 4;
    ViewPager viewPager;
    ArrayList<View> pageList;
    LinearLayout pageIndicator;
    ViewPagerAdapter pageAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_pets_activity);
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.view_pager_pets_activity);
        init();
    }

    public void init() {
        appContext = getApplicationContext();
        initTitle();
        initPetData();
        initViewPager();
    }

    public void initTitle(){
        titleTV = findViewById(R.id.pet_title);
        Drawable backIcon = titleTV.getCompoundDrawables()[0];
        titleTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getX() < backIcon.getBounds().width()+ view.getPaddingLeft()
                        && motionEvent.getX() > view.getPaddingLeft()
                        && motionEvent.getY() < backIcon.getBounds().height() + view.getPaddingBottom()
                        && motionEvent.getY() > view.getPaddingBottom()){
                    finish();
                }
                return false;
            }
        });
    }

    public void initPetData(){
        petViewModelFactory = Injection.provideViewModelFactory(this);
        petViewModel = new ViewModelProvider(this, petViewModelFactory).get(PetViewModel.class);
    }

    public void initViewPager(){
        //viewpager
        viewPager = (ViewPager) findViewById(R.id.view_pager_pets);
        pageList = new ArrayList<View>();

        //Set the first dot to inactive
        pageIndicator = (LinearLayout) findViewById(R.id.page_indicator);
        pageIndicator.getChildAt(0).setEnabled(false);
        pageIndicator.getChildAt(0).setActivated(true);

        //loop to add viewpager
        disposable.add(petViewModel.getAllPets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pets -> {
                            for (int i = 0; i < NUM_PAGE; i++) {
                                pageList.add(getPetsPage(i, pets));
                            }
                            pageAdapter = new ViewPagerAdapter(pageList);
                            viewPager.setAdapter(pageAdapter);
                        },
                        throwable -> Log.e(TAG, "Unable to load image", throwable)));

        // Scroll Listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int lastPage;
            //during scroll
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // after scroll
            @Override
            public void onPageSelected(int position) {
                clearIndicatorFocusedState();
                pageIndicator.getChildAt(position).setEnabled(false);
                pageIndicator.getChildAt(position).setActivated(true);
                pageIndicator.getChildAt(lastPage).setActivated(false);
                lastPage=position;
            }

            // before scroll
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void clearIndicatorFocusedState() {
        int childCount = pageIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            pageIndicator.getChildAt(i).setEnabled(true);
        }
    }

    public View getPetsPage(int pageNum, List<Pet> pets){
        View page = View.inflate(this, R.layout.fragment_pets, null);
        GridLayout gl_pets = page.findViewById(R.id.gl_pets);
        int last = (pageNum+1)*4<pets.size()? (pageNum+1)*4 : pets.size();
        for (int i=pageNum*4; i < last; i++) {
            CardView cv = (CardView) gl_pets.getChildAt(i%4);
            TextView tv_pet = (TextView) cv.getChildAt(0);
            Pet pet = pets.get(i);
            Bitmap pet_img;
            if (pet.isActive()) {
                pet_img = AssetReader.loadImageFromAssets(
                        appContext, "petbook_img/" + pet.getImgName() + ".png");

                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PetScreenSlideActivity.this, ChooseItemActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("petId", pet.getId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }else {
                pet_img = AssetReader.loadImageFromAssets(appContext, "petbook_img/cat_footprint.png");
            }
            Drawable pet_dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(pet_img, 200, 200, true));
            tv_pet.setCompoundDrawablesWithIntrinsicBounds(null, pet_dr, null, null);
            tv_pet.setText(pet.getName());
        }
        return page;
    }
}
