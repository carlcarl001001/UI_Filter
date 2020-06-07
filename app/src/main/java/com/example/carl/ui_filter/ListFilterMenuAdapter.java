package com.example.carl.ui_filter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ListFilterMenuAdapter extends BaseMenuAdapter {
    private String []mItems = {"类型","品牌","价格","更多"};
    private Context mContext;

    public ListFilterMenuAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
        TextView tabView =(TextView) LayoutInflater.from(mContext).inflate(R.layout.ui_list_data_filter_tab,parent,false);
        tabView.setText(mItems[position]);
        tabView.setTextColor(Color.BLACK);
        return tabView;
    }

    @Override
    public View getMenuView(int position, ViewGroup parent) {
        //真正的开发过程中不同的位置显示的布局不一样
        TextView menuView =(TextView) LayoutInflater.from(mContext).inflate(R.layout.ui_list_data_filter_menu,parent,false);
        menuView.setText(mItems[position]);
        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
                Toast.makeText(mContext,"关闭菜单",Toast.LENGTH_LONG).show();
            }
        });
        log("mItems[position]:"+mItems[position]);
        return menuView;
    }

    @Override
    public void menuOpen(View tabView) {
        TextView tabTv = (TextView)tabView;
        tabTv.setTextColor(Color.RED);
    }

    @Override
    public void menuClose(View tabView) {
        TextView tabTv = (TextView)tabView;
        tabTv.setTextColor(Color.BLACK);
}

    private void log(String str){
        Log.i("chen",str);
    }
}
