package com.stk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.stk.eschool.R;
import com.stk.utils.SharedPreferencesUtils;

public class InitialActivity extends AppCompatActivity {
    private int flag;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x12:
                    if (flag == 1) {
                        Intent intent = new Intent(InitialActivity.this, GuideActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intents = new Intent(InitialActivity.this, LoginActivity.class);
                        startActivity(intents);
                    }
                    InitialActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        flag = (int) SharedPreferencesUtils.getParam(this, "flag", 1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x12);
            }
        }, 1000);

    }
}
