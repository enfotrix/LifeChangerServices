package com.enfotrix.life_changer_services.OnlineEarning;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.enfotrix.life_changer_services.Activities.MainActivity;
import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;

public class Activity_Scratch extends AppCompatActivity implements ScratchListener, RewardedVideoAdListener {

    ScratchCardLayout scratchCardLayout;
    private String random_points;
    TextView points_text;
    boolean first_time = true;
    RewardedVideoAd rewardedVideoAd;
    TextView txt_coins;
    private TextView txt_youtubeCoins, txt_earnCoins, txt_wallet;
    private FirebaseFirestore firestore;
    private Utils utils;
    private String u_coin, e_coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);

        iniviews();
        firestore = FirebaseFirestore.getInstance();
        utils = new Utils(this);

        getuserbalance();

        MobileAds.initialize(this, getString(R.string.APP_ID));
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);

        loadads();
    }

    private void getuserbalance() {

        firestore.collection("Customer").document(utils.getToken())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        e_coin = document.getString("ecoin");
                        u_coin = document.getString("ucoin");
                        String balance = document.getString("user_balance");

                        txt_wallet.setText(balance);
                        txt_earnCoins.setText(e_coin);
                        txt_youtubeCoins.setText(u_coin);
                    }
                });

    }

    private void loadads() {

        rewardedVideoAd.loadAd(getString(R.string.ADMOB_UNIT_ID),
                new AdRequest.Builder().build());
    }


    private void iniviews() {

        scratchCardLayout = findViewById(R.id.scratch_view_layout);
        points_text = findViewById(R.id.textView_points_show);

        txt_wallet = findViewById(R.id.txt_wallet);
        txt_youtubeCoins = findViewById(R.id.txt_youtubeCoins);
        txt_earnCoins = findViewById(R.id.txt_earnCoins);

        scratchCardLayout.setScratchListener(this);

    }

    @Override
    public void onScratchComplete() {
//        Toast.makeText(this, "you have Won !5 coin", Toast.LENGTH_SHORT).show();
        if (first_time) {
            first_time = false;
            Log.e("onScratch", "Complete");
            Log.e("onScratch", "Complete" + random_points);
//            int counter = Integer.parseInt(scratch_count_textView.getText().toString());
//            if (counter == 0) {
//                showDialogPoints(0, "0", counter);
//            } else {
//                showDialogPoints(1, points_text.getText().toString(), counter);
//            }
            showDialogPionts(points_text.getText().toString());
        }
    }

    @Override
    public void onScratchProgress(ScratchCardLayout scratchCardLayout, int i) {

    }

    @Override
    public void onScratchStarted() {
        random_points = "";
        Random random = new Random();
        random_points = String.valueOf(random.nextInt(15));

    }

    private void showDialogPionts(String points) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);
        points_text.setText(points + "");

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_time = true;
                scratchCardLayout.resetScratch();


                if (rewardedVideoAd.isLoaded()) {
                    rewardedVideoAd.show();
                }

                dialog.dismiss();

            }
        });

        dialog.show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadads();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        int temCoin = Integer.parseInt(e_coin);
        temCoin = temCoin + 1;
        e_coin = String.valueOf(temCoin);
        points_text.setText(e_coin);
        saveCoinToDB(e_coin);


    }

    private void saveCoinToDB(String e_coin) {

        Map<String, Object> map = new HashMap<>();

        map.put("ecoin", String.valueOf(e_coin));

        firestore.collection("Customer").document(utils.getToken())
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            txt_earnCoins.setText(e_coin + "");

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }

                    }
                });

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    public void onResume() {
        rewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        rewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}