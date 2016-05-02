package com.example.m0724.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrinkMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
        Log.d("debug", "DrinkMenuActivity OnCreate");
    }

    public void add(View view) {
        Button button = (Button)view;

        String text = button.getText().toString();

        int count = Integer.parseInt(text);

        count++;

        button.setText(String.valueOf(count));
    }

    public void cancel(View view) {
        finish(); // 直接 onDestroy 跳回前面那一頁
    }

    public void done(View view) {
        Intent intent = new Intent();
        intent.putExtra("result", getData().toString()); // put 只接受常見的型態 例如String, int, boolean...，所以把JSONArray轉成toString

        // 把界值留在這邊 (resultCode = RESULT_OK)
        setResult(RESULT_OK, intent);
        // 把自己結束掉
        finish();
    }

    public JSONArray getData() {
        //JSON 就像shareReference  有key跟value

        LinearLayout rootLinearLayout = (LinearLayout) findViewById(R.id.root);

        JSONArray jsonArray = new JSONArray();

        for (int i = 1; i < 4; i++) {
            LinearLayout linearLayout = (LinearLayout) rootLinearLayout.getChildAt(i);

            TextView textView = (TextView)linearLayout.getChildAt(0);
            Button mButton = (Button)linearLayout.getChildAt(1);
            Button lButton = (Button)linearLayout.getChildAt(2);

            String drinkName = textView.getText().toString();

            int m = Integer.parseInt(mButton.getText().toString());
            int l = Integer.parseInt(lButton.getText().toString());

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", drinkName);
                jsonObject.put("m", m);
                jsonObject.put("l", l);

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "DrinkMenuActivity OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "DrinkMenuActivity OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "DrinkMenuActivity OnPause");
    }

    @Override
    protected void onStop() { // 通常會在這裡做儲存的動作,以免下次沒有資料
        super.onStop();
        Log.d("debug", "DrinkMenuActivity OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "DrinkMenuActivity onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "DrinkMenuActivity onRestart");
    }
}
