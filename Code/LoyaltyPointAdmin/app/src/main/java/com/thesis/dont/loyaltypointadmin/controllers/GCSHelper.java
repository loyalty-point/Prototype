package com.thesis.dont.loyaltypointadmin.controllers;

import android.graphics.Bitmap;

/*import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;*/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by 11120_000 on 14/03/15.
 */
public class GCSHelper {

    //private OnUploadImageResult mOnUploadImageResult;


    public static void uploadImage(final String bucketName, final String fileName, final Bitmap image,
                                   final OnUploadImageResult mOnUploadImageResult) {

        /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                awardImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                GcsFilename file = new GcsFilename(bucketName, fileName);
                GcsOutputChannel outputChannel = null;

                try {
                    outputChannel = gcsService.createOrReplace(file, GcsFileOptions.getDefaultInstance());
                    outputChannel.write(ByteBuffer.wrap(byteArray));
                    outputChannel.close();

                    mOnUploadImageResult.onComplete(bucketName + " " + fileName);

                } catch (IOException e) {
                    e.printStackTrace();
                    mOnUploadImageResult.onComplete(e.toString());
                }
            }
        });
        t.start();*/
    }

    public interface OnUploadImageResult {
        public void onComplete(String url);
        public void onError(String error);
    }



    /*public void setOnUploadImageResult(OnUploadImageResult mOnUploadImageResult) {
        this.mOnUploadImageResult = mOnUploadImageResult;
    }*/
}
