/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xjtlu.eevee.nekosleep.collections.ui;

import android.os.Debug;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import xjtlu.eevee.nekosleep.collections.PetDataSource;
import xjtlu.eevee.nekosleep.collections.persistence.Item;
import xjtlu.eevee.nekosleep.collections.persistence.Pet;

/**
 * View Model for the {@link PetScreenSlideActivity}
 */
public class PetViewModel extends ViewModel {

    private final PetDataSource mDataSource;

    private Pet mPet;

    private Item mItem;

    private List<Item> petItems;

    private List<Pet> mPets;

    private List<Item> mItems;

    public PetViewModel(PetDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Flowable<Pet> getPet(String id) {
        return mDataSource.getPet(id)
                .map(pet -> {
                    mPet = pet;
                    return pet;
                });
    }

    public Flowable<Item> getItem(String itemId){
        return mDataSource.getItem(itemId)
                .map(item -> {
                     mItem = item;
                     return item;
                }
        );
    }

    public Flowable<List<Item>> getPetItems(String petId) {
        return mDataSource.getPetItems(petId).map(
                items -> {
                    petItems = items;
                    return items;
                }
        );
    }

    public Flowable<List<Pet>> getAllPets(){
        return mDataSource.getAllPets().map(
                pets -> {
                    mPets = pets;
                    return pets;
                }
        );
    }

    public Flowable<List<Item>> getAllItems(){
        Log.d("database", "get all");
        return mDataSource.getAllItems().map(
                items -> {
                    mItems = items;
                    return items;
                }
        );
    }

    public void updatePetActiveness(String petId) {
        if(mPets!=null) {
            for (Pet pet : mPets) {
                if (pet.getId() == petId) pet.setActive();
            }
        }
        mDataSource.updatePetActive(petId);
    }

    public void updateItemActiveness(String itemId) {
        if(mItems!=null) {
            for (Item item : mItems) {
                if (item.getId() == itemId) item.setActive();
            }
        }
        mDataSource.updateItemActive(itemId);
    }
}
