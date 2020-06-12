package com.example.carl.ui_filter;

import android.view.View;
import android.view.ViewGroup;

/*
* 筛选菜单的adapter
* */
public abstract class BaseMenuAdapter {
    private MenuObserver mObserver;
    public void registerDataSetObserver(MenuObserver observer){
        mObserver = observer;
    }
    public void unregisterDataSetObserver(){
        mObserver=null;
    }

    public void closeMenu(){
        if (mObserver!=null){
            mObserver.observerCloseMenu();
        }
    }

    public abstract int getCount();

    public abstract View getTabView(int position, ViewGroup parent);

    public abstract View getMenuView(int position, ViewGroup parent);

    public abstract void menuOpen(View tabView);

    public abstract void menuClose(View tabView);
}
