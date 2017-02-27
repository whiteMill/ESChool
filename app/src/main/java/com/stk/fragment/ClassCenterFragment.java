package com.stk.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Login_Info_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.dpsdk_retval_e;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.stk.adapter.SpinerAdapter;
import com.stk.adapter.TimeAdapter;
import com.stk.app.MyApplication;
import com.stk.eschool.R;
import com.stk.model.AttentRecordVo;
import com.stk.model.ClassVo;
import com.stk.model.StudentVo;
import com.stk.ui.MainActivity;
import com.stk.ui.VideoViewActivity;
import com.stk.utils.DividerItemDecoration;
import com.stk.utils.URL;
import com.stk.view.MyProgressDialog;
import com.stk.view.SpinerPopWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by wangl on 2016/10/31.
 */

public class ClassCenterFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RecyclerView mRecycleView;
    private RelativeLayout class_select_layout;
    private TextView current_time;
    private Button time_select;
    private ListView timeListView;
    private TextView noRecord;
    private RelativeLayout map_select_layout;
    private TextView current_class_select;
    private TextView map_time_select;
    private MyProgressDialog myProgressDialog;
    private List<ClassVo> classVoArrayList = new ArrayList<>();
    private SpinerPopWindow mSpinerPopWindow;
    private SpinerAdapter spinerAdapter;

    private SpinerPopWindow mapSpinerPopWindow;
    private SpinerAdapter mapSpinerAdapter;
    private List<String> mapListType = new ArrayList<String>();  //类型列表

    private List<String> mListType = new ArrayList<String>();  //类型列表
    public  MainActivity.OnChangeClass onChangeClass;
    private List<StudentVo> studentVoList = new ArrayList<>();
    private TextView no_student;
    private int year, month, day;
    private SimpleDateFormat sdf;
    private String selectTime;
    private ReAdapter reAdapter;
    private ArrayList<AttentRecordVo> attendRecordVoArrayList = new ArrayList<>();
    private int currentStudnt;
    private Boolean isSelected = false;
    private MapView TmapView;
    private ScrollView tScrollView;

    private MyApplication mAPP = MyApplication.get();

    public void setOnChangeClass(MainActivity.OnChangeClass onChangeClass) {
        this.onChangeClass = onChangeClass;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.class_fragment, container, false);
        initView();
        myProgressDialog = new MyProgressDialog(getActivity(), "", R.drawable.login);
        myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myProgressDialog.show();
        queryAllClass();
        initMapSpinerWindow();
        return view;
    }

    private void initMapSpinerWindow() {
        String[] times = getResources().getStringArray(R.array.recent);
        for (int i = 0; i < times.length; i++) {
            mapListType.add(times[i]);
        }
        mapSpinerAdapter = new SpinerAdapter(getActivity(), mapListType);
        //  mapSpinerPopWindow.refreshData(mapListType, 0);
        mapSpinerPopWindow = new SpinerPopWindow(getActivity());
        mapSpinerPopWindow.setAdatper(mapSpinerAdapter);
        mapSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                if (pos >= 0 && pos <= mapListType.size()) {
                    String time = mapListType.get(pos);
                    map_time_select.setText(time.toString());
                }
            }
        });
    }

    private void queryAllClass() {
        OkGo.post(URL.CLASS_LIST)
                .tag(this)
                .params("user_id", MyApplication.getUser().getUSER_ID())
                .params("school_id", MyApplication.getUser().getSCHOOL_ID())
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("RESULLLLT", s);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(s);
                            if (jsonObject.getBoolean("success")) {
                                myProgressDialog.dismiss();
                                classVoArrayList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<ClassVo>>() {
                                }.getType());
                                for (int i = 0; i < classVoArrayList.size(); i++) {
                                    Log.d("DDD", classVoArrayList.get(i).getCLASS_NAME());
                                    mListType.add(classVoArrayList.get(i).getCLASS_NAME().trim());
                                }
                                initSpinner();
                            } else {
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        myProgressDialog.dismiss();
                        super.onError(call, response, e);
                        Toast.makeText(getActivity(), "服务器请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void initSpinner() {
        if (mListType.size() == 0) {
            Toast.makeText(getActivity(), "没有班级信息", Toast.LENGTH_SHORT).show();
            return;
        }
        current_class_select.setText(mListType.get(0));
        MyApplication.setClassVo(classVoArrayList.get(0));
        queryStudentByClass(classVoArrayList.get(0).getCLASS_ID());
        spinerAdapter = new SpinerAdapter(getActivity(), mListType);
        spinerAdapter.refreshData(mListType, 0);
        mSpinerPopWindow = new SpinerPopWindow(getActivity());
        mSpinerPopWindow.setAdatper(spinerAdapter);
        mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                timeListView.setVisibility(View.GONE);
                noRecord.setVisibility(View.VISIBLE);
                current_class_select.setText(mListType.get(pos));
                MyApplication.setClassVo(classVoArrayList.get(pos));
                onChangeClass.change(classVoArrayList.get(pos).getCLASS_ID(), classVoArrayList.get(pos).getSCHOOL_ID());
                queryStudentByClass(classVoArrayList.get(pos).getCLASS_ID());
            }
        });
    }

    private void showMapSpinnerWindow() {
        mapSpinerPopWindow.setWidth(map_select_layout.getWidth());
        mapSpinerPopWindow.showAsDropDown(map_time_select);
    }

    private void showSpinWindow() {
        Log.e("", "showSpinWindow");
        mSpinerPopWindow.setWidth(class_select_layout.getWidth());
        mSpinerPopWindow.showAsDropDown(class_select_layout);
    }

    private void initView() {
        tScrollView = (ScrollView) view.findViewById(R.id.tScrollView);
        mRecycleView = (RecyclerView) view.findViewById(R.id.mRecycle);
        mRecycleView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        TmapView = (MapView) view.findViewById(R.id.TmapView);
        View v = TmapView.getChildAt(0);
        v.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    tScrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    tScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        class_select_layout = (RelativeLayout) view.findViewById(R.id.class_select_layout);
        no_student = (TextView) view.findViewById(R.id.no_student);
        current_time = (TextView) view.findViewById(R.id.current_time);
        time_select = (Button) view.findViewById(R.id.time_select);
        timeListView = (ListView) view.findViewById(R.id.timeListView);
        noRecord = (TextView) view.findViewById(R.id.noRecord);
        map_select_layout = (RelativeLayout) view.findViewById(R.id.map_select_layout);
        map_time_select = (TextView) view.findViewById(R.id.map_time_select);
        current_class_select = (TextView) view.findViewById(R.id.current_class_select);
        class_select_layout.setOnClickListener(this);
        time_select.setOnClickListener(this);
        map_select_layout.setOnClickListener(this);
        getCurrentTime();
    }

   /* @Override
    public void onItemClick(int pos) {
        timeListView.setVisibility(View.GONE);
        noRecord.setVisibility(View.VISIBLE);
        current_class_select.setText(mListType.get(pos));
        MyApplication.setClassVo(classVoArrayList.get(pos));
        onChangeClass.change(classVoArrayList.get(pos).getCLASS_ID(),classVoArrayList.get(pos).getSCHOOL_ID());
        queryStudentByClass(classVoArrayList.get(pos).getCLASS_ID());
    }*/

    public void showTime() {
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                current_time.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                selectTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                myProgressDialog.show();
                querySchoolTime();
            }
        }, year, month - 1, day);
        datePicker.show();
    }

    private void querySchoolTime() {
        if (myProgressDialog == null) {
            myProgressDialog = new MyProgressDialog(getActivity(), "", R.drawable.login);
            myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        if (!myProgressDialog.isShowing()) {
            myProgressDialog.show();
        }
        OkGo.post(URL.ATTENCE_RECODE)
                .tag(this)
                .params("schoolID", studentVoList.get(currentStudnt).getSCHOOL_ID())
                .params("classID", studentVoList.get(currentStudnt).getCLASS_ID())
                .params("studentID", studentVoList.get(currentStudnt).getSTUDENT_ID())
                .params("studentNO", studentVoList.get(currentStudnt).getSTUDENT_NO())
                .params("cardNO", studentVoList.get(currentStudnt).getSTUDENT_CARD_NO())
                .params("beginDate", "2016-11-1")
                .params("endDate", selectTime)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("RESULLLLT", s);
                        myProgressDialog.dismiss();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(s);
                            if (jsonObject.getBoolean("success")) {
                                attendRecordVoArrayList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<AttentRecordVo>>() {
                                }.getType());
                                if (attendRecordVoArrayList.size() == 0) {
                                    Toast.makeText(getActivity(), "暂无考勤信息", Toast.LENGTH_SHORT).show();
                                    timeListView.setVisibility(View.GONE);
                                    noRecord.setVisibility(View.VISIBLE);
                                } else {
                                    timeListView.setVisibility(View.VISIBLE);
                                    noRecord.setVisibility(View.GONE);
                                    TimeAdapter timeAdapter = new TimeAdapter(getActivity(), attendRecordVoArrayList);
                                    timeAdapter.setOnTimeChick(new StudentCenterFragment.OnTimeChick() {
                                        @Override
                                        public void timeChick(View v, int position) {
                                            Log.d("current_position", position + "");
                                            myProgressDialog.show();
                                            if(mAPP.getM_loginHandle()==1){
                                                jumpToItemListActivity();
                                            }else{
                                                new LoginTask().execute();
                                            }
                                        }
                                    });
                                    timeListView.setAdapter(timeAdapter);
                                }
                            } else {
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        myProgressDialog.dismiss();
                        super.onError(call, response, e);
                        Toast.makeText(getActivity(), "服务器请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void queryStudentByClass(String class_id) {
        if (myProgressDialog == null) {
            myProgressDialog = new MyProgressDialog(getActivity(), "", R.drawable.login);
            myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        if (!myProgressDialog.isShowing()) {
            myProgressDialog.show();
        }
        OkGo.post(URL.STUDENT_LIST)
                .tag(this)
                .params("class_id", class_id)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("RESULLLLT", s);
                        myProgressDialog.dismiss();
                        Log.d("Student", s);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(s);
                            if (jsonObject.getBoolean("success")) {
                                studentVoList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<StudentVo>>() {
                                }.getType());
                                for (int i = 0; i < studentVoList.size(); i++) {
                                    Log.d("Sname", studentVoList.get(i).getSTUDENT_NAME());
                                }
                                isSelected = false;
                                if (studentVoList.size() == 0) {
                                    no_student.setVisibility(View.VISIBLE);
                                    mRecycleView.setVisibility(View.GONE);
                                } else {
                                    mRecycleView.setVisibility(View.VISIBLE);
                                    no_student.setVisibility(View.GONE);
                                    if (reAdapter == null) {
                                        reAdapter = new ReAdapter();
                                        mRecycleView.setAdapter(reAdapter);
                                    } else {
                                        for (int i = 0; i < mRecycleView.getChildCount(); i++) {
                                            TextView allText = (TextView) mRecycleView.getChildAt(i).findViewById(R.id.stu_name);
                                            allText.setTextColor(getResources().getColor(R.color.contactTex));
                                        }
                                        reAdapter.notifyDataSetChanged();

                                    }
                                    reAdapter.setOnItemClickLitener(new OnItemClickLitener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            TextView nameTxt = (TextView) view.findViewById(R.id.stu_name);
                                            nameTxt.setTextColor(getResources().getColor(R.color.blackText));
                                            for (int i = 0; i < mRecycleView.getChildCount(); i++) {
                                                if (i != position) {
                                                    TextView tText = (TextView) mRecycleView.getChildAt(i).findViewById(R.id.stu_name);
                                                    tText.setTextColor(getResources().getColor(R.color.contactTex));
                                                }
                                            }
                                            currentStudnt = position;
                                            isSelected = true;
                                            querySchoolTime();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        myProgressDialog.dismiss();
                        super.onError(call, response, e);
                        Toast.makeText(getActivity(), "服务器请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private class ReAdapter extends RecyclerView.Adapter<mViewHolder> {

        private OnItemClickLitener onItemClickLitener;

        public OnItemClickLitener getOnItemClickLitener() {
            return onItemClickLitener;
        }

        public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
            this.onItemClickLitener = onItemClickLitener;
        }

        @Override
        public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new mViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.class_adapter, parent, false));
        }

        @Override
        public void onBindViewHolder(final mViewHolder holder, int position) {
            holder.mTextView.setText(studentVoList.get(position).getSTUDENT_NAME());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }

        @Override
        public int getItemCount() {
            return studentVoList.size();
        }
    }

    private class mViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public mViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.stu_name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.class_select_layout:
                if (classVoArrayList.size() == 0) {
                    Toast.makeText(getActivity(), "没有班级信息", Toast.LENGTH_SHORT).show();
                    break;
                }
                showSpinWindow();
                break;
            case R.id.time_select:
                if (isSelected) {
                    showTime();
                } else {
                    Toast.makeText(getActivity(), "请先选择学生", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.map_select_layout:
                showMapSpinnerWindow();
                break;
        }
    }

    public void jumpToItemListActivity() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), VideoViewActivity.class);
        startActivity(intent);
    }

    private void getCurrentTime() {
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH) + 1;
        day = now.get(Calendar.DAY_OF_MONTH);
        now.get(Calendar.MINUTE);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        current_time.setText(sdf.format(new Date()));
        selectTime = sdf.format(new Date());
        Log.d("TIME", selectTime);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private class LoginTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... arg0) {               //在此处处理UI会导致异常
//			if (mloginHandle != 0) {
//	    		IDpsdkCore.DPSDK_Logout(m_loginHandle, 30000);
//        		m_loginHandle = 0;
//	    	}
            Login_Info_t loginInfo = new Login_Info_t();
            Integer error = Integer.valueOf(0);
            loginInfo.szIp = "60.191.94.121".getBytes();
            String strPort = "9000";
            loginInfo.nPort = Integer.parseInt(strPort);
            loginInfo.szUsername = "dss1121".getBytes();
            loginInfo.szPassword = "dss7016".getBytes();
            loginInfo.nProtocol = 2;
            int nRet = IDpsdkCore.DPSDK_Login(mAPP.getDpsdkCreatHandle(), loginInfo, 30000);
            return nRet;
        }

        @Override
        protected void onPostExecute(Integer result) {

            super.onPostExecute(result);
            myProgressDialog.dismiss();
            if (result == 0) {
                //登录成功，开启GetGPSXMLTask线程
                new GetGPSXMLTask().execute();

                Log.d("DpsdkLogin success:", result + ">>>>>>");
                IDpsdkCore.DPSDK_SetCompressType(mAPP.getDpsdkCreatHandle(), 0);
                mAPP.setM_loginHandle(1);
                //	m_loginHandle = 1;
                jumpToItemListActivity();
            } else {
                Log.d("DpsdkLogin failed:", result + "");
                Toast.makeText(getActivity(), "login failed" + result, Toast.LENGTH_SHORT).show();
                mAPP.setM_loginHandle(0);
                //m_loginHandle = 0;
                //jumpToContentListActivity();
            }
        }
    }

    private class GetGPSXMLTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int nRet = GetGPSXML();
            return nRet;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Toast.makeText(getActivity(), "GetGPSXML nRet" + result, Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }
    }

    public int GetGPSXML() {
        int res = -1;
        Return_Value_Info_t nGpsXMLLen = new Return_Value_Info_t();
        int nRet = IDpsdkCore.DPSDK_AskForLastGpsStatusXMLStrCount(mAPP.getDpsdkCreatHandle(), nGpsXMLLen, 10 * 1000);
        if (nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS && nGpsXMLLen.nReturnValue > 1) {
            byte[] LastGpsIStatus = new byte[nGpsXMLLen.nReturnValue - 1];
            nRet = IDpsdkCore.DPSDK_AskForLastGpsStatusXMLStr(mAPP.getDpsdkCreatHandle(), LastGpsIStatus, nGpsXMLLen.nReturnValue);

            if (nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS) {

                //System.out.printf("获取GPS XML成功，nRet = %d， LastGpsIStatus = [%s]", nRet, new String(LastGpsIStatus));
                Log.d("GetGPSXML", String.format("获取GPS XML成功，nRet = %d， LastGpsIStatus = [%s]", nRet, new String(LastGpsIStatus)));
                try {
                    File file = new File(MyApplication.LAST_GPS_PATH); // 路径  sdcard/LastGPS.xml
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(LastGpsIStatus);
                    out.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else {
                //System.out.printf("获取GPS XML失败，nRet = %d", nRet);
                Log.d("GetGPSXML", String.format("获取GPS XML失败，nRet = %d", nRet));
            }
        } else if (nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS && nGpsXMLLen.nReturnValue == 0) {
            //System.out.printf("获取GPS XML  XMLlength = 0");
            Log.d("GetGPSXML", "获取GPS XML  XMLlength = 0");
        } else {
            //System.out.printf("获取GPS XML失败，nRet = %d", nRet);
            Log.d("GetGPSXML", String.format("获取GPS XML失败，nRet = %d", nRet));
        }
        //System.out.println();
        res = nRet;
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TmapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        TmapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        TmapView.onResume();
    }
}
