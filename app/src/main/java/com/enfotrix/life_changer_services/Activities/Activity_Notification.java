package com.enfotrix.life_changer_services.Activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.enfotrix.life_changer_services.Adapter.Adapter_Notification;
import com.enfotrix.life_changer_services.Model.Model_Notification;
import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Activity_Notification extends AppCompatActivity {

    TextView txt_transtatus;
    private FirebaseFirestore firestore;
    private Utils utils;
    List<Model_Notification> list_Announ = new ArrayList<>();
    RecyclerView recyc_Announ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        iniviews();

        firestore = FirebaseFirestore.getInstance();
        utils = new Utils(this);

        recyc_Announ = findViewById(R.id.list_Announ);
        recyc_Announ.setHasFixedSize(true);
        recyc_Announ.setLayoutManager(new LinearLayoutManager(this));

        fetchNotifi(utils.getToken());

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotifi(utils.getToken());
                pullToRefresh.setRefreshing(false);
            }
        });

        gettranstatus();
    }

    private void fetchNotifi(String token) {

        firestore.collection("Customer").document(token)
                .collection("Notifications").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Model_Notification model_Announ = new Model_Notification();
                                model_Announ.setHeading(document.getString("Heading"));
                                model_Announ.setData(document.getString("Data"));
                                list_Announ.add(model_Announ);

                            }


                            Adapter_Notification adapter_Announ = new Adapter_Notification(getApplicationContext(), list_Announ);
                            recyc_Announ.setAdapter(adapter_Announ);


                        } else {

                            Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void gettranstatus() {

        firestore.collection("Customer").document(utils.getToken())
                .collection("Transactions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (document.getString("status").equals("request")) {

                                    txt_transtatus.setText("Your transaction ID and Amount submitted to admin.Please wait for Approvel!");
                                }

                                if (document.getString("status").equals("approve")) {

                                    txt_transtatus.setText("Your transaction approved by admin.Thank you for buying membership!");

                                }

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void iniviews() {

        txt_transtatus = findViewById(R.id.txt_transtatus);

    }
}