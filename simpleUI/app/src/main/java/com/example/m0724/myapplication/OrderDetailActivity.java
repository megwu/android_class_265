package com.example.m0724.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailActivity extends AppCompatActivity {

    TextView note;
    TextView storeInfo;
    TextView menuResults;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        note = (TextView)findViewById(R.id.note);
        storeInfo = (TextView)findViewById(R.id.storeInfo);
        menuResults = (TextView)findViewById(R.id.menuResults);
        photo = (ImageView)findViewById(R.id.photoImageView);

        Intent intent = getIntent();
        note.setText(intent.getStringExtra("note"));
        storeInfo.setText(intent.getStringExtra("storeInfo"));

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
    }

    class ImageLoadingTask extends AsyncTask<String, Void, Bitmap> {
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
