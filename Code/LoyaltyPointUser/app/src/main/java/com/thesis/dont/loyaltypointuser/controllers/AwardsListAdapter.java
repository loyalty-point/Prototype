package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.AwardModel;
import com.thesis.dont.loyaltypointuser.models.Global;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 11120_000 on 18/03/15.
 */
public class AwardsListAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<Award> mAwards = new ArrayList<Award>();
    private int userPoint;

    private Activity mParentActivity;
    private ShopAwardsFragment mParentFragment;

    public AwardsListAdapter(){}

    public AwardsListAdapter(Context context, ShopAwardsFragment mParentFragment, List<Award> mAwards, int userPoint) {
        mInflater = LayoutInflater.from(context);
        this.mAwards = mAwards;
        this.userPoint = userPoint;
        mParentActivity = (Activity) context;
        this.mParentFragment = mParentFragment;
    }

    public void setListAwards(ArrayList<Award> listAwards) {
        mAwards = listAwards;
    }

    @Override
    public int getCount() {
        return mAwards.size();
    }

    @Override
    public Object getItem(int position) {
        return mAwards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;

        if(convertView == null) {
            view = mInflater.inflate(R.layout.awards_list_row, parent, false);

            // Create holder
            holder = new ViewHolder();
            holder.awardName = (TextView) view.findViewById(R.id.awardName);
            holder.awardPoint = (TextView) view.findViewById(R.id.awardPoint);
            holder.awardQuantity = (TextView) view.findViewById(R.id.awardQuantity);
            holder.awardImage = (ImageView) view.findViewById(R.id.awardImage);
            holder.awardBuy = (Button) view.findViewById(R.id.awardBuy);

            // save holder
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        final Award award = (Award) getItem(position);
        if(award.getImage() == null || award.getImage().equals(""))
            award.setImage(null);

        Picasso.with(mParentActivity).load(award.getImage()).placeholder(R.drawable.ic_award).into(holder.awardImage);
        holder.awardName.setText(award.getName());
        holder.awardPoint.setText("Point: " + String.valueOf(award.getPoint()));
        holder.awardQuantity.setText("Quantity: " + String.valueOf(award.getQuantity()));
        if(userPoint < award.getPoint() && award.getQuantity() > 0){
            holder.awardBuy.setEnabled(false);
            holder.awardBuy.setBackgroundColor(mParentActivity.getResources().getColor(R.color.MaterialGrey));
        }else{
            holder.awardBuy.setOnClickListener(new View.OnClickListener() {
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

                                AwardModel.buyAward(Global.userToken, time, award.getShopID(), award.getID(), quantity, new AwardModel.OnBuyAwardResult() {
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
                                                mParentFragment.refresh();
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
        return view;
    }

    public class ViewHolder {
        public TextView awardName;
        public TextView awardPoint;
        public TextView awardQuantity;
        public ImageView awardImage;
        public Button awardBuy;
    }
}
