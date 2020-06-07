package com.example.carl.ui_filter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/*
* 筛选菜单的adapter
* */
public abstract class BaseMenuAdapter {
    private MenuObserver mObserver;
    public void registerDataSetObsever(MenuObserver observer){
        mObserver = observer;
    }
    public void unregisterDataSetObsever(MenuObserver observer){
        mObserver=null;
    }

    public void closeMenu(){
        if (mObserver!=null){
            mObserver.closeMenu();
        }
    }

    public abstract int getCount();

    public abstract View getTabView(int position, ViewGroup parent);

    public abstract View getMenuView(int position, ViewGroup parent);

    public abstract void menuOpen(View tabView);

    public abstract void menuClose(View tabView);
}
