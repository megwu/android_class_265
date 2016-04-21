package com.example.m0724.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView; //宣告 變數型態 變數名稱
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView); //byId找View物件,View是高級的型態,所以要轉型成TextView
        editText = (EditText) findViewById(R.id.editText);

        // 實體鍵盤的 Enter 等同於 click 效果
        editText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    click(v);
                    return true;
                }
                return false;
            }
        });

        // Android 虛擬鍵盤的 Enter
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) { // 但是editText要設定 SingleLine喔喔喔~~虛擬鍵盤才有打勾勾
                    click(v);
                    return true; // 是攔截Enter，若沒有return true 就會跳下一行 (editText取消設定 SingleLine效果才會出現)
                }
                return false;
            }
        });
    }

    public void click(View view) { //建立function 若要Import就按Alt + Enter ,然後再button的onClick下拉選單可以選到此function
        //textView.setText("Android Class"); //程式碼設定顯示的文字

        String text = editText.getText().toString(); //取得畫面上輸入的文字
        textView.setText(text); //將輸入的文字set到TextView
        editText.setText(""); //將editText清空
    }

}
