package com.example.m0724.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MENU_ACTIVITY = 0;
    private static final int REQUEST_CODE_CAMERA_ACTIVITY = 1;

    private boolean hasPhoto = false;

    TextView textView; //宣告 變數型態 變數名稱
    EditText editText;
    RadioGroup radioGroup;
    String sex = "";
//    String drinkName = "black tea";
    String drinkName;
    String note = "";
    CheckBox checkBox;
//    ArrayList<String> orders;
    List<Order> orders;
    ListView listView;
    Spinner spinner;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    ImageView photoImageView;

    String menuResults = "";

    // 寫入記憶體 有上限 (記簡單少量的資訊,不要存大量的資料 ex:ListView)
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("debug", "Main Activity OnCreate");

//        ParseObject testObject = new ParseObject("TestObject"); //Class名稱
//        testObject.put("test", "456"); //創造欄位(不太能打純數字)和欄位的值
//        testObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e != null) {
//                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "save success", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        /*Homework
        ParseObject testObject = new ParseObject("HomeworkParse"); //Class名稱
        testObject.put("sid", "吳曉佩"); //創造欄位(不太能打純數字)和欄位的值
        testObject.put("email", "m07240629@gmail.com");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "save success", Toast.LENGTH_LONG).show();
                }
            }
        });*/

        textView = (TextView) findViewById(R.id.textView); //byId找View物件,View是高級的型態,所以要轉型成TextView
        editText = (EditText) findViewById(R.id.editText);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        checkBox = (CheckBox)findViewById(R.id.hideCheckBox);
        listView = (ListView)findViewById(R.id.listView);
        orders = new ArrayList<>();
        spinner = (Spinner)findViewById(R.id.spinner);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        photoImageView = (ImageView)findViewById(R.id.imageView);
        progressDialog = new ProgressDialog(this);

        // 第一個字串 setting 是哪一本字典, 拿setting的這一本字典; Context.MODE_PRIVATE
        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sp.edit(); // 拿出這一本字典的筆給你,就可以寫字典的內容
        // 讀檔案
//        textView.setText(Utils.readFile(this, "notes"));
        // 讀檔案 - 字串處理 (不work@@)
//        String[] data = Utils.readFile(this, "notes").split("\n");
//        textView.setText(data[0]);

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        // Get a Realm instance for this thread
//        realm = Realm.getInstance(realmConfig);
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();

        // (sp.getString("editText", "") 是假設找不到"editText"這個key值，第二個字串就是預設值
        editText.setText(sp.getString("editText", ""));

        // 實體鍵盤的 Enter 等同於 click 效果
        editText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = editText.getText().toString();
                editor.putString("editText", text);
                editor.apply(); //要寫這一句才會寫入記憶體

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

        // 第二參數不能給0,是要給layout上面物件的id
//        int checkedId = sp.getInt("radioGroup", R.id.blackTeaRadioButton);
//        radioGroup.check(checkedId);
//
//        RadioButton radioButton = (RadioButton) findViewById(checkedId);
//        drinkName = radioButton.getText().toString();
//
//        // RadioButton Change
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // 寫入字典裡面
//                editor.putInt("radioGroup", checkedId);
//                editor.apply();
//
////                if (checkedId == R.id.maleRadioButton) {
////                    selectedSex = "Male";
////                } else if (checkedId == R.id.femaleRadioButton) {
////                    selectedSex = "Female";
////                }
//                RadioButton radioButton = (RadioButton) findViewById(checkedId);
//                drinkName = radioButton.getText().toString();
//            }
//        });

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
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    photoImageView.setVisibility(View.GONE);
                } else {
                    photoImageView.setVisibility(View.VISIBLE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // new 選第一個按Enter 程式會自己長出來
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) parent.getAdapter().getItem(position); // parent.getAdapter() 回傳的型態是T , T 是當初我們設的型態就是甚麼型態

                // 這裡不能直接.this是因為是包在function中,所以要呼叫外部this就要用class名稱.this = MainActivity.this
//                Toast.makeText(MainActivity.this, order.note, Toast.LENGTH_LONG).show(); // 吐司，是下載框框，可以上網查來源，老師說很有趣
                // 另外的顯示框框,顯示速度比土司還要快,點即可以在寫function比土司多一層設計 google會選擇用這個取代掉土司
//                Snackbar.make(view, order.note, Snackbar.LENGTH_LONG).show();
                // .setAction() 可以自己玩玩看
//                Snackbar.make(view, order.note, Snackbar.LENGTH_LONG).setAction().show();
            }
        });


        setupListView();
        setupSpinner();


        int selectedId = sp.getInt("spinner", 0);
        spinner.setSelection(selectedId);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("spinner", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    // Adapter 是轉換器
    void setupListView() {
        // Ctrl + 滑鼠左鍵 點 ArrayAdapter 可以看到許多方法
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orders);
//        OrderAdapter adapter = new OrderAdapter(this, orders); //自建物件
//        listView.setAdapter(adapter); //把東西丟進去

//        RealmResults results = realm.allObjects(Order.class); // 要所有的訂單
//        // 用RealmResults接,可以再用results做很多事情,例如filter, sort ...
//        OrderAdapter adapter = new OrderAdapter(this, results.subList(0, results.size()));
//        listView.setAdapter(adapter); //把東西丟進去

        progressBar.setVisibility(View.VISIBLE); // 讓轉轉轉看的見

        final RealmResults results = realm.allObjects(Order.class); // 要所有的訂單
        // 用RealmResults接,可以再用results做很多事情,例如filter, sort ...
        OrderAdapter adapter = new OrderAdapter(MainActivity.this, results.subList(0, results.size()));
        listView.setAdapter(adapter); //把東西丟進去

        //改用網路上抓資料下來
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Order");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.GONE); // 讓轉轉轉看不見

                    return;
                }
                List<Order> orders = new ArrayList<Order>();

                Realm realm = Realm.getDefaultInstance();

                for (int i = 0; i < objects.size(); i++) {
                    Order order = new Order();
                    order.setNote(objects.get(i).getString("note"));
                    order.setStoreInfo(objects.get(i).getString("storeInfo"));
                    order.setMenuResults(objects.get(i).getString("menuResults"));
                    orders.add(order);

                    // 表示網路上的資料數 > local端的資料數
                    if (results.size() <= i) {
                        // 加到local端的realm
                        realm.beginTransaction();
                        realm.copyToRealm(order);
                        realm.commitTransaction();
                    }
                }

                realm.close();

                progressBar.setVisibility(View.GONE); // 讓轉轉轉看不見

                OrderAdapter adapter = new OrderAdapter(MainActivity.this, orders);
                listView.setAdapter(adapter);
            }
        });
    }

    void setupSpinner() {
//        String[] data = getResources().getStringArray(R.array.storeInfo);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);
//
//        spinner.setAdapter(adapter);

        //改用網路上抓資料下來
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("StoreInfo");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    return;
                }

                Realm realm = Realm.getDefaultInstance();
                ArrayList dataList = new ArrayList();

                for (int i = 0; i < objects.size(); i++) {
                    dataList.add(objects.get(i).getString("name"));
                }

                realm.close();

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, dataList);
                spinner.setAdapter(adapter);
            }
        });

    }

    public void click(View view) { //建立function 若要Import就按Alt + Enter ,然後再button的onClick下拉選單可以選到此function
        //textView.setText("Android Class"); //程式碼設定顯示的文字

        progressDialog.setTitle("Loading...");
        progressDialog.show();

        note = editText.getText().toString(); //取得畫面上輸入的文字
//        sex = drinkName;
//        String text = name + ", sex:" + sex;
//        textView.setText(text); //將輸入的文字set到TextView
//        changeTextView(); //將上面兩行註解 改成changeTextView();
        String text = note;
        textView.setText(text);
//        orders.add(text);

        Order order = new Order();
//        order.drinkName = drinkName;
//        order.note = note;
//        order.storeInfo = (String)spinner.getSelectedItem(); //取得下拉選單的值
        // Order物件有做get和set,所以將上面改成以下寫法
        order.setMenuResults(menuResults);
        order.setNote(note);
        order.setStoreInfo((String) spinner.getSelectedItem());

//        orders.add(order);

//        Utils.writeFile(this, "notes", order.note + "\n"); // \n是空行

        if (hasPhoto) {
            // 一張圖片都是RGB byte array格式
            Uri uri = Utils.getPhotoURI();

            byte[] photo = Utils.uriToBytes(this, uri);

            if (photo == null) {
                Log.d("Debug", "Read Photo Fail");
            } else {
                order.photo = photo;
            }
        }

        SaveCallbackWithRealm callbackWithRealm = new SaveCallbackWithRealm(order, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Save Fail", Toast.LENGTH_LONG).show();
                }

                editText.setText(""); //將editText清空
                menuResults = "";
                photoImageView.setImageResource(0); //將照片刪掉
                hasPhoto = false;

                progressDialog.dismiss();

                setupListView();
            }
        });



        order.saveToRemote(callbackWithRealm);

//        setupSpinner();
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

    public void goToMenu(View view) {
        Intent intent = new Intent(); // Activity 跟 Activity 的媒介

        intent.setClass(this, DrinkMenuActivity.class); // 從這裡呼叫DrinkMenuActivity

//        startActivity(intent); //呼叫
        startActivityForResult(intent, REQUEST_CODE_MENU_ACTIVITY); //呼叫並能辨別是哪個 Actitvity 回傳回來的 (requestCode)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MENU_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                menuResults = data.getStringExtra("result");
            }
        } else if (requestCode == REQUEST_CODE_CAMERA_ACTIVITY) { //判斷是不是從camera會來
            if (resultCode == RESULT_OK) {
                photoImageView.setImageURI(Utils.getPhotoURI());
                hasPhoto = true;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_take_photo) {
            Toast.makeText(this, "Take Photo", Toast.LENGTH_LONG).show();
            goToCamera();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void goToCamera() {
        // 版本若大於等於23
        if (Build.VERSION.SDK_INT >= 23) {
            // 確認user是否按過允許
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                return;
            }
        }

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE); // 呼叫拍照的功能
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Utils.getPhotoURI()); //放照片的位置
        startActivityForResult(intent, REQUEST_CODE_CAMERA_ACTIVITY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "Main Activity OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "Main Activity OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "Main Activity OnPause");
    }

    @Override
    protected void onStop() { // 通常會在這裡做儲存的動作,以免下次沒有資料
        super.onStop();
        Log.d("debug", "Main Activity OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        Log.d("debug", "Main Activity onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "Main Activity onRestart");
    }
}
