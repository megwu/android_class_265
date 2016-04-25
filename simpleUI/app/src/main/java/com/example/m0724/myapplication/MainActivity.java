package com.example.m0724.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textView; //宣告 變數型態 變數名稱
    EditText editText;
    RadioGroup radioGroup;
    String sex = "";
    String drinkName = "black tea";
    String note = "";
    CheckBox checkBox;
//    ArrayList<String> orders;
    ArrayList<Order> orders;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView); //byId找View物件,View是高級的型態,所以要轉型成TextView
        editText = (EditText) findViewById(R.id.editText);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        checkBox = (CheckBox)findViewById(R.id.hideCheckBox);
        listView = (ListView)findViewById(R.id.listView);
        orders = new ArrayList<>();

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

        // RadioButton Change
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.maleRadioButton) {
//                    selectedSex = "Male";
//                } else if (checkedId == R.id.femaleRadioButton) {
//                    selectedSex = "Female";
//                }
                RadioButton radioButton = (RadioButton)findViewById(checkedId);
                drinkName = radioButton.getText().toString();
            }
        });

        /*
        // CheckBox Change
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    String text = name;
//                    textView.setText(text);
//                } else {
//                    String text = name + ", sex:" + sex;
//                    textView.setText(text);
//                }
                if (name != "") {
                    changeTextView();
                }
            }
        });
        */
    }

    // Adapter 是轉換器
    void setupListView() {
        // Ctrl + 滑鼠左鍵 點 ArrayAdapter 可以看到許多方法
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orders);
        OrderAdapter adapter = new OrderAdapter(this, orders); //自建物件
        listView.setAdapter(adapter); //把東西丟進去
    }

    public void click(View view) { //建立function 若要Import就按Alt + Enter ,然後再button的onClick下拉選單可以選到此function
        //textView.setText("Android Class"); //程式碼設定顯示的文字

        note = editText.getText().toString(); //取得畫面上輸入的文字
//        sex = drinkName;
//        String text = name + ", sex:" + sex;
//        textView.setText(text); //將輸入的文字set到TextView
//        changeTextView(); //將上面兩行註解 改成changeTextView();
        String text = note;
        textView.setText(text);
//        orders.add(text);

        Order order = new Order();
        order.drinkName = drinkName;
        order.note = note;

        orders.add(order);

        editText.setText(""); //將editText清空

        setupListView();
    }

    /*
    // 檢查是 hide CheckBox
    public void changeTextView() {
        if (checkBox.isChecked()) {
            String text = name;
            textView.setText(text);
        } else {
            String text = name + ", sex:" + sex;
            textView.setText(text);
        }
    }
    */

}
