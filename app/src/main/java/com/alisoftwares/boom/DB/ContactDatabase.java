package com.alisoftwares.boom.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ContactEnteties.class}, version = 1)
public abstract class ContactDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();


}
