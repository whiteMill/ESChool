package com.stk.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.stk.eschool.R;
import com.stk.fragment.ClassCenterFragment;
import com.stk.fragment.ContactFragemnt;
import com.stk.fragment.StudentCenterFragment;
import com.stk.fragment.UserCenterFragment;
import com.stk.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private StudentCenterFragment studentCenterFragment;
    private ClassCenterFragment classCenterFragment;
    private ContactFragemnt contactFragment;
    private Fragment userCenterFragment;
    private RadioButton studentBtn;
    private RadioButton contactBtn;
    private RadioButton userBtn;
    private int position = 1;
    private static final int STUDENT_CENTER = 0;
    private static final int CONTACT = 1;
    private static final int USER_CENTER = 2;
    private FragmentManager fragmentManager;
    private Intent intent;
    private User user;
    public final static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    public final static int REQUEST_CODE_ASK_LOCATION = 456;
    private static boolean mBackKeyPressed = false;
    //所需要申请的权限数组
    private static final String[] permissionsArray = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION};
    //还需申请的权限列表
    private List<String> permissionsList = new ArrayList<String>();
    //申请权限后的返回码
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = this.getIntent();
        user = (User) intent.getSerializableExtra("user");
        initView();
        fragmentManager = getSupportFragmentManager();
        setTabSelection(STUDENT_CENTER);
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissionsArray) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(permission);
                }
            }
            if(permissionsList.size()==0){

            }else{
                ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
            }
          /*  int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
            }*/
       /*    *//* int checkCallLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);*//*
            int checkCallFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallFineLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_LOCATION);
            }*/
        } else {

           // MyApplication.setCall_falg(1);
         /*   MyApplication.setAsk_location(1);*/
        }
        if (classCenterFragment != null) {
            classCenterFragment.setOnChangeClass(new OnChangeClass() {
                @Override
                public void change(String class_id, String school_id) {
                    if (contactFragment != null) {
                        contactFragment.changeClass(class_id, school_id);
                    }
                }
            });
        }
    }

    private void initView() {
        studentBtn = (RadioButton) findViewById(R.id.StudentCenter);
        contactBtn = (RadioButton) findViewById(R.id.Contact);
        userBtn = (RadioButton) findViewById(R.id.userCenter);
        studentBtn.setOnClickListener(this);
        contactBtn.setOnClickListener(this);
        userBtn.setOnClickListener(this);
    }

    private void change(int index) {
        switch (index) {
            case 0:
                studentBtn.setChecked(true);
                contactBtn.setChecked(false);
                userBtn.setChecked(false);
                studentBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.student_pressed), null, null);
                contactBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.contact_unpressed), null, null);
                userBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.user_unpressed), null, null);
                studentBtn.setTextColor(getResources().getColor(R.color.tabColor));
                contactBtn.setTextColor(getResources().getColor(R.color.tabTex));
                userBtn.setTextColor(getResources().getColor(R.color.tabTex));
                break;
            case 1:
                studentBtn.setChecked(false);
                contactBtn.setChecked(true);
                userBtn.setChecked(false);
                studentBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.student_unpressed), null, null);
                contactBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.contact_pressed), null, null);
                userBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.user_unpressed), null, null);
                studentBtn.setTextColor(getResources().getColor(R.color.tabTex));
                contactBtn.setTextColor(getResources().getColor(R.color.tabColor));
                userBtn.setTextColor(getResources().getColor(R.color.tabTex));
                break;
            case 2:
                studentBtn.setChecked(false);
                contactBtn.setChecked(false);
                userBtn.setChecked(true);
                studentBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.student_unpressed), null, null);
                contactBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.contact_unpressed), null, null);
                userBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.user_pressed), null, null);
                studentBtn.setTextColor(getResources().getColor(R.color.tabTex));
                contactBtn.setTextColor(getResources().getColor(R.color.tabTex));
                userBtn.setTextColor(getResources().getColor(R.color.tabColor));
                break;
        }
    }

    private void setTabSelection(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (position) {
            case STUDENT_CENTER:
                change(STUDENT_CENTER);
                if (user.getTYPE().equals("02")) {
                    if (studentCenterFragment == null) {
                        studentCenterFragment = new StudentCenterFragment();
                        transaction.add(R.id.changeFragment, studentCenterFragment);
                    } else {
                        transaction.show(studentCenterFragment);
                    }
                } else {
                    if (classCenterFragment == null) {
                        classCenterFragment = new ClassCenterFragment();
                        transaction.add(R.id.changeFragment, classCenterFragment);
                    } else {
                        transaction.show(classCenterFragment);
                    }
                }
                break;
            case CONTACT:
                change(CONTACT);
                if (contactFragment == null) {
                    contactFragment = new ContactFragemnt();
                    transaction.add(R.id.changeFragment, contactFragment);
                } else {
                    transaction.show(contactFragment);
                }
                break;
            case USER_CENTER:
                change(USER_CENTER);
                // 是否添加过frgament
                if (userCenterFragment == null) {
                    userCenterFragment = new UserCenterFragment();
                    transaction.add(R.id.changeFragment, userCenterFragment);
                } else {
                    transaction.show(userCenterFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (studentCenterFragment != null) {
            transaction.hide(studentCenterFragment);
        }
        if (classCenterFragment != null) {
            transaction.hide(classCenterFragment);
        }
        if (contactFragment != null) {
            transaction.hide(contactFragment);
        }
        if (userCenterFragment != null) {
            transaction.hide(userCenterFragment);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        position = savedInstanceState.getInt("position");
        setTabSelection(position);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 记录当前的position
        outState.putInt("position", position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.StudentCenter:
                setTabSelection(STUDENT_CENTER);
                break;
            case R.id.Contact:
                setTabSelection(CONTACT);
                break;
            case R.id.userCenter:
                setTabSelection(USER_CENTER);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i=0; i<permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                       // Toast.makeText(MainActivity.this, "做一些申请成功的权限对应的事！"+permissions[i], Toast.LENGTH_SHORT).show();
                    } else {
                       // Toast.makeText(MainActivity.this, "权限被拒绝： "+permissions[i], Toast.LENGTH_SHORT).show();
                    }
                }
           /* case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MyApplication.setCall_falg(1);
                } else {
                    MyApplication.setCall_falg(0);
                }*/
                break;
        /*    case REQUEST_CODE_ASK_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MyApplication.setAsk_location(1);
                } else {
                    MyApplication.setAsk_location(0);
                }
                break;*/
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {
            this.finish();
            System.exit(0);
        }
    }

    public interface OnChangeClass {
        void change(String class_id, String school_id);
    }
}