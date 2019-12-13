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

    private List<Item> petItems;

    private List<Pet> mPets;

    private List<Item> mItems;

    public PetViewModel(PetDataSource dataSource) {
        mDataSource = dataSource;
    }

    /**
     * Get the user name of the user.
     *
     * @return a {@link Flowable} that will emit every time the user name has been updated.
     */
    public Flowable<Pet> getPet(String id) {
        return mDataSource.getPet(id)
                // for every emission of the user, get the user name
                .map(pet -> {
                    mPet = pet;
                    return pet;
                });
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
        return mDataSource.getAllItems().map(
                items -> {
                    mItems = items;
                    return items;
                }
        );
    }

    /**
     * Update activeness of the pet.
     *
     * @param petId the pet id
     * @return a {@link Completable} that completes when the user name is updated
     */
    public void updatePetActiveness(String petId) {
        if(mPets==null) getAllPets();
        for(Pet pet: mPets){
            if(pet.getId()==petId) pet.activate();
        }
        mDataSource.updatePetActive(petId);
    }

    /**
     * Update activeness of the item.
     *
     * @param petId the pet id
     * @return a {@link Completable} that completes when the user name is updated
     */
    public void updateItemActiveness(String petId) {

        if(mPets==null) getAllItems();
        for(Item item: mItems){
            if(item.getId()==petId) item.activate();
        }
        mDataSource.updatePetActive(petId);
    }
}
