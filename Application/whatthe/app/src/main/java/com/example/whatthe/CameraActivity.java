package com.example.whatthe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Semaphore;

public class CameraActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;

    public native void smaller(long matAddrInput, long matAddrResult);

    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    // These variables are used (at the moment) to fix camera orientation from 270degree to 0degree
    Mat mRgba;
    Mat mRgbaF;
    Mat mRgbaT;

    private Mat matInput;
    private Mat matResult;
    Byte[] b;
    Button btnCapture;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    private final Semaphore writeLock = new Semaphore(1);

    public void getWriteLock() throws InterruptedException { writeLock.acquire(); }
    public void releaseWriteLock() { writeLock.release(); }

    private Timer cTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_camera);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.show_camera_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        mOpenCvCameraView.setCameraIndex(1);//1전면


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
        } else {
            Log.d(TAG, "Permissions granted");
            mOpenCvCameraView.setCameraPermissionGranted();
        }

        Button btnEnd = (Button) findViewById(R.id.btnEnd);
        btnEnd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                checkUpdate c = new checkUpdate();
                c.start();
                send_period();
            }
        });
    }

    //소켓
    private Socket socket;
    //private String ip = "192.168.0.56"; // IP
    private String ip = "192.168.0.75"; // IP
    private int port = 8000;

    int w =0,h=0,f=0, dum=0;
    String lth;
    OutputStream outputStream;

    class checkUpdate extends Thread {
        @Override
        public void run() {
            try {
                socket = new Socket(ip, port);
                //w = matInput.cols();
                //h = matInput.rows();
                w = 640;
                h = 360;
                f = w * h;
                lth = Integer.toString(f);

                dum = lth.length();
                if (dum < 12) {
                    for (int i = 1; i+dum<=12; i++)
                        lth = "0" + lth;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send_period(){
        cTimer = new Timer();

        TimerTask sp = new TimerTask(){
            @Override
            public void run() {
                try {

                    getWriteLock();

                    outputStream = socket.getOutputStream();

                    byte[] imageInByte = new byte [f];
                    byte[] inst = lth.getBytes();

                    if ( matResult == null )
                        matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

                    smaller(matInput.getNativeObjAddr(),matResult.getNativeObjAddr());

                    matInput.get(0,0,imageInByte);
                    outputStream.write(inst);
                    for(int i = 0;i<f;i++){
                        //Log.d(TAG,"???"+i);
                        outputStream.write((imageInByte[i] & 0xff));
                    }
                }
                catch (Exception e) {
                }
                releaseWriteLock();
            }
        };

        cTimer.schedule(sp, 500, 500);
    }



    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {

        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
    }

    public void onCameraViewStopped() {
        mRgba.release();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        matInput = inputFrame.rgba();

        return matInput;
    }

    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }


}
