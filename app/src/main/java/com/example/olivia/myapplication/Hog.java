package com.example.olivia.myapplication;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;

/**
 * Created by olivia on 06/04/2016.
 */
public class Hog {
    static {
        // If you use opencv 2.4, System.loadLibrary("opencv_java")
        System.loadLibrary("opencv_java3");
    }

//    These are the default values for block size and everything
//    needed to create the HOGDescriptor. For Size remember it's
//    width then height!
    private int orientBins;
    private Size winSize;
    private Size blockSize;
    private Size cellSize;
    private Size blockStride;

    public Hog(Mat photo) {
        winSize = new Size();
        blockSize = new Size();
        blockStride = new Size();
        cellSize = new Size();
        orientBins = 9;
    }
}
