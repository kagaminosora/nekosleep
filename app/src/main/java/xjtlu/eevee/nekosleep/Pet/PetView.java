package xjtlu.eevee.nekosleep.Pet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

public class PetView extends AppCompatImageView {

    private float x;
    private float y;
    LinearLayout container;
    static int height = 170;
    static int width = 170;


    public PetView (Context context) {
        super(context);
        init();
    }

    public PetView (Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Paint p = new Paint();
//        p.setColor(Color.RED);
//        p.setStrokeWidth(10);
//        canvas.drawCircle(x,y, canvas.getWidth() / 10, new Paint());
    }

    private void init() {
        x = 10;
        y = 10;
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        AnimationDrawable pikaqiuWalking = (AnimationDrawable)this.getDrawable();
        pikaqiuWalking.start();
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL: {
//                doRandomAction();
//            }
//            case MotionEvent.ACTION_MOVE:
//                container.setX(event.getX());
//                container.setY(event.getY());
//                container.postInvalidate();
//        }
        return true;
    }

    private void doRandomAction () {

    }


}
