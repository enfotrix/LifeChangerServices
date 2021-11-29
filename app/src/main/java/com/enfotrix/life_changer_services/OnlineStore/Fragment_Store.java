package com.enfotrix.life_changer_services.OnlineStore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.enfotrix.life_changer_services.Activities.Activity_Notification;
import com.enfotrix.life_changer_services.Activities.Activity_Profile;
import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.SellProduct.Activity_OldProduct;
import com.enfotrix.life_changer_services.SliderAdapter;
import com.enfotrix.life_changer_services.Slider_Model;

import java.util.ArrayList;

public class Fragment_Store extends Fragment implements View.OnClickListener {

    private LinearLayout lay_store, lay_buyer;
    private ViewPager2 viewPagerSlider;
    private SliderAdapter sliderAdapter;
    private ArrayList<Slider_Model> sliderModelArrayList = new ArrayList<>();
    private LinearLayout layDots;
    private ImageView img_histroy, img_profile, img_store,img_oldproduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__store, container, false);

        // ini all views
        iniviews(view);

        imgslider();

        activeindicator(0);

        return view;
    }

    private void imgslider() {

        Slider_Model item1 = new Slider_Model();
        item1.setImages(R.drawable.buy);
        sliderModelArrayList.add(item1);

        Slider_Model item2 = new Slider_Model();
        item2.setImages(R.drawable.cart);
        sliderModelArrayList.add(item2);

        Slider_Model item3 = new Slider_Model();
        item3.setImages(R.drawable.happyaftershop);
        sliderModelArrayList.add(item3);

        Slider_Model item4 = new Slider_Model();
        item4.setImages(R.drawable.sale);
        sliderModelArrayList.add(item4);

        Slider_Model item5 = new Slider_Model();
        item5.setImages(R.drawable.salesperson);
        sliderModelArrayList.add(item5);

        viewPagerSlider.setClipToPadding(false);
        viewPagerSlider.setClipChildren(false);
        viewPagerSlider.setOffscreenPageLimit(3);
        viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        viewPagerSlider.setPageTransformer(compositePageTransformer);
        sliderAdapter = new SliderAdapter(getContext(), sliderModelArrayList);
        viewPagerSlider.setAdapter(sliderAdapter);

        setindicator();

        viewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                activeindicator(position);
            }
        });

    }

    private void iniviews(View view) {

        lay_store = view.findViewById(R.id.lay_store);
        viewPagerSlider = view.findViewById(R.id.viewPagerSlider);
        layDots = view.findViewById(R.id.layDots);
        img_profile = view.findViewById(R.id.img_profile);
        img_histroy = view.findViewById(R.id.img_histroy);
        img_store = view.findViewById(R.id.img_store);
        lay_buyer = view.findViewById(R.id.lay_buyer);
        img_oldproduct = view.findViewById(R.id.img_stor);

        Animation animationUtils = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        img_profile.startAnimation(animationUtils);
        img_histroy.startAnimation(animationUtils);
//        img_store.startAnimation(animationUtils);
//        img_oldproduct.startAnimation(animationUtils);

        lay_store.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        img_histroy.setOnClickListener(this);
        lay_buyer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_store:
                startActivity(new Intent(getContext(), ActivityProduct.class));
                break;
            case R.id.img_profile:
                startActivity(new Intent(getContext(), Activity_Profile.class));
                break;
            case R.id.img_histroy:
                startActivity(new Intent(getContext(), Activity_Notification.class));
                break;
            case R.id.lay_buyer:
                startActivity(new Intent(getContext(), Activity_OldProduct.class));
                break;
            default:
                break;
        }
    }

    private void setindicator() {
        ImageView[] incators = new ImageView[sliderAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < incators.length; i++) {
            incators[i] = new ImageView(getContext());
            incators[i].setImageDrawable(ContextCompat.getDrawable(getContext()
                    , R.drawable.inactive_indicator));
            incators[i].setLayoutParams(layoutParams);
            layDots.addView(incators[i]);
        }
    }

    private void activeindicator(int index) {
        int childcount = layDots.getChildCount();
        for (int i = 0; i < childcount; i++) {
            ImageView imageView = (ImageView) layDots.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getContext(), R.drawable.active_indicator
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getContext(), R.drawable.inactive_indicator
                ));
            }
        }
    }
}