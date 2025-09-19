package com.alac.pocketexpress.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alac.pocketexpress.R;
import com.alac.pocketexpress.UpdateItems;
import com.alac.pocketexpress.entity.Item;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Item> itemList;

    public ItemAdapter(Context context, ArrayList itemList){
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        Item item = itemList.get(position);

        Bitmap image = Utils.getImage(item.getItemImageBlob());
        if (image != null) {
            holder.imgItemImage.setImageBitmap(image);
        } else {
            holder.imgItemImage.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.lblItemName.setText(item.getItemName());
        holder.lblItemDesc.setText(item.getItemDesc());
        holder.lblItemStock.setText("Stock "+String.valueOf(item.getItemStock()));
        holder.lblItemPrice.setText("₱"+String.valueOf(item.getItemPrice()));

        holder.itemRowLayout.setOnClickListener(v ->{
            Intent update = new Intent(context, UpdateItems.class);
            update.putExtra("itemCode", String.valueOf(item.getItemCode()));
            context.startActivity(update);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemImage;
        TextView lblItemName, lblItemDesc, lblItemStock, lblItemPrice;
        LinearLayout itemRowLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //imgItemImage = itemView.findViewById(R.id.imgAddItemImage); I hate u so much
            imgItemImage = itemView.findViewById(R.id.imgItemImage);
            lblItemName = itemView.findViewById(R.id.lblItemName);
            lblItemDesc = itemView.findViewById(R.id.lblItemDesc);
            lblItemStock = itemView.findViewById(R.id.lblItemStock);
            lblItemPrice = itemView.findViewById(R.id.lblItemPrice);
            itemRowLayout = itemView.findViewById(R.id.itemRowLayout);
        }
    }

}
