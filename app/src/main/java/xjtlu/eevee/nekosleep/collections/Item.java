package xjtlu.eevee.nekosleep.collections;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Item.class,
                parentColumns = "id",
                childColumns = "pet_id"))

public class Item {
    @PrimaryKey
    public int itemId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "pet_id")
    public String petId;

    @ColumnInfo(name = "active")
    public boolean active;

    @Ignore
    public Bitmap imageActive;

    @Ignore
    public Bitmap imageInactive;
}
