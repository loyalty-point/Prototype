package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;

import java.io.FileNotFoundException;

public class CreateShopActivity extends ActionBarActivity {

    ImageView shopLogoImgView;
    EditText mShopName, mPhone, mExchangeRatio, mAddress;
    Spinner mCategory;
    CheckBox mAgreeTerm;
    ProgressDialog mDialog;

    private static final int SELECT_PHOTO = 100;

    Bitmap shopLogo = null;

    //String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Uploading shop logo");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        mCategory = (Spinner) findViewById(R.id.shopcategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shopCategory, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mCategory.setAdapter(adapter);

        mShopName = (EditText) findViewById(R.id.shopname);
        mPhone = (EditText) findViewById(R.id.phone);
        mExchangeRatio = (EditText) findViewById(R.id.exchangeRatio);
        mAddress = (EditText) findViewById(R.id.shopAddress);
        mAgreeTerm = (CheckBox) findViewById(R.id.agreeTerm);

        final ButtonRectangle createShopBtn = (ButtonRectangle) findViewById(R.id.confirmBtn);
        ButtonRectangle cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);

        if(mAgreeTerm.isChecked()) {
            createShopBtn.setEnabled(true);
            createShopBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        }else {
            createShopBtn.setEnabled(false);
            createShopBtn.setBackgroundColor(getResources().getColor(R.color.MaterialDisable));
        }
        mAgreeTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAgreeTerm.isChecked()) {
                    createShopBtn.setEnabled(true);
                    createShopBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
                }
                else {
                    createShopBtn.setEnabled(false);
                    createShopBtn.setBackgroundColor(getResources().getColor(R.color.MaterialDisable));
                }
            }
        });

        shopLogoImgView = (ImageView) findViewById(R.id.shopLogo);
        shopLogoImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        createShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String shopname = mShopName.getText().toString();
                final String phone = mPhone.getText().toString();
                final String exchangeRatio = mExchangeRatio.getText().toString();
                final String address = mAddress.getText().toString();
                final String category = mCategory.getSelectedItem().toString();

                // Kiểm tra khác null
                if(Helper.checkNotNull(shopname, phone, exchangeRatio, address)) {
                    Toast.makeText(CreateShopActivity.this, "please enter all the information", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra shopname hợp lệ
                if(Helper.checkShopName(shopname)) {
                    Toast.makeText(CreateShopActivity.this, "shop name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Đến đây thì thông tin người dùng nhập vào đã hoàn toàn hợp lệ
                // Gọi api để tạo shop

                // Tạo đường dẫn của shopLogo trên Google Cloud Storage
                // Upload ảnh lên Google Cloud Storage

                // Show progress dialog
                mDialog.show();

                GCSHelper.uploadImage("loyalty-point-photos", "test", shopLogo, new GCSHelper.OnUploadImageResult() {
                    @Override
                    public void onComplete(String url) {

                        // dismiss Progress Dialog
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                            }
                        });

                        // Create shop
                        Shop shop = new Shop(shopname, address, phone, category, Float.valueOf(exchangeRatio), url);
                        ShopModel.setOnCreateShopResult(new ShopModel.OnCreateShopResult() {
                            @Override
                            public void onSuccess() {
                                // Tạo shop thành công
                                Intent i = new Intent(CreateShopActivity.this, ShopsListActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }

                            @Override
                            public void onError(String e) {
                                final String exception = e;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Tạo shop không thành công
                                        Toast.makeText(CreateShopActivity.this, exception, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                        ShopModel.createShop(shop, Global.userToken);
                    }

                    @Override
                    public void onError(final String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Toast.makeText(CreateShopActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateShopActivity.this, ShopsListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        //super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        shopLogo = decodeUri(selectedImage);
                        shopLogoImgView.setImageBitmap(shopLogo);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    /*InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);*/
                }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

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
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
