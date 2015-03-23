package com.thesis.loyaltypointsystem.backend;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import sun.rmi.runtime.Log;

/**
 * Created by CPU10207-local on 3/23/2015.
 */
public class GCSHelper {

    //private OnUploadImageResult mOnUploadImageResult;


    public static void uploadImage(final String bucketName, final String fileName, final byte[] byteArray,
                                   final OnUploadImageResult mOnUploadImageResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                RetryParams params = RetryParams.getDefaultInstance();
                GcsService gcsService = GcsServiceFactory.createGcsService(params);

                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();*/

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
        t.start();
    }

    public interface OnUploadImageResult {
        public void onComplete(String url);
        public void onError(String error);
    }

    /*public void setOnUploadImageResult(OnUploadImageResult mOnUploadImageResult) {
        this.mOnUploadImageResult = mOnUploadImageResult;
    }*/
}
