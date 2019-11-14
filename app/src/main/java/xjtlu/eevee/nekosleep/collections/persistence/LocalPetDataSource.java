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

package xjtlu.eevee.nekosleep.collections.persistence;

import io.reactivex.Flowable;
import xjtlu.eevee.nekosleep.collections.PetDataSource;

/**
 * Using the Room database as a data source.
 */
public class LocalPetDataSource implements PetDataSource {

    private final PetDao mPetDao;

    public LocalPetDataSource(PetDao petDao) {
        mPetDao = petDao;
    }

    @Override
    public Flowable<Pet> getPet(String id) {
        return mPetDao.getPetById(id);
    }

    @Override
    public void updatePetActive(String petId) {
       mPetDao.setActive(true, petId);
    }
}
