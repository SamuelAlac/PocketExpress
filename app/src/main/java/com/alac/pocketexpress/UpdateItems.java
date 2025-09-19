package com.alac.pocketexpress;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.InputStream;

public class UpdateItems extends AppCompatActivity {

    ImageView imgBackToManage, imgUpdateItemImage;
    EditText txtUpdateItemName, txtUpdateItemDesc, txtUpdateItemStock, txtUpdateItemPrice;
    Button btnUpdateItem;
    //int itemCode;
    byte[] itemImage;
    String itemCode, itemName, itemDesc;
    int itemStock;
    double itemPrice;
    DatabaseHelper db;

    private static final int SELECT_IMAGE = 100;
    private static final String TAG = "UpdateItems";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_items);

        txtUpdateItemName = findViewById(R.id.txtUpdateItemName);
        txtUpdateItemDesc = findViewById(R.id.txtUpdateItemDesc);
        txtUpdateItemStock = findViewById(R.id.txtUpdateItemStock);
        txtUpdateItemPrice = findViewById(R.id.txtUpdateItemPrice);

        imgBackToManage = findViewById(R.id.imgBackToManage);
        imgBackToManage.setOnClickListener(v ->{
            Intent back = new Intent(UpdateItems.this, ManageItems.class);
            startActivity(back);
            finish();
        });

        imgUpdateItemImage = findViewById(R.id.imgUpdateItemImage);
        imgUpdateItemImage.setOnClickListener(v ->{
            if (hasStoragePermission(UpdateItems.this)) {
                openImageChooser();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(((AppCompatActivity) UpdateItems.this),
                            new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 200);
                } else {
                    ActivityCompat.requestPermissions(((AppCompatActivity) UpdateItems.this),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
                }
            }
        });

        Intent update = getIntent();
        itemCode = update.getStringExtra("itemCode");

        db = new DatabaseHelper(UpdateItems.this);
        Cursor cursor = db.readItemByCode(itemCode);

        if(cursor.moveToFirst()){
            Bitmap image = Utils.getImage(cursor.getBlob(1));
            imgUpdateItemImage.setImageBitmap(image);
            itemImage = cursor.getBlob(1); //I hate blob
            txtUpdateItemName.setText(cursor.getString(2));
            txtUpdateItemDesc.setText(cursor.getString(3));
            txtUpdateItemStock.setText(String.valueOf(cursor.getInt(4)));
            txtUpdateItemPrice.setText(String.valueOf(cursor.getDouble(5)));
        }

        btnUpdateItem = findViewById(R.id.btnUpdateItem);
        btnUpdateItem.setOnClickListener(v ->{
            int ID = Integer.parseInt(update.getStringExtra("itemCode"));

            itemName = txtUpdateItemName.getText().toString();
            itemDesc = txtUpdateItemDesc.getText().toString();
            String stock = txtUpdateItemStock.getText().toString();
            String price = txtUpdateItemPrice.getText().toString();

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

                Item item = new Item(ID, itemImage, itemName, itemDesc, itemStock, itemPrice);
                long result = db.updateItem(item);

                if(result != -1){
                    Toast.makeText(this, "SUCCESSFULLY UPDATED",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "FAILED TO UPDATE", Toast.LENGTH_SHORT).show();
                }

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
                        imgUpdateItemImage.setImageURI(selectedImage);
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