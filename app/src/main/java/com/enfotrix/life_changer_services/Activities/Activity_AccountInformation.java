package com.enfotrix.life_changer_services.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Activity_AccountInformation extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        EditText ac_name = findViewById(R.id.edit_acName);
        AutoCompleteTextView ac_bank = findViewById(R.id.edit_acBank);//
        EditText ac_number = findViewById(R.id.edit_acNumber);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        utils = new Utils(this);

        String[] weatherarray = {"Easy-paisa", "Jazz-cash", "Habib Bank Limited", "National Bank of Pakistan",
                "MCB Bank", "United Bank Limited UBL", "Allied Bank Limited", "Meezan Bank", "Bank Of Punjab", "Bank Al Habib Ltd", "Bank Alfalah"};
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_list, weatherarray);
        ac_bank.setAdapter(arrayAdapter1);

        Button btn_next = findViewById(R.id.btn_next_signUp);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsEmptyfields(ac_name, ac_bank, ac_number)) {

                    storedataonFirestore(getIntent().getStringExtra("user_name"), getIntent().getStringExtra("user_number"),
                            getIntent().getStringExtra("user_email"), getIntent().getStringExtra("user_password"),
                            getIntent().getStringExtra("user_referral"), getIntent().getStringExtra("referralcode"),
                            ac_name.getText().toString(), ac_bank.getText().toString(), ac_number.getText().toString());
                }

            }
        });

    }

    private void storedataonFirestore(String uName, String uNMBR, String uEmail, String uPassword
            , String user_referral, String referralcode, String ac_name, String ac_bank, String ac_number) {

        utils.startLoading();
        DocumentReference documentReference = firestore.collection("Customer").document();

        Map<String, Object> map = new HashMap<>();
        map.put("name", uName);
        map.put("number", uNMBR);
        map.put("email", uEmail);
        map.put("password", uPassword);
        map.put("userReferral", user_referral);
        map.put("picture", "");
        map.put("membership", "pending");
        map.put("autoReferral", referralcode);


        map.put("ac_title", ac_name);
        map.put("ac_bank", ac_bank);
        map.put("ac_number", ac_number);
        map.put("user_balance", "0");
        map.put("ecoin", "0");
        map.put("ucoin", "0");
        map.put("videoStatus", "unwatch");

        Map<String, Object> wrongreferral = new HashMap<>();
        wrongreferral.put("userReferral", "null");
        wrongreferral.put("name", uName);
        wrongreferral.put("number", uNMBR);
        wrongreferral.put("email", uEmail);
        wrongreferral.put("address", "");
        wrongreferral.put("picture", "");
        wrongreferral.put("membership", "pending");
        wrongreferral.put("password", uPassword);
        wrongreferral.put("autoReferral", referralcode);

        wrongreferral.put("ac_title", ac_name);
        wrongreferral.put("ac_bank", ac_bank);
        wrongreferral.put("ac_number", ac_number);
        wrongreferral.put("user_balance", "0");
        wrongreferral.put("ecoin", "0");
        wrongreferral.put("ucoin", "0");
        wrongreferral.put("videoStatus", "unwatch");

        firestore.collection("Customer").whereEqualTo("autoReferral", user_referral).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {

                                documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                                        Toast.makeText(getApplicationContext(), "Congratulation your account created successFully!", Toast.LENGTH_SHORT).show();

                                        utils.endLoading();
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                utils.endLoading();

                                            }

                                        });

                            } else

                                utils.endLoading();
                                Toast.makeText(getApplicationContext(), "Incorrect Referral", Toast.LENGTH_SHORT).show();
//                                documentReference.set(wrongreferral).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
//
//                                        utils.endLoading();
//                                    }
//                                })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//
//                                                utils.endLoading();
//                                            }
//                                        });
                            // startActivity(new Intent(getApplicationContext(), ActivitySignup.class));
                            // Toast.makeText(ActivityOtp.this, "Your Referral Code Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public boolean IsEmptyfields(EditText ac_name, AutoCompleteTextView ac_bank, EditText ac_number) {
        boolean empty = true;
        if (ac_name.getText().toString().isEmpty()) ac_name.setError("Enter Account Tittle");
        else if (ac_bank.getText().toString().isEmpty()) ac_bank.setError("Enter Bank Name");
        else if (ac_number.getText().toString().isEmpty())
            ac_number.setError("Enter User Account Number");
        else empty = false;
        return empty;
    }
}