package com.enfotrix.life_changer_services.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.enfotrix.life_changer_services.R;

public class Activity_AccountInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        EditText ac_name = findViewById(R.id.edit_acName);
        EditText ac_bank = findViewById(R.id.edit_acBank);
        EditText ac_number = findViewById(R.id.edit_acNumber);


        Button btn_next = findViewById(R.id.btn_next_signUp);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsEmptyfields(ac_name, ac_bank, ac_number)) {
                    Intent intent = new Intent(getApplicationContext(), ActivityOtp.class);

                    intent.putExtra("ac_name", ac_name.getText().toString());
                    intent.putExtra("ac_bank", ac_bank.getText().toString());
                    intent.putExtra("ac_number", ac_number.getText().toString());

                    intent.putExtra("user_name", getIntent().getStringExtra("user_name"));
                    intent.putExtra("user_email", getIntent().getStringExtra("user_email"));
                    intent.putExtra("user_password", getIntent().getStringExtra("user_password"));
                    intent.putExtra("user_number", getIntent().getStringExtra("user_number"));
                    intent.putExtra("referralcode", getIntent().getStringExtra("referralcode"));
                    intent.putExtra("user_referral", getIntent().getStringExtra("user_referral"));
                    startActivity(intent);
                }

            }
        });

    }

    public boolean IsEmptyfields(EditText ac_name, EditText ac_bank, EditText ac_number) {
        boolean empty = true;
        if (ac_name.getText().toString().isEmpty()) ac_name.setError("Enter Account Tittle");
        else if (ac_bank.getText().toString().isEmpty()) ac_bank.setError("Enter Bank Name");
        else if (ac_number.getText().toString().isEmpty())
            ac_number.setError("Enter User Account Number");
        else empty = false;
        return empty;
    }
}