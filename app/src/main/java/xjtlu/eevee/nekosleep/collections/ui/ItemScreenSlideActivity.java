package xjtlu.eevee.nekosleep.collections.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.collections.AssetReader;
import xjtlu.eevee.nekosleep.collections.Injection;
import xjtlu.eevee.nekosleep.collections.persistence.Item;

public class ItemScreenSlideActivity extends AppCompatActivity {
    Context appContext;

    private static final String TAG = PetScreenSlideActivity.class.getSimpleName();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private ViewModelFactory petViewModelFactory;
    private PetViewModel petViewModel;

    private static int NUM_PAGE = 3;
    ViewPager page_one;
    ArrayList<View> pageList;
    LinearLayout pageIndicator;
    ViewPagerAdapter pageAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_item_activity);
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

        disposable.add(petViewModel.getAllItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemList -> {
                    //loop to add viewpager
                    for (int i = 0; i < NUM_PAGE; i++) {
                        pageList.add(getItemPage(i, itemList));
                    }

                    pageAdapter = new ViewPagerAdapter(pageList);
                    page_one.setAdapter(pageAdapter);
                    }, throwable -> Log.e(TAG, "Unable to load image", throwable)));

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
        petViewModelFactory = Injection.provideViewModelFactory(this);
        petViewModel = new ViewModelProvider(this, petViewModelFactory).get(PetViewModel.class);
    }

    private void clearIndicatorFocusedState() {
        int childCount = pageIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            pageIndicator.getChildAt(i).setEnabled(true);
        }
    }

    public View getItemPage(int pageNum, List<Item> itemList){
        View page = View.inflate(this, R.layout.fragment_items, null);
        GridLayout itemGrid = page.findViewById(R.id.gl_items);
        int itemNum = itemGrid.getChildCount();
        int last = (pageNum+1)*itemNum<itemList.size()? (pageNum+1)*itemNum : itemList.size();
        for (int i = pageNum * itemNum; i < last; i++) {
            Item item = itemList.get(i);
            if (item.active) {
                CardView itemCardView = (CardView) itemGrid.getChildAt(i-pageNum*itemNum);
                ImageView imageView = (ImageView)itemCardView.getChildAt(0);
                setItemImg(item.getItemName(), imageView);
            }
        }
        return page;
    }

    public void setItemImg(String itemName, ImageView itemView){
        Drawable item_img = AssetReader.getDrawableFromAssets(
                appContext, "itembook_img/" + itemName + ".png");
        item_img.setBounds(0,0,item_img.getIntrinsicWidth(),item_img.getIntrinsicHeight());
        itemView.setImageDrawable(item_img);
    }
}
