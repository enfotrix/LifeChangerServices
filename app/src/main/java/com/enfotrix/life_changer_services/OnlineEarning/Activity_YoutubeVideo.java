package com.enfotrix.life_changer_services.OnlineEarning;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import com.enfotrix.life_changer_services.Activities.MainActivity;
import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Activity_YoutubeVideo extends AppCompatActivity {

    YouTubePlayerView youTubePlayerView;
    LifecycleObserver lifecycleOwner;
    YouTubePlayerSeekBar seekBar;
    private SimpleDateFormat timeformat;
    private Calendar calendar;
    private String time;
    private String times;
    private int viewCount = 0;
    TextView txt_youtubeCoins, txt_earnCoins, txt_wallet;
    private FirebaseFirestore firestore;
    private ImageView img_status;
    YouTubePlayerTracker tracker;
    private Utils utils;
    private String u_coin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        txt_youtubeCoins = findViewById(R.id.txt_youtubeCoins);
        txt_earnCoins = findViewById(R.id.txt_earnCoins);
        txt_wallet = findViewById(R.id.txt_wallet);
        img_status = findViewById(R.id.img_status);

        calendar = Calendar.getInstance();
        timeformat = new SimpleDateFormat("HH:mm:ss");
        firestore = FirebaseFirestore.getInstance();
        utils = new Utils(this);

        tracker = new YouTubePlayerTracker();

        getuserbalance();

        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                String videoid = "VTcqPv5KZm0";
                youTubePlayer.loadVideo(videoid, 0);

                youTubePlayer.addListener(tracker);
//                time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
//                Toast.makeText(Activity_YoutubeVideo.this, "" + time, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVideoDuration(YouTubePlayer youTubePlayer, float duration) {
                super.onVideoDuration(youTubePlayer, duration);


            }
        });

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {


                if (second >= 20.50) {

//                    Toast.makeText(Activity_YoutubeVideo.this, "okkk", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onCurrentSecond: " + viewCount + "   " + second);
                }
            }
        });


    }

    private void addcointodatabase() {

        int temCoin = Integer.parseInt(u_coin);
        temCoin = temCoin + 1;
        u_coin = String.valueOf(temCoin);
        txt_youtubeCoins.setText(u_coin);
        saveCoinToDB(u_coin);

    }

    private void saveCoinToDB(String val) {

        Map<String, Object> map = new HashMap<>();

        map.put("ucoin", String.valueOf(val));

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
    public void onBackPressed() {
        super.onBackPressed();

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {

                youTubePlayer.pause();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        youTubePlayerView.release();
    }

    private void getuserbalance() {

        firestore.collection("Customer").document(utils.getToken())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        String e_coin = document.getString("ecoin");
                        u_coin = document.getString("ucoin");
                        String balance = document.getString("user_balance");
                        String videostatus = document.getString("videoStatus");

                        txt_wallet.setText(balance);
                        txt_earnCoins.setText(e_coin);
                        txt_youtubeCoins.setText(u_coin);


                        if (document.getString("videoStatus").equals("unwatch")) {

                            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                @Override
                                public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {

                                    if (state == PlayerConstants.PlayerState.ENDED) {

                                        Log.d("TAG", "onCurrentSecond: " + "akmcaokncanclakncl");

                                        img_status.setImageResource(R.drawable.check);
                                        ////// add coin in counter

                                        addcointodatabase();

                                        Map<String, Object> addwatchstatus = new HashMap<>();

                                        addwatchstatus.put("videoStatus", "watch");

                                        firestore.collection("Customer").document(utils.getToken())
                                                .update(addwatchstatus)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                        finish();

                                                    }
                                                });


                                        Toast.makeText(Activity_YoutubeVideo.this, "enndddd", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                        }

                        if (document.getString("videoStatus").equals("watch")) {

                            img_status.setImageResource(R.drawable.check);
                            Toast.makeText(Activity_YoutubeVideo.this, "You have already earn points from this video", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

}