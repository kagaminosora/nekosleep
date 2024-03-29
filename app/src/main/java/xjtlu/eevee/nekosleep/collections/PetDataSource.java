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

    Flowable<Pet> getPet(String id);

    Flowable<Item> getItem(String id);

    Flowable<List<Pet>> getAllPets();

    void updatePetActive(String petId);

    void updateItemActive(String itemId);

    Flowable<List<Item>> getPetItems(String petId);

    Flowable<List<Item>> getAllItems();
}
