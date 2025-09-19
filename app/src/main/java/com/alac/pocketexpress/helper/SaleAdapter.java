package com.alac.pocketexpress.helper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alac.pocketexpress.MainActivity;
import com.alac.pocketexpress.OrderItemInfo;
import com.alac.pocketexpress.R;
import com.alac.pocketexpress.entity.Item;
import com.alac.pocketexpress.entity.Order;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Item> saleList;
    DatabaseHelper db;
    boolean isExisting;

    public SaleAdapter(Context context, ArrayList saleList){
        this.context = context;
        this.saleList = saleList;
    }

    @NonNull
    @Override
    public SaleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_grid, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleAdapter.MyViewHolder holder, int position) {
        Item item = saleList.get(position);

        Bitmap image = Utils.getImage(item.getItemImageBlob());
        if (image != null) {
            holder.imgSaleImage.setImageBitmap(image);
        } else {
            holder.imgSaleImage.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.lblSaleName.setText(item.getItemName());
        holder.lblSaleStock.setText("Stock "+String.valueOf(item.getItemStock()));
        holder.lblSalePrice.setText("₱"+String.valueOf(item.getItemPrice()));

        holder.btnAddToCart.setOnClickListener(v ->{

            db = new DatabaseHelper(context);
            isExisting = db.readAddedCartItem(String.valueOf(item.getItemCode()));
            if(isExisting){
                Toast.makeText(context, "ITEM ALREADY EXISTS IN CART", Toast.LENGTH_SHORT).show();
            }else{
                Intent buyItem = new Intent(context, OrderItemInfo.class);
                buyItem.putExtra("itemCode", String.valueOf(item.getItemCode()));
                context.startActivity(buyItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return saleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout saleLayout;
        ImageView imgSaleImage;
        TextView lblSaleName, lblSaleStock, lblSalePrice;
        Button btnAddToCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSaleImage = itemView.findViewById(R.id.imgSaleImage);
            lblSaleName = itemView.findViewById(R.id.lblSaleName);
            lblSaleStock = itemView.findViewById(R.id.lblSaleStock);
            lblSalePrice = itemView.findViewById(R.id.lblSalePrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            saleLayout = itemView.findViewById(R.id.saleLayout);
        }
    }
}
