package com.stk.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.app.MyApplication;
import com.stk.eschool.R;
import com.stk.model.User;
import com.stk.utils.URL;
import com.stk.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText user_name;
    private EditText user_password;
    private CheckBox rem_box;
    private ImageView eye;
    private TextView loginBtn;
    private int flag = 1;
    private String userNama;
    private String password;
    private MyProgressDialog myProgressDialog;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private int sum=0;
    private String str_names,str_pwds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        share = getSharedPreferences("ispwd", MODE_PRIVATE);
        str_names = share.getString("name", "");
        str_pwds = share.getString("pwd", "");
        sum = share.getInt("sum", 0);
        editor = share.edit();
        // 记住密码设置
        if (sum == 0) {
            rem_box.setChecked(false);
        } else {
            rem_box.setChecked(true);
            user_name.setText(str_names);
            user_password.setText(str_pwds);
        }
    }

    private void initView(){
        user_name = (EditText) findViewById(R.id.user_name);
        user_password = (EditText) findViewById(R.id.user_pass);
        rem_box = (CheckBox) findViewById(R.id.rem_box);
        eye = (ImageView) findViewById(R.id.eeye);
        loginBtn = (TextView) findViewById(R.id.login_btn);
        rem_box.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        eye.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.rem_box:
              break;
          case R.id.eeye:
              if(flag==1){
                  eye.setBackground(getResources().getDrawable(R.drawable.pass_out));
                  user_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                  flag=0;
              }else{
                  eye.setBackground(getResources().getDrawable(R.drawable.pass_in));
                  user_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                  flag=1;
              }
              break;
          case R.id.login_btn:
              userNama = user_name.getText().toString().trim();
              password  = user_password.getText().toString().trim();
              if(isflag(userNama,password)){
                  myProgressDialog = new MyProgressDialog(this, "正在加载中......", R.drawable.login);
                  myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                  myProgressDialog.show();
                  OkGo.post(URL.LOGIN)
                          .tag(this)
                          .params("USERNAME",userNama)
                          .params("PASSWORD",password)
                          .cacheKey("cacheKey")
                          .cacheMode(CacheMode.DEFAULT)
                          .execute(new StringCallback() {
                              @Override
                              public void onSuccess(String s, Call call, Response response) {
                                  Log.d("RESULLLLT",s);
                                  myProgressDialog.dismiss();
                                  JSONObject jsonObject= null;
                                  try {
                                      jsonObject = new JSONObject(s);
                                      if(jsonObject.getBoolean("success")){
                                          User user = new Gson().fromJson(jsonObject.getString("data"), User.class);
                                          if(user.getSTATUS().equals("0")){
                                              Toast.makeText(LoginActivity.this,"该账户已冻结,请联系管理员！",Toast.LENGTH_SHORT).show();
                                          }else{
                                              MyApplication.setUser(user);
                                              if (rem_box.isChecked()) {
                                                  editor.putString("name", userNama);
                                                  editor.putString("pwd", password);
                                                  editor.putInt("sum", 1);
                                              } else {
                                                  editor.putInt("sum", 0);
                                              }
                                              editor.commit();
                                              Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                              Bundle bundle=new Bundle();
                                              bundle.putSerializable("user",user);
                                              intent.putExtras(bundle);
                                              startActivity(intent);
                                              LoginActivity.this.finish();
                                          }
                                      }else{
                                          Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                      }
                                  } catch (JSONException e) {
                                      e.printStackTrace();
                                  }
                              }
                              @Override
                              public void onError(Call call, Response response, Exception e) {
                                  myProgressDialog.dismiss();
                                  super.onError(call, response, e);
                                  Toast.makeText(LoginActivity.this, "服务器请求失败", Toast.LENGTH_SHORT).show();
                              }
                          });

              }else{
                  Toast.makeText(this, "请把信息输入完整。", Toast.LENGTH_SHORT).show();
              }
              break;
      }
    }

    public boolean isflag(String str, String str1) {
        if (str.equals("") || str == null || str1.equals("") || str1 == null) {
            return false;
        }
        return true;
    }
}
