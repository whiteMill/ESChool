package com.stk.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.company.PlaySDK.IPlaySDK;
import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_RealStream_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.stk.app.MyApplication;
import com.stk.eschool.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.stk.eschool.R.id.mSurfaceView;

public class VideoViewActivity extends AppCompatActivity {
    private SurfaceView m_svPlayer;
    private Button open;
    private Button close;
    private TextView cameraName;
    private Button scr_change;

    public final static String IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/snapshot/";
    public final static String IMGSTR = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
    private static final int PicFormat_JPEG = 1;

    private byte[] m_szCameraId = null;
    private int m_pDLLHandle = 0;
    private int m_nPort = 0;
    private int m_nSeq = 0;
    private int mTimeOut = 30 * 1000;
    private Boolean isOri = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        initView();
        m_pDLLHandle = MyApplication.get().getDpsdkHandle();
        m_nPort = IPlaySDK.PLAYGetFreePort();
        SurfaceHolder holder = m_svPlayer.getHolder();
        m_szCameraId = "1001141$1$0$0".getBytes();
        cameraName.setText("手机测试球机44_1");
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                IPlaySDK.InitSurface(m_nPort, m_svPlayer);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d("xss", "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d("xss", "surfaceDestroyed");
            }
        });

        final fMediaDataCallback fm = new fMediaDataCallback() {

            @Override
            public void invoke(int nPDLLHandle, int nSeq, int nMediaType,
                               byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) {

                int ret = IPlaySDK.PLAYInputData(m_nPort, szData, nDataLen);
                if (ret == 1) {
                    Log.e("xss", "playing success=" + nSeq + " package size=" + nDataLen);
                } else {
                    Log.e("xss", "playing failed=" + nSeq + " package size=" + nDataLen);
                }
            }
        };

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StartRealPlay()) {
                    Log.e("xss", "StartRealPlay failed!");
                    Toast.makeText(getApplicationContext(), "Open video failed!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Return_Value_Info_t retVal = new Return_Value_Info_t();
                    Get_RealStream_Info_t getRealStreamInfo = new Get_RealStream_Info_t();
                    //m_szCameraId = etCam.getText().toString().getBytes();
                    System.arraycopy(m_szCameraId, 0, getRealStreamInfo.szCameraId, 0, m_szCameraId.length);
                    //getRealStreamInfo.szCameraId = "1000096$1$0$0".getBytes();
                    getRealStreamInfo.nMediaType = 1;
                    getRealStreamInfo.nRight = 0;
                    getRealStreamInfo.nStreamType = 1;
                    getRealStreamInfo.nTransType = 1;
                    Enc_Channel_Info_Ex_t ChannelInfo = new Enc_Channel_Info_Ex_t();
                    IDpsdkCore.DPSDK_GetChannelInfoById(m_pDLLHandle, m_szCameraId, ChannelInfo);
                    int ret = IDpsdkCore.DPSDK_GetRealStream(m_pDLLHandle, retVal, getRealStreamInfo, fm, mTimeOut);
                    if (ret == 0) {
                        open.setEnabled(false);
                        close.setEnabled(true);
                        m_nSeq = retVal.nReturnValue;
                        Log.e("DPSDKStream success!", ret + "");
                        Toast.makeText(getApplicationContext(), "Open video success!", Toast.LENGTH_SHORT).show();
                    } else {
                        StopRealPlay();
                        Log.e("DPSDKStream failed!", ret + "");
                        Toast.makeText(getApplicationContext(), "Open video failed!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("xss", e.toString());
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int ret = IDpsdkCore.DPSDK_CloseRealStreamByCameraId(m_pDLLHandle, m_szCameraId, mTimeOut);
                int ret = IDpsdkCore.DPSDK_CloseRealStreamBySeq(m_pDLLHandle, m_nSeq, mTimeOut);
                if (ret == 0) {
                    open.setEnabled(true);
                    close.setEnabled(false);
                    Log.e("xss", "DPSDK_CloseRealStreamByCameraId success!");
                    Toast.makeText(getApplicationContext(), "Close video success!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("xss", "DPSDK_CloseRealStreamByCameraId failed! ret = " + ret);
                    Toast.makeText(getApplicationContext(), "Close video failed!", Toast.LENGTH_SHORT).show();
                }
                StopRealPlay();
            }
        });

    }

    private void initView() {
        m_svPlayer = (SurfaceView) findViewById(mSurfaceView);
        open = (Button) findViewById(R.id.open);
        close = (Button) findViewById(R.id.close);
        cameraName = (TextView) findViewById(R.id.cameraName);
        scr_change = (Button) findViewById(R.id.src_change);


    }

    public boolean StartRealPlay() {
        if (m_svPlayer == null)
            return false;

        boolean bOpenRet = IPlaySDK.PLAYOpenStream(m_nPort, null, 0, 1500 * 1024) == 0 ? false : true;
        if (bOpenRet) {
            boolean bPlayRet = IPlaySDK.PLAYPlay(m_nPort, m_svPlayer) == 0 ? false : true;
            Log.i("StartRealPlay", "StartRealPlay1");
            if (bPlayRet) {
                boolean bSuccess = IPlaySDK.PLAYPlaySoundShare(m_nPort) == 0 ? false : true;

                Log.i("StartRealPlay", "StartRealPlay2");
                if (!bSuccess) {
                    IPlaySDK.PLAYStop(m_nPort);
                    IPlaySDK.PLAYCloseStream(m_nPort);
                    Log.i("StartRealPlay", "StartRealPlay3");
                    return false;
                }
            } else {
                IPlaySDK.PLAYCloseStream(m_nPort);
                Log.i("StartRealPlay", "StartRealPlay4");
                return false;
            }
        } else {
            Log.i("StartRealPlay", "StartRealPlay5");
            return false;
        }

        return true;
    }

    public void StopRealPlay() {
        try {
            IPlaySDK.PLAYStopSoundShare(m_nPort);
            IPlaySDK.PLAYStop(m_nPort);
            IPlaySDK.PLAYCloseStream(m_nPort);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
