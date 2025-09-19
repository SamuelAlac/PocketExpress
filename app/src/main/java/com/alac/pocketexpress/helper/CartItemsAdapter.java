package com.alac.pocketexpress.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alac.pocketexpress.R;
import com.alac.pocketexpress.UpdateCartItems;
import com.alac.pocketexpress.UpdateItems;
import com.alac.pocketexpress.entity.CartItem;

import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<CartItem> cartItemList;

    public CartItemsAdapter(Context context, ArrayList cartItemList){
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ordereditem_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsAdapter.MyViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);

        Bitmap image = Utils.getImage(cartItem.getItemImageBlob());
        if (image != null) {
            holder.imgOrderImage.setImageBitmap(image);
        } else {
            holder.imgOrderImage.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.lblOrderName.setText(cartItem.getItemName());
        holder.lblOrderQuantity.setText(String.valueOf(cartItem.getCartItemQuantity()));
        holder.lblOrderPrice.setText(String.valueOf(cartItem.getCartItemPrice()));

        holder.orderedItemLayout.setOnClickListener(v ->{

            Intent boughtItem = new Intent(context, UpdateCartItems.class);
            boughtItem.putExtra("itemCode", String.valueOf(cartItem.getFkItemCode()));
            boughtItem.putExtra("quantity", cartItem.getCartItemQuantity());
            boughtItem.putExtra("totalprice", cartItem.getCartItemPrice());
            context.startActivity(boughtItem);
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgOrderImage;
        TextView lblOrderName, lblOrderQuantity, lblOrderPrice;
        LinearLayout orderedItemLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgOrderImage = itemView.findViewById(R.id.imgOrderImage);
            lblOrderName = itemView.findViewById(R.id.lblOrderName);
            lblOrderQuantity = itemView.findViewById(R.id.lblOrderQuantity);
            lblOrderPrice = itemView.findViewById(R.id.lblOrderPrice);
            orderedItemLayout = itemView.findViewById(R.id.orderedItemLayout);

        }
    }
}
