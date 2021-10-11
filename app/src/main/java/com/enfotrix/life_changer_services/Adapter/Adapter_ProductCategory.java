package com.enfotrix.life_changer_services.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.life_changer_services.Model.Model_ProductCategory;
import com.enfotrix.life_changer_services.R;

import java.util.ArrayList;

public class Adapter_ProductCategory extends RecyclerView.Adapter<Adapter_ProductCategory.ViewHolder> {

    Context context;
    ArrayList<Model_ProductCategory> productCategoryArrayList;
    private int selectedItem;
    private OnItemClickListener mListener;

    public Adapter_ProductCategory(Context context, ArrayList<Model_ProductCategory> productCategoryArrayList) {

        this.context = context;
        this.productCategoryArrayList = productCategoryArrayList;

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Model_ProductCategory productCategory = productCategoryArrayList.get(position);

        holder.txt_productCat.setText(productCategory.getProductName());

        holder.txt_productCat.setTextColor(context.getResources().getColor(R.color.black_m));

        if (selectedItem == position) {
            holder.txt_productCat.setTextColor(context.getResources().getColor(R.color.teal_700));
//            holder.lay_select.setBackground(drawable);
        }

    }

    @Override
    public int getItemCount() {
        return productCategoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_productCat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_productCat = itemView.findViewById(R.id.txt_productCat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);

                            int previousItem = selectedItem;
                            selectedItem = position;

                            notifyItemChanged(previousItem);
                            notifyItemChanged(position);

                        }
                    }
                }
            });

        }
    }
}
