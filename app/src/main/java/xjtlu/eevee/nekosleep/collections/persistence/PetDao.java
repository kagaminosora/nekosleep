package xjtlu.eevee.nekosleep.collections.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface PetDao {

    @Query("SELECT * FROM book_pet WHERE pet_id = (:petId)")
    Flowable<Pet> getPetById(String petId);

    @Query("SELECT * FROM book_pet")
    Flowable<List<Pet>> getAll();

    @Query("SELECT * FROM book_pet WHERE name LIKE :name")
    Pet findByName(String name);

    @Query("UPDATE book_pet SET active = :active WHERE pet_id = :petId")
    void setActive(boolean active, String petId);

    @Insert
    void insertAll(Pet... pets);

    @Delete
    void delete(Pet pet);
}
