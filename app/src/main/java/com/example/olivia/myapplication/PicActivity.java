package com.example.olivia.myapplication;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.os.Bundle;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.Bitmap;

/**
 * Created by olivia on 06/04/2016.
 */
public class PicActivity extends Activity {
    static {
        // If you use opencv 2.4, System.loadLibrary("opencv_java")
        System.loadLibrary("opencv_java3");
    }

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS ) {
                // now we can call opencv code !
                helloworld();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onResume() {;
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_5,this, mLoaderCallback);
        // you may be tempted, to do something here, but it's *async*, and may take some time,
        // so any opencv call here will lead to unresolved native errors.
    };

    public void helloworld() {
        // make a mat and draw something
        Mat m = Mat.zeros(100,400, CvType.CV_8UC3);
        Imgproc.putText(m, "Elmer is great", new Point(30, 80), Core.FONT_HERSHEY_SCRIPT_SIMPLEX, 2.2, new Scalar(200, 200, 0), 2);
        // convert to bitmap:
        Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bm);

        // find the imageview and draw it!
        ImageView iv = (ImageView) findViewById(R.id.imageView1);
        iv.setImageBitmap(bm);
    }
}

