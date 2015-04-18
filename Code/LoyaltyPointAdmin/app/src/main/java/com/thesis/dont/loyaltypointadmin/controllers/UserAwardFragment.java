package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.TicketModel;
import com.thesis.dont.loyaltypointadmin.models.UserModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;


public class UserAwardFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    public static final String SHOP_ID = "shop_ID";
    public static final String USER_ID = "user_ID";
    public static final String USER_POINT = "user_point";
    public static final String AWARD_OBJECT = "award_object";

    //    @InjectView(R.id.textView)
    TextView textView;

    ListView mListView;
    UserAwardsListAdapter mAdapter;
    AlertDialog.Builder mDialogBuilder;
    private int position;

    String shopID;
    String userID;

    public UserAwardFragment(int position, String userId, String shopId) {
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(SHOP_ID, shopId);
        b.putString(USER_ID, userId);
        this.setArguments(b);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        shopID = getArguments().getString(SHOP_ID);
        userID = getArguments().getString(USER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_awards, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getListAwards();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // set listener for createAward button
        ButtonFloat createAwardBtn = (ButtonFloat) getActivity().findViewById(R.id.createAwardBtn);
        createAwardBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        createAwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateAwardActivity.class);
                i.putExtra(SHOP_ID, ((ShopDetailActivity) getActivity()).getCurrentShop().getId());
                startActivity(i);
            }
        });

        // Lấy danh sách awards của shop về
        // Tạo và set adapter cho listview
        mAdapter = new UserAwardsListAdapter(getActivity(), new ArrayList<Award>());
        mListView = (ListView) getActivity().findViewById(R.id.listAwards);
        mListView.setAdapter(mAdapter);

        // set listener for Item Click
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        // Load dữ liệu lên list
        getListAwards();
    }

    public void getListAwards() {
        AwardModel.getListAwards(Global.userToken, shopID, new AwardModel.OnGetListAwardsResult() {
            @Override
            public void onSuccess(ArrayList<Award> listAwards) {
                // Get listAwards thành công
                // Cập nhật dữ liệu lên mAdapter
                mAdapter.setListAwards(listAwards);
                UserAwardFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listAwards không thành công
                        Toast.makeText(UserAwardFragment.this.getActivity(), error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void refresh() {
        getListAwards();
    }

    public class UserAwardsListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<Award> mAwards = new ArrayList<Award>();

        private Activity mParentActivity;

        public UserAwardsListAdapter() {
        }

        public UserAwardsListAdapter(Context context, List<Award> mAwards) {
            mInflater = LayoutInflater.from(context);
            this.mAwards = mAwards;
            mParentActivity = (Activity) context;
            mDialogBuilder = new AlertDialog.Builder(mParentActivity);
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

            if (convertView == null) {
                view = mInflater.inflate(R.layout.user_awards_list_row, parent, false);

                // Create holder
                holder = new ViewHolder();
                holder.awardName = (TextView) view.findViewById(R.id.awardName);
                holder.awardPoint = (TextView) view.findViewById(R.id.awardPoint);
                holder.awardQuantity = (TextView) view.findViewById(R.id.awardQuantity);
                holder.awardImage = (ImageView) view.findViewById(R.id.awardImage);
                holder.awardSellBtn = (Button) view.findViewById(R.id.awardSellBtn);

                // save holder
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }

            final Award award = (Award) getItem(position);
            Picasso.with(mParentActivity).load(award.getImage()).placeholder(R.drawable.ic_award).into(holder.awardImage);
            holder.awardName.setText(award.getName());
            holder.awardPoint.setText("Point: " + String.valueOf(award.getPoint()));
            holder.awardQuantity.setText("Quantity: " + String.valueOf(award.getQuantity()));

            holder.awardSellBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogBuilder = new AlertDialog.Builder(mParentActivity);
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
                            if (quantity > award.getQuantity()) {
                                Toast.makeText(mParentActivity, "Sorry, we just have " + award.getQuantity() + " remaining items", Toast.LENGTH_LONG).show();
                            } else {

                                mDialogBuilder = new AlertDialog.Builder(mParentActivity);
                                mDialogBuilder.setTitle("Identity number confirm");
                                mDialogBuilder.setCancelable(false);

                                // Set up the input
                                final EditText IdentityNumberEditText = new EditText(mParentActivity);

                                // set properties for quantityEditText
                                IdentityNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                IdentityNumberEditText.setGravity(Gravity.CENTER);
                                IdentityNumberEditText.setHint("User's Identity Number");

                                mDialogBuilder.setView(IdentityNumberEditText);
                                mDialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UserModel.checkIdentityNumberUser(Global.userToken, userID, IdentityNumberEditText.getText().toString(), new UserModel.OnCheckIdentityNumberResult() {
                                            @Override
                                            public void onSuccess() {
                                                // Lấy thời gian hiện tại
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                Date date = new Date();
                                                String time = dateFormat.format(date); //2014/08/06 15:59:48
                                                AwardModel.sellAward(Global.userToken, userID, time, shopID, award.getID(), quantity, new AwardModel.OnSellAwardResult() {
                                                    @Override
                                                    public void onSuccess() {
                                                        mParentActivity.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                refresh();
                                                                new AlertDialog.Builder(mParentActivity)
                                                                        .setTitle("Success")
                                                                        .setMessage("Award's sold!")
                                                                        .setIcon(android.R.drawable.ic_dialog_info)
                                                                        .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                // do nothing
                                                                            }
                                                                        })
                                                                        .show();
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

                                            @Override
                                            public void onError(final String error) {
                                                mParentActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        new AlertDialog.Builder(mParentActivity)
                                                                .setTitle("Error")
                                                                .setMessage(error)
                                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        // do nothing
                                                                    }
                                                                })
                                                                .show();
                                                    }
                                                });
                                            }
                                        });
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
            return view;
        }

        public class ViewHolder {
            public TextView awardName;
            public TextView awardPoint;
            public TextView awardQuantity;
            public ImageView awardImage;
            public Button awardSellBtn;
        }
    }
}
