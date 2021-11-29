package com.enfotrix.life_changer_services.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Activity_Transaction extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton btn_request;
    private TextInputLayout edit_lay_transaction;
    private FirebaseFirestore firestore;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        iniviews();

        firestore = FirebaseFirestore.getInstance();
        utils = new Utils(this);

    }

    private void iniviews() {

        btn_request = findViewById(R.id.btn_request);
        edit_lay_transaction = findViewById(R.id.edit_lay_transaction);

        Animation animationUtils = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        btn_request.startAnimation(animationUtils);

        btn_request.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request:
                if (checkEmpty()) {
                    addtransaction(edit_lay_transaction.getEditText().getText().toString().trim());
                }
                break;
        }
    }

    private void addtransaction(String transaction) {

        DocumentReference documentReference = firestore.collection("Customer").document(utils.getToken())
                .collection("Transactions").document(utils.getToken());
        Map<String, Object> map = new HashMap<>();
        map.put("tid", transaction);
        map.put("amount", "500");
        map.put("status", "request");
        map.put("userID", utils.getToken());

        documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Activity_Transaction.this, "Request Made", Toast.LENGTH_SHORT).show();
                generatenotification();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Transaction.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void generatenotification() {

        DocumentReference documentReference = firestore.collection("Customer").document(utils.getToken())
                .collection("Notifications").document();
        Map<String, Object> map = new HashMap<>();
        map.put("Data", "if you will buy membership you can earn money from our app and withdraw amount into your account");
        map.put("Heading", "membership");

        documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                Toast.makeText(Activity_Transaction.this, "Request Made", Toast.LENGTH_SHORT).show();
//                generatenotification();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Transaction.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private boolean checkEmpty() {
        Boolean isEmpty = false;
        if (edit_lay_transaction.getEditText().getText().toString().trim().isEmpty())
            edit_lay_transaction.setError("Please Enter Transaction");
        else isEmpty = true;
        return isEmpty;
    }
}