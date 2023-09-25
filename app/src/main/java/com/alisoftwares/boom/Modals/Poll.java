package com.alisoftwares.boom.Modals;

public class Poll {
    String time, uid , phoneNumber, pollImage, title, op1,op2,op3,op4;

    public Poll() {

    }

    public Poll(String time, String uid, String phoneNumber, String pollImage, String title, String op1, String op2, String op3, String op4) {
        this.time = time;
        this.uid = uid;
        this.phoneNumber = phoneNumber;
        this.pollImage = pollImage;
        this.title = title;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPollImage() {
        return pollImage;
    }

    public void setPollImage(String pollImage) {
        this.pollImage = pollImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }
}

