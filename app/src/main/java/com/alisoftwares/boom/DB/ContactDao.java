package com.alisoftwares.boom.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {


    @Query("DELETE FROM contacts")
    void deleteAllContacts();


    @Insert
    void insert(ContactEnteties contact);

    @Query("SELECT * FROM contacts")
    List<ContactEnteties> getAllContacts();

    @Query("UPDATE contacts SET isChecked = :checked WHERE phoneNumber = :phoneNumber AND name= :name")
    void update(Boolean checked, String phoneNumber,String name);
}
