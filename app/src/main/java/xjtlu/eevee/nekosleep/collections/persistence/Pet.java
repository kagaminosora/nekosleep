package xjtlu.eevee.nekosleep.collections.persistence;

import android.graphics.Bitmap;
import android.provider.Settings;

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
    private String id;

    @NonNull
    @ColumnInfo(name = "img_name")
    private String imgName;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "active")
    private boolean active;

    @Ignore

    public Pet(){
        this.id = "00000000";
        this.imgName = "pikachu";
        this.name = "Pikachu";
        this.active = true;
    }

    public Pet(String id, String imgName, String name, Boolean active){
        this.id = id;
        this.imgName = imgName;
        this.name = name;
        this.active = active;
    }

    public String getId(){ return id; }

    public void setId(String id){this.id = id;}

    public String getName() { return name; }

    public void setName(String name){this.name = name;}

    public String getImgName() { return imgName; }

    public void setImgName(String imgName){this.imgName = imgName;}

    public boolean isActive() { return active; }

    public void setActive(){this.active = true;}

}
