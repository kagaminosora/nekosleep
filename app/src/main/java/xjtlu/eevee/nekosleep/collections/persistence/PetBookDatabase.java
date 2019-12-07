package xjtlu.eevee.nekosleep.collections.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Pet.class, Item.class}, version = 1, exportSchema = false)
public abstract class PetBookDatabase extends RoomDatabase {
    private static volatile PetBookDatabase INSTANCE;
    public abstract PetDao petDAO();
    public abstract ItemDao itemDAO();

    public static final String DATABASE_NAME = "pet_book";
    public static final String ITEM_DATABASE_NAME = "book_item";

    // Provide and instance of the database
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


    // For database migration in the future
    private static PetBookDatabase buildDatabase(final Context appContext,
                                             final Executor executor) {
        return Room.databaseBuilder(appContext, PetBookDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executor.execute(() -> {
                            PetBookDatabase database = PetBookDatabase.getInstance(appContext);
                        });
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
}
