package xjtlu.eevee.nekosleep.result.share;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import xjtlu.eevee.nekosleep.R;

public class ShareUtil{
    Context context;
    Drawable itemImg;
    String type;

    public ShareUtil(Context context, Drawable itemImg, String type){
        this.context = context;
        this.itemImg = itemImg;
        this.type = type;
    }

    public Intent createShareImage(){
        ShareView shareView = new ShareView(context);
        if(type.equals("pet")) {
            shareView.setInfo(context.getResources().getString(R.string.share_text_pet));
        }else if(type.equals("item")){
            shareView.setInfo(context.getResources().getString(R.string.share_text_item));
        }else if(type.equals("finish")){
            shareView.setInfo("I got all items and pets in NekoSleep!");
        }
        shareView.setImg(itemImg);
        shareView.setQRCode(generateQRCode());
        final Bitmap image = shareView.createImage();
        final Uri uri = saveImage(image);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //Log.d("share", uri.toString());
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        //Log.d("share", "putExtra");
        shareIntent.setType("image/png");
        if (image != null && !image.isRecycled()) {
            image.recycle();
        }
        return shareIntent;
    }

    public Uri saveImage(Bitmap bitmap){
        File path = context.getCacheDir();
        String fileName = "shareImage.png";
        File file = new File(path, fileName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return FileProvider.getUriForFile(context, "xjtlu.eevee.nekosleep.fileprovider", file);
    }

    public Bitmap generateQRCode(){
        int BLACK = 0xff000000;
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode("NekoSleep", BarcodeFormat.QR_CODE, 350, 350);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
