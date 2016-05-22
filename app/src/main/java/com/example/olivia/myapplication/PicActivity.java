package com.example.olivia.myapplication;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by olivia on 06/04/2016.
 */
public class PicActivity extends AppCompatActivity {
    static {
        // If you use opencv 2.4, System.loadLibrary("opencv_java")
        System.loadLibrary("opencv_java3");
    }

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    private static final int PICK_IMAGE = 100;
    public static final int num32 = 32;
    public static final int num64 = 64;
    public static final int num16 = 16;
    public static  final int nBins = 9;
    private static final String TAG = "HOGSelector";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        imageView = (ImageView) findViewById(R.id.image_view);

        Button pickImageButton = (Button) findViewById(R.id.pick_image_button);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private BaseLoaderCallback mLoaderCallback =  new BaseLoaderCallback(this){
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.d(TAG, "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();

            Log.d(TAG, "Got uri");

            String selectedImagePath = getPath(imageUri);

            Bitmap myBitmap = BitmapFactory.decodeFile(selectedImagePath);
            Bitmap myBitmap32 = myBitmap.copy(Bitmap.Config.ARGB_8888, true);

            Mat pic1 = new Mat();
            Utils.bitmapToMat(myBitmap32, pic1);

            Mat grey = new Mat();
            Imgproc.cvtColor(pic1, grey, Imgproc.COLOR_BGR2GRAY);

            Log.d(TAG, "Created matrix from bitmap");

            // Create all the matrix sizes to be put into a HOG descriptor
            MatOfFloat HOGDescs = new MatOfFloat();

            Size kSize32 = new Size(num32, num32);
            Size kSize64 = new Size(num64, num64);
            Size kSize16 = new Size(num16, num16);

            Size Size16_32 = new Size(num16, num32);
            Size Size32_16 = new Size(num32, num16);
            Size Size32_64 = new Size(num32, num64);
            Size Size64_32 = new Size(num64, num32);
            Size imSize = new Size(grey.cols(), grey.rows());

            Log.d(TAG, "the number of columns of pic1 " + grey.cols());
            Log.d(TAG, "the number of rows of pic1 " + grey.rows());
            Log.d(TAG, "the size of pic1 is " + grey.size());

            HOGDescriptor hog = new HOGDescriptor(imSize, Size32_16, Size32_16, Size32_16, nBins);

            Log.d(TAG, "created the hog descriptor");

            hog.compute(grey, HOGDescs);

            Log.d(TAG, "computed HOG");

            imageView.setImageBitmap(AlteredPic);
        }
    }

    private String getPath(Uri uri){
        // just some safety built in
        if(uri == null) {
            return null;
        }
        // try to retrieve the image the image from the media store
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA};
        Cursor cursor =  getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

}

