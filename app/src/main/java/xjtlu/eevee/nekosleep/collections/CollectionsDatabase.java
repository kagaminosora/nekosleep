package xjtlu.eevee.nekosleep.collections;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CollectionsDatabase extends SQLiteOpenHelper {
    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "collections";

    public CollectionsDatabase(Context context){
        super(context, DATABASE_NAME, null,  DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_NEW_TABLE =     "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
//                FeedEntry._ID + " INTEGER PRIMARY KEY," +
//                FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
//                FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
