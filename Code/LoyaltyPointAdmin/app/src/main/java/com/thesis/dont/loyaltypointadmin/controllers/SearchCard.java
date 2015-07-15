package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.BaseColumns;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Card;
import com.thesis.dont.loyaltypointadmin.models.CardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class SearchCard extends ActionBarActivity {

    private static final String CARD_NAME = "card_name";
    public static final String CARD_IMG = "card_image";

    private ArrayList<CardCheckList> listCard = new ArrayList<CardCheckList>();
    ListView listCards;
    private CardsListAdapter mAdapter;
    MatrixCursor cursor;
    FancyButton createCardBtn;
    public static Picasso mPicaso;
    Shop shop;
    Bitmap shopImage;
    int chosenCardPosition = -1;
    ButtonRectangle cancelBtn, finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_card);
        mPicaso = Picasso.with(this);
        Intent intent = getIntent();
        shop = (Shop) intent.getParcelableExtra(Global.SHOP_OBJECT);
        shopImage = (Bitmap) intent.getParcelableExtra(Global.OBJECT);

        cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        finishBtn = (ButtonRectangle) findViewById(R.id.confirmBtn);
        //checking chosing card event and create shop
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chosenCardPosition == -1){
                    Toast.makeText(SearchCard.this,"choose card please!", Toast.LENGTH_LONG).show();
                }else {
                    ShopModel.setOnCreateShopResult(new ShopModel.OnCreateShopResult() {
                        @Override
                        public void onSuccess(ShopModel.CreateShopResult result) {
                            if (shopImage != null) {
                                GCSHelper.uploadImage(SearchCard.this, result.bucketName, result.fileName, shopImage, new GCSHelper.OnUploadImageResult() {
                                    @Override
                                    public void onComplete() {
//                                        Intent i = new Intent(SearchCard.this, CardsListActivity.class);
//                                        startActivity(i);
                                        if(!Global.tempActivity.equals(null)){
                                            Global.tempActivity.finish();
                                        }

                                        Intent i = new Intent(SearchCard.this, CardsListActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                    }

                                    @Override
                                    public void onError(final String error) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(SearchCard.this, error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            } else {
//                                Intent i = new Intent(SearchCard.this, CardsListActivity.class);
//                                startActivity(i);
                                if(!Global.tempActivity.equals(null)){
                                    Global.tempActivity.finish();
                                }
                                Intent i = new Intent(SearchCard.this, CardsListActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                        }

                        @Override
                        public void onError(final String e) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Tạo shop không thành công
                                    Toast.makeText(SearchCard.this, e, Toast.LENGTH_LONG).show();
                                }
                            });
                            if(!Global.tempActivity.equals(null)){
                                Global.tempActivity.finish();
                            }
                            Intent i = new Intent(SearchCard.this, CardsListActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    });
                    ShopModel.createShop(shop, listCard.get(chosenCardPosition).getCard().getId(), Global.userToken);
                }
            }
        });
        //create new card
        createCardBtn = (FancyButton) findViewById(R.id.createCardBtn);
        createCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchCard.this, CreateCardActivity.class);
                startActivity(i);
            }
        });

        //create list user and search adapter.
        final String[] from = new String[]{CARD_NAME};
        final int[] to = new int[]{R.id.cardName, R.id.cardImg};
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, CARD_NAME, CARD_IMG});
        //create adapter and add it to list
        mAdapter = new CardsListAdapter(this,
                R.layout.search_user_layout,
                cursor,
                from,
                to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        listCards = (ListView) findViewById(R.id.listCards);
        listCards.setAdapter(mAdapter);
        //select card to add shop
        listCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //new checkbox
                View v = getViewByPosition(position, listCards);

                CheckBox checkBox = (CheckBox) ((ViewGroup)v).getChildAt(0);//get checkbox view
                checkBox.setChecked(!checkBox.isChecked());
                listCard.get(position).setSelected(checkBox.isChecked());

                //old checkbox
                if(chosenCardPosition!= -1) {
                    v = getViewByPosition(chosenCardPosition, listCards);

                    checkBox = (CheckBox) ((ViewGroup) v).getChildAt(0);//get checkbox view
                    checkBox.setChecked(!checkBox.isChecked());
                    listCard.get(chosenCardPosition).setSelected(checkBox.isChecked());
                }

                chosenCardPosition = position;
            }
        });
        //getListUsers();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getListCards();
    }

    private void getListCards() {
        CardModel.getListCards(Global.userToken, new CardModel.OnGetListResult() {
            @Override
            public void onSuccess(ArrayList<Card> listCards) {
                listCard.clear();
                for (int i = 0; i < listCards.size(); i++) {
                    CardCheckList card = new CardCheckList(listCards.get(i), false);
                    listCard.add(card);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateAdapter("");
                    }
                });
            }

            @Override
            public void onError(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchCard.this, "error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
//populate to search data
    private void populateAdapter(String query) {
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, CARD_NAME, CARD_IMG});
        for (int i = 0; i < listCard.size(); i++) {
            if (listCard.get(i).getCard().getName().toLowerCase().startsWith(query.toLowerCase()))
                cursor.addRow(new Object[]{i, listCard.get(i).getCard().getName(), listCard.get(i).getCard().getImage()});
        }
        mAdapter.changeCursor(cursor);

    }

    //card list adapter
    public class CardsListAdapter extends SimpleCursorAdapter {
        private Context mContext;

        public CardsListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            this.mContext = context;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.search_card_layout, parent, false);

            return view;
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            String cardname = cursor.getString(cursor.getColumnIndex(CARD_NAME));
            String image = cursor.getString(cursor.getColumnIndex(CARD_IMG));
            final int position = cursor.getPosition();

            TextView cardName = (TextView) view.findViewById(R.id.cardName);
            ImageView cardImg = (ImageView) view.findViewById(R.id.cardImg);

            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

            checkBox.setChecked(listCard.get(position).isSelected());
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //new checkbox
                    listCard.get(position).setSelected(checkBox.isChecked());

                    //old checkbox
                    if(chosenCardPosition!= -1) {
                        v = getViewByPosition(chosenCardPosition, listCards);

                        CheckBox checkBox = (CheckBox) ((ViewGroup) v).getChildAt(0);//get checkbox view
                        checkBox.setChecked(!checkBox.isChecked());
                        listCard.get(chosenCardPosition).setSelected(checkBox.isChecked());
                    }

                    chosenCardPosition = position;
                }
            });



            cardName.setText(cardname);

            if (image.equals(""))
                image = null;

            mPicaso.load(image).placeholder(R.drawable.ic_user_avatar).into(cardImg);

        }
    }

    public class CardCheckList {

        private Card card;
        private boolean selected = false;

        public CardCheckList(Card card, boolean isSelected) {
            this.setSelected(isSelected);
            this.setCard(card);
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
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
