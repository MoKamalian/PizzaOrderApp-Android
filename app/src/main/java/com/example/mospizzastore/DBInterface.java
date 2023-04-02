/**
 *
 * author: amir kamalian
 * date:   25 Mar 2023
 *
 * */

package com.example.mospizzastore;


/**
 * DBInterface class is the wrapper for DBAdapter. This is the class
 * that should be used to make queries and commit orders to the
 * database.
 * */

import android.content.Context;
import android.widget.Toast;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;

public class DBInterface {

    /** database adapter */
    private DBAdapter db_adapter;

    /** constructor */
    public DBInterface(Context ctx) {
        this.db_adapter = new DBAdapter(ctx);
    };


    /** Submitting an order to the database */
    public void submitOrder(String c_name, String c_address, String c_phone, String toppings,
                            String size) {
        this.db_adapter.open();
        this.db_adapter.inserOrder(c_name, c_address, c_phone, toppings, size);
        this.db_adapter.close();
    };

    /** deleting a single order */
    public void deleteOrder(String id) {
        this.db_adapter.open();
        this.db_adapter.deleteOrder(id);
        this.db_adapter.close();
    };

    /** Changes an order made; if order does not exit an
     *  error message is returned */
    public void changeOrder(long order_id, String c_name, String c_address, String c_phone, String toppings,
                            String size) {
        this.db_adapter.open();
        this.db_adapter.updateOrder(order_id, c_name, c_address, c_phone, toppings, size);
        this.db_adapter.close();
    };

    /** look up and return a specific order in the database under a name */
    public void getOrder() {

    };

    /** method gets all the orders under a particular name
     * @return formatted string of the all the orders under the-
     * specific name */
    public ArrayList<String> getAllOrdersFor(String name) {
        this.db_adapter.open();
        /** place holders for returned data */
        ArrayList<String> orders = new ArrayList<String>();
        Cursor cursor = this.db_adapter.getAllOrders(name);

        if(cursor.getCount() == 0) {
            /* ---- no records found ---- */
            return orders;
        } else {
            while(cursor.moveToNext()) {
                orders.add(cursor.getString(0) +
                        ":" + cursor.getString(1) +
                        ":" + cursor.getString(2) +
                        ":" + cursor.getString(3) +
                        ":" + cursor.getString(4) +
                        ":" + cursor.getString(5));
            }

            this.db_adapter.close();
            return orders;
        }

    };

};








