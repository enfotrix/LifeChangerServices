package com.enfotrix.life_changer_services.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class Activity_Profile extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton btn_buy;
    private FirebaseFirestore firestore;
    private Utils utils;
    private LinearLayout lay_referral;
    private TextView txt_referral, txtname, txt_cnic, txt_number, txt_email, txt_address;
    private ImageView img_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iniviews();

        firestore = FirebaseFirestore.getInstance();
        utils = new Utils(this);

        getdata();
    }

    private void getdata() {

        firestore.collection("Customer").document(utils.getToken()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        String name = document.getString("name");
                        String cnic = document.getString("cnic");
                        String number = document.getString("number");
                        String email = document.getString("email");
                        String address = document.getString("address");
                        String referral = document.getString("autoReferral");

                        txtname.setText(name);
                        txt_cnic.setText(cnic);
                        txt_number.setText(number);
                        txt_email.setText(email);
                        txt_address.setText(address);
                        txt_referral.setText(referral);
                    }
                });
    }

    private void iniviews() {

        btn_buy = findViewById(R.id.btn_buy);
        lay_referral = findViewById(R.id.lay_referral);
        txt_referral = findViewById(R.id.txt_referral);
        txtname = findViewById(R.id.txtname);
        txt_cnic = findViewById(R.id.txt_cnic);
        txt_number = findViewById(R.id.txt_number);
        txt_email = findViewById(R.id.txt_email);
        txt_address = findViewById(R.id.txt_address);
        img_logout = findViewById(R.id.img_logout);

        Animation animationUtils = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        btn_buy.startAnimation(animationUtils);
        img_logout.startAnimation(animationUtils);

        lay_referral.setOnClickListener(this);
        btn_buy.setOnClickListener(this);
        img_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_referral:
                setClipboard(Activity_Profile.this, txt_referral.getText().toString());//txt_refCode.getText().toString());

                break;
            case R.id.btn_buy:
                startActivity(new Intent(getApplicationContext(), Activity_Transaction.class));
                break;
            case R.id.img_logout:

                logout();

                break;
        }
    }

    public void logout() {
        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure want to logout!")
                .setCancelable(false)
                .setPositiveButton("Logout", R.drawable.ic_baseline_logout_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        utils.logout();
                        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                        finish();
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_baseline_cancel_presentation_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);

        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "" + clipboard.toString(), Toast.LENGTH_SHORT).show();

        }
        Toast.makeText(context, "Referral Code " + text + " Copied!", Toast.LENGTH_SHORT).show();
    }
}