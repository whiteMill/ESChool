package com.stk.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.UserAdapter;
import com.stk.app.MyApplication;
import com.stk.eschool.R;
import com.stk.model.ContactUser;
import com.stk.model.StuContactVo;
import com.stk.utils.CharacterUtils;
import com.stk.utils.URL;
import com.stk.view.CustomDialog;
import com.stk.view.HandVew;
import com.stk.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wangl on 2016/10/27.
 */
public class StuConFragment extends Fragment implements View.OnClickListener{

    private View view;
    private TextView mTextView;
    private ListView mListView;
    private HandVew mHandView;
    private ArrayList<ContactUser> userArrayList = null;
    private ArrayList<StuContactVo> stuContactVoArrayList= null;
    private UserAdapter userAdapter;
    private EditText sEditText;
    private ImageView seaImageView;
    private MyProgressDialog myProgressDialog;
    private String stuCon;
    private final  static int REQUEST_CODE_ASK_CALL_PHONE = 123;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stu_con_fragment, container, false);
        initView();
        queryAll();
        mHandView.setmTextView(mTextView);
        mHandView.setOnTouchCharacterListener(new HandVew.onTouchCharacterListener() {
            @Override
            public void touchCharacterListener(String s) {
                int position = userAdapter.getSelection(s);
                Log.d("DDD",position+"");
                if(position!=-1){
                    mListView.setSelection(position);
                }
            }
        });

        return view;
    }

    public void currentChange(String class_id,String school_id){
        Log.d("CLASSSSSSSSS",class_id);
        if(MyApplication.getClassVo()==null&&MyApplication.getStudentVo()==null){

        }else{
            myProgressDialog = new MyProgressDialog(getActivity(), "请稍后...", R.drawable.login);
            myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            myProgressDialog.show();
            OkGo.post(URL.STUDENT_CONT)
                    .tag(this)
                    .params("class_id", class_id)
                    .cacheKey("cacheKey")
                    .cacheMode(CacheMode.DEFAULT)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.d("RESULLLLT",s);
                            myProgressDialog.dismiss();
                            JSONObject jsonObject=null;
                            try {
                                jsonObject=new JSONObject(s);
                                if(jsonObject.getBoolean("success")){
                                    stuContactVoArrayList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<StuContactVo>>(){}.getType());
                                    for(StuContactVo stuContactVo:stuContactVoArrayList){
                                        Log.d("FDFD",stuContactVo.toString());
                                    }
                                    initDate();
                                }else{
                                    Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Toast.makeText(getActivity(),"服务器请求失败",Toast.LENGTH_SHORT).show();
                            super.onError(call, response, e);
                            myProgressDialog.dismiss();
                        }
                    });
        }
    }

    private void queryAll(){
        if(MyApplication.getClassVo()==null&&MyApplication.getStudentVo()==null){

        }else{
            myProgressDialog = new MyProgressDialog(getActivity(), "请稍后...", R.drawable.login);
            myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            myProgressDialog.show();
            OkGo.post(URL.STUDENT_CONT)
                    .tag(this)
                    .params("class_id", MyApplication.getStudentVo()==null?MyApplication.getClassVo().getCLASS_ID():MyApplication.getStudentVo().getCLASS_ID())
                    .cacheKey("cacheKey")
                    .cacheMode(CacheMode.DEFAULT)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.d("RESULLLLT",s);
                            myProgressDialog.dismiss();
                            JSONObject jsonObject=null;
                            try {
                                jsonObject=new JSONObject(s);
                                if(jsonObject.getBoolean("success")){
                                    stuContactVoArrayList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<StuContactVo>>(){}.getType());
                                    for(StuContactVo stuContactVo:stuContactVoArrayList){
                                        Log.d("FDFD",stuContactVo.toString());
                                    }
                                    initDate();
                                }else{
                                    Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Toast.makeText(getActivity(),"服务器请求失败",Toast.LENGTH_SHORT).show();
                            super.onError(call, response, e);
                            myProgressDialog.dismiss();
                        }
                    });
        }

    }

    public void queryBy(){
        if(MyApplication.getClassVo()==null&&MyApplication.getStudentVo()==null){

        }else{
            myProgressDialog = new MyProgressDialog(getActivity(), "请稍后...", R.drawable.login);
            myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            myProgressDialog.show();
            OkGo.post(URL.STUDENT_TEL)
                    .tag(this)
                    .params("class_id",  MyApplication.getStudentVo()==null?MyApplication.getClassVo().getCLASS_ID():MyApplication.getStudentVo().getCLASS_ID())
                    .params("student_name",stuCon)
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
                                    Log.d("XXX",s);
                                    stuContactVoArrayList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<StuContactVo>>(){}.getType());
                                    for(StuContactVo stuContactVo:stuContactVoArrayList){
                                        Log.d("FDFD",stuContactVo.toString());
                                    }
                                    initDate();
                                }else{
                                    Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Toast.makeText(getActivity(),"服务器请求失败",Toast.LENGTH_SHORT).show();
                            myProgressDialog.dismiss();
                            super.onError(call, response, e);
                        }
                    });
        }

    }

    private void initView(){
        seaImageView= (ImageView) view.findViewById(R.id.stu_Search);
        seaImageView.setOnClickListener(this);
        sEditText = (EditText) view.findViewById(R.id.stu_EditText);
        mTextView = (TextView)view.findViewById(R.id.sTextView);
        mListView = (ListView) view.findViewById(R.id.sListView);
        mHandView = (HandVew) view.findViewById(R.id.sHandView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final  int position, long id) {
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage(userArrayList.get(position).getUsername()+":"+userArrayList.get(position).getPhone());
                builder.setTitle("确定要拨打电话么？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /* if(MyApplication.getCall_falg()==1){*/
                             callDirectly(userArrayList.get(position).getPhone());
                       /*  }else{
                             Toast.makeText(getActivity(),"没有权限",Toast.LENGTH_SHORT).show();
                         }*/
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });
    }
    private void initDate(){
        userArrayList = new ArrayList<>();
        for(int i=0;i<stuContactVoArrayList.size();i++){
            userArrayList.add(new ContactUser(stuContactVoArrayList.get(i).getSTUDENT_NAME(), CharacterUtils.getFirstSpell(stuContactVoArrayList.get(i).getSTUDENT_NAME()).toUpperCase(),stuContactVoArrayList.get(i).getPHONE()));
        }
        Collections.sort(userArrayList, new Comparator<ContactUser>() {
            @Override
            public int compare(ContactUser userOne, ContactUser userTwo) {
                return userOne.getFirstCharacter().compareTo(userTwo.getFirstCharacter());
            }
        });
        userAdapter  =  new UserAdapter(getActivity(),userArrayList);
        mListView.setAdapter(userAdapter);
    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.stu_Search:
             stuCon = sEditText.getText().toString();
             if(stuCon==null||stuCon.isEmpty()){
                 queryAll();
             }else{
                 queryBy();
             }
             break;
     }
    }

    private void callDirectly(String mobile){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + mobile));
        startActivity(intent);
    }


}
