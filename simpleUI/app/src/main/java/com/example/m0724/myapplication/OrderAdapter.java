package com.example.m0724.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m0724 on 2016/4/25.
 * 資料轉換器
 */
public class OrderAdapter extends BaseAdapter {
    List<Order> orders;
    LayoutInflater inflater; //自動作縮小放大的動作

    public OrderAdapter(Context context, List<Order> orders) {
        this.inflater = LayoutInflater.from(context);
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size(); //總共要顯示幾欄，可以給固定數值或是會變動的size
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position); //取得選第幾個
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //最主要的
        Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, null);

            holder = new Holder();

            holder.drinkName = (TextView) convertView.findViewById(R.id.drinkName);
            holder.note = (TextView) convertView.findViewById(R.id.note);
            holder.storeInfo = (TextView )convertView.findViewById(R.id.store);

            convertView.setTag(holder); //holder 放到converView的其中一個空間裡面去
        } else {
            holder = (Holder)convertView.getTag();
        }

//        TextView drinkName = (TextView) convertView.findViewById(R.id.drinkName);
//        TextView note = (TextView) convertView.findViewById(R.id.note);

//        drinkName.setText(orders.get(position).drinkName);
//        note.setText(orders.get(position).note);

        holder.drinkName.setText(orders.get(position).getDrinkName());
        holder.note.setText(orders.get(position).getNote());
        holder.storeInfo.setText(orders.get(position).getStoreInfo());

        return convertView;
    }


    class Holder {
        TextView drinkName;
        TextView note;
        TextView storeInfo;
    }
}
