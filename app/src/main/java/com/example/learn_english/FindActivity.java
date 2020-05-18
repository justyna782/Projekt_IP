package com.example.learn_english;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.*;
import org.opencv.imgproc.Imgproc;


import android.nfc.Tag;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

public class FindActivity extends AppCompatActivity implements  CameraBridgeViewBase.CvCameraViewListener2 {

        JavaCameraView javaCameraView;
        CameraBridgeViewBase cameraBridgeViewBase;
        int counter =0;
        Mat mRGBA, mRGBAT;

    BaseLoaderCallback  baseLoaderCallback = new BaseLoaderCallback(FindActivity.this) {
        @Override
        public void onManagerConnected(int status) {

            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        javaCameraView = findViewById(R.id.CameraView);
        javaCameraView.setVisibility((SurfaceView.VISIBLE));
        javaCameraView.setCvCameraViewListener(FindActivity.this);

        cameraBridgeViewBase = (JavaCameraView) findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility((SurfaceView.VISIBLE));
        cameraBridgeViewBase.setCvCameraViewListener(this);




    }
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Core.flip(mRGBA.t(),mRGBAT, 1);
        Imgproc.resize(mRGBAT, mRGBAT,mRGBA.size());
        return mRGBAT;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA=new Mat(height,width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(OpenCVLoader.initDebug())
        {
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
        else
        {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this,baseLoaderCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView!=null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView!=null){
            javaCameraView.disableView();
        }
    }
}
