package xjtlu.eevee.nekosleep.collections;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Fts4(languageId = "lid")
@Entity(tableName = "pets")
public class Pet {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "active")
    public boolean active;

    @Ignore
    public Bitmap imageActive;

    @Ignore
    public Bitmap imageInactive;

}
