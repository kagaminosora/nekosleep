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

import io.reactivex.Completable;
import io.reactivex.Flowable;
import xjtlu.eevee.nekosleep.collections.PetDataSource;
import xjtlu.eevee.nekosleep.collections.persistence.Pet;

/**
 * View Model for the {@link PetScreenSlideActivity}
 */
public class PetViewModel extends ViewModel {

    private final PetDataSource mDataSource;

    private Pet mPet;

    public PetViewModel(PetDataSource dataSource) {
        mDataSource = dataSource;
    }

    /**
     * Get the user name of the user.
     *
     * @return a {@link Flowable} that will emit every time the user name has been updated.
     */
    public Flowable<String> getPetName(String id) {
        return mDataSource.getPet(id)
                // for every emission of the user, get the user name
                .map(pet -> {
                    mPet = pet;
                    return pet.getPetName();
                });
    }

    /**
     * Update the user name.
     *
     * @param petId the new user name
     * @return a {@link Completable} that completes when the user name is updated
     */
    public void updateUserName(String petId) {
        // if there's no user, create a new user.
        // if we already have a user, then, since the user object is immutable,
        // create a new user, with the id of the previous user and the updated user name.
        mDataSource.updatePetActive(petId);
    }
}
