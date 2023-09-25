package com.alisoftwares.boom.Modals;

public class FriendsPollsModal {
    String imageUrl, Name, NumberOfPolls, lastUpdateDate;

    public FriendsPollsModal() {

    }

    public FriendsPollsModal(String imageUrl, String name, String numberOfPolls, String lastUpdateDate) {
        this.imageUrl = imageUrl;
        Name = name;
        NumberOfPolls = numberOfPolls;
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumberOfPolls() {
        return NumberOfPolls;
    }

    public void setNumberOfPolls(String numberOfPolls) {
        NumberOfPolls = numberOfPolls;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
