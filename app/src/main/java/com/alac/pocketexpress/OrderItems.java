package com.alac.pocketexpress;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alac.pocketexpress.entity.Item;
import com.alac.pocketexpress.helper.DatabaseHelper;
import com.alac.pocketexpress.helper.SaleAdapter;

import java.util.ArrayList;

public class OrderItems extends AppCompatActivity {

    RecyclerView rvDisplayItems;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHelper db;
    ArrayList<Item> saleList = new ArrayList<>();
    SaleAdapter saleAdapter;
    ImageView imgBackToMain, imgOpenCart, imgEmptySaleContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_items);
        imgEmptySaleContainer = findViewById(R.id.imgEmptySaleContainer);
        imgOpenCart = findViewById(R.id.imgOpenCart);

        imgBackToMain = findViewById(R.id.imgBackToMain);
        imgBackToMain.setOnClickListener(v ->{
            db = new DatabaseHelper(OrderItems.this);
            db.deleteNullOrder();
            Intent main = new Intent(OrderItems.this, MainActivity.class);
            startActivity(main);
            finish();
        });

        rvDisplayItems = findViewById(R.id.rvDisplayItems);
        layoutManager = new GridLayoutManager(this, 2);
        rvDisplayItems.setLayoutManager(layoutManager);
        saleAdapter = new SaleAdapter(OrderItems.this, saleList);
        rvDisplayItems.setAdapter(saleAdapter);
        displaySales();


        imgOpenCart.setOnClickListener(v ->{
            db = new DatabaseHelper(OrderItems.this);
            Cursor cursor = db.readCartItems();
                Intent openCart = new Intent(OrderItems.this, ShoppingCart.class);
                startActivity(openCart);
                finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displaySales();
    }

    public void displaySales(){
        saleList.clear();
        db = new DatabaseHelper(OrderItems.this);
        Cursor cursor = db.readItems();
        if(cursor.getCount()==0){
            imgEmptySaleContainer.setVisibility(View.VISIBLE);
            rvDisplayItems.setVisibility(View.GONE);
            imgOpenCart.setVisibility(View.GONE);
        }else{
            while(cursor.moveToNext()){

                Item item = new Item(cursor.getInt(0), cursor.getBlob(1), cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getDouble(5));

                saleList.add(item);
            }
        }
        saleAdapter.notifyDataSetChanged();
    }
}