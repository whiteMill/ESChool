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
import com.stk.model.TeaContactVo;
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
public class TeaConFragment extends Fragment implements View.OnClickListener{
    private View view;
    private EditText tea_EditText;
    private ImageView tea_Search;
    private HandVew tHandView;
    private TextView tTextView;
    private ListView tListView;
    private MyProgressDialog myProgressDialog;
    private ArrayList<TeaContactVo> teaContactVoArrayList=new ArrayList<>();
    private ArrayList<ContactUser> userArrayList=new ArrayList<>();
    private UserAdapter userAdapter;
    private String teaName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tea_con_fragment, container, false);
        initView();
        queryAll();
        tHandView.setmTextView(tTextView);
        tHandView.setOnTouchCharacterListener(new HandVew.onTouchCharacterListener() {
            @Override
            public void touchCharacterListener(String s) {
                int position = userAdapter.getSelection(s);
                Log.d("DDD",position+"");
                if(position!=-1){
                    tListView.setSelection(position);
                }
            }
        });
        return view;
    }

    private void queryAll(){
        myProgressDialog = new MyProgressDialog(getActivity(), "请稍后...", R.drawable.login);
        myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myProgressDialog.show();
        OkGo.post(URL.TEACHER_CONT)
                .tag(this)
                .params("school_id",MyApplication.getStudentVo()!=null?MyApplication.getStudentVo().getSCHOOL_ID():MyApplication.getUser().getSCHOOL_ID())
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
                                teaContactVoArrayList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<TeaContactVo>>(){}.getType());
                                for(TeaContactVo teaContactVo:teaContactVoArrayList){
                                    Log.d("TDTD",teaContactVo.toString());
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

    public void queryBy(){
        myProgressDialog = new MyProgressDialog(getActivity(), "请稍后...", R.drawable.login);
        myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myProgressDialog.show();
        OkGo.post(URL.TEACHER_TEL)
                .tag(this)
                .params("school_id",MyApplication.getStudentVo()!=null?MyApplication.getStudentVo().getSCHOOL_ID():MyApplication.getUser().getSCHOOL_ID())
                .params("teacher_name",teaName)
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
                                teaContactVoArrayList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<TeaContactVo>>(){}.getType());
                                for(TeaContactVo teaContactVo:teaContactVoArrayList){
                                    Log.d("FDFD",teaContactVo.toString());
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

    private void initView(){
        tea_EditText = (EditText) view.findViewById(R.id.tea_EditText);
        tea_Search = (ImageView) view.findViewById(R.id.tea_Search);
        tHandView = (HandVew) view.findViewById(R.id.tHandView);
        tTextView = (TextView) view.findViewById(R.id.tTextView);
        tListView = (ListView) view.findViewById(R.id.tListView);
        tea_Search.setOnClickListener(this);
        tListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage(userArrayList.get(position).getUsername()+":"+userArrayList.get(position).getPhone());
                builder.setTitle("确定要拨打电话么？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      /*  if(MyApplication.getCall_falg()==1){*/
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
        for(int i=0;i<teaContactVoArrayList.size();i++){
            userArrayList.add(new ContactUser(teaContactVoArrayList.get(i).getNAME(), CharacterUtils.getFirstSpell(teaContactVoArrayList.get(i).getNAME()).toUpperCase(),teaContactVoArrayList.get(i).getPHONE()));
        }
        Collections.sort(userArrayList, new Comparator<ContactUser>() {
            @Override
            public int compare(ContactUser userOne, ContactUser userTwo) {
                return userOne.getFirstCharacter().compareTo(userTwo.getFirstCharacter());
            }
        });
        userAdapter  =  new UserAdapter(getActivity(),userArrayList);
        tListView.setAdapter(userAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tea_Search:
                teaName = tea_EditText.getText().toString();
                if(teaName==null||teaName.isEmpty()){
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
