package com.example.m0724.myapplication;

import io.realm.RealmObject;

/**
 * Created by m0724 on 2016/4/25.
 */
public class Order extends RealmObject {
    private String note;
    private String drinkName;
    private String storeInfo;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }
}
