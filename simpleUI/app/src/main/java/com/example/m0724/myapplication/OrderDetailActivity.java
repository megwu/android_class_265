package com.example.m0724.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailActivity extends AppCompatActivity {

    TextView note;
    TextView storeInfo;
    TextView menuResults;
    ImageView photo;
    ImageView mapImageView;

    String storeName; //店名
    String address; //地址

    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        note = (TextView)findViewById(R.id.note);
        storeInfo = (TextView)findViewById(R.id.storeInfo);
        menuResults = (TextView)findViewById(R.id.menuResults);
        photo = (ImageView)findViewById(R.id.photoImageView);
        mapImageView = (ImageView)findViewById(R.id.mapImageView);

        Intent intent = getIntent();
        note.setText(intent.getStringExtra("note"));
        storeInfo.setText(intent.getStringExtra("storeInfo"));

        String[] info = intent.getStringExtra("storeInfo").split(",");

        storeName = info[0];
        address = info[1];

        String results = intent.getStringExtra("menuResults") ;
        String text = "";
        try {
            JSONArray jsonArray = new JSONArray(results);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                text += object.getString("name") + "： 大杯 " + object.getString("l") + "杯／中杯 " + object.getString("m") + "杯" + "\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        menuResults.setText(text);

        String url = intent.getStringExtra("photoURL");
        if (!"".equals(url)) {
//            Picasso.with(this).load(intent.getStringExtra("photoURL")).into(photo);
            // 將上一行Picassp mark掉,改用寫的Task試試看
            (new ImageLoadingTask(photo)).execute(url);
//            (new GeoCodingTask(photo)).execute("新北市汐止區大同路一段369號");

            // 10萬秒跑10次 測試記憶體爆掉的狀況
            /*for (int i = 0; i < 10; i++) {
                // 不要輕易使用,會讓記憶體爆掉
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            wait(100000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }*/
        }


        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.googleMapFragment);

        // mapFragment只是一個生命週期的activity，真正要的是Callback裡面的 googleMap
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                (new GeoCodingTask(googleMap)).execute(address);
            }
        });
        /*
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        SystemClock.sleep(1000);
                    }
                }
            });
        }*/
    }

    private static class GeoCodingTask extends AsyncTask<String, Void, double[]> {
        GoogleMap googleMap;
        @Override
        protected double[] doInBackground(String... params) {
            String address = params[0];
            double[] latlng = Utils.addressToLatLng(address);
            return latlng;
        }

        @Override
        protected void onPostExecute( double[] latlng) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlng[0], latlng[1]), 17)); //google map 是一個相機從上往下拍,所以來移動相機
        }

        public GeoCodingTask(GoogleMap googleMap){this.googleMap = googleMap;}
    }

    private static class ImageLoadingTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            byte[] bytes = Utils.urlToBytes(url);
            if (bytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return bitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) { //執行完之後所帶進來的參數 就是我們當初解的參數的
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

        public ImageLoadingTask(ImageView imageView){this.imageView = imageView;}
    }
}
