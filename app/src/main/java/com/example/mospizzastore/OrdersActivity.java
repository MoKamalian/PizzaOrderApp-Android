package com.example.mospizzastore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    /** arrays to hold record values for the recycler view */
    private ArrayList<String> ids = new ArrayList<String>();
    private ArrayList<String> names= new ArrayList<String>();
    private ArrayList<String> addresses = new ArrayList<String>();
    private ArrayList<String> phones= new ArrayList<String>();
    private ArrayList<String> first_top = new ArrayList<String>();
    private ArrayList<String> second_top = new ArrayList<String>();
    private ArrayList<String> third_top = new ArrayList<String>();
    private ArrayList<String> sizes = new ArrayList<String>();

    /** recycle view that will display the orders */
    private OrdersRecycleView custom_recycle_view;
    private RecyclerView recycler_view;

    /** button that takes you back to the orders screen */
    private Button back_button;
    private ImageButton do_search_btn;
    private Button search_for_all;

    /** the search field used to grab name to query for orders */
    private TextView field_name;

    /** database object for SQLite interfacing */
    private DBInterface db_interface = new DBInterface(OrdersActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        linkAll();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrdersActivity.this, MainActivity.class);
                startActivity(i);
            };
        });



        recycler_view = findViewById(R.id.recyclerView);
        /** search for all orders under a particular name */
        search_for_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getFieldName();
                if(name == null) {
                    Toast.makeText(OrdersActivity.this, "You must enter a name", Toast.LENGTH_LONG).show();
                } else {

                    ArrayList<String> orders = db_interface.getAllOrdersFor(name);
                    if(orders.isEmpty()) {
                        /* ---- no orders found ---- */
                        Toast.makeText(OrdersActivity.this, "No Orders Under That Name", Toast.LENGTH_LONG).show();
                    } else {
                        /* ---- display all the orders found under that name ---- */
                        /** recycler view */

                        if(!ids.isEmpty()) {
                            clearAllData();
                        }

                        processRecords(orders);
                        custom_recycle_view = new OrdersRecycleView(OrdersActivity.this, ids, names, addresses, phones, first_top, second_top,
                                                                    third_top, sizes);
                        recycler_view.setAdapter(custom_recycle_view);
                        recycler_view.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));


                    }

                }
            };
        });


    };


    /** processing the queries */
    private void processRecords(ArrayList<String> orders) {
        if(orders.isEmpty()) {
            /*---- no data was retrieved ----*/
            return;
        } else {

            for(String s : orders) {

                String[] temp = s.split(":");
                ids.add(temp[0]);
                names.add(temp[1]);
                addresses.add(temp[2]);
                phones.add(temp[3]);
                /* ---- separating the toppings ---- */
                String[] topp = temp[4].split(",");
                first_top.add(topp[0]);
                second_top.add(topp[1]);
                third_top.add(topp[2]);

                sizes.add(temp[5]);
            }

        }
    };

    /** utility methods */

    /** gets the field name entered for querying */
    private String getFieldName() {
        String name = field_name.getText().toString();
        return name.isEmpty() ? null : name;
    };

    /** linking all the views */
    private void linkAll() {
        back_button = findViewById(R.id.backButton);
        do_search_btn = findViewById(R.id.doSearch);
        search_for_all = findViewById(R.id.searchAll);

        field_name = findViewById(R.id.fieldName);
        field_name.setHint("Enter Name To Search");
    };

    /** clear all the data lists to allow for new data to be displayed */
    private void clearAllData() {
        ids.clear();
        names.clear();
        addresses.clear();
        phones.clear();
        first_top.clear();
        second_top.clear();
        third_top.clear();
        sizes.clear();
    };

};











