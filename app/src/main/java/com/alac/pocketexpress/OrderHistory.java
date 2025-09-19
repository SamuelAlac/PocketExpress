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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alac.pocketexpress.entity.Order;
import com.alac.pocketexpress.helper.DatabaseHelper;
import com.alac.pocketexpress.helper.OrderHistoryAdapter;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {

    ImageView imgBackToMainMenu, imgEmptyOrderHistory;
    DatabaseHelper db;
    ArrayList<Order> orderList = new ArrayList<>();
    RecyclerView rvOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);

        imgBackToMainMenu = findViewById(R.id.imgBackToMainMenu);
        imgEmptyOrderHistory = findViewById(R.id.imgEmptyOrderHistory);
        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        imgBackToMainMenu.setOnClickListener(v ->{
            Intent back = new Intent(OrderHistory.this, MainActivity.class);
            startActivity(back);
            finish();
        });

        OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(OrderHistory.this, orderList);
        rvOrderHistory.setAdapter(orderHistoryAdapter);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistory.this));
        displayOrderHistory();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayOrderHistory();
    }

    public void displayOrderHistory(){
        orderList.clear();
        db = new DatabaseHelper(OrderHistory.this);
        Cursor cursor = db.readOrderHistory();
        if(cursor.getCount()==0){
            imgEmptyOrderHistory.setVisibility(View.VISIBLE);
            rvOrderHistory.setVisibility(View.GONE);
        }else{
            while(cursor.moveToNext()){
                Order order = new Order(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4));

                orderList.add(order);
            }

        }
    }
}