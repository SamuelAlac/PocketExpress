package com.alac.pocketexpress;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alac.pocketexpress.entity.CartItem;
import com.alac.pocketexpress.entity.Order;
import com.alac.pocketexpress.helper.CartItemsAdapter;
import com.alac.pocketexpress.helper.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShoppingCart extends AppCompatActivity {

    RecyclerView rvDisplayOrders;

    ImageView imgEmpty, imgDeleteAllCartItem;
    TextView txtTotalPrice, lblTotal, lblChange, lblCash;
    EditText txtCash;
    TextView txtChange;
    double totalPrice, change;
    String cash, orderDateAndTime;
    double money;
    int orderID;
    CartItemsAdapter cartItemsAdapter;

    Button btnOrder;

    ImageView imgbackToStore;
    DatabaseHelper db;
    ArrayList<CartItem> cartItemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);
        imgEmpty = findViewById(R.id.imgEmpty);

        imgbackToStore = findViewById(R.id.imgBackToStore);
        imgbackToStore.setOnClickListener(v ->{
            Intent back = new Intent(ShoppingCart.this, OrderItems.class);
            startActivity(back);
            finish();
        });


        rvDisplayOrders = findViewById(R.id.rvDisplayOrders);
        cartItemsAdapter = new CartItemsAdapter(ShoppingCart.this, cartItemList);
        rvDisplayOrders.setAdapter(cartItemsAdapter);
        rvDisplayOrders.setLayoutManager(new LinearLayoutManager(ShoppingCart.this));
        btnOrder = findViewById(R.id.btnOrder);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtCash = findViewById(R.id.txtCash);
        txtChange = findViewById(R.id.txtChange);
        lblTotal = findViewById(R.id.lblTotal);
        lblChange = findViewById(R.id.lblChange);
        lblCash = findViewById(R.id.lblCash);
        imgDeleteAllCartItem = findViewById(R.id.imgDeleteAllCartItem);
        displayOrders();

        db = new DatabaseHelper(ShoppingCart.this);
        totalPrice = calcTotalPrice();
        txtTotalPrice.setText(String.valueOf(totalPrice));
        btnOrder.setEnabled(false);

        txtCash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                cash = txtCash.getText().toString();

                if(cash.isEmpty()){
                    money = 0;
                    txtChange.setText("0");
                    return;
                }

                money = Integer.parseInt(cash);
                change = money - totalPrice;

                txtChange.setText(String.valueOf(change));
//                if(change == 0){
//                    txtChange.setText("0"); bruh
                if(change < 0 || cash.isEmpty()){
                    btnOrder.setEnabled(false);
                }else{
                    btnOrder.setEnabled(true);
                }
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd,yyyy h:mm a");
        Date date = new Date();
        orderDateAndTime = formatter.format(date);


        btnOrder.setOnClickListener(v ->{

            orderID = db.readCurrentOrderID();
            totalPrice = Double.parseDouble(txtTotalPrice.getText().toString());

                Order order = new Order(orderID, totalPrice,orderDateAndTime);
                db.purchaseOrder(order);
                Toast.makeText(this, "Successfully Purchased",Toast.LENGTH_SHORT).show();

                btnOrder.setEnabled(false);
                btnOrder.setText("ORDER COMPLETED");
                txtCash.setEnabled(false);
                rvDisplayOrders.setEnabled(false);

                Intent main = new Intent(ShoppingCart.this, MainActivity.class);
                startActivity(main);
                finish();
        });

        imgDeleteAllCartItem.setOnClickListener(v ->{
            db.deleteCartItems();
            //Will add alertdialog
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
        displayOrders();
    }

    public void displayOrders(){
        cartItemList.clear();
        db = new DatabaseHelper(ShoppingCart.this);
        Cursor cursor = db.readCartItems();
        if(cursor.getCount()==0){
            imgEmpty.setVisibility(View.VISIBLE);
            rvDisplayOrders.setVisibility(View.GONE);
            txtTotalPrice.setVisibility(View.GONE);
            txtCash.setVisibility(View.GONE);
            txtChange.setVisibility(View.GONE);
            btnOrder.setVisibility(View.GONE);
            lblTotal.setVisibility(View.GONE);
            lblChange.setVisibility(View.GONE);
            lblCash.setVisibility(View.GONE);
            imgDeleteAllCartItem.setVisibility(View.GONE);

        }else{
            while(cursor.moveToNext()){

                CartItem cartItem = new CartItem(
                        cursor.getBlob(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getDouble(3),
                        cursor.getInt(4));

                cartItemList.add(cartItem);
            }
        }
        cartItemsAdapter.notifyDataSetChanged();
    }

    public double calcTotalPrice(){
        db = new DatabaseHelper(ShoppingCart.this);
        double totalPrice = 0;

        Cursor cursor = db.readTotalPrice();
        if(cursor.getCount() > 0){
            if(cursor.moveToFirst()){
                totalPrice = cursor.getDouble(0);
                return totalPrice;
            }
        }else{
            return totalPrice;
        }

        return totalPrice;
    }


}