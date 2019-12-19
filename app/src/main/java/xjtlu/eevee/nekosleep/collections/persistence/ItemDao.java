package xjtlu.eevee.nekosleep.collections.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM book_item")
    Flowable<List<Item>> getAll();

    @Query("SELECT * FROM book_item WHERE owner = (:petId)")
    Flowable<List<Item>> getItemByPetId(String petId);

    @Query("SELECT * FROM book_item WHERE item_id = :itemId")
    Flowable<Item> getItemById(String itemId);

    @Query("SELECT * FROM book_item WHERE name LIKE :name")
    Item findByName(String name);

    @Query("UPDATE book_item SET active = :active WHERE item_id = :itemId")
    void setActive(boolean active, String itemId);

    @Insert
    void insertAll(Item... items);

    @Delete
    void delete(Item item);
}
