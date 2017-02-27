package com.stk.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.stk.app.MyApplication;
import com.stk.eschool.R;
import com.stk.model.UpdataInfo;
import com.stk.ui.AboutActivity;
import com.stk.ui.EditPassActivity;
import com.stk.ui.EditPhoneActivity;
import com.stk.ui.LoginActivity;
import com.stk.utils.ApkUtils;
import com.stk.utils.URL;
import com.stk.view.CustomDialog;
import com.stk.view.MyProgressDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wl on 2016/10/26.
 */
public class UserCenterFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RelativeLayout editPass;
    private RelativeLayout editPhone;
    private RelativeLayout changeUser;
    private RelativeLayout exitUser;
    private RelativeLayout updateApp;
    private RelativeLayout aboutApp;
    private TextView user_name;
    private TextView user_phone;
    private View appView;
    private ProgressBar progressBar;
    private TextView curr_pro;
    private CustomDialog.Builder builder;
    private TextView tips;
    private MyProgressDialog myProgressDialog;
    private UpdataInfo updataInfo;
    private int flag = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_fragment, container, false);
        initView();
        return view;
    }

    private void initView() {
        editPass = (RelativeLayout) view.findViewById(R.id.editPass);
        editPhone = (RelativeLayout) view.findViewById(R.id.editPhone);
        changeUser = (RelativeLayout) view.findViewById(R.id.changeUser);
        exitUser = (RelativeLayout) view.findViewById(R.id.exitUser);
        updateApp = (RelativeLayout) view.findViewById(R.id.updateApp);
        aboutApp = (RelativeLayout) view.findViewById(R.id.aboutApp);
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_phone = (TextView) view.findViewById(R.id.user_phone);
        builder = new CustomDialog.Builder(getActivity());
        editPass.setOnClickListener(this);
        editPhone.setOnClickListener(this);
        changeUser.setOnClickListener(this);

        exitUser.setOnClickListener(this);
        updateApp.setOnClickListener(this);
        aboutApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editPass:
                Intent editPassIn = new Intent(getActivity(), EditPassActivity.class);
                startActivity(editPassIn);
                break;
            case R.id.editPhone:
                Intent editPhoneIn = new Intent(getActivity(), EditPhoneActivity.class);
                startActivity(editPhoneIn);
                break;
            case R.id.aboutApp:
                Intent abourAppIn = new Intent(getActivity(), AboutActivity.class);
                startActivity(abourAppIn);
                break;
            case R.id.changeUser:
                Intent changeIn = new Intent(getActivity(), LoginActivity.class);
                startActivity(changeIn);
                getActivity().finish();
                break;
            case R.id.exitUser:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("提醒").setMessage("是否退出程序")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getActivity().finish();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create(); // 创建对话框
                alertDialog.show(); // 显示对话框
                break;
            case R.id.updateApp:
                if (flag == 0) {
                    Toast.makeText(getActivity(), "正在更新.请稍后", Toast.LENGTH_SHORT).show();
                } else {
                    if (myProgressDialog == null) {
                        myProgressDialog = new MyProgressDialog(getActivity(), "请稍后...", R.drawable.login);
                        myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    }
                    myProgressDialog.show();
                    OkGo.get(URL.APP_DOWNLOAD)//
                            .tag(getActivity())//
                            .execute(new FileCallback() {  //文件下载时，可以指定下载的文件目录和文件名
                                @Override
                                public void onSuccess(File file, Call call, Response response) {

                                }

                                @Override
                                public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                    //这里回调下载进度(该回调在主线程,可以直接更新ui)
                                    if (currentSize / totalSize == 1) {
                                        myProgressDialog.dismiss();
                                        try {
                                            InputStream in1 = new FileInputStream(Environment.getExternalStorageDirectory().getPath() + File.separator + "Download" + File.separator + "update.xml");
                                            BufferedInputStream in2 = new BufferedInputStream(in1);
                                            updataInfo = ApkUtils.getUpdataInfo(in2);
                                            Log.d("info", updataInfo.toString());
                                            if (!updataInfo.getVersion().equals(getVersionName())) {
                                                appView = LayoutInflater.from(getActivity()).inflate(R.layout.update_app_pop, null);
                                                progressBar = (ProgressBar) appView.findViewById(R.id.up_progress);
                                                curr_pro = (TextView) appView.findViewById(R.id.curr_pro);
                                                tips = (TextView) appView.findViewById(R.id.tips);
                                                builder.setContentView(appView);
                                                builder.setTitle("提醒");
                                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    public void onClick(final DialogInterface dialog, int which) {
                                                        tips.setVisibility(View.GONE);
                                                        progressBar.setVisibility(View.VISIBLE);
                                                        curr_pro.setVisibility(View.VISIBLE);
                                                        OkGo.get(updataInfo.getUrl())
                                                                .tag(getActivity())
                                                                .execute(new FileCallback() {  //文件下载时，可以指定下载的文件目录和文件名
                                                                    @Override
                                                                    public void onSuccess(File file, Call call, Response response) {

                                                                    }

                                                                    @Override
                                                                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                                                        flag = 0;
                                                                        //这里回调下载进度(该回调在主线程,可以直接更新ui)
                                                                        progressBar.setMax((int) totalSize);
                                                                        progressBar.setProgress((int) currentSize);
                                                                        curr_pro.setText("当前进度：" + (Math.round(progress * 10000) * 1.0f / 100) + "%");
                                                                        if (currentSize / totalSize == 1) {
                                                                            flag = 1;
                                                                            dialog.dismiss();
                                                                            ApkUtils.install(getActivity(), new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "Download" + File.separator + "update.apk"));
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onError(Call call, Response response, Exception e) {
                                                                        dialog.dismiss();
                                                                        super.onError(call, response, e);
                                                                        Toast.makeText(getActivity(), "服务器请求失败", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                });
                                                builder.setNegativeButton("取消",
                                                        new android.content.DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                builder.create().show();
                                            } else {
                                                Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    myProgressDialog.dismiss();
                                    Toast.makeText(getActivity(), "获取新版本失败", Toast.LENGTH_SHORT).show();
                                    super.onError(call, response, e);
                                }
                            });
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        user_name.setText(MyApplication.getUser().getNAME());
        user_phone.setText(MyApplication.getUser().getPHONE());
    }

    private String getVersionName() throws Exception {
        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        return packInfo.versionName;
    }
}
