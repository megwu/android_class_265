package com.example.m0724.myapplication;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import io.realm.RealmObject;

/**
 * Created by m0724 on 2016/4/25.
 */
public class Order extends RealmObject {
    private String note;
    private String menuResults;
    private String storeInfo;

    byte[] photo = null; // 因為照片已存取在stroge裡面 所以不用做get set 避免佔realm空間。給null是因為有可能沒有拍照片
    String photoURL = "";

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMenuResults() {
        return menuResults;
    }

    public void setMenuResults(String menuResults) {
        this.menuResults = menuResults;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }

    // 將訂單上傳到Parse
    public void saveToRemote(SaveCallback saveCallback) {
        ParseObject parseObject = new ParseObject("Order");
        parseObject.put("note", note);
        parseObject.put("storeInfo", storeInfo);
        parseObject.put("menuResults", menuResults);

        if (photo != null) {
            // 因為照片檔案比較大,所以要用ParseFile來存取
            ParseFile file = new ParseFile("photo.png", photo);
            parseObject.put("photo", file); //再將檔案放在praseObject
        }

        parseObject.saveInBackground(saveCallback);
    }
}
