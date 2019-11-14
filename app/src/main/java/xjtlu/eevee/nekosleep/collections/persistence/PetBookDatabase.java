package xjtlu.eevee.nekosleep.collections.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Pet.class, Item.class}, version = 1)
public abstract class PetBookDatabase extends RoomDatabase {
    private static volatile PetBookDatabase INSTANCE;
    public abstract PetDao petDAO();
    public abstract ItemDao itemDAO();

    public static final String PET_DATABASE_NAME = "book_pet";
    public static final String ITEM_DATABASE_NAME = "book_item";

    public static PetBookDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (PetBookDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, PetBookDatabase.class, "PetBook.db")
                            .createFromAsset("database/petbook.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
