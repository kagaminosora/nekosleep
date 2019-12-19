package xjtlu.eevee.nekosleep.collections.persistence;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Pet.class,
                parentColumns = "pet_id",
                childColumns = "owner"),
        tableName = "book_item",
        indices = {@Index(value = "owner")})

public class Item {
    @NonNull
    @PrimaryKey
    @ColumnInfo (name = "item_id")
    private String id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "active")
    private boolean active;

    @NonNull
    @ColumnInfo(name = "img_name")
    private String imgName;

    @ColumnInfo(name = "owner")
    private String owner;

    @Ignore
    public Item(){
        this.id = "00000000";
        this.imgName = "pikachu";
        this.name = "Pikachu";
        this.active = true;
        this.owner = "00000000";
    }

    public Item(String id, String imgName, String name, Boolean active, String owner){
        this.id = id;
        this.imgName = imgName;
        this.name = name;
        this.active = active;
        this.owner = owner;
    }

    public String getId(){ return id; }

    public String getName() { return name; }

    public String getImgName() { return imgName; }

    public String getOwner() { return owner; }

    public void setActive(){active = true; }

    public boolean isActive() { return active; }

}
