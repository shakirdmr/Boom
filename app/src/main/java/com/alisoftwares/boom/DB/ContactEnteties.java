package com.alisoftwares.boom.DB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class ContactEnteties {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String phoneNumber;
    Boolean isChecked;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
// Constructor, getters, and setters


    public ContactEnteties() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
