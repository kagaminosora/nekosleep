package xjtlu.eevee.nekosleep.collections.ui;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> pages;

    public ViewPagerAdapter(List<View> pages){
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    public Object instantiateItem(ViewGroup container, int position){
        ViewGroup parent = (ViewGroup) pages.get(position).getParent();
        //Unbind from an existing parent
        if (parent != null)
            parent.removeAllViews();
        container.addView(pages.get(position));
        return pages.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pages.get(position));
    }
}
