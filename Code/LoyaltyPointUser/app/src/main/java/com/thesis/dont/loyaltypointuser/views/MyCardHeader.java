package com.thesis.dont.loyaltypointuser.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.controllers.Helper;
import com.thesis.dont.loyaltypointuser.models.Card;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.User;

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by 11120_000 on 17/04/15.
 */
public class MyCardHeader extends CardHeader {

    User mUser;
    Card mCard;
    Picasso mPicasso;
    boolean mIsPendingCard;
    String mQRCode;

    public MyCardHeader(Context context, Card card, User user, boolean isPending, String qrCode) {
        super(context, R.layout.card_header_inner_layout);
        mUser = user;
        mCard = card;
        mIsPendingCard = isPending;
        mQRCode = qrCode;

        mPicasso = Picasso.with(context);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null){
            RoundedImageView qrCode = (RoundedImageView) view.findViewById(R.id.qrCode);
            if(!mIsPendingCard) {
                Bitmap qrCodeBitmap = Helper.generateQRCodeImage(mQRCode, 100, 100);
                qrCode.setImageBitmap(qrCodeBitmap);
                qrCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                        // Create content layout for dialog
                        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
                        View contentView = inflater.inflate(R.layout.qrcode_dialog_layout, null);

                        final ImageView qrCodeImageView = (ImageView) contentView.findViewById(R.id.qrCodeImageView);

                        qrCodeImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                int width = qrCodeImageView.getWidth();
                                int heigth = qrCodeImageView.getHeight();

                                if(width < heigth)
                                    heigth = width;
                                else
                                    width = heigth;

                                Bitmap bigQRCodeBitmap = Helper.generateQRCodeImage(mQRCode, width, heigth);
                                qrCodeImageView.setImageBitmap(bigQRCodeBitmap);
                            }
                        });

                        builder.setTitle("Your Card's QR Code")
                                .setView(contentView)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .show();
                    }
                });
            }

            TextView cardName = (TextView) view.findViewById(R.id.cardName);
            cardName.setText(mCard.getName());

            TextView userName = (TextView) view.findViewById(R.id.userName);
            userName.setText(mUser.getFullname());
        }
    }
}
