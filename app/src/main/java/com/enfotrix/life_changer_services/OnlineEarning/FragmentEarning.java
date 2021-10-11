package com.enfotrix.life_changer_services.OnlineEarning;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.enfotrix.life_changer_services.Activities.Activity_Profile;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FragmentEarning extends Fragment implements RewardedVideoAdListener {

    private LinearLayout lay_watchVideo, lay_scratch, lay_spin, lay_youtube;
    RewardedVideoAd rewardedVideoAd;
    private TextView txt_wallet, txt_e_coins, txt_u_coins;
    int val = 0;
    Utils utils;
    AppCompatButton btn_withdraw;
    private FirebaseFirestore firestore;
    private String e_coin, ecoinrate, ucoinrate, user_balance, u_coin;
    private LinearLayout lay_ecoin, lay_ucoin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_earning, container, false);

        // ini views
        iniviews(view);
        utils = new Utils(getContext());
        firestore = FirebaseFirestore.getInstance();

        getuserbalance();
        getrates();

        lay_watchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (utils.getISBuy() == false) {
//                    Toast.makeText(getContext(), "Buy form", Toast.LENGTH_SHORT).show();
                    showbuydialog();
                } else {
                    if (rewardedVideoAd.isLoaded()) {
                        rewardedVideoAd.show();
                    }
                }

            }
        });

        lay_scratch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (utils.getISBuy() == false) {
                    showbuydialog();
                } else {
                    startActivity(new Intent(getActivity().getApplicationContext(), Activity_Scratch.class));

                }
            }
        });

        lay_spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utils.getISBuy() == false) {
                    showbuydialog();
                } else {
                    startActivity(new Intent(getActivity().getApplicationContext(), Activity_SpinWin.class));

                }
            }
        });

        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utils.getISBuy() == false) {
                    showbuydialog();
                } else {
                    startActivity(new Intent(getContext(), Activity_WithDraw.class));

                }

            }
        });

        lay_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utils.getISBuy() == false) {
                    showbuydialog();
                } else {
                    startActivity(new Intent(getContext(), Activity_YoutubeVideo.class));

                }
            }
        });

        lay_ecoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utils.getISBuy() == false) {
                    showbuydialog();
                } else {
                    addcointouserBalance("ecoins");
                }
            }
        });

        lay_ucoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utils.getISBuy() == false) {
                    showbuydialog();
                } else {
                    addcointouserBalance("ucoins");
                }
            }
        });

        MobileAds.initialize(getContext(), getString(R.string.APP_ID));
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        rewardedVideoAd.setRewardedVideoAdListener(this);

        loadads();

        return view;

    }

    private void addcointouserBalance(String coins) {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.addcointobalance);

        TextView priceheading = dialog.findViewById(R.id.txt_currentpriceheading);
        TextView currentprice = dialog.findViewById(R.id.txt_currentprice);
        TextView txt_yourcoinheading = dialog.findViewById(R.id.txt_yourcoinheading);
        TextView txt_yourcoin = dialog.findViewById(R.id.txt_yourcoin);
        TextView txt_currentbalance = dialog.findViewById(R.id.txt_currentbalance);
        AppCompatButton btn_addToBalance = dialog.findViewById(R.id.btn_addToBalance);
        TextView txt_newBalance = dialog.findViewById(R.id.txt_newBalance);


        if (coins.equals("ecoins")) {

            txt_currentbalance.setText(user_balance);
            priceheading.setText("Current price of ecoin is: ");
            currentprice.setText(ecoinrate + " Rs.");
            txt_yourcoinheading.setText("your ecoins: ");
            txt_yourcoin.setText(e_coin);

            float userbal = Float.parseFloat(user_balance);
            float currentcoinprice = Float.parseFloat(ecoinrate);
            int currentcoins = Integer.parseInt(e_coin);

            float tempbal = userbal + currentcoins * currentcoinprice;

            String newbalance = String.format("%.2f", tempbal);

            addnewbaltoDB(newbalance, "ecoins");

            txt_newBalance.setText("your new balance is: " + newbalance + " Rs.");

//            Toast.makeText(getContext(), "" + newbalance, Toast.LENGTH_SHORT).show();


        }
        if (coins.equals("ucoins")) {

            txt_currentbalance.setText(user_balance + " Rs.");
            priceheading.setText("Current price of ucoin is: ");
            currentprice.setText(ucoinrate + " Rs.");
            txt_yourcoinheading.setText("your ucoins: ");
            txt_yourcoin.setText(u_coin);

            float userbal = Float.parseFloat(user_balance);
            float currentcoinprice = Float.parseFloat(ucoinrate);
            int currentcoins = Integer.parseInt(u_coin);

            float tempbal = userbal + currentcoins * currentcoinprice;

            String newbalance = String.format("%,2f", tempbal);

            addnewbaltoDB(newbalance, "ucoins");

            txt_newBalance.setText("your new balance is: " + newbalance + " Rs.");

//            Toast.makeText(getContext(), "" + newbalance, Toast.LENGTH_SHORT).show();


        }

        dialog.show();
        dialog.setCancelable(false);

        btn_addToBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), MainActivity.class));

                dialog.dismiss();


            }
        });


    }

    private void addnewbaltoDB(String newbalance, String coins) {

        Map<String, Object> map = new HashMap<>();

        map.put("user_balance", newbalance);

        firestore.collection("Customer").document(utils.getToken())
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            if (coins.equals("ecoins")) {

                                Map<String, Object> map = new HashMap<>();
                                map.put("ecoin", "0");

                                firestore.collection("Customer").document(utils.getToken())
                                        .update(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });

                            }

                            if (coins.equals("ucoins")) {

                                Map<String, Object> map = new HashMap<>();
                                map.put("ucoin", "0");

                                firestore.collection("Customer").document(utils.getToken())
                                        .update(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });

                            }


                        }

                    }
                });

    }

    private void getrates() {

        firestore.collection("Rates").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Toast.makeText(ActivityGallery.this, "" + document.getString("path"), Toast.LENGTH_SHORT).show();

                                ecoinrate = document.getString("ecoin");
                                ucoinrate = document.getString("ucoin");

                            }

                        }

                    }
                });


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
                        user_balance = document.getString("user_balance");

                        float bal = Float.parseFloat(user_balance);
                        txt_wallet.setText(bal + "");
                        txt_e_coins.setText(e_coin);
                        txt_u_coins.setText(u_coin);
                    }
                });

    }

    private void showbuydialog() {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.buydilog);

        AppCompatButton btn_buyForum = dialog.findViewById(R.id.btn_buyForum);

        dialog.show();

        btn_buyForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Activity_Profile.class));
            }
        });
    }

    private void loadads() {

        rewardedVideoAd.loadAd(getString(R.string.ADMOB_UNIT_ID),
                new AdRequest.Builder().build());
    }

    private void iniviews(View view) {

        lay_watchVideo = view.findViewById(R.id.lay_watchVideo);
        lay_scratch = view.findViewById(R.id.lay_scratch);
        lay_spin = view.findViewById(R.id.lay_spin);
        lay_youtube = view.findViewById(R.id.lay_youtube);

        txt_e_coins = view.findViewById(R.id.txt_e_coins);
        txt_wallet = view.findViewById(R.id.txt_wallet);
        txt_u_coins = view.findViewById(R.id.txt_y_coins);

        lay_ucoin = view.findViewById(R.id.lay_ucoin);
        lay_ecoin = view.findViewById(R.id.lay_ecoin);

        btn_withdraw = view.findViewById(R.id.btn_withdraw);

        Animation animationUtils = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        btn_withdraw.startAnimation(animationUtils);

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
        txt_e_coins.setText(e_coin);
        saveCoinToDB(e_coin);

    }

    private void saveCoinToDB(String val) {

        Map<String, Object> map = new HashMap<>();

        map.put("ecoin", String.valueOf(val));

        firestore.collection("Customer").document(utils.getToken())
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

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
        rewardedVideoAd.resume(getContext());
        super.onResume();
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(getContext());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        rewardedVideoAd.destroy(getContext());
        super.onDestroy();
    }


}