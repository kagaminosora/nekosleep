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

import android.content.Context;

import xjtlu.eevee.nekosleep.collections.persistence.LocalPetDataSource;
import xjtlu.eevee.nekosleep.collections.persistence.PetBookDatabase;
import xjtlu.eevee.nekosleep.collections.ui.ViewModelFactory;

/**
 * Enables injection of data sources.
 */
public class Injection {

    public static PetDataSource providePetDataSource(Context context) {
        PetBookDatabase database = PetBookDatabase.getInstance(context);
        return new LocalPetDataSource(database.petDAO(), database.itemDAO());
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        PetDataSource dataSource = providePetDataSource(context);
        return new ViewModelFactory(dataSource);
    }
}
