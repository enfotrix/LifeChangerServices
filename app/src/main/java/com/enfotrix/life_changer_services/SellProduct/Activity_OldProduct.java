package com.enfotrix.life_changer_services.SellProduct;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.enfotrix.life_changer_services.R;

public class Activity_OldProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);


        final TextView textView = findViewById(R.id.txt_changer);
        final int[] array = {R.string.text1, R.string.text2, R.string.text3,
                R.string.text4, R.string.text5, R.string.text6, R.string.text7
                , R.string.text8, R.string.text9};
        textView.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                textView.setText(array[i]);
                i++;
                if (i == 9)
                    i = 0;
                textView.postDelayed(this, 1000);
            }
        });

    }
}