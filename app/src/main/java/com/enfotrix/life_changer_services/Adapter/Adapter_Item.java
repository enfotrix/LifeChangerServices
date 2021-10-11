package com.enfotrix.life_changer_services.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.life_changer_services.Model.Model_Item;
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

        holder.img_item.setImageResource(model_item.getImg());
        holder.txt_itemName.setText(model_item.getItemname());
        holder.txt_itemPrice.setText(model_item.getItemprice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context.startActivity(new Intent(context, ActivityItem.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView txt_itemName, txt_itemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_item = itemView.findViewById(R.id.img_item);
            txt_itemName = itemView.findViewById(R.id.txt_itemName);
            txt_itemPrice = itemView.findViewById(R.id.txt_itemPrice);

        }
    }
}
