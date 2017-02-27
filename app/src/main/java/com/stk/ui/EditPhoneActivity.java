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

public class EditPhoneActivity extends AppCompatActivity implements View.OnClickListener{
     private ImageView phone_back;
    private  EditText phone_edit;
    private Button phone_edit_button;
    private TextView phone_warn;
    private MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        initView();
    }

    private void initView(){
        phone_back = (ImageView) findViewById(R.id.phone_back);
        phone_edit = (EditText) findViewById(R.id.phone_edit);
        phone_edit_button = (Button) findViewById(R.id.phone_edit_button);
        phone_warn= (TextView) findViewById(R.id.phone_warn);
        phone_edit_button.setOnClickListener(this);
        phone_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phone_back:
                EditPhoneActivity.this.finish();
                break;
            case R.id.phone_edit_button:
                if(phone_edit.getText().toString().isEmpty()){
                    phone_warn.setText("电话号码不能为空");
                    phone_warn.setVisibility(View.VISIBLE);
                }else{
                    if(matchNumber(phone_edit.getText().toString().trim())){
                        phone_warn.setVisibility(View.INVISIBLE);
                        editPhone();
                    }else{
                        phone_warn.setText("电话号码格式不正确");
                        phone_warn.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    private void editPhone(){
        myProgressDialog = new MyProgressDialog(this, "请稍后...", R.drawable.login);
        myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myProgressDialog.show();
        OkGo.post(URL.EDIT_PHONE)
                .tag(this)
                .params("userId", MyApplication.getUser().getUSER_ID())
                .params("phone",phone_edit.getText().toString().trim())
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
                                MyApplication.getUser().setPHONE(phone_edit.getText().toString().trim());
                                phone_edit.setText("");
                            }
                            Toast.makeText(EditPhoneActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(EditPhoneActivity.this,"服务器请求失败",Toast.LENGTH_SHORT).show();
                        super.onError(call, response, e);
                        myProgressDialog.dismiss();
                    }
                });
    }

    public boolean matchNumber(String maPhone) {
        Pattern pat = Pattern.compile("0?(13|14|15|18|16|17|19)[0-9]{9}");
        Matcher mat = pat.matcher(maPhone);
        if (mat.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
