package com.enfotrix.life_changer_services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {
    Context context;
    ArrayList<Slider_Model> sliderModelArrayList;

    public SliderAdapter(Context context, ArrayList<Slider_Model> sliderModelArrayList) {
        this.context = context;
        this.sliderModelArrayList = sliderModelArrayList;
    }

    @NonNull
    @Override
    public SliderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.slider_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.ViewHolder holder, int position) {
        final Slider_Model slider_model = sliderModelArrayList.get(position);

        holder.roundedImageView.setImageResource(slider_model.getImages());

    }

    @Override
    public int getItemCount() {
        return sliderModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView roundedImageView,roundedImageView2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            roundedImageView = itemView.findViewById(R.id.roundedImageView);

        }
    }
}
