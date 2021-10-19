package com.enfotrix.life_changer_services.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;

import java.security.SecureRandom;
import java.util.Random;

public class ActivitySignup extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout edit_Full_Name, edit_Layphone, edit_yourreferral,
            edit_Email, edit_address, edit_password, edit_Confrim_Password;
    private TextInputEditText edt_phone;
    private AppCompatButton btn_signup;
    CountryCodePicker mCountryCodePicker;
    private String completenumber;
    private TextView text_Login;
    private CheckBox checkBox;
    Utils utils;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        iniviews();

        utils = new Utils(this);
        firestore = FirebaseFirestore.getInstance();

        checkBox.setChecked(false);
        //check current state of a check box (true or false)

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    edit_yourreferral.setVisibility(View.VISIBLE);

                } else {

                }
            }
        });

        checkvalidation();
    }

    private void checkvalidation() {

        edit_Full_Name.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edit_Full_Name.setError(null);
            }
        });
        edit_yourreferral.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edit_yourreferral.setError(null);
            }
        });

        edit_Layphone.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edit_Layphone.setError(null);
            }
        });
        edit_Email.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edit_Email.setError(null);
            }
        });

        edit_password.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edit_password.setError(null);
            }
        });
        edit_Confrim_Password.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edit_Confrim_Password.setError(null);
            }
        });


    }

    private void iniviews() {

        edit_Full_Name = findViewById(R.id.edit_Full_Name);
        edit_Layphone = findViewById(R.id.edit_Layphone);
        edit_Email = findViewById(R.id.edit_Email);
        edit_address = findViewById(R.id.edit_address);
        edit_password = findViewById(R.id.edit_password);
        edit_Confrim_Password = findViewById(R.id.edit_Confrim_Password);
        btn_signup = findViewById(R.id.btn_signup);
        edt_phone = findViewById(R.id.edt_phone);
        mCountryCodePicker = findViewById(R.id.ccp);
        text_Login = findViewById(R.id.text_Login);
        edit_yourreferral = findViewById(R.id.edit_yourreferral);
        checkBox = findViewById(R.id.checkBox);


        btn_signup.setOnClickListener(this);
        text_Login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                if (checkEmpty()) {
                    mCountryCodePicker.registerCarrierNumberEditText(edt_phone);
                    completenumber = mCountryCodePicker.getFullNumberWithPlus().replace("", "").trim();
                    // Toast.makeText(this, ""+completenumber, Toast.LENGTH_SHORT).show();

                    movetoOtpScreen();
                }
                break;
            case R.id.text_Login:
                startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                finish();
                break;
        }
    }

    public String createRandomCode(int codeLength) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }

    private void movetoOtpScreen() {

        utils.startLoading();

        String referralcode = createRandomCode(6);
        String user_name = edit_Full_Name.getEditText().getText().toString().trim();
        String user_email = edit_Email.getEditText().getText().toString().trim();
        String user_password = edit_password.getEditText().getText().toString().trim();
        String user_referral = edit_yourreferral.getEditText().getText().toString().trim();

//        Intent otpscreen = new Intent(getApplicationContext(), Activity_AccountInformation.class);
//        otpscreen.putExtra("user_name", user_name);
//        otpscreen.putExtra("user_email", user_email);
//        otpscreen.putExtra("user_password", user_password);
//        otpscreen.putExtra("user_number", completenumber);
//        otpscreen.putExtra("referralcode", referralcode);
//        otpscreen.putExtra("user_referral", user_referral);
//        startActivity(otpscreen);



        firestore.collection("Customer").whereEqualTo("autoReferral", user_referral).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {

                                Intent otpscreen = new Intent(getApplicationContext(), Activity_AccountInformation.class);
                                otpscreen.putExtra("user_name", user_name);
                                otpscreen.putExtra("user_email", user_email);
                                otpscreen.putExtra("user_password", user_password);
                                otpscreen.putExtra("user_number", completenumber);
                                otpscreen.putExtra("referralcode", referralcode);
                                otpscreen.putExtra("user_referral", user_referral);
                                startActivity(otpscreen);

                                utils.endLoading();

                            } else {

                                Toast.makeText(getApplicationContext(), "Incorrect Referral", Toast.LENGTH_SHORT).show();
                                utils.endLoading();
                            }

//                                 documentReference.set(wrongreferral).addOnSuccessListener(new OnSuccessListener<Void>() {
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
//        finish();

    }

    private Boolean checkEmpty() {
        String patter = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                // "(?=.*[a-zA-Z])" +      //any letter
                // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                //"(?=\\S+$)" +           //no white spaces
                ".{2,}" +               //at least 4 characters
                "$";
        Boolean isEmpty = false;
        if (edit_Full_Name.getEditText().getText().toString().isEmpty())
            edit_Full_Name.setError("Please Enter Name");
        else if (edit_Layphone.getEditText().getText().toString().isEmpty())
            edit_Layphone.setError("Please Enter Phone Number");
        else if (edit_Email.getEditText().getText().toString().isEmpty())
            edit_Email.setError("Please Enter Email");
        else if (edit_password.getEditText().getText().toString().isEmpty())
            edit_password.setError("Please Enter Password");
        else if (edit_Confrim_Password.getEditText().getText().toString().isEmpty())
            edit_Confrim_Password.setError("Please Enter Re Password");
        else if (!edit_password.getEditText().getText().toString().equals(edit_Confrim_Password.getEditText().getText().toString()))
            edit_Confrim_Password.setError("Passwords Don't Match");
        else isEmpty = true;
        return isEmpty;
    }

}