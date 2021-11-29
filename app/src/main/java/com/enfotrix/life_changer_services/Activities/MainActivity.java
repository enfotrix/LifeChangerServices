package com.enfotrix.life_changer_services.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.enfotrix.life_changer_services.OnlineStore.Fragment_Cart;
import com.enfotrix.life_changer_services.OnlineEarning.FragmentEarning;
import com.enfotrix.life_changer_services.OnlineStore.Fragment_Store;
import com.enfotrix.life_changer_services.R;
import com.enfotrix.life_changer_services.SellProduct.FragmentSell_Product;
import com.enfotrix.life_changer_services.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottom_navigation;
    private FirebaseFirestore firestore;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.Frame_layout, new Fragment_Store());
        tx.commit();
        setContentView(R.layout.activity_main);

        bottom_navigation = findViewById(R.id.bottom_navigation);
        firestore = FirebaseFirestore.getInstance();
        utils = new Utils(this);


        bottom_navigation.setOnNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_store:
                //itemView.removeView(view);
                getSupportFragmentManager().beginTransaction().replace(R.id.Frame_layout, new Fragment_Store()).addToBackStack(null).commit();
                break;
            case R.id.menu_earn:
                //itemView.removeView(view);
                getFormStatus();
                getSupportFragmentManager().beginTransaction().replace(R.id.Frame_layout, new FragmentEarning()).addToBackStack(null).commit();

                break;
            case R.id.menu_sellitem:

                getSupportFragmentManager().beginTransaction().replace(R.id.Frame_layout, new FragmentSell_Product()).addToBackStack(null).commit();

                break;
            case R.id.menu_service:
                //itemView.removeView(view);
                // getSupportFragmentManager().beginTransaction().replace(R.id.Frame_layout, new Fragment_Services()).addToBackStack(null).commit();
                Toast.makeText(getApplicationContext(), "Available Soon...", Toast.LENGTH_SHORT).show();

                break;
            case R.id.menu_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.Frame_layout, new Fragment_Cart()).commit();
                break;
            default:
                break;
        }
        return true;
    }

    private void getFormStatus() {

        firestore.collection("Customer").document(utils.getToken())
                .collection("Transactions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {

                                utils.putIsBuy(false);

                            } else {

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    if (document.getString("status").equals("request")) {

                                        utils.putIsBuy(false);
                                    }

                                    if (document.getString("status").equals("approve")) {

                                        utils.putIsBuy(true);

                                    }

                                }
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}