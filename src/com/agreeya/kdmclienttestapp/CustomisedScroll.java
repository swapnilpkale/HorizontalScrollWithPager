package com.agreeya.kdmclienttestapp;


import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomisedScroll extends HorizontalScrollView implements PageIndicator{

    private Context mContext ;
    private ViewPager mViewPager;
    private LinearLayout   mTabLayout;
    private static final CharSequence EMPTY_TITLE = "";
    
    public CustomisedScroll(Context context) {
        super(context);
        mContext = context;
    }
    
    
    public CustomisedScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);

        mTabLayout = new LinearLayout(context);
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("asd", "onPageScrollStateChanged = "+state);
       
        
        if(mState == ViewPager.SCROLL_STATE_DRAGGING && state == ViewPager.SCROLL_STATE_SETTLING){
            isSettledAfterDrag = true;
        }
        
        if(state == ViewPager.SCROLL_STATE_IDLE){
            mHandler.removeMessages(0);
            onPageSelected(mPosition);
        }
        
        mState = state;
    }

    
    private int mState = 0;
    boolean isSettledAfterDrag = true;
    
    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            if(msg.what == SCROLL_MSG_WHAT_DRAG){
                int scrollBy =  (msg.arg1 - msg.arg2);
                Log.d("asd", "scrolling to selected "+scrollBy);
                CustomisedScroll.this.scrollBy(-(scrollBy)/5, 0);
            }else  if(msg.what == 0){
                int scrollBy =  (msg.arg1 - msg.arg2);
                Log.d("asd", "scrolling to selected "+scrollBy);
                CustomisedScroll.this.scrollBy((scrollBy)/5, 0);
            }
        }
    };
    
    private int mPreviousOffset = 0;
    private int  SCROLL_MSG_WHAT_SETT = 1002;
    private int SCROLL_MSG_WHAT_DRAG = 1002;
    private int mPosition= 0;
    
    @Override
    public void onPageScrolled(int position,  float positionOffset,final int positionOffsetPixels) {
       Log.d("asd", "onPageScrolled : mState = "+mState +", position = "+position +", positionOffsetPixels = "+positionOffsetPixels);
       /*//        if(mPreviousOffset != 0){
            Message msg ;
            if(mState == ViewPager.SCROLL_STATE_DRAGGING && mPreviousOffset !=0){
               msg = mHandler.obtainMessage(SCROLL_MSG_WHAT_DRAG, mPreviousOffset, positionOffsetPixels);
               mHandler.sendMessage(msg);
            }
            mPreviousOffset = positionOffsetPixels;
            if (mState == ViewPager.SCROLL_STATE_IDLE){
                mPreviousOffset = 0;
//                msg = mHandler.obtainMessage(SCROLL_MSG_WHAT_SETT, mPreviousOffset, positionOffsetPixels);
//                mHandler.sendMessage(msg);
            }
//        }*/
        
        if(mState ==  ViewPager.SCROLL_STATE_DRAGGING && mPosition ==  position && mPreviousOffset !=0){
            Message ms =mHandler.obtainMessage(0, positionOffsetPixels, mPreviousOffset);
            mHandler.sendMessage(ms);
        }
        
//        if(mState == ViewPager.SCROLL_STATE_SETTLING  && isSettledAfterDrag && mPreviousOffset !=0){
//            int changedOffset = positionOffsetPixels;
//            if(mPreviousOffset >= positionOffsetPixels){
//                changedOffset = positionOffsetPixels * -1;
//            }
//            Message ms =mHandler.obtainMessage(0, changedOffset, 0);
//            mHandler.sendMessage(ms);
//            mPreviousOffset = 0;
//            isSettledAfterDrag = false;
//        }
        
      

        mPosition = position;
        mPreviousOffset = positionOffsetPixels;
        
    }
    @Override
    public void onPageSelected(int arg0) {
        int count = mViewPager.getAdapter().getCount();
        if (arg0 == 1) {
            setCurrentItem(count-3);
            mHandler.removeMessages(0);
        } else if (arg0 == count-2) {
            setCurrentItem(2);
            mHandler.removeMessages(0);
        } else {
            setCurrentItem(arg0);
        }
    }
    
    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }
    
    
    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        // TODO Auto-generated method stub
    }
    
//    private ViewPager.OnPageChangeListener mListener;
    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item, false);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
    }
    
    private Runnable mTabSelector;
    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                scrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }
    
    
    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
//        mListener = listener;
    }
    
    @Override
    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        
        final int count = adapter.getCount();
        for (int i = 0; i < (count); i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            addTab(i, title, iconResId);
        }
        setCurrentItem(2);
        requestLayout();
    }
    
    private class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context );
        }
        public int getIndex() {
            return mIndex;
        }
    }
    
    private void addTab(int index, CharSequence text, int iconResId) {
        final TabView tabView = new TabView(getContext());
        
        Log.e("asd", "addTab at "+index +", text ="+text);
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setText(text);
        tabView.setFocusable(true);
        tabView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(MainActivity.display_width/5,MainActivity.display_width/5);
        tabView.setLayoutParams(Params1);  
        if (iconResId != 0) {
            tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        }

        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(MainActivity.display_width/5, MATCH_PARENT, 1));
    }

}
