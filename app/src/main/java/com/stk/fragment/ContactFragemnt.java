package com.stk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stk.eschool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangl on 2016/10/26.
 */
public class ContactFragemnt extends Fragment implements View.OnClickListener{
    private StuConFragment stuConFragment;
    private TeaConFragment teaConFragment;
    private View view;
    private TextView studentTex;
    private TextView teacherTex;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private LinearLayout mLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contact_fragment, container, false);
        initView();
        return view;
    }

    private void initView() {
        mViewPager= (ViewPager) view.findViewById(R.id.mViewPager);
        studentTex = (TextView) view.findViewById(R.id.studentTex);
        teacherTex = (TextView) view.findViewById(R.id.teacherTex);
        mLayout= (LinearLayout) view.findViewById(R.id.mLayout);
        studentTex.setOnClickListener(this);
        teacherTex.setOnClickListener(this);
        stuConFragment=new StuConFragment();
        teaConFragment=new TeaConFragment();
        fragmentList.add(stuConFragment);
        fragmentList.add(teaConFragment);

        mViewPager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                change(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void change(int index){
        switch (index) {
            case 0:
                studentTex.setTextColor(getResources().getColor(R.color.contact_line));
                studentTex.setBackgroundColor(getResources().getColor(R.color.layoutBackground));
                teacherTex.setTextColor(getResources().getColor(R.color.layoutBackground));
                teacherTex.setBackgroundColor(getResources().getColor(R.color.contact_line));
                mLayout.setBackground(getResources().getDrawable(R.drawable.contact_shape));
                break;
            case 1:
                studentTex.setTextColor(getResources().getColor(R.color.layoutBackground));
                studentTex.setBackgroundColor(getResources().getColor(R.color.contact_line));
                teacherTex.setTextColor(getResources().getColor(R.color.contact_line));
                teacherTex.setBackgroundColor(getResources().getColor(R.color.layoutBackground));
                mLayout.setBackground(getResources().getDrawable(R.drawable.contact_shape));
                break;
        }


    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    public void changeClass(String class_id,String school_id){
        //Log.d("ClassId","当前的classId为"+class_id+"当前的schoolId为"+school_id);
        stuConFragment.currentChange(class_id,school_id);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.studentTex:
              mViewPager.setCurrentItem(0);
              change(0);
              break;
          case R.id.teacherTex:
              mViewPager.setCurrentItem(1);
              change(1);
              break;
      }
    }
}
