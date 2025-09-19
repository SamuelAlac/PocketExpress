package com.alac.pocketexpress.helper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alac.pocketexpress.OrderedItemHistory;
import com.alac.pocketexpress.R;
import com.alac.pocketexpress.entity.Order;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Order> orderList;

    public OrderHistoryAdapter(Context context, ArrayList orderList){
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.orderhistory_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.MyViewHolder holder, int position) {
            Order order = orderList.get(position);

            holder.lblOrderID.setText(String.valueOf(order.getOrderID()));
            holder.lblStaffUsername.setText(order.getStaffName());
            holder.lblTotalOrderPrice.setText(String.valueOf(order.getOrderPrice()));
            holder.lblOrderDate.setText(order.getOrderDateAndTime());

            holder.orderHistoryLayout.setOnClickListener(v ->{
                Intent orderedItemsHistory = new Intent(context, OrderedItemHistory.class);
                orderedItemsHistory.putExtra("orderID", String.valueOf(order.getOrderID()));
                context.startActivity(orderedItemsHistory);

            });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lblOrderID, lblStaffUsername, lblTotalOrderPrice, lblOrderDate;
        LinearLayout orderHistoryLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lblOrderID = itemView.findViewById(R.id.lblOrderID);
            lblStaffUsername = itemView.findViewById(R.id.lblStaffUsername);
            lblTotalOrderPrice = itemView.findViewById(R.id.lblTotalOrderPrice);
            lblOrderDate = itemView.findViewById(R.id.lblOrderDate);
            orderHistoryLayout = itemView.findViewById(R.id.orderHistoryLayout);
        }
    }
}
