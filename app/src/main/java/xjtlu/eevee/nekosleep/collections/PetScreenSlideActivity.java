package xjtlu.eevee.nekosleep.collections;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import xjtlu.eevee.nekosleep.R;

public class PetScreenSlideActivity extends AppCompatActivity {
    private static int NUM_PAGE = 4;
    ViewPager page_one;
    ArrayList<View> pageList;
    LinearLayout pageIndicator;
    PetViewPagerAdapter pageAdapter;

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

    private void clearIndicatorFocusedState() {
        int childCount = pageIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            pageIndicator.getChildAt(i).setEnabled(true);
        }
    }

    public View getPetsPage(int pageNum){
        View page = View.inflate(this, R.layout.fragment_pets, null);
        GridLayout gl_pets = findViewById(R.id.gl_pets);

        switch (pageNum){
            case 0:
                //gl_pets.getChildCount();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        return page;
    }
}
