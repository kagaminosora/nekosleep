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

package xjtlu.eevee.nekosleep.collections;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import xjtlu.eevee.nekosleep.collections.persistence.Item;
import xjtlu.eevee.nekosleep.collections.persistence.Pet;

/**
 * Access point for managing user data.
 */
public interface PetDataSource {

    /**
     * Gets the pet from the data source.
     *
     * @return the pet from the data source.
     */
    Flowable<Pet> getPet(String id);

    /**
     * Gets the pets from the data source.
     *
     * @return all pets from the data source.
     */
    Flowable<List<Pet>> getAllPets();

    /**
     * Update the pet's condition
     *
     * @param petId the pet to be inserted or updated.
     */
    void updatePetActive(String petId);

    Flowable<List<Item>> getPetItems(String petId);

    Flowable<List<Item>> getAllItems();
}
