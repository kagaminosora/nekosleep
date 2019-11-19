package xjtlu.eevee.nekosleep.Pet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PetView extends View {


    public PetView (Context context) {
        super(context);
    }

    public PetView (Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(10);
        canvas.drawCircle(50,50, canvas.getWidth() / 10, new Paint());
    }

    private void init() {

    }

}
