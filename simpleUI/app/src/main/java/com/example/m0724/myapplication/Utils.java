package com.example.m0724.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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

    public static byte[] uriToBytes(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1 ) {
                byteArrayOutputStream.write(buffer);
            }

            return byteArrayOutputStream.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] urlToBytes(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1 ) {
                byteArrayOutputStream.write(buffer, 0, len); //要給定範圍不然會load不出圖
            }

            return byteArrayOutputStream.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static String getGeoCodingUrl(String address) {
        try {
            address = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://maps.google.com/maps/api/geocode/json?address=" + address;
        return url;
    }

    public static double[] addressToLatLng(String address) {
        String url = Utils.getGeoCodingUrl(address);
        byte[] bytes = Utils.urlToBytes(url);
        if (bytes != null) {
            String result = new String(bytes);
            try {
                JSONObject object = new JSONObject(result);
                // 判斷回傳的JSON狀態是否OK
                if (object.getString("status").equals("OK")) {
                    //看api回傳的結構,抓想要的值
                    JSONObject location = object.getJSONArray("results")
                                                .getJSONObject(0)
                                                .getJSONObject("geometry")
                                                .getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    return new double[]{lat, lng};
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap getStaticMap(double[] latlng) {
        String center = latlng[0] + "," + latlng[1];
        String staticMapUrl = "http://maps.google.com/maps/api/staticmap?center=" + center + "&zoom=17&&size=640x400";

        byte[] bytes = Utils.urlToBytes(staticMapUrl);
        if (bytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        }
        return null;
    }
}
