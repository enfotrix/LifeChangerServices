package com.enfotrix.life_changer_services.OnlineStore;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.life_changer_services.Adapter.Adapter_Item;
import com.enfotrix.life_changer_services.Adapter.Adapter_ProductCategory;
import com.enfotrix.life_changer_services.Model.Model_Item;
import com.enfotrix.life_changer_services.Model.Model_ProductCategory;
import com.enfotrix.life_changer_services.R;

import java.util.ArrayList;

public class ActivityProduct extends AppCompatActivity implements Adapter_ProductCategory.OnItemClickListener {

    private RecyclerView recyclerView_productCategory, recyclerView_item;
    private Adapter_ProductCategory adapterProductCategory;
    private ArrayList<Model_ProductCategory> productCategoryArrayList = new ArrayList<>();
    private Adapter_Item adapterItem;
    private ArrayList<Model_Item> modelItemArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView_productCategory = findViewById(R.id.recyclerView_productCategory);
        recyclerView_productCategory.setHasFixedSize(true);
        recyclerView_productCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerView_item = findViewById(R.id.recyclerView_item);
        recyclerView_item.setLayoutManager(new GridLayoutManager(ActivityProduct.this, 3));


        setProductCategory();
        setItem();

    }

    private void setItem() {

        Model_Item item1 = new Model_Item();
        item1.setItemname("lays");
        item1.setItemprice("50");
        item1.setImg(R.drawable.buy);
        modelItemArrayList.add(item1);

        Model_Item item2 = new Model_Item();
        item2.setItemname("toothpaste");
        item2.setItemprice("100");
        item2.setImg(R.drawable.sale);
        modelItemArrayList.add(item2);

        Model_Item item3 = new Model_Item();
        item3.setItemname("mango juice");
        item3.setItemprice("25");
        item3.setImg(R.drawable.salesperson);
        modelItemArrayList.add(item3);

        Model_Item item4 = new Model_Item();
        item4.setItemname("oil");
        item4.setItemprice("300");
        item4.setImg(R.drawable.happyaftershop);
        modelItemArrayList.add(item4);

        Model_Item item5 = new Model_Item();
        item5.setItemname("icecream");
        item5.setItemprice("50");
        item5.setImg(R.drawable.cart);
        modelItemArrayList.add(item5);

        adapterItem = new Adapter_Item(getApplicationContext(),modelItemArrayList);
        recyclerView_item.setAdapter(adapterItem);
        adapterItem.notifyDataSetChanged();

    }

    private void setProductCategory() {

        Model_ProductCategory item1 = new Model_ProductCategory();
        item1.setProductName("lays");
        productCategoryArrayList.add(item1);

        Model_ProductCategory item2 = new Model_ProductCategory();
        item2.setProductName("oil");
        productCategoryArrayList.add(item2);

        Model_ProductCategory item3 = new Model_ProductCategory();
        item3.setProductName("cosmatic");
        productCategoryArrayList.add(item3);

        Model_ProductCategory item4 = new Model_ProductCategory();
        item4.setProductName("dry fruit");
        productCategoryArrayList.add(item4);

        Model_ProductCategory item5 = new Model_ProductCategory();
        item5.setProductName("toothpaste");
        productCategoryArrayList.add(item5);

        adapterProductCategory = new Adapter_ProductCategory(getApplicationContext(), productCategoryArrayList);
        recyclerView_productCategory.setAdapter(adapterProductCategory);
        adapterProductCategory.setOnItemClickListener(this);
        adapterProductCategory.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(int position) {
        final Model_ProductCategory model_productCategory = productCategoryArrayList.get(position);

        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
    }
}