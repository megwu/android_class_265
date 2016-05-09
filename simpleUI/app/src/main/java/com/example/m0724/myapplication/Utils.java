package com.example.m0724.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * Created by user on 2016/4/28.
 */
public class Utils {

    public static void writeFile(Context context, String fileName, String content) {
        // 一個串流,寫檔案用 ; Context.MODE_APPEND 是會累加每次新增的資料 / Context.MODE_PRIVATE 是每次存都會覆蓋掉舊的資料
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            fis.read(buffer, 0, buffer.length);
            fis.close(); //記得關掉,下次別人在讀取才不會壞掉
            return new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 若IO壞掉了,可以傳空字串當回傳值
        return "";
    }

    public static Uri getPhotoURI() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //取得相片的資料夾
        // 判斷資料夾是否存在
        if (dir.exists() == false) {
            // 若不存在就要建立一個
            dir.mkdir();
        }

        File file = new File(dir, "simpleUI_photo.png"); // 放路徑(dir) 跟 檔名(simpleUI_photo.png)
        return Uri.fromFile(file);
    }
}
