package com.enfotrix.life_changer_services.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.enfotrix.life_changer_services.Model.Model_Item;
import com.enfotrix.life_changer_services.OnlineStore.ActivityItem;
import com.enfotrix.life_changer_services.R;

import java.util.ArrayList;

public class Adapter_Item extends RecyclerView.Adapter<Adapter_Item.ViewHolder> {
    Context context;
    ArrayList<Model_Item> modelItemArrayList;

    public Adapter_Item(Context context, ArrayList<Model_Item> modelItemArrayList) {
        this.context = context;
        this.modelItemArrayList = modelItemArrayList;
    }

    @NonNull
    @Override
    public Adapter_Item.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_Item model_item = modelItemArrayList.get(position);

        Glide.with(holder.img_item)
                .load(modelItemArrayList.get(position).getImg())
                .fitCenter().into(holder.img_item);

        holder.txt_itemName.setText(model_item.getItemname());
        holder.txt_deiscountPrice.setVisibility(View.GONE);

        int price = Integer.parseInt(model_item.getItemprice());
        int DiscountPrice = Integer.parseInt(
                getpercentage(
                        model_item.getItemprice(),
                        model_item.getDiscoutnprice()
                ));
        int newPrice = price - DiscountPrice;

        if (Integer.parseInt(model_item.getDiscoutnprice()) > 0) {
            holder.txt_itemPrice.setText("Rs " + String.valueOf(newPrice));
            //discounted price
            holder.txt_deiscountPrice.setText(" Rs " + price);
            holder.txt_deiscountPrice.setVisibility(View.VISIBLE);
            holder.txt_deiscountPrice.setPaintFlags(holder.txt_deiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            holder.txt_off.setText(popularitem_modelclass.getDiscount()+"% OFF");
        } else holder.txt_itemPrice.setText("Rs " + String.valueOf(price));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityItem.class);
                intent.putExtra("name", model_item.getItemname());
                intent.putExtra("price", model_item.getItemprice());
                intent.putExtra("img", model_item.getImg());
                intent.putExtra("detailsitemid", model_item.getSuccatid());
                intent.putExtra("discount", model_item.getDiscoutnprice());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private String getpercentage(String percen, String price) {

        int origianlPrice = Integer.parseInt(price);
        int tempvalue = (int) Double.parseDouble(String.valueOf(origianlPrice * (Integer.parseInt(percen) / 100.0f)));

        return String.valueOf(tempvalue);
    }

    @Override
    public int getItemCount() {
        return modelItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView txt_itemName, txt_itemPrice, txt_deiscountPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_item = itemView.findViewById(R.id.img_item);
            txt_itemName = itemView.findViewById(R.id.txt_itemName);
            txt_itemPrice = itemView.findViewById(R.id.txt_itemPrice);
            txt_deiscountPrice = itemView.findViewById(R.id.txt_deiscountPrice);

        }
    }
}
