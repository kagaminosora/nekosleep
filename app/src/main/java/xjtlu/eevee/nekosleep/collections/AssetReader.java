package xjtlu.eevee.nekosleep.collections;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;


public class AssetReader {
    /*
    * Load a drawable from assets, may get a null.
    */
    public static Bitmap loadImageFromAssets(Context context, String url){
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(url);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }

        return bitmap;
    }
}
