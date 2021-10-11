package com.enfotrix.life_changer_services.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private TextView text_forgetpass, text_Signup;
    AppCompatButton btn_login;
    ImageView img_logo;
    Animation animation;
    private FirebaseFirestore firestore;
    private TextInputLayout edit_Email, editpassword;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ini db
        firestore = FirebaseFirestore.getInstance();
        utils = new Utils(this);

        //iniviews
        iniviews();

    }

    private void iniviews() {

        text_forgetpass = findViewById(R.id.text_forgetpass);
        text_Signup = findViewById(R.id.text_Signup);
        img_logo = findViewById(R.id.img_logo);
        btn_login = findViewById(R.id.btn_login);
        edit_Email = findViewById(R.id.edit_Email);
        editpassword = findViewById(R.id.editpassword);

        animation = AnimationUtils.loadAnimation(this, R.anim.translatetb);
        img_logo.startAnimation(animation);


        text_forgetpass.setOnClickListener(this);
        text_Signup.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_forgetpass:
               // startActivity(new Intent(getApplicationContext(), ActivityForgetPasswordAuth.class));
                break;
            case R.id.text_Signup:
                startActivity(new Intent(getApplicationContext(), ActivitySignup.class));
                break;
            case R.id.btn_login:
                if (checkEmpty()) {
                    auth(edit_Email.getEditText().getText().toString().trim(), editpassword.getEditText().getText().toString().trim());

                }
                break;
        }
    }

    private void auth(String email, String password) {

        firestore.collection("Customer").whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getString("password").equals(password)) {
                                        utils.putToken(document.getId());

                                        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                                        intent.putExtra("documentID", document.getId());
                                        startActivity(intent);
                                        finish();
                                    } else
                                        editpassword.setError("Please Enter Correct Password");

                                }
                            } else
                                edit_Email.setError("Please Enter Correct Email");
                        }
                    }
                });
    }

    private boolean checkEmpty() {
        Boolean isEmpty = false;
        if (edit_Email.getEditText().getText().toString().trim().isEmpty())
            edit_Email.setError("Please Enter Email");
        else if (editpassword.getEditText().getText().toString().trim().isEmpty())
            editpassword.setError("Please Enter Password");
        else isEmpty = true;
        return isEmpty;
    }
}