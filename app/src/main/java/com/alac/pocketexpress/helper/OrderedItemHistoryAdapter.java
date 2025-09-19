package com.alac.pocketexpress.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alac.pocketexpress.R;
import com.alac.pocketexpress.entity.CartItem;

import java.util.ArrayList;

public class OrderedItemHistoryAdapter extends RecyclerView.Adapter<OrderedItemHistoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<CartItem> orderedItemList;

    public OrderedItemHistoryAdapter(Context context, ArrayList orderedItemList){
        this.context = context;
        this.orderedItemList = orderedItemList;
    }

    @NonNull
    @Override
    public OrderedItemHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ordered_item_in_history_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedItemHistoryAdapter.MyViewHolder holder, int position) {
        CartItem cartItem = orderedItemList.get(position);

        Bitmap image = Utils.getImage(cartItem.getItemImageBlob());
        if (image != null) {
            holder.imgOrderedItemImage.setImageBitmap(image);
        } else {
            holder.imgOrderedItemImage.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.lblNameOrdered.setText(cartItem.getItemName());
        holder.lblOrderedItemQuantity.setText(String.valueOf(cartItem.getCartItemQuantity()));
        holder.lblOrderedItemPrice.setText(String.valueOf(cartItem.getCartItemPrice()));

    }

    @Override
    public int getItemCount() {
        return orderedItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgOrderedItemImage;
        TextView lblNameOrdered, lblOrderedItemQuantity, lblOrderedItemPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrderedItemImage = itemView.findViewById(R.id.imgOrderedItemImage);
            lblNameOrdered = itemView.findViewById(R.id.lblNameOrdered);
            //lblOrderedItemQuantity = itemView.findViewById(R.id.lblOrderQuantity); IT HAPPENED AGAIN smh
            lblOrderedItemQuantity = itemView.findViewById(R.id.lblOrderedItemQuantity);
            lblOrderedItemPrice = itemView.findViewById(R.id.lblOrderedItemPrice);

        }
    }
}
