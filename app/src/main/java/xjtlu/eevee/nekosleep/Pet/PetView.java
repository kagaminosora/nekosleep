package xjtlu.eevee.nekosleep.Pet;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;

import xjtlu.eevee.nekosleep.R;

public class PetView extends AppCompatImageView {

    private float x;
    private float y;
    private ArrayList <String> animations;
    LinearLayout container;
    static int height = 170;
    static int width = 170;
    private int petNum;
    private Context mContext;


    public PetView (Context context) {
        super(context);
        init();
        this.mContext = context;
    }

    public PetView (Context context, AttributeSet attrs){
        super(context, attrs);
        init();
        this.mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void init() {
        x = 10;
        y = 10;
        animations = new ArrayList<>();
        pikaqiuAnim ();
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setImageResource(R.drawable.pikaqiu_walking);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("ACTION DOWN");
            case MotionEvent.ACTION_MOVE:
                System.out.println("ACTION MOVE");
        }
        return super.onTouchEvent(motionEvent);
    }

    public void doRandomAction () {
        int actionNumber = (int) (Math.random() * animations.size());
        switch (actionNumber) {
            case 0:
                this.setImageResource(getResources().getIdentifier(animations.get(0), "drawable", "xjtlu.eevee.nekosleep"));
                break;
            case 1:
                this.setImageResource(getResources().getIdentifier(animations.get(1), "drawable", "xjtlu.eevee.nekosleep"));
                break;
            case 2:
                this.setImageResource(getResources().getIdentifier(animations.get(2), "drawable", "xjtlu.eevee.nekosleep"));
                break;
            case 3:
                this.setImageResource(getResources().getIdentifier(animations.get(3), "drawable", "xjtlu.eevee.nekosleep"));
                break;
            case 4:
                this.setImageResource(getResources().getIdentifier(animations.get(4), "drawable", "xjtlu.eevee.nekosleep"));
                break;
        }
        AnimationDrawable animationDrawable = (AnimationDrawable)this.getDrawable();
        animationDrawable.start();
    }

    private void pikaqiuAnim () {
        animations.add("pikaqiu_walking");
        animations.add("pikaqiu_wave");
        animations.add("pikaqiu_sit");
        animations.add("pikaqiu_sit2");
        animations.add("pikaqiu_crawl");
    }

    public void setPetNum (int petNum) {
        this.petNum = petNum;
    }
}
