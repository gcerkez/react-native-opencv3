
// @author Adam G. Freeman - adamgf@gmail.com
package com.adamfreeman.rnocv3;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfFloat;
import org.opencv.core.Scalar;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.img_hash.Img_hash;

public class RNOpencv3Module extends ReactContextBaseJavaModule {

    private static final String TAG = RNOpencv3Module.class.getSimpleName();

    static {
        System.loadLibrary("opencv_java3");
    }

    private ReactApplicationContext reactContext;


    public RNOpencv3Module(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNOpencv3";
    }

    @ReactMethod
    public void runPhash(String filePath, final Promise promise) {
        Log.d("RNOpencv3Module", "Running ReactMethod: runPhash!");
        Log.d("RNOpencv3Module", "img path: " + filePath);
        try {
            Mat imageMat = FileUtils.getMatFromImage(filePath, promise);
            Mat out = new Mat();
            Img_hash.pHash(imageMat, out);

            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < out.cols(); col++) {
                String hex = Integer.toHexString((int) out.get(0, col)[0]);
                sb.append(hex);
            }
            promise.resolve(sb.toString());
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("Exception in runPhash!");
            promise.reject("ENOENT", "ENOENT: no such file or directory, please check '" + filePath + "'");
        }
    }	
}
