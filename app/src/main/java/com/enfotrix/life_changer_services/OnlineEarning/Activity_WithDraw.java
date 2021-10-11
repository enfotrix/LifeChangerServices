package com.enfotrix.life_changer_services.OnlineEarning;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.enfotrix.life_changer_services.Activities.MainActivity;
import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Activity_WithDraw extends AppCompatActivity {

    private TextView txt_acNumber, txt_acTitle_w;
    private CheckBox chk_acNumber;
    private EditText ac_amount;
    private AppCompatButton btn_withdraw_w;

    private FirebaseFirestore firestore;
    private Utils utils;
    private String ac_title, ac_bank, user_balance, ecoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);

        firestore = FirebaseFirestore.getInstance();
        utils = new Utils(this);

        ac_amount = findViewById(R.id.edit_ac_amount_w);
        txt_acNumber = findViewById(R.id.txt_acNumber_w);
        chk_acNumber = findViewById(R.id.chk_acNumber);
        btn_withdraw_w = findViewById(R.id.btn_withdraw_w);
        txt_acTitle_w = findViewById(R.id.txt_acTitle_w);

        // get account info
        getdata();


        chk_acNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chk_acNumber.isChecked())
                    acNumberdialog();

            }
        });

        btn_withdraw_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float balance = Float.parseFloat(user_balance);
                float withdrawamount = Float.parseFloat(ac_amount.getText().toString());

                if (ac_amount.getText().toString().isEmpty())
                    ac_amount.setError("Please enter withdraw amount");
                else if (Integer.parseInt(ac_amount.getText().toString()) > balance
                        || Integer.parseInt(ac_amount.getText().toString()) < 1)
                    ac_amount.setError("Insufficient amount");
                else {


                    deposit(utils.getToken(), ac_amount, txt_acNumber.getText().toString(), txt_acTitle_w.getText().toString());

                    float tempbalance = balance - withdrawamount;
                    remainbalance(String.format("%.2f", tempbalance));

                }


            }
        });

    }

    private void remainbalance(String remainbalance) {

        Map<String, Object> map = new HashMap<>();

        map.put("user_balance", remainbalance);

        firestore.collection("Customer").document(utils.getToken())
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    private void deposit(String userID, EditText amount, String ac_number, String ac_title) {


        Map<String, Object> map = new HashMap<>();


        map.put("date", getDateTime());
        map.put("amount", amount.getText().toString());
        map.put("withdraw_account", ac_number);
        map.put("withdraw_account_title", ac_title);
        map.put("status", "pending");
        map.put("userID", userID);

        firestore.collection("Customer")
                .document(userID)
                .collection("Earning")
                .add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

//                        Toast.makeText(Activity_WithDraw.this, "ancksan", Toast.LENGTH_SHORT).show();


                        recipdialog(
                                amount.getText().toString(),
                                ac_number,
                                getDate(),
                                ac_title
                        );

                    }
                });


    }

    private void recipdialog(String depositBalance, String depositAccount, String date, String depositBy) {

        Dialog dialog = new Dialog(Activity_WithDraw.this);
        dialog.setContentView(R.layout.dialog_voucherhu_wr);
        AppCompatButton btn_skip = dialog.findViewById(R.id.btn_skip);
        TextView txt_depositBalance = dialog.findViewById(R.id.txt_depositBalance);
        txt_depositBalance.setText(depositBalance + " Rs");
        TextView txt_depositAccount = dialog.findViewById(R.id.txt_depositAccount);
        txt_depositAccount.setText(depositAccount);
        TextView txt_depositBy = dialog.findViewById(R.id.txt_depositBy);
        txt_depositBy.setText(depositBy);
        TextView txt_date = dialog.findViewById(R.id.txt_date);
        txt_date.setText(date);

        dialog.show();

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                startActivity(new Intent(Activity_WithDraw.this, MainActivity.class));
                finish();
            }
        });

    }

    private void getdata() {

        firestore.collection("Customer").document(utils.getToken()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        ac_title = document.getString("ac_title");
                        ac_bank = document.getString("ac_bank");
                        String ac_number = document.getString("ac_number");
                        user_balance = document.getString("user_balance");
                        ecoin = document.getString("ecoin");

                        txt_acNumber.setText(ac_number);
                        txt_acTitle_w.setText(ac_title);
                    }
                });
    }

    private void acNumberdialog() {

        Dialog dialog = new Dialog(Activity_WithDraw.this);
        dialog.setContentView(R.layout.dialog_another_acount);
        AppCompatButton btn_use = dialog.findViewById(R.id.btn_use);
        EditText txt_depositBalance = dialog.findViewById(R.id.edit_ac_amount);
        EditText edit_ac_title = dialog.findViewById(R.id.edit_ac_title);

        dialog.show();

        btn_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_depositBalance.getText().toString().isEmpty())
                    txt_depositBalance.setError("Please Enter Account Number");
                else if (edit_ac_title.getText().toString().isEmpty()) {
                    edit_ac_title.setError("Please Enter Account Title");
                } else {
                    txt_acNumber.setText(txt_depositBalance.getText().toString());
                    txt_acTitle_w.setText(edit_ac_title.getText().toString());
                    chk_acNumber.setEnabled(false);
                    dialog.cancel();
                }
            }
        });
    }

    private String getDate() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}