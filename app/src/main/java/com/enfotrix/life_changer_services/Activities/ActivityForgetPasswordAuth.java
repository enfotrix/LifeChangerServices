package com.enfotrix.life_changer_services.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.enfotrix.life_changer_services.R;

public class ActivityForgetPasswordAuth extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton btn_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_auth);

        //iniviews
        iniviews();


    }

    private void iniviews() {

        btn_verify = findViewById(R.id.btn_verify);

        btn_verify.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify:
                startActivity(new Intent(getApplicationContext(), ActivityResetPassword.class));
                break;

        }
    }
}