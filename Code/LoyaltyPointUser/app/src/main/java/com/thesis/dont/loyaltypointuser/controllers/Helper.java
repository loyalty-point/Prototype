package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.EnumMap;
import java.util.Formatter;
import java.util.Map;

/**
 * Created by tinntt on 2/4/2015.
 */
public class Helper {
    public static String objectToJson(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Object jsonToObject(String json, Class myClass){
        Gson gson = new Gson();
        return gson.fromJson(json, myClass);
    }

    public static String hashPassphrase(String passPhrase, String salt) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        digest.reset();
        digest.update(salt.getBytes());
        byte[] hashedBytes = digest.digest((passPhrase.getBytes()));
        return bytesToString(hashedBytes);
    }

    private static String bytesToString(byte[] hashValue) {
        Formatter form = new Formatter();
        for (int i = 0; i < hashValue.length; i++)
            form.format("%02x", hashValue[i]);
        return form.toString();
    }

    public static boolean checkPassword(String password) {
        // check size (trên 6 kí tự)
        if(password.length() < 6)
            return true;

        return false;
    }

    public static boolean checkUserName(String username) {
        // check size (trên 6 kí tự)
        if(username.length() < 6)
            return true;

        // check kí tự đặc biệt (username chỉ chứa kí tự và số)
        for(int i=0; i<username.length(); i++) {
            char c = username.charAt(i);
            if(c < '0' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a') || c > 'z')
                return true;
        }

        return false;
    }

    public static boolean checkShopName(String shopname) {
        // check size (trên 2 kí tự)
        if(shopname.length() < 2)
            return true;

        return false;
    }

    public static boolean checkAwardName(String awardName) {
        // check size (trên 2 kí tự)
        if(awardName.length() < 1)
            return true;

        return false;
    }

    public static boolean checkNotNull(String username, String password, String confirmPassword, String fullname, String phone) {
        if(username.equals("") || password.equals("") || confirmPassword.equals("") ||
                fullname.equals("") || phone.equals("")) {
            return true;
        }

        return false;
    }

    public static boolean checkNotNull(String shopname, String phone, String exchangeRatio, String address) {
        if(shopname.equals("") || phone.equals("") || exchangeRatio.equals("") ||
                address.equals("")) {
            return true;
        }

        return false;
    }

    public static boolean checkNotNull(String awardName, String point, String quantity) {
        if(awardName.equals("") || point.equals("") || quantity.equals("")) {
            return true;
        }

        return false;
    }

    public static boolean checkNotNull(String username, String password) {
        if(username.equals("") || password.equals("")) {
            return true;
        }

        return false;
    }

    public static ResponseHandler<String> getResponseHandler() {
        return new ResponseHandler<String>() {
            public String handleResponse(final HttpResponse response)
                    throws HttpResponseException, IOException {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }

                HttpEntity entity = response.getEntity();
                return entity == null ? null : EntityUtils.toString(entity, "UTF-8");
            }
        };
    }

    private static int REQUIRED_SIZE = 280;

    public static Bitmap decodeUri(Context mContext, Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(selectedImage), null, o);

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public static Bitmap decodeUri(Context mContext, byte[] selectedImage) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new ByteArrayInputStream(selectedImage), null, o);

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(new ByteArrayInputStream(selectedImage), null, o2);
    }

    public static Bitmap resizeBitmap(Context mContext, Bitmap bitmapIn) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        o.outWidth = bitmapIn.getWidth();
        o.outHeight = bitmapIn.getHeight();
        //BitmapFactory.decodeStream(BitmapToInputStream(bitmapIn), null, o);

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(BitmapToInputStream(bitmapIn), null, o2);
    }

    public static Bitmap decodeUriNormal(Context mContext, Uri selectedImage) throws FileNotFoundException {

        InputStream imageStream = null;
        try {
            imageStream = mContext.getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap result = BitmapFactory.decodeStream(imageStream);
        return result;
    }

    public static byte[] BitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static InputStream BitmapToInputStream(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        return bs;
    }

    public static String generateBarCode(String username) {
        String hash = String.valueOf(username.hashCode());
        return hash.substring(0, 10);
    }

    public static Bitmap generateBarCodeImage(String barcode) {
        Bitmap bitmap = null;
        try {
            bitmap = encodeAsBitmap(barcode, BarcodeFormat.CODE_128, 800, 500);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
}
