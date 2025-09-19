package com.alac.pocketexpress;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.alac.pocketexpress.entity.Cart;
import com.alac.pocketexpress.entity.Item;
import com.alac.pocketexpress.helper.DatabaseHelper;
import com.alac.pocketexpress.helper.Utils;

public class OrderItemInfo extends AppCompatActivity {

    ImageView imgBackToSale, imgCartItemImage;
    TextView lblCartStock, lblCartPrice, txtCartItemName, txtCartItemDesc;
    byte[] itemImage;
    String itemCode, itemName, itemDesc;
    int itemStock;
    double itemPrice;
    EditText txtCartQuantity;
    TextView lblCartTotalPrice;
    Button btnIncrement, btnDecrement, btnAddItemInCart;
    int count;
    int quantity;
    double price;
    DatabaseHelper db;
    int orderID;
    double totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_item_info);

        imgBackToSale = findViewById(R.id.imgBackToSale);
        imgBackToSale.setOnClickListener(v ->{
            Intent backToShop = new Intent(OrderItemInfo.this, OrderItems.class);
            startActivity(backToShop);
            finish();
        });

        imgCartItemImage = findViewById(R.id.imgCartItemImage);
        lblCartStock = findViewById(R.id.lblCartStock);
        lblCartPrice = findViewById(R.id.lblCartPrice);
        txtCartItemName = findViewById(R.id.txtCartItemName);
        txtCartItemDesc = findViewById(R.id.txtCartItemDesc);

        //saleIntentData();
        Intent itemInfo = getIntent();
        itemCode = itemInfo.getStringExtra("itemCode");
        db = new DatabaseHelper(OrderItemInfo.this);
        Cursor cursor = db.readItemByCode(itemCode);
        if(cursor.moveToFirst()){
            Bitmap image = Utils.getImage(cursor.getBlob(1));
            imgCartItemImage.setImageBitmap(image);
            itemImage = cursor.getBlob(1); //I hate blob
            txtCartItemName.setText(cursor.getString(2));
            txtCartItemDesc.setText(cursor.getString(3));
            lblCartStock.setText(String.valueOf(cursor.getInt(4)));
            lblCartPrice.setText(String.valueOf(cursor.getDouble(5)));

            itemStock = cursor.getInt(4);
            itemPrice = cursor.getDouble(5);

        }

        txtCartQuantity = findViewById(R.id.txtCartQuantity);
        lblCartTotalPrice = findViewById(R.id.lblCartTotalPrice);

        count = 1;
        txtCartQuantity.setText(String.valueOf(count));
        lblCartTotalPrice.setText(String.valueOf(itemPrice));
        btnDecrement = findViewById(R.id.btnDecrement);
        btnDecrement.setOnClickListener(v ->{
            --count;
            if(count <= 0){
                count = 0;
            }
            txtCartQuantity.setText(String.valueOf(count));
        });
        btnIncrement = findViewById(R.id.btnIncrement);
        btnIncrement.setOnClickListener(v ->{
            count++;
            txtCartQuantity.setText(String.valueOf(count));
        });

        txtCartQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //count *= itemPrice; bruh
                String txtCount = txtCartQuantity.getText().toString();

                if(txtCount.isEmpty()){
                    totalPrice = 0;
                    btnAddItemInCart.setEnabled(false);
                    return;
                }else{
                    btnAddItemInCart.setEnabled(true);
                }
                count = Integer.parseInt(txtCount);
                totalPrice = count * itemPrice;
                lblCartTotalPrice.setText(String.valueOf(totalPrice));
            }
        });

        btnAddItemInCart = findViewById(R.id.btnAddItemInCart);
        btnAddItemInCart.setOnClickListener(v ->{

            orderID = db.readCurrentOrderID();
            quantity = Integer.parseInt(txtCartQuantity.getText().toString());
            price = Double.parseDouble(lblCartTotalPrice.getText().toString());

            Item checkItem = new Item(Integer.parseInt(itemCode));
            int itemStock = db.readStock(checkItem);
            if(quantity > itemStock){
                Toast.makeText(this, "NOT ENOUGH STOCK AVAILABLE", Toast.LENGTH_SHORT).show();
                btnAddItemInCart.setEnabled(false);
                btnAddItemInCart.setText("OUT OF STOCK");
                return;
            }else if(quantity == 0 || txtCartQuantity.getText().toString().equals("0") || txtCartQuantity.getText().toString().matches("^0\\d+$")){
            Toast.makeText(this, "ADD QUANTITY", Toast.LENGTH_SHORT).show();
            return;
        }

            Cart cart = new Cart(orderID, Integer.parseInt(itemCode), quantity, price);
            db.createCart(cart);

            int deducted = itemStock - quantity;
            Item item = new Item(Integer.parseInt(itemCode), deducted);
            db.updateStock(item);
            lblCartStock.setText(String.valueOf(deducted));
            if(deducted == 0){
                Toast.makeText(this, "OUT OF STOCK", Toast.LENGTH_SHORT).show();
                btnAddItemInCart.setEnabled(false);
                btnAddItemInCart.setText("OUT OF STOCK");
            }

            getOnBackPressedDispatcher().onBackPressed();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}