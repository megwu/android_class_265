package com.example.m0724.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView; //宣告 變數型態 變數名稱

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView); //byId找View物件,View是高級的型態,所以要轉型成TextView

    }

    public void click(View view) { //建立function 若要Import就按Alt + Enter ,然後再button的onClick下拉選單可以選到此function
        textView.setText("Android Class"); //程式碼設定顯示的文字
    }
}
