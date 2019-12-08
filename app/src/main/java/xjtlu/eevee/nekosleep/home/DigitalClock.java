package xjtlu.eevee.nekosleep.home;

import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;

public class DigitalClock extends android.widget.DigitalClock{

    private Calendar calendar;
    private FormatChangeObject formatChange;
    private Runnable mTicker;
    private Handler handler;
    private boolean TickerStop = false;
    private final static String m12 = "h:mm aa";
    private final static String m24 = "k:mm";
    private String format;

    public DigitalClock(Context context) {
        super(context);
        initClock(context);
    }
    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }
    private void initClock(Context context) {
        Resources resources = context.getResources();
        if(calendar==null){
            calendar = Calendar.getInstance();
        }

        formatChange = new FormatChangeObject(handler);
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI,true,formatChange);

        setFormat();
    }
    /**
     * 判断时间格式
     */
    private void setFormat() {
        if(get24HourMode()){
            format = m24;
        }else{
            format = m12;
        }
    }
    /**
     * 时间格式
     * @return
     */
    private boolean get24HourMode() {

        return android.text.format.DateFormat.is24HourFormat(getContext());
    }
    /**
     * 监听时间变化
     * @author Administrator
     *
     */
    private class FormatChangeObject extends ContentObserver{

        public FormatChangeObject(Handler handler) {
            super(handler);
        }
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            setFormat();
        }
    }
    //方法在onDraw方法之前调用，也就是view还没有画出来的时候调用，只一次
    protected void onAttachedToWindow() {

        TickerStop = false;
        handler = new Handler();

        mTicker = new Runnable() {

            public void run() {
                if(TickerStop){
                    return;
                }
                calendar.setTimeInMillis(System.currentTimeMillis());
                setText(DateFormat.format(format, calendar));
                invalidate();//刷新
                long now = SystemClock.uptimeMillis();
                long next = now+(1000-now%1000);
                //添加到消息队列，指定运行时间
                handler.postAtTime(mTicker, next);
            }
        };
        super.onAttachedToWindow();
    }

}

