package com.alac.pocketexpress;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class UpdateCartItems extends AppCompatActivity {

    ImageView imgCartItemIMG, imgBackToCart;
    TextView lblCartItemStock, lblCartItemPrice,lblCartItemName, lblCartItemDesc, lblCartItemTotalPrice;
    Button btnMinus, btnAdd, btnUpdateItemInCart;
    EditText txtCartItemQuantity;
    DatabaseHelper db;
    String itemCode;
    byte[] itemImage;
    int itemStock;
    double itemPrice;
    int count;
    int quantity;
    double price;
    int orderID;
    int currentCount;
    double currentTotalPrice;
    double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_cart_items);

        imgCartItemIMG = findViewById(R.id.imgCartItemIMG);
        lblCartItemStock = findViewById(R.id.lblCartItemStock);
        lblCartItemPrice = findViewById(R.id.lblCartItemPrice);
        lblCartItemName = findViewById(R.id.lblCartItemName);
        lblCartItemDesc = findViewById(R.id.lblCartItemDesc);
        btnMinus = findViewById(R.id.btnMinus);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdateItemInCart = findViewById(R.id.btnUpdateItemInCart);
        txtCartItemQuantity = findViewById(R.id.txtCartItemQuantity);
        lblCartItemTotalPrice = findViewById(R.id.lblCartItemTotalPrice);
        imgBackToCart = findViewById(R.id.imgBackToCart);

        imgBackToCart.setOnClickListener(v ->{
            Intent back = new Intent(UpdateCartItems.this, ShoppingCart.class);
            startActivity(back);
            finish();
        });


        Intent boughtItem = getIntent();
        itemCode = boughtItem.getStringExtra("itemCode");
        db = new DatabaseHelper(UpdateCartItems.this);
        Cursor cursor = db.readItemByCode(itemCode);
        if(cursor.moveToFirst()){
            Bitmap image = Utils.getImage(cursor.getBlob(1));
            imgCartItemIMG.setImageBitmap(image);
            itemImage = cursor.getBlob(1); //I hate blob
            lblCartItemName.setText(cursor.getString(2));
            lblCartItemDesc.setText(cursor.getString(3));
            lblCartItemStock.setText(String.valueOf(cursor.getInt(4)));
            lblCartItemPrice.setText(String.valueOf(cursor.getInt(5)));

            itemStock = cursor.getInt(4);
            itemPrice = cursor.getInt(5);
        }

        currentCount = boughtItem.getIntExtra("quantity",0);
        currentTotalPrice = boughtItem.getDoubleExtra("totalprice", 0);
        count = currentCount;
        txtCartItemQuantity.setText(String.valueOf(count));
        lblCartItemTotalPrice.setText(String.valueOf(itemPrice));
        btnMinus.setOnClickListener(v ->{
            --count;
            if(count <= 0){
                count = 0;
            }
            txtCartItemQuantity.setText(String.valueOf(count));
        });

        btnAdd.setOnClickListener(v ->{
            count++;
            txtCartItemQuantity.setText(String.valueOf(count));
        });

        txtCartItemQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String txtCount = txtCartItemQuantity.getText().toString();
                if(txtCount.isEmpty()){
                    totalPrice = 0;
                    btnUpdateItemInCart.setEnabled(false);
                    return;
                }else{
                    btnUpdateItemInCart.setEnabled(true);
                }

                count = Integer.parseInt(txtCount);
                totalPrice = count * itemPrice;
                lblCartItemTotalPrice.setText(String.valueOf(totalPrice));
            }
        });

        btnUpdateItemInCart.setOnClickListener(v ->{
            orderID = db.readCurrentOrderID();
            quantity = Integer.parseInt(txtCartItemQuantity.getText().toString());
            price = Double.parseDouble(lblCartItemTotalPrice.getText().toString());

            Item checkItem = new Item(Integer.parseInt(itemCode));
            int itemStock = db.readStock(checkItem);
            if(quantity > itemStock){
                Toast.makeText(this, "NOT ENOUGH STOCK AVAILABLE", Toast.LENGTH_SHORT).show();
                btnUpdateItemInCart.setEnabled(false);
                btnUpdateItemInCart.setText("OUT OF STOCK");
                return;

            }else if(quantity == 0 || txtCartItemQuantity.getText().toString().equals("0") || txtCartItemQuantity.getText().toString().matches("^0\\d+$")){
                Toast.makeText(this, "ADD QUANTITY", Toast.LENGTH_SHORT).show();
                return;
            }

            Cart cart = new Cart(orderID, Integer.parseInt(itemCode), quantity, price);
            long result = db.updateCart(cart);
            if(result != -1){
                Toast.makeText(this, "SUCCESSFULLY UPDATED",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "FAILED TO UPDATE", Toast.LENGTH_SHORT).show();
            }

            int updateCount = count - currentCount;
            int deducted = itemStock - updateCount;

            Item item = new Item(Integer.parseInt(itemCode), deducted);
            db.updateStock(item);
            lblCartItemStock.setText(String.valueOf(deducted));
            if(deducted == 0){
                Toast.makeText(this, "OUT OF STOCK", Toast.LENGTH_SHORT).show();
                btnUpdateItemInCart.setEnabled(false);
                btnUpdateItemInCart.setText("OUT OF STOCK");
            }


        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}