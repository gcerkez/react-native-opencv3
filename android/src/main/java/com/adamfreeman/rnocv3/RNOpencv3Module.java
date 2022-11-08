
// @author Adam G. Freeman - adamgf@gmail.com
package com.adamfreeman.rnocv3;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.img_hash.Img_hash;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

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

    private static void reject(Promise promise, String filepath, Exception ex) {
        if (ex instanceof FileNotFoundException) {
            rejectFileNotFound(promise, filepath);
            return;
        }
        promise.reject(null, ex.getMessage());
    }

    private static void rejectFileNotFound(Promise promise, String filepath) {
        promise.reject("ENOENT", "ENOENT: no such file or directory, open '" + filepath + "'");
    }

    private static void rejectFileIsDirectory(Promise promise, String filepath) {
        promise.reject("EISDIR", "EISDIR: illegal operation on a directory, open '" + filepath + "'");
    }

    private static void rejectInvalidParam(Promise promise, String param) {
        promise.reject("EINVAL", "EINVAL: invalid parameter, read '" + param + "'");
    }

    private static Mat getMatFromImage(final String inPath, final Promise promise) {
        System.out.println("inPath: " + inPath);
        try {
            if (inPath == null || inPath.length() == 0) {
                rejectInvalidParam(promise, inPath);
                return null;
            }

            File inFileTest = new File(inPath);
            if(!inFileTest.exists()) {
                System.out.println("not exist: " + inPath);
                rejectFileNotFound(promise, inPath);
                return null;
            }
            if (inFileTest.isDirectory()) {
                rejectFileIsDirectory(promise, inPath);
                return null;
            }

            Bitmap bitmap = BitmapFactory.decodeFile(inPath);
            if (bitmap == null) {
                System.out.println("bitmap null: " + inPath);
                throw new IOException("Decoding error unable to decode: " + inPath);
            }
            Mat img = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
            Utils.bitmapToMat(bitmap, img);
            // int matIndex = MatManager.getInstance().addMat(img);

            System.out.println("returning mat: " + inPath);
            return img;
        }
        catch (Exception ex) {
            reject(promise, "EGENERIC", ex);
        }
        System.out.println("after try still null: " + inPath);
        return null;
    }

    @ReactMethod
    public void runPhash(String filePath, final Promise promise) {
        Log.d("RNOpencv3Module", "Running ReactMethod: runPhash! slim");
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
