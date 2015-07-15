package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Card;
import com.thesis.dont.loyaltypointadmin.models.CardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.RequiredCustomerInfo;
import com.thesis.dont.loyaltypointadmin.views.CustomCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateCardActivity extends ActionBarActivity {

    CustomCard mCustomCard;
    Bitmap cardBackgroud = null;
    EditText cardName;
    ProgressDialog mDialog, mCreatingDialog;
    ButtonRectangle cancelBtn, createBtn;
    Button mColorPickerBtn;
    static Picasso mPicasso;
    AmbilWarnaDialog mColorPickerDialog;
    int mColor = R.color.MaterialRed;
    ListView userInfoList;
    String[] requiredInfo = new String[]{"Phone", "Email", "Full name", "Address", "Identity Number"};
    RequiredCustomerInfo requiredCustomerInfo = new RequiredCustomerInfo(false, false, false, false, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        mColorPickerDialog = new AmbilWarnaDialog(this, R.color.MaterialRed, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                // do nothing
            }

            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {
                mColor = color;
                mCustomCard.getCardName().setTextColor(mColor);
                mCustomCard.getUserName().setTextColor(mColor);
            }
        });

        final RequiredUserInfoAdapter adapter = new RequiredUserInfoAdapter(this, requiredInfo);

        mPicasso = Picasso.with(this);
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Updating Card Background");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        mCreatingDialog = new ProgressDialog(this);
        mCreatingDialog.setTitle("Creating Card");
        mCreatingDialog.setMessage("Please wait...");
        mCreatingDialog.setCancelable(false);

        userInfoList = (ListView) findViewById(R.id.requiredUserInfoList);
        userInfoList.setAdapter(adapter);
        userInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v = getViewByPosition(position, userInfoList);

                CheckBox checkBox = (CheckBox) ((ViewGroup) v).getChildAt(0);//get checkbox view
                checkBox.setChecked(!checkBox.isChecked());
            }
        });

        mCustomCard = (CustomCard) findViewById(R.id.cardBackground);
        cardName = (EditText) findViewById(R.id.cardName);
        mColorPickerBtn = (Button) findViewById(R.id.colorPicker);
        mColorPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorPickerDialog.show();
            }
        });
        cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);
        createBtn = (ButtonRectangle) findViewById(R.id.confirmBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardName.getText().toString().equals("")) {
                    Toast.makeText(CreateCardActivity.this, "input card name pls!", Toast.LENGTH_LONG).show();
                } else {
                    if (mCustomCard.getCardBackground().getDrawable() != null)
                        cardBackgroud = ((BitmapDrawable) mCustomCard.getCardBackground().getDrawable()).getBitmap();

                    Card card = new Card(cardName.getText().toString(), "", mCustomCard.getCardNameX(), mCustomCard.getCardNameY(),
                            mCustomCard.getUserNameX(), mCustomCard.getUserNameY(),
                            mCustomCard.getQRCodeX(), mCustomCard.getQRCodeY(),
                            mColor);

                    mCreatingDialog.show();
                    CardModel.createCard(Global.userToken, card, requiredCustomerInfo,  new CardModel.OnCreateCardResult() {
                        @Override
                        public void onSuccess(final CardModel.CreateCardResult result) {
                            //update anh background card
                            if (cardBackgroud != null) {
                                GCSHelper.uploadImage(CreateCardActivity.this, result.bucketName, result.fileName, cardBackgroud, new GCSHelper.OnUploadImageResult() {
                                    @Override
                                    public void onComplete() {
                                        String imageLink = "http://storage.googleapis.com/" + result.bucketName + "/" + result.fileName;
                                        mPicasso.invalidate(imageLink);
                                        mCreatingDialog.dismiss();
                                        finish();
                                    }

                                    @Override
                                    public void onError(final String error) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mCreatingDialog.dismiss();
                                                Toast.makeText(CreateCardActivity.this, error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            } else {
                                mCreatingDialog.dismiss();
                                finish();
                            }
                        }

                        @Override
                        public void onError(final String error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mCreatingDialog.dismiss();
                                    Toast.makeText(CreateCardActivity.this, error, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            }
        });

        mCustomCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateCardActivity.this, CropImageActivity.class);
                i.putExtra(CropImageActivity.ASPECT_RATIO_Y, 610);
                i.putExtra(CropImageActivity.ASPECT_RATIO_X, 948);
                ((Activity) CreateCardActivity.this).startActivityForResult(i, Global.SELECT_PHOTO);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Global.SELECT_PHOTO: {
                if (resultCode == RESULT_OK) {
                    mDialog.show();
                    Bundle bundle = data.getExtras();
                    byte[] imageByteArray = data.getByteArrayExtra(CropImageActivity.CROPPED_IMAGE);
                    final Bitmap cardBackground = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    BitmapDrawable ob = new BitmapDrawable(getResources(), cardBackground);
                    mCustomCard.getCardBackground().setImageDrawable(ob);
                    mDialog.dismiss();
                }
                break;
            }
        }
    }

    private class RequiredUserInfoAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] userRequiredInfo;

        public RequiredUserInfoAdapter(Context context, String[] values) {
            super(context, -1, values);
            this.context = context;
            this.userRequiredInfo = values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.user_required_info_row, parent, false);

            TextView userInfo = (TextView) rowView.findViewById(R.id.userInfo);
            userInfo.setText(userRequiredInfo[position]);
            final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
            checkBox.setChecked(requiredCustomerInfo.getStateAtPosition(position));

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    requiredCustomerInfo.setStateAtPosition(position, checkBox.isChecked());
                }
            });
            return rowView;
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

}


