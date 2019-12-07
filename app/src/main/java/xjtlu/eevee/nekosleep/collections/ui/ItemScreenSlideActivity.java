package xjtlu.eevee.nekosleep.collections.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.persistence.Pet;
import xjtlu.eevee.nekosleep.collections.persistence.PetBookDatabase;
import xjtlu.eevee.nekosleep.collections.persistence.PetDao;
import xjtlu.eevee.nekosleep.menu.MainActivity;
import xjtlu.eevee.nekosleep.settings.UserSettingsActivity;

public class ItemScreenSlideActivity extends AppCompatActivity {
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
    PetViewPagerAdapter pageAdapter;

    List<Pet> petList;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        init();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
//        mDrawerToggle.syncState();
        setSupportActionBar(toolbar);

//        drawer.addDrawerListener(mDrawerToggle);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
//        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent it = new Intent(ItemScreenSlideActivity.this, MainActivity.class);
                        startActivity(it);
                        return true;
                    case R.id.nav_pets:
                        Intent it1 = new Intent(ItemScreenSlideActivity.this, PetScreenSlideActivity.class);
                        startActivity(it1);
                        return true;
                    case R.id.nav_items:
                        Intent it2 = new Intent(ItemScreenSlideActivity.this, ItemScreenSlideActivity.class);
                        startActivity(it2);
                        return true;
                    case R.id.nav_settings:
                        Intent it3 = new Intent(ItemScreenSlideActivity.this, UserSettingsActivity.class);
                        startActivity(it3);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.view_pager_item_activity);
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

        pageAdapter = new PetViewPagerAdapter(pageList);
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
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ItemScreenSlideActivity.this, ChooseItemActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("petId", finalPetId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            final TextView tv_pet;
            switch (i) {
                case 0:
                    tv_pet = gl_pets.findViewById(R.id.pb_name0);
                    break;
                case 1:
                    tv_pet = gl_pets.findViewById(R.id.pb_name1);
                    break;
                case 2:
                    tv_pet = gl_pets.findViewById(R.id.pb_name2);
                    break;
                case 3:
                    tv_pet = gl_pets.findViewById(R.id.pb_name3);
                    break;
                default:
                    tv_pet = gl_pets.findViewById(R.id.pb_name0);
            }
            disposable.add(petDao.getPetById(petId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pet -> {
                                Drawable pet_img = getResources().getDrawable(R.drawable.default_cat);
                                //if(pet.isActive()) {
                                pet_img = AssetReader.getDrawableFromAssets(
                                        appContext, "petbook_img/" + pet.getImageName() + ".png");
                                //}
                                pet_img.setBounds(0,0,pet_img.getMinimumWidth(),pet_img.getMinimumHeight());
                                tv_pet.setCompoundDrawablesWithIntrinsicBounds(null, pet_img, null, null);
                                tv_pet.setText(pet.getPetName());
                            },
                            throwable -> Log.e(TAG, "Unable to load image", throwable)));
        }
        return page;
    }
}
