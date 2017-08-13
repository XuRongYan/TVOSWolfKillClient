package com.rongyant.commonlib.util;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XRY on 2017/8/13.
 */

public class CameraUtil {
    public static File tempFile;
    public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 2;// 拍摄视频

    public static Uri openCamera(Activity activity, Application application) {
        int sdkInt = Build.VERSION.SDK_INT;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = null;
        activity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
        return uri;
    }

    public static boolean hasSdCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File createMediaFile() throws IOException {
        if (hasSdCard()) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES),
                    "CameraVideos");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "VID_" + timeStamp;
            String suffix = ".mp4";
            File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
            return mediaFile;
        }
        return null;
    }

}
