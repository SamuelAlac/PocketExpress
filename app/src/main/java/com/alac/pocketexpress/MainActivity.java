package com.alac.pocketexpress;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alac.pocketexpress.entity.Order;
import com.alac.pocketexpress.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    TextView btnOrderItems, btnManageItems, btnOrderHistory;
    int staffID;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnOrderItems = findViewById(R.id.btnOrderItems);
        btnOrderItems.setOnClickListener(v ->{
            Intent orderItems = new Intent(MainActivity.this, OrderItems.class);
            db = new DatabaseHelper(MainActivity.this);
            staffID = currentStaffLoggedIn();

            Order order = new Order(staffID);
            db.createOrder(order);

            startActivity(orderItems);
            finish();
        });

        btnManageItems = findViewById(R.id.btnManageItems);
        btnManageItems.setOnClickListener(v ->{
            Intent manageItems = new Intent(MainActivity.this, ManageItems.class);
            startActivity(manageItems);
            finish();
        });
        btnOrderHistory = findViewById(R.id.btnOrderHistory);
        btnOrderHistory.setOnClickListener(v ->{
            Intent checkOrderHistory = new Intent(MainActivity.this, OrderHistory.class);
            startActivity(checkOrderHistory);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public int currentStaffLoggedIn(){
        db = new DatabaseHelper(MainActivity.this);
        Cursor cursor = db.readStaffLoggedIn();
        int currentStaffID = -1;

        if(cursor.moveToFirst()) {
            return currentStaffID = cursor.getInt(0);
        }
        return currentStaffID;
    }
}