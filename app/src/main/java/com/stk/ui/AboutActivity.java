package com.stk.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.stk.eschool.R;

public class AboutActivity extends AppCompatActivity {

    private ImageView backView;
    private TextView mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView(){
        backView = (ImageView) findViewById(R.id.about_back);
        mVersion = (TextView) findViewById(R.id.mVersion);
        try {
            mVersion.setText("版本号 V："+ getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
    }

    private String getVersionName() throws Exception {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
        return packInfo.versionName;
    }


}
