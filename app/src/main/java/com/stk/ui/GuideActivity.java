package com.stk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.stk.eschool.R;
import com.stk.utils.SharedPreferencesUtils;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager guiViewPager;
    private LayoutInflater layoutInflater;
    private ArrayList<View> views = new ArrayList<>();
    private View view_one, view_two, view_three;
    private Boolean misScrolled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_guide);
        SharedPreferencesUtils.setParam(this, "flag", 0);
        initDate();
        initView();
    }

    private void initView() {
        guiViewPager = (ViewPager) findViewById(R.id.guiViewPager);
        guiViewPager.setAdapter(new mPagerAdapter());
        guiViewPager.addOnPageChangeListener(this);
    }

    private void initDate() {
        layoutInflater = LayoutInflater.from(this);
        view_one = layoutInflater.inflate(R.layout.guide_one, null);
        view_two = layoutInflater.inflate(R.layout.guide_two, null);
        view_three = layoutInflater.inflate(R.layout.guide_three, null);
        views.add(view_one);
        views.add(view_two);
        views.add(view_three);
    }

    private class mPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            // 空闲状态
            case ViewPager.SCROLL_STATE_IDLE:
                if (guiViewPager.getCurrentItem() == guiViewPager.getAdapter().getCount() - 1 && !misScrolled) {
                    Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                    startActivity(intent);
                    GuideActivity.this.finish();
                }
                break;
            //正在被拖动
            case ViewPager.SCROLL_STATE_DRAGGING:
                misScrolled = false;
                break;
            //一个拖动过程完成
            case ViewPager.SCROLL_STATE_SETTLING:
                misScrolled = true;
                break;

        }
    }
}
