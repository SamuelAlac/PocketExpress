package com.alac.pocketexpress;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alac.pocketexpress.entity.CartItem;
import com.alac.pocketexpress.helper.DatabaseHelper;
import com.alac.pocketexpress.helper.OrderedItemHistoryAdapter;

import java.util.ArrayList;

public class OrderedItemHistory extends AppCompatActivity {

    ImageView imgBackToHistory;
    RecyclerView rvDisplayOrderedItem;
    DatabaseHelper db;
    String orderID;
    ArrayList<CartItem> orderedItemList = new ArrayList<>();
    OrderedItemHistoryAdapter orderedItemHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ordered_item_history);
        db = new DatabaseHelper(OrderedItemHistory.this);

        displayOrderedItems();
        imgBackToHistory = findViewById(R.id.imgBackToHistory);
        imgBackToHistory.setOnClickListener(v ->{
            Intent back = new Intent(OrderedItemHistory.this, OrderHistory.class);
            startActivity(back);
            finish();
        });

        rvDisplayOrderedItem = findViewById(R.id.rvDisplayOrderedItem);
        orderedItemHistoryAdapter = new OrderedItemHistoryAdapter(OrderedItemHistory.this, orderedItemList);
        rvDisplayOrderedItem.setAdapter(orderedItemHistoryAdapter);
        rvDisplayOrderedItem.setLayoutManager(new LinearLayoutManager(OrderedItemHistory.this));



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayOrderedItems();
    }

    public void displayOrderedItems(){
        orderedItemList.clear();
        Intent orderedItem = getIntent();
        orderID = orderedItem.getStringExtra("orderID");
        Cursor cursor = db.readOrderedItems(orderID);
        if(cursor.getCount()==0){
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                CartItem cartItem = new CartItem(
                        cursor.getBlob(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getDouble(3));

                orderedItemList.add(cartItem);
            }
        }
    }
}