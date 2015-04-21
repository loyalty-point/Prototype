package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.AchievedEvent;
import com.thesis.dont.loyaltypointadmin.models.EventModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Product;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.thesis.dont.loyaltypointadmin.models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConfirmUpdatePointActivity extends ActionBarActivity {

    User mUser;
    String mUserName;
    Shop mShop;
    int totalMoney;
    int mTotalPoint;
    ArrayList<Product> mProducts;

    // User info
    ImageView mUserAvatar;
    TextView mFullName, mPhone;

    // Bill info
    EditText mBillCode;
    ImageView mBillImage;
    Bitmap mBillImageBitmap;

    // Point
    TextView mPointFromMoneyTV, mTotalPointsTV, mTotalMoney;
    ArrayList<AchievedEvent> achivedEventList = new ArrayList<AchievedEvent>();
    // List achieved events
    ListView mListView;
    AchievedEventsAdapter mAdapter;

    static Picasso mPicasso;
    ProgressDialog mCalculatePointDialog, mUpdatePointDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_update_point);

        // init dialog
        mCalculatePointDialog = new ProgressDialog(this);
        mCalculatePointDialog.setTitle("Calculating point");
        mCalculatePointDialog.setMessage("Please wait...");
        mCalculatePointDialog.setCancelable(false);

        mUpdatePointDialog = new ProgressDialog(this);
        mUpdatePointDialog.setTitle("Updating point");
        mUpdatePointDialog.setMessage("Please wait...");
        mUpdatePointDialog.setCancelable(false);

        mPicasso = Picasso.with(this);

        // get data
        Intent i = getIntent();
        mShop = (Shop) i.getParcelableExtra(Global.SHOP_OBJECT);
        totalMoney = i.getIntExtra(Global.TOTAL_MONEY, 0);
        mProducts = i.getParcelableArrayListExtra(Global.PRODUCT_LIST);

        // set user info to layout
        mUserAvatar = (ImageView) findViewById(R.id.userAvatarImgView);
        mFullName = (TextView) findViewById(R.id.fullname);
        mPhone = (TextView) findViewById(R.id.phone);

        //mUser = (User) i.getParcelableExtra(Global.USER_OBJECT);
        mUserName = i.getStringExtra(Global.USER_NAME);

        // get references to another view components
        mBillImage = (ImageView) findViewById(R.id.billImage);
        mBillCode = (EditText) findViewById(R.id.billCode);
        mPointFromMoneyTV = (TextView) findViewById(R.id.totalPointFromMoney);
        mTotalPointsTV = (TextView) findViewById(R.id.totalPoints);
        mTotalMoney = (TextView) findViewById(R.id.totalMoney);
        mTotalMoney.setText(String.valueOf(totalMoney));

        // init list achieved events
        mListView = (ListView) findViewById(R.id.listEvents);
        mAdapter = new AchievedEventsAdapter(this, new ArrayList<AchievedEvent>());
        mListView.setAdapter(mAdapter);

        // get user info
        ShopModel.getCustomerInfo(Global.userToken, mShop.getId(), mUserName, new ShopModel.OnGetCustomerInfoResult() {
            @Override
            public void onSuccess(User user) {
                mUser = user;
                if(mUser.getAvatar().equals(""))
                    mUser.setAvatar(null);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPicasso.load(mUser.getAvatar()).placeholder(R.drawable.user_avatar).into(mUserAvatar);
                        mFullName.setText(mUser.getFullname());
                        mPhone.setText(mUser.getPhone());
                    }
                });
            }

            @Override
            public void onError(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConfirmUpdatePointActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // set listener for buttons
        ButtonRectangle backBtn = (ButtonRectangle) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ButtonRectangle confirmBtn = (ButtonRectangle) findViewById(R.id.confirmBtn);
        confirmBtn.setEnabled(false);
        confirmBtn.setBackgroundColor(getResources().getColor(R.color.MaterialDisable));

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy billcode
                final String billcode = mBillCode.getText().toString();

                // Lấy thời gian hiện tại
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String time = dateFormat.format(date); //2014/08/06 15:59:48

                // Gọi API để cập nhật điểm
                mUpdatePointDialog.show();
                ShopModel.updatePoint(Global.userToken, achivedEventList, mShop.getId(), mUser.getUsername(), mUser.getFullname(), mUser.getPhone(),
                        mTotalPoint, billcode, time, new ShopModel.OnUpdatePointResult() {
                    @Override
                    public void onSuccess(ShopModel.UpdatePointResult result) {
                        // Nếu bucketName == "", nghĩa là chủ shop không nhập hóa đơn vào
                        // Tắt dialog và chuyển về trang ShopDetailActivity
                        if(result.bucketName.equals("")) {
                            mUpdatePointDialog.dismiss();

                            Intent i = new Intent(ConfirmUpdatePointActivity.this, ShopDetailActivity.class);
                            i.putExtra(Global.SHOP_OBJECT, mShop);
                            i.putExtra(Global.TAB_INDEX, 3);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                            return;
                        }

                        // Nếu bucketName != "", ta upload mBillImageBitmap lên Google Cloud Storage
                        GCSHelper.uploadImage(ConfirmUpdatePointActivity.this, result.bucketName, result.fileName,
                                mBillImageBitmap, new GCSHelper.OnUploadImageResult() {
                                    @Override
                                    public void onComplete() {
                                        mUpdatePointDialog.dismiss();

                                        Intent i = new Intent(ConfirmUpdatePointActivity.this, ShopDetailActivity.class);
                                        i.putExtra(Global.SHOP_OBJECT, mShop);
                                        i.putExtra(Global.TAB_INDEX, 3);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                    }

                                    @Override
                                    public void onError(final String error) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mUpdatePointDialog.dismiss();
                                                Toast.makeText(ConfirmUpdatePointActivity.this, error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                    }

                    @Override
                    public void onError(final String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mUpdatePointDialog.dismiss();
                                Toast.makeText(ConfirmUpdatePointActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

        ButtonFloat captureBtn = (ButtonFloat) findViewById(R.id.captureBtn);
        captureBtn.setBackgroundColor(getResources().getColor(R.color.PrimaryColor));
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Global.CAMERA_REQUEST);
            }
        });

        // call calculate point API
        mCalculatePointDialog.show();
        EventModel.calculatePoint(Global.userToken, mShop.getId(), totalMoney, mProducts, new EventModel.OnCalculatePointResult() {
            @Override
            public void onSuccess(final ArrayList<AchievedEvent> result, final int pointFromMoney, final int totalPoint) {
                achivedEventList = result;
                mTotalPoint = totalPoint;

                // update layout
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCalculatePointDialog.dismiss();

                        // update point
                        mPointFromMoneyTV.setText(String.valueOf(pointFromMoney));
                        mTotalPointsTV.setText(String.valueOf(totalPoint));

                        // update list achieved events data
                        mAdapter.setListAchievedEvents(result);
                        mAdapter.notifyDataSetChanged();

                        // enable confirmBtn
                        confirmBtn.setEnabled(true);
                        confirmBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
                    }
                });
            }

            @Override
            public void onError(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConfirmUpdatePointActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        switch(requestCode) {
            case Global.CAMERA_REQUEST:
                if(resultCode == RESULT_OK){
                    mBillImageBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    mBillImageBitmap = Helper.resizeBitmap(this, mBillImageBitmap);
                    mBillImage.setImageBitmap(mBillImageBitmap);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_update_point, menu);
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
