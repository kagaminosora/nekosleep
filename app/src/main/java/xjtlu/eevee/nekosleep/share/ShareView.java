package xjtlu.eevee.nekosleep.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import xjtlu.eevee.nekosleep.R;

public class ShareView extends FrameLayout {

    private final int IMAGE_WIDTH = 1080;
    private final int IMAGE_HEIGHT = 1920;

    private TextView shareTV;
    private ImageView shareImg;
    private ImageView shareQR;

    public ShareView(@NonNull Context context) {
        super(context);
        init();
    }

    public void init(){
        View layout = View.inflate(getContext(), R.layout.share_view_layout, this);
        shareImg = layout.findViewById(R.id.img_share);
        shareTV = layout.findViewById(R.id.tv_share);
        shareQR = layout.findViewById(R.id.img_shareQR);
    }

    public void setInfo(String info){
        shareTV.setText(info);
    }

    public void setImg(Drawable drawable){
        shareImg.setImageDrawable(drawable);
    }

    public void setQRCode(Bitmap qrCode){
        shareQR.setImageBitmap(qrCode);
    }

    public Bitmap createImage(){
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(IMAGE_WIDTH, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(IMAGE_HEIGHT, MeasureSpec.EXACTLY);

        measure(widthMeasureSpec, heightMeasureSpec);
        layout(0,0,IMAGE_WIDTH, IMAGE_HEIGHT);
        Bitmap bitmap = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        draw(canvas);

        return bitmap;
    }
}
