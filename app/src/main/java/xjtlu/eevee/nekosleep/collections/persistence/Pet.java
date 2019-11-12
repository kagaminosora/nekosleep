package xjtlu.eevee.nekosleep.collections.persistence;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Fts4(languageId = "lid")
@Entity(tableName = "book_pet")
public class Pet {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "pet_id")
    public String petId;

    @NonNull
    @ColumnInfo(name = "img_name")
    public String imageName;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @NonNull
    @ColumnInfo(name = "active")
    public boolean active;



    @Ignore

    public String getId(){ return petId; }

    public String getPetName() { System.out.println("get name");return name; }

    public String getImageName() { return imageName; }

    public boolean isActive() { return active; }

}
