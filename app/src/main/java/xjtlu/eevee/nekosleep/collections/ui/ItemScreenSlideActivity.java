package xjtlu.eevee.nekosleep.collections.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.persistence.Item;
import xjtlu.eevee.nekosleep.collections.persistence.ItemDao;
import xjtlu.eevee.nekosleep.collections.persistence.PetBookDatabase;
import xjtlu.eevee.nekosleep.collections.persistence.PetDao;
import xjtlu.eevee.nekosleep.menu.MainActivity;
import xjtlu.eevee.nekosleep.settings.UserSettingsActivity;

public class ItemScreenSlideActivity extends AppCompatActivity {
    Context appContext;
    static PetBookDatabase dbPet;
    static ItemDao itemDao;

    private static final String TAG = PetScreenSlideActivity.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();
    //private ViewModelFactory mViewModelFactory;
    //private PetViewModel mViewModel;

    private static int NUM_PAGE = 3;
    ViewPager page_one;
    ArrayList<View> pageList;
    LinearLayout pageIndicator;
    ViewPagerAdapter pageAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        init();
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
        page_one = (ViewPager) findViewById(R.id.view_pager_items);
        pageList = new ArrayList<View>();

        //Set the first dot to inactive
        pageIndicator = (LinearLayout) findViewById(R.id.page_indicator);
        pageIndicator.getChildAt(0).setEnabled(false);
        pageIndicator.getChildAt(0).setActivated(true);

        //loop to add viewpager
        for (int i = 0; i < NUM_PAGE; i++) {
            pageList.add(getItemPage(i));
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
            itemDao = dbPet.itemDAO();
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

    public View getItemPage(int pageNum){
        View page = View.inflate(this, R.layout.fragment_items, null);
        GridLayout itemGrid = page.findViewById(R.id.gl_items);
        int itemNum = itemGrid.getChildCount();

        disposable.add(itemDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemList -> {
                            if((pageNum+1)*itemNum<itemList.size()) {
                                for (int i = pageNum * itemNum; i < (pageNum+1) * itemNum; i++) {
                                    Item item = itemList.get(i);
                                    if (item.active) {
                                        CardView itemCardView = (CardView) itemGrid.getChildAt(i-pageNum*itemNum);
                                        ImageView imageView = (ImageView)itemCardView.getChildAt(0);
                                        setItemImg(item.getId(), imageView);
                                    }
                                }
                            }else {
                                for (int i = pageNum * itemNum; i < itemList.size(); i++) {
                                    Item item = itemList.get(i);
                                    if (item.active) {
                                        CardView itemCardView = (CardView) itemGrid.getChildAt(i-pageNum*itemNum);
                                        ImageView imageView = (ImageView)itemCardView.getChildAt(0);
                                        setItemImg(item.getId(), imageView);
                                    }
                                }
                            }
                        },
                        throwable -> Log.e(TAG, "Unable to load image", throwable)));
        return page;
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
}
