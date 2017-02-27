package com.stk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.app.MyApplication;
import com.stk.eschool.R;
import com.stk.utils.URL;
import com.stk.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Response;

public class EditPassActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView pass_back;
    private EditText pass_edit;
    private EditText re_pass_edit;
    private TextView warn_text;
    private Button pass_edit_button;
    private MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);
        initView();
    }

    private void initView(){
        pass_back = (ImageView) findViewById(R.id.pass_back);
        pass_edit = (EditText) findViewById(R.id.pass_edit);
        re_pass_edit = (EditText) findViewById(R.id.re_pass_edit);
        warn_text = (TextView) findViewById(R.id.warn_text);
        pass_edit_button = (Button) findViewById(R.id.pass_edit_button);
        pass_edit_button.setOnClickListener(this);
        pass_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.pass_back:
               EditPassActivity.this.finish();
               break;
           case R.id.pass_edit_button:
               if(!re_pass_edit.getText().toString().trim().equals(pass_edit.getText().toString().trim())){
                   warn_text.setText("两次输入的密码不一致");
                   warn_text.setVisibility(View.VISIBLE);
               }else if(re_pass_edit.getText().toString().trim().isEmpty()&&pass_edit.getText().toString().trim().isEmpty()){
                   warn_text.setText("密码不能为空");
                   warn_text.setVisibility(View.VISIBLE);
               }else if(!matchPass(re_pass_edit.getText().toString().trim())){
                   warn_text.setText("密码格式不正确");
                   warn_text.setVisibility(View.VISIBLE);
               }else {
                   warn_text.setVisibility(View.INVISIBLE);
                   editPass();
               }
               break;
       }
    }

    private void editPass(){
        myProgressDialog = new MyProgressDialog(this, "请稍后...", R.drawable.login);
        myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myProgressDialog.show();
        OkGo.post(URL.EDIT_PASSWORD)
                .tag(this)
                .params("userId", MyApplication.getUser().getUSER_ID())
                .params("password",pass_edit.getText().toString())
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        myProgressDialog.dismiss();
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=new JSONObject(s);
                            if(jsonObject.getBoolean("success")){
                                pass_edit.setText("");
                                re_pass_edit.setText("");
                            }
                            Toast.makeText(EditPassActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(EditPassActivity.this,"服务器请求失败",Toast.LENGTH_SHORT).show();
                        super.onError(call, response, e);
                        myProgressDialog.dismiss();
                    }
                });
    }

    public boolean matchPass(String maPhone) {
        Pattern pat = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$");
        Matcher mat = pat.matcher(maPhone);
        if (mat.matches()) {
            return true;
        } else {
            return false;
        }
    }



}
