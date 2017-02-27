package com.stk.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
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
import com.stk.model.MachineReceiveVO;
import com.stk.model.StudentVo;
import com.stk.ui.VideoViewActivity;
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
 * Created by wangl on 2016/10/26.
 */
public class StudentCenterFragment extends Fragment implements View.OnClickListener,SpinerAdapter.IOnItemSelectListener{
    private View view;
    private TextView stu_name;
    private TextView stu_card;
    private TextView stu_sex;
    private TextView stu_age;
    private TextView stu_phone;
    private TextView stu_grade;
    private TextView stu_School;
    private TextView stu_no;
    private StudentVo studentVo;
    private MyProgressDialog myProgressDialog;
    private TextView current_time;
    private Button time_select;
    private int year, month, day;
    private SimpleDateFormat sdf;
    private ListView timeListView;
    private String selectTime;
    private ArrayList<AttentRecordVo> attendRecordVoArrayList = new ArrayList<>();
    private TextView noRecord;
    private RelativeLayout map_select_layout;
    private TextView map_time_select;
    private List<String> mListType = new ArrayList<String>();  //类型列表
    private SpinerAdapter mAdapter;
    //设置PopWindow
    private SpinerPopWindow mSpinerPopWindow;
    private MapView mMapView;
    private ScrollView mScrollView;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private Boolean isFirstLoc=true;
    private BaiduMap baiduMap;
    private ArrayList<MachineReceiveVO> machineReceiveVOArrayList = new ArrayList<>();
    private MyApplication mAPP = MyApplication.get();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.student_fragment, container, false);
        initView();
        myProgressDialog = new MyProgressDialog(getActivity(), "", R.drawable.login);
        myProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myProgressDialog.show();
        OkGo.post(URL.STUDENT_INFO)
                .tag(this)
                .params("studentId", MyApplication.getUser().getSTUDENT_ID())
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("RESULLLLT",s);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(s);
                            if (jsonObject.getBoolean("success")) {
                                studentVo = new Gson().fromJson(jsonObject.getString("data"), StudentVo.class);
                                MyApplication.setStudentVo(studentVo);
                                initDate();
                                querySchoolTime();
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
        //初始化数据
        String[] names = getResources().getStringArray(R.array.recent);
        for (int i = 0; i < names.length; i++) {
            mListType.add(names[i]);
        }
        mAdapter = new SpinerAdapter(getActivity(), mListType);
        mAdapter.refreshData(mListType, 0);
        mSpinerPopWindow = new SpinerPopWindow(getActivity());
        mSpinerPopWindow.setAdatper(mAdapter);
        mSpinerPopWindow.setItemListener(this);

        baiduMap.setMyLocationEnabled(true);
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getActivity());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
       mLocationClient.start();
        return view;
    }

    private void querySchoolTime() {
        OkGo.post(URL.ATTENCE_RECODE)
                .tag(this)
                .params("schoolID", MyApplication.getStudentVo().getSCHOOL_ID())
                .params("classID", MyApplication.getStudentVo().getCLASS_ID())
                .params("studentID", MyApplication.getStudentVo().getSTUDENT_ID())
                .params("studentNO", MyApplication.getStudentVo().getSTUDENT_NO())
                .params("cardNO", MyApplication.getStudentVo().getSTUDENT_CARD_NO())
                .params("beginDate", "2016-11-1")
                .params("endDate", selectTime)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("RESULLLLT",s);
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
                                    TimeAdapter  timeAdapter = new TimeAdapter(getActivity(), attendRecordVoArrayList);
                                    timeAdapter.setOnTimeChick(new OnTimeChick() {
                                        @Override
                                        public void timeChick(View v, int position) {
                                            Log.d("current_position",position+"");
                                            myProgressDialog.show();
                                            new LoginTask().execute();
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

    //轨迹查询
    private void queryMap(){
        OkGo.post(URL.STUDENT_MAP)
                .tag(this)
                .params("student_id", MyApplication.getStudentVo().getSTUDENT_ID())
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("RESULLLLT",s);
                        myProgressDialog.dismiss();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(s);
                            if (jsonObject.getBoolean("success")) {
                                machineReceiveVOArrayList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<MachineReceiveVO>>() {}.getType());
                                if (machineReceiveVOArrayList.size() == 0) {
                                    Toast.makeText(getActivity(), "没有轨迹数据", Toast.LENGTH_SHORT).show();
                                } else {
                                    timeListView.setVisibility(View.VISIBLE);
                                    noRecord.setVisibility(View.GONE);
                                    timeListView.setAdapter(new TimeAdapter(getActivity(), attendRecordVoArrayList));
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

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private void initView() {
        mScrollView = (ScrollView) view.findViewById(R.id.mScrollView);
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();
        View v = mMapView.getChildAt(0);
        v.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mScrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    mScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        stu_name = (TextView) view.findViewById(R.id.stu_name);
        stu_card = (TextView) view.findViewById(R.id.stu_card);
        stu_sex = (TextView) view.findViewById(R.id.stu_sex);
        stu_age = (TextView) view.findViewById(R.id.stu_age);
        stu_phone = (TextView) view.findViewById(R.id.stu_phone);
        stu_grade = (TextView) view.findViewById(R.id.stu_grade);
        stu_School = (TextView) view.findViewById(R.id.stu_School);
        stu_no = (TextView) view.findViewById(R.id.stu_no);
        current_time = (TextView) view.findViewById(R.id.current_time);
        time_select = (Button) view.findViewById(R.id.time_select);
        timeListView = (ListView) view.findViewById(R.id.timeListView);
        noRecord = (TextView) view.findViewById(R.id.noRecord);
        map_select_layout = (RelativeLayout) view.findViewById(R.id.map_select_layout);
        map_time_select = (TextView) view.findViewById(R.id.map_time_select);
        time_select.setOnClickListener(this);
        map_select_layout.setOnClickListener(this);
        getCurrentTime();
    }

    private void initDate() {
        stu_name.setText(studentVo.getSTUDENT_NAME().trim());
        stu_card.setText(studentVo.getSTUDENT_CARD_NO().trim());
        stu_sex.setText(studentVo.getSTUDENT_GENDER().trim().equals("00") ? "男" : "女");
        stu_age.setText(studentVo.getSTUDENT_AGE() + "".trim());
        stu_phone.setText(studentVo.getPHONE().trim());
        stu_grade.setText(studentVo.getCLASS_NAME().trim());
        stu_School.setText(studentVo.getSCHOOL_NAME().trim());
        stu_no.setText(studentVo.getSTUDENT_NO().trim());
    }

    private void showTime() {
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

    private void showSpinWindow() {
        mSpinerPopWindow.setWidth(map_select_layout.getWidth());
        mSpinerPopWindow.showAsDropDown(map_time_select);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_select:
                showTime();
                break;
            case R.id.map_select_layout:
                showSpinWindow();
                break;
        }
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
    }

    @Override
    public void onItemClick(int pos) {
        if (pos >= 0 && pos <= mListType.size()) {
            String value = mListType.get(pos);
            map_time_select.setText(value.toString());
        }
    }

    public void jumpToItemListActivity()
    {
        Intent intent = new Intent();
        intent.setClass(getActivity(), VideoViewActivity.class);
        //intent.setClass(this, ItemListActivity.class);
        startActivityForResult(intent, 2);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(100)
                    .latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData); // 设置定位数据

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16); // 设置地图中心点以及缩放级别
                baiduMap.animateMapStatus(u);
            }
        }
    }


    private   class LoginTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... arg0) {               //在此处处理UI会导致异常
//			if (mloginHandle != 0) {
//	    		IDpsdkCore.DPSDK_Logout(m_loginHandle, 30000);
//        		m_loginHandle = 0;
//	    	}
            Login_Info_t loginInfo = new Login_Info_t();
            Integer error = Integer.valueOf(0);
            loginInfo.szIp 	= "60.191.94.121".getBytes();
            String strPort 	= "9000";
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

                Log.d("DpsdkLogin success:",result+"");
                IDpsdkCore.DPSDK_SetCompressType(mAPP.getDpsdkCreatHandle(), 0);
                mAPP.setM_loginHandle(1);
                //	m_loginHandle = 1;
                jumpToItemListActivity();
            } else {
                Log.d("DpsdkLogin failed:",result+"");
                Toast.makeText(getActivity(), "login failed" + result, Toast.LENGTH_SHORT).show();
                mAPP.setM_loginHandle(0);
                //m_loginHandle = 0;
                //jumpToContentListActivity();
            }
        }

    }

    private class GetGPSXMLTask extends AsyncTask<Void, Integer, Integer>{

        @Override
        protected Integer doInBackground(Void... params) {
            int nRet = GetGPSXML();
            return nRet;
        }


        @Override
        protected void onPostExecute(Integer result) {
            Toast.makeText(getActivity(), "GetGPSXML nRet"+result, Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }

    }

    public int GetGPSXML(){
        int res = -1;
        Return_Value_Info_t nGpsXMLLen = new Return_Value_Info_t();
        int nRet = IDpsdkCore.DPSDK_AskForLastGpsStatusXMLStrCount(mAPP.getDpsdkCreatHandle(), nGpsXMLLen, 10*1000);
        if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS && nGpsXMLLen.nReturnValue > 1)
        {
            byte[] LastGpsIStatus = new byte[nGpsXMLLen.nReturnValue - 1];
            nRet = IDpsdkCore.DPSDK_AskForLastGpsStatusXMLStr(mAPP.getDpsdkCreatHandle(), LastGpsIStatus, nGpsXMLLen.nReturnValue);

            if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS){

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
            }else
            {
                //System.out.printf("获取GPS XML失败，nRet = %d", nRet);
                Log.d("GetGPSXML", String.format("获取GPS XML失败，nRet = %d", nRet));
            }
        }else if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS && nGpsXMLLen.nReturnValue == 0)
        {
            //System.out.printf("获取GPS XML  XMLlength = 0");
            Log.d("GetGPSXML", "获取GPS XML  XMLlength = 0");
        }
        else
        {
            //System.out.printf("获取GPS XML失败，nRet = %d", nRet);
            Log.d("GetGPSXML", String.format("获取GPS XML失败，nRet = %d", nRet));
        }
        //System.out.println();
        res = nRet;
        return res;
    }

     public interface OnTimeChick{
         void timeChick(View v,int position);
     }
}
