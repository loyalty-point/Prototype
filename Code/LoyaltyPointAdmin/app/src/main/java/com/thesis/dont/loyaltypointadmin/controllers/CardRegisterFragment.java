package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.thesis.dont.loyaltypointadmin.models.User;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class CardRegisterFragment extends Fragment implements SearchView.OnQueryTextListener{
    private static final String ARG_POSITION = "position";
    private static final String ARG_CARDID = "mCardID";
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";
    private static final String USER_IMG = "userImg";
    private static final String USER_PHONENUMBER = "userPhoneNumber";

    Activity mParentActivity;

    private String cardId;

    private ArrayList<User> listUser = new ArrayList<User>();
    private ListView listView;
    private CustomSimpleCursorAdapter mAdapter;
    MatrixCursor cursor;
    public static Picasso mPicaso;

    private int position;

    ProgressDialog mDialog;

    public CardRegisterFragment() {}

    public CardRegisterFragment(int position, String cardId){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_CARDID, cardId);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        cardId = getArguments().getString(ARG_CARDID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_register,container,false);

        ButterKnife.inject(this, rootView);

        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void getListRegisters(){
        ShopModel.getListRegisters(Global.userToken, cardId, new ShopModel.OnGetListRegistersResult() {
            @Override
            public void onSuccess(ArrayList<User> listRegisters) {
                listUser = listRegisters;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateAdapter("");
                    }
                });
            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mParentActivity = getActivity();
        mPicaso = Picasso.with(mParentActivity);
        // add expandable button
        //addExpandableButton();

        // init dialog
        mDialog = new ProgressDialog(mParentActivity);
        mDialog.setTitle("Accepting register request");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        //create list user and search adapter.
        final String[] from = new String[]{USER_NAME};
        final int[] to = new int[]{R.id.userName, R.id.userPhone, R.id.userImg};
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, USER_NAME, USER_PHONENUMBER, USER_IMG, USER_ID});
        //create adapter and add it to list
        mAdapter = new CustomSimpleCursorAdapter(getActivity(),
                R.layout.search_registers_layout,
                cursor,
                from,
                to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView = (ListView) getActivity().findViewById(R.id.listRegisters);
        listView.setAdapter(mAdapter);
        final Dialog userInfoDialog = new Dialog(mParentActivity);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                userInfoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                userInfoDialog.setContentView(R.layout.activity_customer_register_info);

                TextView userName = (TextView) userInfoDialog.findViewById(R.id.customer_name);
                TextView userPhone = (TextView) userInfoDialog.findViewById(R.id.customer_phone);
                TextView userAddress = (TextView) userInfoDialog.findViewById(R.id.customer_address);
                TextView userEmail = (TextView) userInfoDialog.findViewById(R.id.customer_email);
                TextView userIdentityNumber = (TextView) userInfoDialog.findViewById(R.id.customer_identity);

                ImageView userImage = (ImageView) userInfoDialog.findViewById(R.id.user_image);

                ButtonRectangle acceptBtn = (ButtonRectangle) userInfoDialog.findViewById(R.id.acceptBtn);
////        addPointBtn = (ButtonRectangle) findViewById(R.id.addPointBtn);
                ButtonRectangle cancelBtn = (ButtonRectangle) userInfoDialog.findViewById(R.id.cancelBtn);

                acceptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);
                        builder.setMessage("Do you want to add this user?").
                                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDialog.show();

                                        ShopModel.acceptRegisterRequest(Global.userToken, cardId, listUser.get(position).getUsername(), new ShopModel.OnAcceptRegisterRequestResult() {
                                            @Override
                                            public void onSuccess() {
                                                mParentActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mDialog.dismiss();

                                                        AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);
                                                        builder.setTitle("Congratulations!")
                                                                .setMessage("You've added a member to your card")
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                        listUser.remove(position);
                                                                        populateAdapter("");
                                                                        userInfoDialog.dismiss();
                                                                    }
                                                                }).show();

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(final String error) {
                                                mParentActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mDialog.dismiss();
                                                        userInfoDialog.dismiss();
                                                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);
                        builder.setMessage("Do you want to reject this user?").
                                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDialog.show();

                                        ShopModel.cancelRegisterRequest(Global.userToken, cardId, listUser.get(position).getUsername(), new ShopModel.OnCancelRegisterRequestResult() {
                                            @Override
                                            public void onSuccess() {
                                                mParentActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mDialog.dismiss();
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);
                                                        builder.setTitle("Cancel Request Successfully!")
                                                                .setMessage("You've canceled this request")
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                        listUser.remove(position);
                                                                        populateAdapter("");
                                                                        userInfoDialog.dismiss();
                                                                    }
                                                                }).show();

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(final String error) {
                                                mParentActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mDialog.dismiss();
                                                        userInfoDialog.dismiss();
                                                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();

                    }
                });
                if(listUser.get(position).getFullname().equals("")){
                    userName.setVisibility(View.GONE);
                }else{
                    userName.setText("Name: " + listUser.get(position).getFullname());
                }

                if(listUser.get(position).getPhone().equals("")){
                    userPhone.setVisibility(View.GONE);
                }else{
                    userPhone.setText("Phone: " + listUser.get(position).getPhone());
                }

                if(listUser.get(position).getAddress().equals("")){
                    userAddress.setVisibility(View.GONE);
                }else{
                    userAddress.setText("Address: " + listUser.get(position).getAddress());
                }

                if(listUser.get(position).getEmail().equals("")){
                    userEmail.setVisibility(View.GONE);
                }else{
                    userEmail.setText("Email: " + listUser.get(position).getEmail());
                }

                if(listUser.get(position).getIdentityNumber().equals("")){
                    userIdentityNumber.setVisibility(View.GONE);
                }else{
                    userIdentityNumber.setText("Identity Number: " + listUser.get(position).getIdentityNumber());
                }
                String avatar = listUser.get(position).getAvatar();
                if (avatar != null && avatar.equals(""))
                    avatar = null;
                Picasso.with(mParentActivity).load(avatar).placeholder(R.drawable.ic_user).into(userImage);

                userInfoDialog.show();
//                Intent i = new Intent(mParentActivity, CustomerRegisterInfoActivity.class);
//                i.putExtra(Global.CARD_ID, cardId);
//                i.putExtra(Global.USER_OBJECT, listUser.get(position));
//                startActivity(i);
            }
        });
        //getListRegisters();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        getListRegisters();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_shop, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
    }

    //call back when scan the bar code successfully
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        /*IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            // Load user from barcode
            String barcode = scanningResult.getContents();
            // Start CalculatePointActivity
            Intent i = new Intent(mParentActivity, CalculatePointActivity.class);
            User user = new User("username", "password", "fullname", "phone", "email", "address", "avatar", "token");
            i.putExtra(Global.USER_OBJECT, user);
            startActivity(i);
        } else {

        }*/

        /*switch(requestCode) {
            case Global.SCAN_BARCODE:
                if(resultCode == Activity.RESULT_OK){
                    String barcode = intent.getStringExtra(Global.BARCODE);

                }
        }*/
    }

    // search logic
    private void populateAdapter(String query) {
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, USER_NAME, USER_PHONENUMBER, USER_IMG, USER_ID});
        for (int i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getFullname().toLowerCase().startsWith(query.toLowerCase()))
                cursor.addRow(new Object[]{i, listUser.get(i).getFullname(), listUser.get(i).getPhone(), listUser.get(i).getAvatar(), listUser.get(i).getUsername()});
        }
        mAdapter.changeCursor(cursor);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        populateAdapter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        populateAdapter(s);
        return false;
    }

    //Custom cursor to update data for suggestion list
    public class CustomSimpleCursorAdapter extends SimpleCursorAdapter {
        private Context mContext;

        public CustomSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            this.mContext = context;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.search_registers_layout, parent, false);

            return view;
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            String fullname = cursor.getString(cursor.getColumnIndex(USER_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(USER_PHONENUMBER));
            String image = cursor.getString(cursor.getColumnIndex(USER_IMG));
            final String username = cursor.getString(cursor.getColumnIndex(USER_ID));

            TextView userName = (TextView) view.findViewById(R.id.userName);
            TextView userPhone = (TextView) view.findViewById(R.id.userPhone);
            ImageView userImg = (ImageView) view.findViewById(R.id.userImg);

            userName.setText(fullname);
            userPhone.setText(phone);
            if(image.equals(""))
                image = null;
            mPicaso.load(image).placeholder(R.drawable.ic_user).into(userImg);

            Button acceptBtn = (Button) view.findViewById(R.id.acceptBtn);
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to add this user?").
                            setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDialog.show();

                                    // Gọi API để cập nhật lại là user này đã được chủ shop chấp nhận làm thành viên
                                    ShopModel.acceptRegisterRequest(Global.userToken, cardId, username, new ShopModel.OnAcceptRegisterRequestResult() {
                                        @Override
                                        public void onSuccess() {
                                            mParentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mDialog.dismiss();

                                                    // Hiện dialog thông báo
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);
                                                    builder.setTitle("Congratulations!")
                                                            .setMessage("You've added a member to your card")
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            }).show();

                                                    // Cập nhật lại danh sách registers và users
                                                    for(User user : listUser) {
                                                        if(user.getUsername().equals(username))
                                                            listUser.remove(user);
                                                    }

                                                    mParentActivity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            populateAdapter("");
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
                                                    Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }).show();
                }
            });
        }
    }


}
