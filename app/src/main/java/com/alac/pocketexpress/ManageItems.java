package com.alac.pocketexpress;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alac.pocketexpress.entity.Item;
import com.alac.pocketexpress.helper.DatabaseHelper;
import com.alac.pocketexpress.helper.ItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManageItems extends AppCompatActivity {

    FloatingActionButton btnAddItems;
    ImageView imgBack, imgDeleteAllItems, imgEmptyItemContainer;
    RecyclerView rvItemView;
    ArrayList<Item> itemList = new ArrayList<>();
    DatabaseHelper db;

    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_items);

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v ->{
            Intent back = new Intent(ManageItems.this, MainActivity.class);
            startActivity(back);
            finish();
        });

        imgDeleteAllItems = findViewById(R.id.imgDeleteAllItems);
        imgDeleteAllItems.setOnClickListener(v ->{
            deleteAllDialog();
        });
        imgEmptyItemContainer = findViewById(R.id.imgEmptyItemContainer);
        btnAddItems = findViewById(R.id.btnAddItems);
        rvItemView = findViewById(R.id.rvItemView);
        displayItems();

        ItemAdapter itemAdapter = new ItemAdapter(ManageItems.this, itemList);
        rvItemView.setAdapter(itemAdapter);
        rvItemView.setLayoutManager(new LinearLayoutManager(ManageItems.this));
//        layoutManager = new GridLayoutManager(this, 2);
//        rvItemView.setLayoutManager(layoutManager);


        btnAddItems.setOnClickListener(v ->{
            Intent addItems = new Intent(ManageItems.this, CreateItems.class);
            startActivity(addItems);
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
        itemList.clear();
        displayItems();
    }

    public void displayItems(){
        db = new DatabaseHelper(ManageItems.this);
        Cursor cursor = db.readItems();
        if(cursor.getCount()==0){
            imgEmptyItemContainer.setVisibility(View.VISIBLE);
            rvItemView.setVisibility(View.GONE);
        }else{
            while(cursor.moveToNext()){

                Item item = new Item(cursor.getInt(0), cursor.getBlob(1), cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getDouble(5));

                itemList.add(item);
            }
        }
    }


    public void deleteAllDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all items?");
        builder.setMessage("Are you sure you want to delete all items?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteAllItems();
                Intent refresh = new Intent(ManageItems.this, ManageItems.class);
                startActivity(refresh);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

}