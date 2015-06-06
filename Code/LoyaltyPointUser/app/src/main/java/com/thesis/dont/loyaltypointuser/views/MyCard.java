package com.thesis.dont.loyaltypointuser.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.controllers.CardDetailActivity;
import com.thesis.dont.loyaltypointuser.controllers.CardsListActivity;
import com.thesis.dont.loyaltypointuser.controllers.Helper;
import com.thesis.dont.loyaltypointuser.controllers.ShopDetailActivity;
import com.thesis.dont.loyaltypointuser.models.Card;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.User;

import app.mosn.zdepthshadowlayout.ZDepthParam;
import app.mosn.zdepthshadowlayout.ZDepthShadowLayout;
//import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by 11120_000 on 17/04/15.
 */
public class MyCard extends it.gmariotti.cardslib.library.internal.Card {


    Card mCard;
    boolean mIsPendingCard;

    User mUser;
    String mQRCode;

    Picasso mPicasso;

    Context mContext;

    Typeface customFont1;

    public MyCard(Context context, Card card, boolean isPending, User user, String qrCode) {
        this(context, R.layout.my_card_inner_layout, card, isPending, user, qrCode);
    }

    public MyCard(Context context, int innerLayout, Card card, boolean isPending, User user, String qrCode) {
        super(context, innerLayout);
        mContext = context;
        mCard = card;
        mUser = user;
        mQRCode = qrCode;
        mIsPendingCard = isPending;

        mPicasso = Picasso.with(context);
        init();
    }

    private void init(){
        customFont1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/orange_juice.ttf");
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, final View view) {

        //MyRoundedImageView cardImage = (MyRoundedImageView) view.findViewById(R.id.cardImg);
        CustomCard mCustomCard = (CustomCard) view.findViewById(R.id.customCard);
        if(mCard.getImage() == null || mCard.getImage().equals(""))
            mCard.setImage(null);

        // Load mCard.getImage() vào mCustomCard.getCardBackground
        mPicasso.load(mCard.getImage()).placeholder(R.drawable.card_img2).into(mCustomCard.getCardBackground());

        // Đọc các thông tin từ mCard, set giá trị tương ứng cho mCustomCard
        mCustomCard.setInfo(mCard.getName(), mUser.getFullname(), mQRCode,
                mCard.getCardnameX(), mCard.getCardnameY(),
                mCard.getUsernameX(), mCard.getUsernameY(),
                mCard.getQrcodeX(), mCard.getQrcodeY(),
                mCard.getTextColor(), mIsPendingCard);

        // Load qrCode lên trên mCustomCard.getQRCode()
        if(!mIsPendingCard) {
            Bitmap qrCodeBitmap = Helper.generateQRCodeImage(mQRCode, 100, 100);
            mCustomCard.getQRCode().setImageBitmap(qrCodeBitmap);

            // Set onClickListener cho mCustomCard.getQRCode()
            mCustomCard.getQRCode().setOnClickListener(new View.OnClickListener() {
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
        } else {
            //Bitmap qrCodeBitmap = Helper.generateQRCodeImage("123456789", 100, 100);
            Bitmap qrCodeBitmap = Helper.generateWhiteBitmap(100, 100);
            mCustomCard.getQRCode().setImageBitmap(qrCodeBitmap);
        }

        // Set onClickListener cho mCustomCard
        mCustomCard.getCardBackground().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent((Activity) mContext, CardDetailActivity.class);
                i.putExtra(Global.CARD_OBJECT, mCard);
                i.putExtra(Global.USER_POINT, mCard.getPoint());
                mContext.startActivity(i);
            }
        });


        /*cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent((Activity) mContext, CardDetailActivity.class);
                i.putExtra(Global.CARD_OBJECT, mCard);
                i.putExtra(Global.USER_POINT, mCard.getPoint());
                mContext.startActivity(i);
            }
        });


        ZDepthShadowLayout zdepth = (ZDepthShadowLayout) cardImage.getParent();
        zdepth.setPadding(0, 0, 0, 0);

        mPicasso.load(mCard.getImage()).placeholder(R.drawable.card_img2).into(cardImage);*/

        TextView point = (TextView) view.findViewById(R.id.point);
        if(mIsPendingCard)
            point.setText("...");
        else
            point.setText(String.valueOf(mCard.getPoint()));

        point.setTypeface(customFont1);
    }
}
