package com.enfotrix.life_changer_services.OnlineEarning;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import androidx.core.content.res.ResourcesCompat;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Activity_SpinWin extends AppCompatActivity implements RewardedVideoAdListener {

    List<WheelItem> wheelItems = new ArrayList<>();
    LuckyWheel luckyWheel;
    private AppCompatButton play_btn;
    private String points;
    RewardedVideoAd rewardedVideoAd;
    private TextView txt_wallet, txt_youtubeCoins, txt_earnCoins;
    private FirebaseFirestore firestore;
    private Utils utils;
    private String e_coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_win2);

        luckyWheel = findViewById(R.id.lwv);
        play_btn = findViewById(R.id.play);

        txt_wallet = findViewById(R.id.txt_wallet);
        txt_youtubeCoins = findViewById(R.id.txt_youtubeCoins);
        txt_earnCoins = findViewById(R.id.txt_earnCoins);

        firestore = FirebaseFirestore.getInstance();
        utils = new Utils(this);

        getuserbalance();

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.dark_bule, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "1"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.yellow_spin, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "1"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.dark_bule, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "1"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.yellow_spin, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "1"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.dark_bule, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "1"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.yellow_spin, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "1"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.dark_bule, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "1"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.yellow_spin, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "1"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.dark_bule, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "1"));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.yellow_spin, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), "1"));
        luckyWheel.addWheelItems(wheelItems);

        onclick();

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
                        String u_coin = document.getString("ucoin");
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

    private void onclick() {

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                play_btn.setEnabled(false);
                Random random = new Random();
                points = String.valueOf(random.nextInt(10));
                luckyWheel.rotateWheelTo(Integer.parseInt(points));

            }
        });

        luckyWheel.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                try {
                    WheelItem item = wheelItems.get(Integer.parseInt(points) - 1);
                    String points_amount = item.text;
                    Log.e("TAG", "onReachTarget: " + points_amount);

//                    int counter = Integer.parseInt(spin_count_text_view.getText().toString());
//                    if (counter == 0) {
//                        showDialogPoints(0, "0", counter);
//                    } else {
//                        showDialogPoints(1, points_amount, counter);
//                    }
                    // Toast.makeText(Activity_SpinWin.this, ""+points_amount, Toast.LENGTH_SHORT).show();
                    showDialogPionts(points_amount);

                } catch (Exception e) {
                    Log.e("TAG", "onReachTarget: " + e.getMessage().toString());
                }

            }
        });
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
        AppCompatButton can_btn = dialog.findViewById(R.id.cancel_btn);
        points_text.setText(points + "");

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_btn.setEnabled(true);

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
        txt_earnCoins.setText(e_coin);
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