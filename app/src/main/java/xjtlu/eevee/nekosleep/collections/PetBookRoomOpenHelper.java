package xjtlu.eevee.nekosleep.collections;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.RoomOpenHelper;

public class PetBookRoomOpenHelper extends RoomOpenHelper {
    public PetBookRoomOpenHelper(@NonNull DatabaseConfiguration configuration, @NonNull Delegate delegate, @NonNull String identityHash, @NonNull String legacyHash) {
        super(configuration, delegate, identityHash, legacyHash);
    }
}
