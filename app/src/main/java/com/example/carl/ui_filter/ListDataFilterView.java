package com.example.carl.ui_filter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ListDataFilterView extends LinearLayout implements View.OnClickListener {
    private LinearLayout mMenuTabView;
    private FrameLayout mMenuMiddleView;
    private View mShadowView;
    private Context mContext;
    private FrameLayout mMenuContainerView;
    private int mMenuContainerHeight;
    private int mShadowColor = Color.parseColor("#88888888");
    private BaseMenuAdapter mAdapter;
    //当前打开位置
    private int mCurrentPosition = -1;
    private long DURATION_TIME = 350;
    //动画是否在执行
    private boolean mAnimatorExecute;

    public ListDataFilterView(Context context) {
        this(context,null);
    }

    public ListDataFilterView(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    public ListDataFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initLayout();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("chen","onMeasure -----");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //内容的高度应该不是全部 应该是个View的75%
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mMenuContainerHeight ==0&&height>0) {
            mMenuContainerHeight = (int) (height * 75f / 100);
            ViewGroup.LayoutParams params = mMenuContainerView.getLayoutParams();
            params.height = mMenuContainerHeight;
            mMenuContainerView.setLayoutParams(params);
            mMenuContainerView.setTranslationY(-mMenuContainerHeight);
        }

    }

    private void initLayout() {
        //用代码创建布局
        //创建头部存放tab
        setOrientation(VERTICAL);
        mMenuTabView = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mMenuTabView.setLayoutParams(params);
        addView(mMenuTabView);
        //创建framelayout用来存放 阴影+内容
        mMenuMiddleView = new FrameLayout(mContext);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        params2.weight = 1;
        mMenuMiddleView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mMenuMiddleView);
        //创建阴影 可以不用设置LayoutParams默认就是MATCH_PARENT MATCH_PARENT
        mShadowView = new View(mContext);
        mShadowView.setBackgroundColor(mShadowColor);
        mShadowView.setAlpha(0f);
        mShadowView.setVisibility(GONE);
        mShadowView.setOnClickListener(this);
        mMenuMiddleView.addView(mShadowView);
        //创建菜单用来存放菜单内容
        mMenuContainerView = new FrameLayout(mContext);
        mMenuMiddleView.addView(mMenuContainerView);
    }
    public void setAdapter(BaseMenuAdapter adapter){
            //观察者 用户
            if (mAdapter!=null&&mMenuObserver!=null){
                mAdapter.unregisterDataSetObsever(mMenuObserver);
            }

            this.mAdapter = adapter;
            //注册观察者 具体的观察者实例对象 订阅
            mMenuObserver = new AdapterMenuObserver();
            mAdapter.registerDataSetObsever(mMenuObserver);
            int count = mAdapter.getCount();
            for (int i=0;i<count;i++){
                View tabView = mAdapter.getTabView(i,mMenuTabView);
                mMenuTabView.addView(tabView);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                tabView.setLayoutParams(params);
                setTabClick(tabView,i);
                View menuView= mAdapter.getMenuView(i,mMenuContainerView);
                menuView.setVisibility(GONE);
                mMenuContainerView.addView(menuView);
            }

    }

    private void setTabClick(final View tabView, final int position) {

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition ==-1){
                    //没打开
                    log("position:"+position);
                    openMenu(position,tabView);
                }else {
                    //打开了
                    if (mCurrentPosition == position){
                        closeMenu();
                    }
                    else {
                        View currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                        currentMenu.setVisibility(View.GONE);
                        mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
                        mCurrentPosition = position;
                        mAdapter.menuOpen(mMenuTabView.getChildAt(mCurrentPosition));
                        currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                        currentMenu.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void closeMenu() {
        if (mAnimatorExecute){
            return;
        }
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContainerView,"translationY",0,-mMenuContainerHeight);
        translationAnimator.setDuration(DURATION_TIME);
        translationAnimator.start();
        mShadowView.setVisibility(VISIBLE);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView,"alpha",1f,0f);
        alphaAnimator.setDuration(DURATION_TIME);
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View menuView = mMenuContainerView.getChildAt(mCurrentPosition);
                menuView.setVisibility(GONE);
                mCurrentPosition = -1;
                mShadowView.setVisibility(GONE);
                mAnimatorExecute = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
                mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
            }
        });
        alphaAnimator.start();

    }

    private void openMenu(final int position, final View tabView) {
        if (mAnimatorExecute){
            return;
        }
        mShadowView.setVisibility(VISIBLE);
        View menuView = mMenuContainerView.getChildAt(position);
        TextView t = (TextView)menuView;
        log("t.getText().toString():"+t.getText().toString());
        menuView.setVisibility(VISIBLE);
        //mMenuContainerView.getChildAt(0).setVisibility(VISIBLE);
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContainerView,"translationY",-mMenuContainerHeight,0);
        log("mMenuContainerHeight:"+mMenuContainerHeight);
        translationAnimator.setDuration(DURATION_TIME);
        translationAnimator.start();


        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView,"alpha",0f,1f);
        alphaAnimator.setDuration(DURATION_TIME);
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorExecute = false;
                mCurrentPosition = position;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
                mAdapter.menuOpen(tabView);
            }
        });
        alphaAnimator.start();



    }

    @Override
    public void onClick(View v) {
        closeMenu();
        Toast.makeText(mContext,"Shadow click",Toast.LENGTH_LONG).show();
    }
    /*
    * 具体的观察者类对象
    * */
    private class AdapterMenuObserver extends MenuObserver{

        @Override
        public void closeMenu() {
            //如果有注册就会收到通知
            log("into AdapterMenuObserver closeMenu");
            ListDataFilterView.this.closeMenu();
        }
    }

    private AdapterMenuObserver mMenuObserver;

    private void log(String str){
        Log.i("chen",str);
    }
}










