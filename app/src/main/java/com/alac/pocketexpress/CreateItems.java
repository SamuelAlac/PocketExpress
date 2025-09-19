package com.alac.pocketexpress;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alac.pocketexpress.entity.Item;
import com.alac.pocketexpress.helper.DatabaseHelper;
import com.alac.pocketexpress.helper.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CreateItems extends AppCompatActivity {

    ImageView imgAddItemImage, backToManageItem;
    EditText txtAddItemName, txtAddItemDesc, txtAddItemStock, txtAddItemPrice;
    Button btnCreateItem;
    byte[] itemImage;
    String itemName, itemDesc;
    int itemStock;
    double itemPrice;
    DatabaseHelper db;

    private static final int SELECT_IMAGE = 100;
    private static final String TAG = "CreateItems";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_items);

        backToManageItem = findViewById(R.id.imgBackToManageItem);
        backToManageItem.setOnClickListener(v ->{
            Intent back = new Intent(CreateItems.this, ManageItems.class);
            startActivity(back);
            finish();
        });


        imgAddItemImage = findViewById(R.id.imgAddItemImage);
        imgAddItemImage.setOnClickListener(v -> {
            if (hasStoragePermission(CreateItems.this)) {
                openImageChooser();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(((AppCompatActivity) CreateItems.this),
                            new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 200);
                } else {
                    ActivityCompat.requestPermissions(((AppCompatActivity) CreateItems.this),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
                }
            }
        });

        txtAddItemName = findViewById(R.id.txtAddItemName);
        txtAddItemDesc = findViewById(R.id.txtAddItemDesc);
        txtAddItemStock = findViewById(R.id.txtAddItemStock);
        txtAddItemPrice = findViewById(R.id.txtAddItemPrice);

        btnCreateItem = findViewById(R.id.btnCreateItem);
        btnCreateItem.setOnClickListener(v ->{

            itemName = txtAddItemName.getText().toString();
            itemDesc = txtAddItemDesc.getText().toString();
            String stock = txtAddItemStock.getText().toString();
            String price = txtAddItemPrice.getText().toString();

            if(itemImage == null) {
                Toast.makeText(this, "NO IMAGE ADDED", Toast.LENGTH_SHORT).show();
                return;
            }else if(itemName.isEmpty() || itemDesc.isEmpty() || stock.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "PLEASE INPUT REQUIRED ALL FIELDS", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                itemStock = Integer.parseInt(stock);
                itemPrice = Double.parseDouble(price);
                if(itemStock <= 0 || itemPrice <= 0){
                    return;
                }
            }catch(NumberFormatException e){
                Toast.makeText(this, "PLEASE ENTER VALID VALUE ONLY", Toast.LENGTH_SHORT).show();
                return;
            }
                Item item = new Item(itemImage, itemName, itemDesc, itemStock, itemPrice);
                db = new DatabaseHelper(CreateItems.this);
                db.createItem(item);


            imgAddItemImage.setImageResource(R.drawable.ic_launcher_background);
            txtAddItemName.setText("");
            txtAddItemDesc.setText("");
            txtAddItemStock.setText("");
            txtAddItemPrice.setText("");
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
    }

    private boolean hasStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    if (saveImageinDB(selectedImage)) {
                        Toast.makeText(this, "Imaged successfully saved", Toast.LENGTH_SHORT).show();
                        imgAddItemImage.setImageURI(selectedImage);
                    }
                }
            }
        }

        }

    private boolean saveImageinDB(Uri selectedImage) {
        try{
            InputStream inputStream = getContentResolver().openInputStream(selectedImage);
            itemImage = Utils.getBytes(inputStream);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    }








//            NumberFormatException: This exception is raised when a method could not convert a string into a numeric format.
//            StringIndexOutOfBoundsException: It is thrown by String class methods to indicate that an index is either negative
//            or greater than the size of the string
//             IllegalArgumentException : This exception will throw the error or error statement when the method receives an argument which is not
//             accurately fit to the given relation or condition. It comes under the unchecked exception.
