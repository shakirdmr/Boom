package com.alisoftwares.boom.Modals;

public class Contact {

    String name, phoneNumber;
    Boolean isChecked;

    public Contact(String name, String phoneNumber,Boolean isChecked) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isChecked = isChecked;
    }


    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
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
