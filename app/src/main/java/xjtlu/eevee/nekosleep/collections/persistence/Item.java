package xjtlu.eevee.nekosleep.collections.persistence;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Pet.class,
                parentColumns = "pet_id",
                childColumns = "owner"),
        tableName = "book_item")

public class Item {
    @NonNull
    @PrimaryKey
    @ColumnInfo (name = "item_id")
    public String itemId;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @NonNull
    @ColumnInfo(name = "active")
    public boolean active;

    @NonNull
    @ColumnInfo(name = "img_name")
    public String imageName;

    @ColumnInfo(name = "owner")
    public String owner;

    @Ignore
    public String getId(){ return itemId; }

    public String getItemName() { return name; }

    public String getImageName() { return imageName; }

    public String getOwnerId() { return owner; }

    public boolean isActive() { return active; }

}
