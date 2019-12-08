package xjtlu.eevee.nekosleep.collections.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.persistence.Pet;
import xjtlu.eevee.nekosleep.collections.persistence.PetBookDatabase;
import xjtlu.eevee.nekosleep.collections.persistence.PetDao;

public class PetScreenSlideActivity extends AppCompatActivity {
    Context appContext;
    static PetBookDatabase dbPet;
    static PetDao petDao;


    private static final String TAG = PetScreenSlideActivity.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();
    //private ViewModelFactory mViewModelFactory;
    //private PetViewModel mViewModel;

    private static int NUM_PAGE = 4;
    ViewPager page_one;
    ArrayList<View> pageList;
    LinearLayout pageIndicator;
    ViewPagerAdapter pageAdapter;

    List<Pet> petList;

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
        initPetData();

        //viewpager
        page_one = (ViewPager) findViewById(R.id.view_pager_pets);
        pageList = new ArrayList<View>();

        //Set the first dot to inactive
        pageIndicator = (LinearLayout) findViewById(R.id.page_indicator);
        pageIndicator.getChildAt(0).setEnabled(false);
        pageIndicator.getChildAt(0).setActivated(true);

        //loop to add viewpager
        for (int i = 0; i < NUM_PAGE; i++) {
            pageList.add(getPetsPage(i));
        }

        pageAdapter = new ViewPagerAdapter(pageList);
        page_one.setAdapter(pageAdapter);

        // Scroll Listener
        page_one.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    public void initPetData(){
        if(dbPet == null) {
            dbPet = PetBookDatabase.getInstance(appContext);
            petDao = dbPet.petDAO();
        }
        //mViewModelFactory = Injection.provideViewModelFactory(this);
        //mViewModel = new ViewModelProvider(this, mViewModelFactory).get(PetViewModel.class);
    }

    private void clearIndicatorFocusedState() {
        int childCount = pageIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            pageIndicator.getChildAt(i).setEnabled(true);
        }
    }

    public View getPetsPage(int pageNum){
        View page = View.inflate(this, R.layout.fragment_pets, null);
        GridLayout gl_pets = page.findViewById(R.id.gl_pets);
        int petNum = gl_pets.getChildCount();

        for(int i=0; i<petNum; i++){
            String petId = "00000000";
            int index = pageNum*4+i;
            if(index<10){
                petId = "0000000"+index;
            }else if(index<100){
                petId = "000000"+index;
            }else{
                petId = "00000"+index;
            }
            CardView cv = (CardView)gl_pets.getChildAt(i);
            String finalPetId = petId;
            TextView tv_pet = (TextView) cv.getChildAt(0);
            disposable.add(petDao.getPetById(petId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pet -> {
                                Drawable pet_img = getResources().getDrawable(R.drawable.default_cat);
                                if(pet.isActive()) {
                                    pet_img = AssetReader.getDrawableFromAssets(
                                            appContext, "petbook_img/" + pet.getImageName() + ".png");
                                    cv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(PetScreenSlideActivity.this, ChooseItemActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("petId", finalPetId);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    });
                                }
                                pet_img.setBounds(0,0,pet_img.getMinimumWidth(),pet_img.getMinimumHeight());
                                tv_pet.setCompoundDrawablesWithIntrinsicBounds(null, pet_img, null, null);
                                tv_pet.setText(pet.getPetName());
                            },
                            throwable -> Log.e(TAG, "Unable to load image", throwable)));
        }
        return page;
    }
}
