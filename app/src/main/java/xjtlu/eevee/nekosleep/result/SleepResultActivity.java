package xjtlu.eevee.nekosleep.result;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Index;

import xjtlu.eevee.nekosleep.R;
import xjtlu.eevee.nekosleep.share.ShareUtil;

public class SleepResultActivity extends AppCompatActivity {
    TextView titleTV;
    Drawable shareIcon;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_result);
        init();
    }

    public void onCreate(Bundle savedInstanceState, PersistableBundle persistableState){
        super.onCreate(savedInstanceState, persistableState);
        setContentView(R.layout.sleep_result);
        init();
    }

    public void init(){
        titleTV = findViewById(R.id.title_result);
        initShareFunction();
    }

    public void initShareFunction(){
        shareIcon = titleTV.getCompoundDrawables()[2];
        titleTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getX() < view.getWidth() - view.getPaddingRight()
                        && motionEvent.getX() > view.getWidth() - view.getPaddingRight() - shareIcon.getBounds().width()
                        && motionEvent.getY() < shareIcon.getBounds().height() + view.getPaddingBottom()
                        && motionEvent.getY() > view.getPaddingTop()){
                    ShareUtil util = new ShareUtil(getApplicationContext());
                    Intent shareIntent = util.createShareImage();
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                }
                return false;
            }

            public boolean performClick(){
                return false;
            }
        });
    }
}
