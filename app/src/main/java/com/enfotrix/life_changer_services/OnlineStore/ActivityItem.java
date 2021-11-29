package com.enfotrix.life_changer_services.OnlineStore;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;

public class ActivityItem extends AppCompatActivity implements View.OnClickListener {

    ImageView img_item;
    TextView txt_itemName, txt_itemPrice, text_howmany,
            txt_discount, txt_discountPrice;
    AppCompatButton btn_card;
    ImageButton btn_decrement, btn_increment;
    private int limit = 0;
    private Utils utils;
    private String price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        img_item = findViewById(R.id.img_item);
        txt_itemName = findViewById(R.id.txt_itemName);
        txt_itemPrice = findViewById(R.id.txt_itemPrice);
        text_howmany = findViewById(R.id.text_howmany);
        btn_card = findViewById(R.id.btn_card);
        btn_decrement = findViewById(R.id.btn_decrement);
        btn_increment = findViewById(R.id.btn_increment);
        txt_discount = findViewById(R.id.txt_discount);
        txt_discountPrice = findViewById(R.id.txt_discountPrice);
        txt_discountPrice.setPaintFlags(txt_discountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txt_discountPrice.setVisibility(View.GONE);
        txt_discount.setVisibility(View.GONE);

        ////////////////////// ini databse///////////////////////////////
        utils = new Utils(this);


        /////////////////////// set image //////////////////////////////////
        Glide.with(img_item)
                .load(getIntent().getStringExtra("img"))
                .fitCenter().into(img_item);

        //////////////////////// set name and price , discoutnprice //////////////////////////
        getIntent().getStringExtra("detailsitemid");
        txt_itemName.setText(getIntent().getStringExtra("name"));

        if (Integer.parseInt(getIntent().getStringExtra("discount")) > 0) {

            txt_discount.setVisibility(View.VISIBLE);
            txt_discount.setText(getIntent().getStringExtra("discount") + "% OFF");

            int old_price = Integer.parseInt(getIntent().getStringExtra("price"));
            int discountPrice = Integer.parseInt(getpercentage(
                    getIntent().getStringExtra("discount"),
                    getIntent().getStringExtra("price")
            ));
            int newPrice = old_price - discountPrice;
            price = String.valueOf(newPrice);
            txt_itemPrice.setText("Rs " + newPrice);
            txt_discountPrice.setVisibility(View.VISIBLE);
            txt_discountPrice.setText("Rs " + old_price);

        } else {
            txt_discount.setVisibility(View.GONE);
            txt_discountPrice.setVisibility(View.GONE);
            txt_itemPrice.setText("RS " + getIntent().getStringExtra("price"));
            price = getIntent().getStringExtra("price");
            //size=availablesizeModel.getItemsize();
        }

        ///////////////////// set on click ///////////////////////
        btn_decrement.setOnClickListener(this);
        btn_increment.setOnClickListener(this);
        btn_card.setOnClickListener(this);


    }

    private String getpercentage(String discount, String price) {

        int origianlPrice = Integer.parseInt(price);
        int tempvalue = (int) Double.parseDouble(String.valueOf(origianlPrice * (Integer.parseInt(discount) / 100.0f)));

        return String.valueOf(tempvalue);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_decrement: {
                int quantity = Integer.parseInt(text_howmany.getText().toString());

                if (quantity > 1)
                    quantity--;

                text_howmany.setText(String.valueOf(quantity));


            }
            break;
            case R.id.btn_increment: {
                int quantity = Integer.parseInt(text_howmany.getText().toString());
                if (limit == 0)
                    quantity++;
                else {
                    if (quantity < limit)
                        quantity++;
                    else Toast.makeText(this, "Product limit reached!", Toast.LENGTH_SHORT).show();
                }

                text_howmany.setText(String.valueOf(quantity));

            }
            break;
            case R.id.btn_card: {

                if (utils.isLoggedIn()) {

                } else
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
}