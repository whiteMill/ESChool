package com.stk.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.stk.model.ClassVo;
import com.stk.model.StudentVo;
import com.stk.model.User;

/**
 * Created by wangl on 2016/10/28.
 */
public class MyApplication extends Application {
    public static User user;
    public static StudentVo studentVo;
    public static ClassVo classVo;

    private static final String TAG = "AppApplication";
    private static final String LOG_PATH = Environment.getExternalStorageDirectory().getPath() + "/DPSDKlog.txt";
    public static final String LAST_GPS_PATH = Environment.getExternalStorageDirectory().getPath() + "/LastGPS.xml";
    private Return_Value_Info_t m_ReValue = new Return_Value_Info_t();
    private int m_loginHandle = 0;   //标记登录是否成功   1登录成功   0登录失败
    private int m_nLastError = 0;

    private static MyApplication _instance;

    public static synchronized MyApplication get() {
        return _instance;
    }
   /* public static int Call_falg;
    public static int Ask_location;*/

   /* public static int getAsk_location() {
        return Ask_location;
    }

    public static void setAsk_location(int ask_location) {
        Ask_location = ask_location;
    }

    public static int getCall_falg() {
        return Call_falg;
    }

    public static void setCall_falg(int call_falg) {
        Call_falg = call_falg;
    }*/

    public static ClassVo getClassVo() {
        return classVo;
    }

    public static void setClassVo(ClassVo classVo) {
        MyApplication.classVo = classVo;
    }

    public static StudentVo getStudentVo() {
        return studentVo;
    }

    public static void setStudentVo(StudentVo studentVo) {
        MyApplication.studentVo = studentVo;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MyApplication.user = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        initApp();
        SDKInitializer.initialize(getApplicationContext());
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "commonParamsValue2");
        //-----------------------------------------------------------------------------------//

        //必须调用初始化
        OkGo.init(this);

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()
                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                    .debug("OkGo")
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                 //   .setRetryCount(3)

                    //如果不想让框架管理cookie,以下不需要
//                .setCookieStore(new MemoryCookieStore())                //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())          //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置,不需要不用设置
//                    .setCertificates()                                  //方法一：信任所有证书
//                    .setCertificates(getAssets().open("srca.cer"))      //方法二：也可以自己设置https证书
//                    .setCertificates(getAssets().open("aaaa.bks"), "123456", getAssets().open("srca.cer"))//方法三：传入bks证书,密码,和cer证书,支持双向加密

                    //可以添加全局拦截器,不会用的千万不要传,错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

                    //这两行同上,不需要就不要传
                    .addCommonHeaders(headers)                                         //设置全局公共头
                    .addCommonParams(params);                                          //设置全局公共参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDpsdkHandle(){
        if(m_loginHandle == 1)  //登录成功，返回PDSDK_Creat时返回的 有效句柄
            return m_ReValue.nReturnValue;
        else
            return 0;
    }


    public int getM_loginHandle() {
        return m_loginHandle;
    }

    public int getDpsdkCreatHandle(){  //仅用于获取DPSDK_login的句柄
        return m_ReValue.nReturnValue;
    }

    public void setM_loginHandle(int m_loginHandle) {
        this.m_loginHandle = m_loginHandle;
    }

    public void initApp(){
        //Creat DPSDK
        Log.d("initApp:",m_nLastError+"");
        int nType = 1;
        Log.d("m_nLastError",m_nLastError+"");
        m_nLastError = IDpsdkCore.DPSDK_Create(nType, m_ReValue);
        Log.d("m_nLastError",m_nLastError+"");

        //set logPath
        m_nLastError = IDpsdkCore.DPSDK_SetLog(m_ReValue.nReturnValue, LOG_PATH.getBytes());
        Log.d("DPSDK_SetLog:",m_nLastError+"");

    }
}
