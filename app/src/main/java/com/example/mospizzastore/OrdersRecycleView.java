/**
 *
 * author: amir kamalian
 * date:   27 Mar 2023
 *
 * */


package com.example.mospizzastore;

/**
 *
 * This is the class used to display the orders in orders
 * query.
 *
 * @references: - https://stackoverflow.com/questions/45474333/how-can-i-set-onclicklistener-to-two-buttons-in-recyclerview
 *
 * */

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersRecycleView extends RecyclerView.Adapter<OrdersRecycleView.MyViewHolder> {

    /** database adapter to edit or delete database records */
    public DBInterface db_interface;

    /** order fields */
    private Context context;
    private ArrayList<String> ids;
    private ArrayList<String> names;
    private ArrayList<String> addresses;
    private ArrayList<String> phones;
    private ArrayList<String> first_top;
    private ArrayList<String> second_top;
    private ArrayList<String> third_top;
    private ArrayList<String> sizes;

    public OrdersRecycleView(Context context, ArrayList<String> ids, ArrayList<String> names,
                             ArrayList<String> addresses, ArrayList<String> phones, ArrayList<String> first_top,
                             ArrayList<String> second_top, ArrayList<String> third_top, ArrayList<String> sizes) {
        this.context = context;
        this.db_interface = new DBInterface(context);
        this.ids = ids;
        this.names = names;
        this.addresses = addresses;
        this.phones = phones;
        this.first_top = first_top;
        this.second_top = second_top;
        this.third_top = third_top;
        this.sizes = sizes;
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view, new MyClickListener() {
            @Override
            public void onEdit(int p) {
                // editing an order
            };

            @Override
            public void onDelete(int p) {


                db_interface.deleteOrder(ids.get(p));
                removeOrder(p);

            };

        });

        return holder;
    };

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.order_id.setText(String.valueOf(ids.get(position)));
        holder.customer_name.setText(String.valueOf(names.get(position)));
        holder.customer_phone.setText(String.valueOf(addresses.get(position)));
        holder.customer_address.setText(String.valueOf(phones.get(position)));
        holder.topping_one.setText(String.valueOf(first_top.get(position)));
        holder.topping_two.setText(String.valueOf(second_top.get(position)));
        holder.topping_three.setText(String.valueOf(third_top.get(position)));
        holder.size.setText(String.valueOf(sizes.get(position)));
    };

    @Override
    public int getItemCount() {
        return ids.size();
    };

    /** utility function for deleting a row */
    public void removeOrder(int position) {
        ids.remove(ids.get(position));
        names.remove(names.get(position));
        addresses.remove(addresses.get(position));
        phones.remove(phones.get(position));
        first_top.remove(first_top.get(position));
        second_top.remove(second_top.get(position));
        third_top.remove(third_top.get(position));
        sizes.remove(sizes.get(position));
    };


    /** view holder class */
    public class MyViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        MyClickListener listener;

        /** edit and delete order buttons */
        private Button delete_order;
        private Button edit_order;

        /** order information views */
        private TextView order_id;
        private TextView customer_name;
        private TextView customer_phone;
        private TextView customer_address;
        private TextView topping_one;
        private TextView topping_two;
        private TextView topping_three;
        private TextView size;

        public MyViewHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
            customer_name = itemView.findViewById(R.id.Cname);
            customer_phone = itemView.findViewById(R.id.Cphone);
            customer_address = itemView.findViewById(R.id.Caddress);

            topping_one = itemView.findViewById(R.id.toppingOne);
            topping_two = itemView.findViewById(R.id.toppingTwo);
            topping_three = itemView.findViewById(R.id.toppingThree);
            size = itemView.findViewById(R.id.orderSize);

            /** edit and delete order buttons */
            delete_order = itemView.findViewById(R.id.deleteOrderButton);
            edit_order = itemView.findViewById(R.id.editOrderButton);

            this.listener = listener;

            delete_order.setOnClickListener(this);
            edit_order.setOnClickListener(this);

        };

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.deleteOrderButton:
                    listener.onDelete(this.getLayoutPosition());
                    break;
                case R.id.editOrderButton:
                    listener.onEdit(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        };


    }; /* ---- end view holder class ---- */


    /** interface used for delete and edit buttons */
    public interface MyClickListener {
        void onEdit(int p);
        void onDelete(int p);
    };


};











