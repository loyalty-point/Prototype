package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.AwardModel;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;


public class ShopAwardsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    public static final String SHOP_ID = "shop_ID";
    public static final String AWARD_OBJECT = "award_object";
    public static final String USER_POINT = "user_point";

    //    @InjectView(R.id.textView)
    TextView textView;

    private CardGridView mListView;
    CardGridArrayAdapter mAdapter;

    Activity mParentActivity;

    private int position;

    String shopID, cardID;
    private int userPoint;

    public ShopAwardsFragment() {}

    public ShopAwardsFragment(int position, int userPoint){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putInt(USER_POINT, userPoint);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        userPoint = getArguments().getInt(USER_POINT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_awards,container,false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getListAwards();
    }

    public void refresh() {
        getListAwards();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        shopID = ((ShopDetailActivity)mParentActivity).getCurrentShop().getId();
        cardID = ((ShopDetailActivity)mParentActivity).getCurrentCardId();
        // Lấy danh sách awards của shop về
        // Tạo và set adapter cho listview

        mAdapter = new CardGridArrayAdapter(mParentActivity, new ArrayList<Card>());
        mListView = (CardGridView) mParentActivity.findViewById(R.id.listAwards);
        mListView.setAdapter(mAdapter);
        // set listener for Item Click
    }

    public class AwardCard extends Card {

        protected TextView awardNameTv, awardQuantityTv, awardPointTv;
        protected ImageView awardImgIv;
        protected Award award;
        protected ArrayList<Shop> listShops;
        protected ButtonRectangle buyBtn;

        protected String awardName, awardQuantity, awardPoint, awardImg;

        public AwardCard(Context context) {
            super(context, R.layout.awards_list_row);
        }

        public AwardCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            //Populate the inner elements

            awardNameTv = (TextView) view.findViewById(R.id.awardName);
            awardNameTv.setText(awardName);

            awardQuantityTv = (TextView) view.findViewById(R.id.awardQuantity);
            awardQuantityTv.setText(awardQuantity);

            awardPointTv = (TextView) view.findViewById(R.id.awardPoint);
            awardPointTv.setText(awardPoint);


            awardImgIv = (ImageView) view.findViewById(R.id.awardImg);
            if (awardImg.equals(""))
                awardImg = "null";
            Picasso.with(mParentActivity).load(awardImg).placeholder(R.drawable.ic_about).into(awardImgIv);

            buyBtn = (ButtonRectangle) view.findViewById(R.id.buyBtn);
            if(userPoint < award.getPoint() && award.getQuantity() > 0){
                buyBtn.setEnabled(false);
                buyBtn.setBackgroundColor(mParentActivity.getResources().getColor(R.color.MaterialGrey));
            }else{
                buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dialog
                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mParentActivity);
                        mDialogBuilder.setTitle("How many?");
                        mDialogBuilder.setCancelable(false);

                        // Set up the input
                        final EditText quantityEditText = new EditText(mParentActivity);

                        // set properties for quantityEditText
                        quantityEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        quantityEditText.setGravity(Gravity.CENTER);
                        quantityEditText.setText("1");
                        quantityEditText.setHint("Quantity?");

                        mDialogBuilder.setView(quantityEditText);

                        //initDialog();
                        // Set listeners for dialog's buttons
                        mDialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final int quantity = Integer.valueOf(quantityEditText.getText().toString());

                                if(quantity > award.getQuantity()) {
                                    Toast.makeText(mParentActivity, "Sorry, we just have " + award.getQuantity() + " remaining items", Toast.LENGTH_LONG).show();
                                }else if((quantity*award.getPoint()) > userPoint){
                                    Toast.makeText(mParentActivity, "Sorry, Not enough point, you only have " + String.valueOf(userPoint) + "but you need " + String.valueOf(quantity*award.getPoint()) +" point to buy this award!", Toast.LENGTH_LONG).show();
                                }else {
                                    // Lấy thời gian hiện tại
                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    Date date = new Date();
                                    String time = dateFormat.format(date); //2014/08/06 15:59:48

                                    AwardModel.buyAward(Global.userToken, time, shopID, cardID, award.getID(), quantity, new AwardModel.OnBuyAwardResult() {
                                        @Override
                                        public void onSuccess() {
                                            mParentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);

                                                    // Create content layout for dialog
                                                    LayoutInflater inflater = mParentActivity.getLayoutInflater();
                                                    View contentView = inflater.inflate(R.layout.buy_award_succecssfully_dialog_layout, null);

                                                    TextView quantityTv = (TextView) contentView.findViewById(R.id.quantity);
                                                    quantityTv.setText(String.valueOf(quantity) + " x " + award.getName());

                                                    ImageView awardImageView = (ImageView) contentView.findViewById(R.id.awardImage);
                                                    Picasso.with(mParentActivity).load(award.getImage()).placeholder(R.drawable.ic_award).into(awardImageView);

                                                    builder.setTitle("Buy award successfully")
                                                            .setView(contentView)
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    // do nothing
                                                                }
                                                            })
                                                            .setNegativeButton("Go to My Awards", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent i = new Intent(mParentActivity, CardsListActivity.class);
                                                                    i.putExtra(Global.FRAGMENT_ID, Global.MY_AWARDS);
                                                                    mParentActivity.startActivity(i);
                                                                    mParentActivity.finish();
                                                                }
                                                            })
                                                            .show();
                                                    refresh();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(final String error) {
                                            mParentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                        mDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        mDialogBuilder.show();
                    }
                });
            }
        }

    }

    public void getListAwards() {
        AwardModel.getListAwards(Global.userToken, shopID, cardID, new AwardModel.OnGetListAwardsResult() {
            @Override
            public void onSuccess(final ArrayList<Award> listAwards, final ArrayList<ArrayList<Shop>> listShops) {
                // Get listAwards thành công
                // Cập nhật dữ liệu lên mAdapter
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        for (int i = 0; i < listAwards.size(); i++) {

                            AwardCard card = new AwardCard(mParentActivity);

                            //Only for test, use different titles and ratings
                            card.awardName = listAwards.get(i).getName();
                            card.awardQuantity = "Quantity: " + String.valueOf(listAwards.get(i).getQuantity());
                            card.awardPoint = String.valueOf(listAwards.get(i).getPoint()) + " points";
                            card.awardImg = listAwards.get(i).getImage();
                            card.award = listAwards.get(i);
                            card.listShops = listShops.get(i);

                            card.setOnClickListener(new Card.OnCardClickListener() {
                                @Override
                                public void onClick(Card card, View view) {
                                    Intent i = new Intent(mParentActivity, AwardDetailActivity.class);
                                    i.putExtra(Global.AWARD_OBJECT, ((AwardCard) card).award);
                                    i.putParcelableArrayListExtra(Global.SHOP_ARRAY_OBJECT, ((AwardCard) card).listShops);
                                    startActivity(i);
                                }
                            });
                            mAdapter.add(card);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listAwards không thành công
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
