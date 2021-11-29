package com.enfotrix.life_changer_services.OnlineStore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.life_changer_services.Adapter.Adapter_Item;
import com.enfotrix.life_changer_services.Adapter.Adapter_ProductCategory;
import com.enfotrix.life_changer_services.Lottiedialog;
import com.enfotrix.life_changer_services.Model.Model_Item;
import com.enfotrix.life_changer_services.Model.Model_ProductCategory;
import com.enfotrix.life_changer_services.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ActivityProduct extends AppCompatActivity implements Adapter_ProductCategory.OnItemClickListener {

    private RecyclerView recyclerView_productCategory, recyclerView_item;
    private Adapter_ProductCategory adapterProductCategory;
    private ArrayList<Model_ProductCategory> productCategoryArrayList = new ArrayList<>();
    private Adapter_Item adapterItem;
    private ArrayList<Model_Item> modelItemArrayList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private String idd;
    private int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView_productCategory = findViewById(R.id.recyclerView_productCategory);
        recyclerView_productCategory.setHasFixedSize(true);
        recyclerView_productCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerView_item = findViewById(R.id.recyclerView_item);
        recyclerView_item.setLayoutManager(new GridLayoutManager(ActivityProduct.this, 3));

        firestore = FirebaseFirestore.getInstance();

//        setItem();
        fetchproduct();

    }

    private void fetchproduct() {

        counter = 1;
        final Lottiedialog lottiedialog = new Lottiedialog(this);
        lottiedialog.show();

        firestore.collection("Product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Model_ProductCategory item = new Model_ProductCategory();
                                item.setProductName(document.getString("name"));
                                item.setProductimg(document.getString("path"));
                                item.setId(document.getId());
                                productCategoryArrayList.add(item);

                                if (counter == 1) {
                                    idd = document.getId();

                                    counter++;
                                }


//                                Toast.makeText(getApplicationContext(), "" + document.getString("name"), Toast.LENGTH_SHORT).show();
                            }

                            fetchsuccat(idd);

                            adapterProductCategory = new Adapter_ProductCategory(getApplicationContext(), productCategoryArrayList);
                            recyclerView_productCategory.setAdapter(adapterProductCategory);
                            adapterProductCategory.setOnItemClickListener(ActivityProduct.this);
                            adapterProductCategory.notifyDataSetChanged();

                            lottiedialog.dismiss();

                        }
                    }
                });
    }

    private void setItem() {


    }

    @Override
    public void onItemClick(int position) {
        final Model_ProductCategory model_productCategory = productCategoryArrayList.get(position);

//        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();

        fetchsuccat(model_productCategory.getId());
    }

    private void fetchsuccat(String id) {

        final Lottiedialog lottiedialog = new Lottiedialog(this);
        lottiedialog.show();

        modelItemArrayList.clear();

        firestore.collection("Product").document(id)
                .collection("Category").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Model_Item item1 = new Model_Item();
                                item1.setItemname(document.getString("name"));
                                item1.setItemprice(document.getString("price"));
                                item1.setImg(document.getString("path"));
                                item1.setDiscoutnprice(document.getString("discount"));
                                item1.setSuccatid(document.getId());
                                modelItemArrayList.add(item1);

//                                Toast.makeText(getApplicationContext(), "" + document.getString("name"), Toast.LENGTH_SHORT).show();
                            }

                            adapterItem = new Adapter_Item(getApplicationContext(), modelItemArrayList);
                            recyclerView_item.setAdapter(adapterItem);
                            adapterItem.notifyDataSetChanged();

                            lottiedialog.dismiss();

                        }
                    }
                });

    }
}