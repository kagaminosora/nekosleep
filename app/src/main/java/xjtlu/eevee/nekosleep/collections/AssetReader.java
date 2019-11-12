package xjtlu.eevee.nekosleep.collections;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;


public class AssetReader {
    /*
    * Load a drawable from assets, may get a null.
    */
    public static Drawable getDrawableFromAssets(Context context, String url){
        Drawable drawable = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(url);
            drawable = Drawable.createFromStream(inputStream, null);
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

        return drawable;
    }
}
