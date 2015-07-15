package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Context;
import android.graphics.Bitmap;

import com.thesis.dont.loyaltypointadmin.apis.CloudStorage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by 11120_000 on 14/03/15.
 */
public class GCSHelper {

    public static void editImage(final Context mContext, final String bucketName, final String fileName, final Bitmap image,
                                   final OnEditImageResult mOnEditImageResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                // convert Bitmap image to InputStream
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);

                CloudStorage cloudStorage = new CloudStorage(mContext);
                try {
                    //cloudStorage.deleteFile(bucketName, fileName);
                    cloudStorage.uploadFile(bucketName, fileName, inputStream);
                    //cloudStorage.editFile(bucketName, fileName, inputStream);

                    mOnEditImageResult.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    mOnEditImageResult.onError(e.toString());
                }
            }
        });
        t.start();
    }

    public interface OnEditImageResult {
        public void onComplete();
        public void onError(String error);
    }

    public static void uploadImage(final Context mContext, final String bucketName, final String fileName, final Bitmap image,
                                   final OnUploadImageResult mOnUploadImageResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);

                CloudStorage cloudStorage = new CloudStorage(mContext);
                try {
                    cloudStorage.uploadFile(bucketName, fileName, inputStream);

                    mOnUploadImageResult.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    mOnUploadImageResult.onError(e.toString());
                }
            }
        });
        t.start();
    }

    public interface OnUploadImageResult {
        public void onComplete();
        public void onError(String error);
    }



    /*public void setOnUploadImageResult(OnUploadImageResult mOnUploadImageResult) {
        this.mOnUploadImageResult = mOnUploadImageResult;
    }*/
}
