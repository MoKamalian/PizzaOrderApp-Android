/**
 *
 * author: amir kamalian
 * date:   29 Mar 2023
 *
 * */

package com.example.mospizzastore;

/**
 *
 * this is the activity used to update an order.
 *
 * */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.util.HashMap;

public class UpdateOrderActivity extends AppCompatActivity {

    /** the list of toppings ordered */
    private HashMap<String, Integer> toppings_ordered = new HashMap<String, Integer>() {{
        put("SAUSAGE", 0);
        put("BACON", 0);
        put("CHICKEN", 0);
        put("PEPERONI", 0);
        put("MUSHROOM", 0);
        put("SALAMI", 0);
        put("DONAIR", 0);
    }};

    /** All remove and add buttons */
    private Button sausage_add_btn2;
    private Button peperoni_add_btn2;
    private Button bacon_add_btn2;
    private Button mushroom_add_btn2;
    private Button chicken_add_btn2;
    private Button salami_add_btn2;
    private Button donair_add_btn2;

    private Button sausage_remove_btn2;
    private Button peperoni_remove_btn2;
    private Button bacon_remove_btn2;
    private Button mushroom_remove_btn2;
    private Button chicken_remove_btn2;
    private Button salami_remove_btn2;
    private Button donair_remove_btn2;

    /** amount of toppings displays */
    private TextView amount_sausage2;
    private TextView amount_peperoni2;
    private TextView amount_bacon2;
    private TextView amount_mushroom2;
    private TextView amount_chicken2;
    private TextView amount_salami2;
    private TextView amount_donair2;

    /** Customer info text fields */
    private TextView customer_name2;
    private TextView customer_address2;
    private TextView customer_phone2;

    /** submit order button */
    private Button update_order;

    /** Topping labels */
    private TextView topping_sausage2;
    private TextView topping_peperoni2;
    private TextView topping_bacon2;
    private TextView topping_mushroom2;
    private TextView topping_chicken2;
    private TextView topping_salami2;
    private TextView topping_donair2;

    /** enter pizza info label */
    private TextView update_info_label;

    /** pizza size radio buttons */
    private RadioGroup size_rg2;
    private RadioButton size_small_rb2;
    private RadioButton size_medium_rb2;
    private RadioButton size_large_rb2;

    /** back to the order review button */
    private Button back_to_orders;

    /** display for the current order ID being edited */
    private TextView current_order_id;

    /** TOPPINGS LIMIT AND MIN*/
    private static final int TOPPING_LIMIT = 3;
    private static final int TOPPING_MIN = 0;

    /** main interface to the database for updating order records */
    private DBInterface db_interface = new DBInterface(UpdateOrderActivity.this);

    /** the selected order ID */
    private int current_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order);

        /* ---- linking UI Elements ---- */
        link_all_widgets();
        linkAddRemoveBtns();

        /* ---- grabbing language preference ---- */
        /** getting the saved language setting */
        changeLanguage(loadLanguagePreference());

        /** update the views with the current order selected for editing */
        updateView();

        /** back to orders review button: goes to
         *  @link OrdersActivity */
        back_to_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(UpdateOrderActivity.this, OrdersActivity.class);
                startActivity(I);
            };
        });


        /** submit the update to the database */
        update_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkOrder()) {

                    db_interface.changeOrder(current_id, customer_name2.getText().toString(),
                            customer_address2.getText().toString(),
                            customer_phone2.getText().toString(),
                            getToppings(),
                            getSizeSelected());

                    clearOrderScreen();
                    Toast order = Toast.makeText(UpdateOrderActivity.this, "Order Updated", Toast.LENGTH_LONG);
                    order.show();
                    topping_count = 0;
                } else {
                    /** if there are no toppings, customer info is missing, or size is
                     * not selected */
                    Toast pop = Toast.makeText(UpdateOrderActivity.this, "Missing info!", Toast.LENGTH_LONG);
                    pop.show();
                }

            };
        });


    };

    /** populates the UI views with the values gathered from OrdersRecycleView */
    private void updateView() {
        /** setting the text views */
        current_order_id.setText(getIntent().getStringExtra("id"));
        current_id = Integer.parseInt(getIntent().getStringExtra("id"));

        customer_name2.setText(getIntent().getStringExtra("name"));
        customer_address2.setText(getIntent().getStringExtra("address"));
        customer_phone2.setText(getIntent().getStringExtra("phone"));

        updateToppings(getIntent().getStringExtra("firstTopping"));
        updateToppings(getIntent().getStringExtra("secondTopping"));
        updateToppings(getIntent().getStringExtra("thirdTopping"));

        updateSizeToggle(getIntent().getStringExtra("size"));

        topping_count = TOPPING_LIMIT;
    };

    /** update the size toggle buttons */
    private void updateSizeToggle(String size) {
        switch(size) {
            case "SMALL":
                size_small_rb2.setChecked(true);
                break;
            case "MEDIUM":
                size_medium_rb2.setChecked(true);
                break;
            case "LARGE":
                size_large_rb2.setChecked(true);
                break;
            default:
                break;
        }
    };

    /** updates the topping views to the current order */
    private void updateToppings(String topping) {
        toppings_ordered.put(topping, toppings_ordered.get(topping) + 1);
        switch(topping) {
            case "SAUSAGE":
                amount_sausage2.setText(Integer.toString(toppings_ordered.get(topping)));
                break;
            case "PEPERONI":
                amount_peperoni2.setText(Integer.toString(toppings_ordered.get(topping)));
                break;
            case "BACON":
                amount_bacon2.setText(Integer.toString(toppings_ordered.get(topping)));
                break;
            case "MUSHROOM":
                amount_mushroom2.setText(Integer.toString(toppings_ordered.get(topping)));
                break;
            case "CHICKEN":
                amount_chicken2.setText(Integer.toString(toppings_ordered.get(topping)));
                break;
            case "SALAMI":
                amount_salami2.setText(Integer.toString(toppings_ordered.get(topping)));
                break;
            case "DONAIR":
                amount_donair2.setText(Integer.toString(toppings_ordered.get(topping)));
                break;
        }
    }; /* ---- END OF TOPPING UPDATE ---- */


    /** ==================== COPY FROM MAIN ACTIVITY ==================== */
    /** utility methods */
    /** the below methods are the exact copy from MainActivity to provide
     * the functionality to choose toppings */

    /** updating the count views */
    private int topping_count = 0;
    /** simple utility function to change the topping count display */
    private void changeCountDisplay(TextView t, boolean add, String topping_key) {
        if (add) {
            if(topping_count >= TOPPING_LIMIT) {
                Toast pop = Toast.makeText(this, "Max 3 toppings!!", Toast.LENGTH_LONG);
                pop.show();
                return;
            }

            int i = Integer.parseInt(t.getText().toString());
            t.setText(Integer.toString(i + 1));
            topping_count++;

            /** adding the topping count to hashmap */
            toppings_ordered.put(topping_key, toppings_ordered.get(topping_key) + 1);

        } else {
            if(topping_count <= TOPPING_MIN || t.getText().toString().equals("0")) {
                Toast pop = Toast.makeText(this, "Cant have less than zero toppings!", Toast.LENGTH_LONG);
                pop.show();
                return;
            }

            int i = Integer.parseInt(t.getText().toString());
            t.setText(Integer.toString(i - 1));
            topping_count--;

            /** removing the topping count from hashmap */
            toppings_ordered.put(topping_key, toppings_ordered.get(topping_key) - 1);

        }
    }; /* ---- END CHANGE COUNT DISPLAY ---- */

    /** Toppings button listener */
    public View.OnClickListener update_toppings_btns = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                /** add buttons */
                case R.id.addButtonSausage2:
                    changeCountDisplay(amount_sausage2, true, "SAUSAGE");
                    break;
                case R.id.addButtonBacon2:
                    changeCountDisplay(amount_bacon2, true, "BACON");
                    break;
                case R.id.addButtonChicken2:
                    changeCountDisplay(amount_chicken2, true, "CHICKEN");
                    break;
                case R.id.addButtonDonair2:
                    changeCountDisplay(amount_donair2, true, "DONAIR");
                    break;
                case R.id.addButtonSalami2:
                    changeCountDisplay(amount_salami2, true, "SALAMI");
                    break;
                case R.id.addButtonMushroom2:
                    changeCountDisplay(amount_mushroom2, true, "MUSHROOM");
                    break;
                case R.id.addButtonPeperoni2:
                    changeCountDisplay(amount_peperoni2, true, "PEPERONI");
                    break;

                /** remove buttons */
                case R.id.removeButtonSausage2:
                    changeCountDisplay(amount_sausage2, false, "SAUSAGE");
                    break;
                case R.id.removeButtonBacon2:
                    changeCountDisplay(amount_bacon2, false, "BACON");
                    break;
                case R.id.removeButtonChicken2:
                    changeCountDisplay(amount_chicken2, false, "CHICKEN");
                    break;
                case R.id.removeButtonDonair2:
                    changeCountDisplay(amount_donair2, false, "DONAIR");
                    break;
                case R.id.removeButtonSalami2:
                    changeCountDisplay(amount_salami2, false, "SALAMI");
                    break;
                case R.id.removeButtonMushroom2:
                    changeCountDisplay(amount_mushroom2, false, "MUSHROOM");
                    break;
                case R.id.removeButtonPeperoni2:
                    changeCountDisplay(amount_peperoni2, false, "PEPERONI");
                    break;
                default:
                    break;
            }
        };
    };


    /** linking all the UI Elements */
    private void link_all_widgets() {
        /** All remove and add buttons */
        sausage_add_btn2 = findViewById(R.id.addButtonSausage2);
        peperoni_add_btn2 = findViewById(R.id.addButtonPeperoni2);
        bacon_add_btn2 = findViewById(R.id.addButtonBacon2);
        mushroom_add_btn2 = findViewById(R.id.addButtonMushroom2);
        chicken_add_btn2 = findViewById(R.id.addButtonChicken2);
        salami_add_btn2 = findViewById(R.id.addButtonSalami2);
        donair_add_btn2 = findViewById(R.id.addButtonDonair2);

        sausage_remove_btn2 = findViewById(R.id.removeButtonSausage2);
        peperoni_remove_btn2 = findViewById(R.id.removeButtonPeperoni2);
        bacon_remove_btn2 = findViewById(R.id.removeButtonBacon2);
        mushroom_remove_btn2 = findViewById(R.id.removeButtonMushroom2);
        chicken_remove_btn2 = findViewById(R.id.removeButtonChicken2);
        salami_remove_btn2 = findViewById(R.id.removeButtonSalami2);
        donair_remove_btn2 = findViewById(R.id.removeButtonDonair2);

        /** amount of toppings displays */
        amount_sausage2 = findViewById(R.id.amountSausage2);
        amount_peperoni2 = findViewById(R.id.amountPeperoni2);
        amount_bacon2 = findViewById(R.id.amountBacon2);
        amount_mushroom2 = findViewById(R.id.amountMushroom2);
        amount_chicken2 = findViewById(R.id.amountChicken2);
        amount_salami2 = findViewById(R.id.amountSalami2);
        amount_donair2 = findViewById(R.id.amountDonair2);

        /** submit order button */
        update_order = findViewById(R.id.updateOrder);

        /** customer info */
        customer_name2 = findViewById(R.id.customerName2);
        customer_address2 = findViewById(R.id.customerAddress2);
        customer_phone2 = findViewById(R.id.customerPhone2);

        /** topping labels */
        topping_sausage2 = findViewById(R.id.toppingSausage2);
        topping_peperoni2 = findViewById(R.id.toppingPeperoni2);
        topping_bacon2 = findViewById(R.id.toppingBacon2);
        topping_mushroom2 = findViewById(R.id.toppingMushroom2);
        topping_chicken2 = findViewById(R.id.toppingChicken2);
        topping_salami2 = findViewById(R.id.toppingSalami2);
        topping_donair2 = findViewById(R.id.toppingDonair2);

        /** size radio buttons */
        size_rg2 = findViewById(R.id.pizzaSizeGroup2);
        size_small_rb2 = findViewById(R.id.sizeSmall2);
        size_medium_rb2 = findViewById(R.id.sizeMedium2);
        size_large_rb2 = findViewById(R.id.sizeLarge2);

        update_info_label = findViewById(R.id.enterUpdateOrderInfo);

        back_to_orders = findViewById(R.id.backToOrderReview);

        current_order_id = findViewById(R.id.orderID_display);

    }; /* END OF LINK ALL WIDGETS METHOD*/

    /** linking the change order buttons to the listener */
    /** setting the on click listener for the add and remove buttons */
    private void linkAddRemoveBtns() {
        /** All remove and add buttons */
        sausage_add_btn2.setOnClickListener(update_toppings_btns);
        peperoni_add_btn2.setOnClickListener(update_toppings_btns);
        bacon_add_btn2.setOnClickListener(update_toppings_btns);
        mushroom_add_btn2.setOnClickListener(update_toppings_btns);
        chicken_add_btn2.setOnClickListener(update_toppings_btns);
        salami_add_btn2.setOnClickListener(update_toppings_btns);
        donair_add_btn2.setOnClickListener(update_toppings_btns);

        sausage_remove_btn2.setOnClickListener(update_toppings_btns);
        peperoni_remove_btn2.setOnClickListener(update_toppings_btns);
        bacon_remove_btn2.setOnClickListener(update_toppings_btns);
        mushroom_remove_btn2.setOnClickListener(update_toppings_btns);
        chicken_remove_btn2.setOnClickListener(update_toppings_btns);
        salami_remove_btn2.setOnClickListener(update_toppings_btns);
        donair_remove_btn2.setOnClickListener(update_toppings_btns);
    };

    /** for language changing */
    /** Method that will change the language text to Farsi or English */
    private void changeLanguage(String s) {
        /** change language display to english */
        if(s.equals("ENGLISH")) {

            /** submit order button */
            update_order.setText(R.string.UpdateOrderButton);

            /** customer info */
            customer_name2.setText(R.string.CustomerName);
            customer_address2.setText(R.string.CustomerAddress);
            customer_phone2.setText(R.string.CustomerPhoneNumber);

            /** topping labels */
            topping_sausage2.setText(R.string.ToppingSausage);
            topping_peperoni2.setText(R.string.ToppingPeperoni);
            topping_bacon2.setText(R.string.ToppingBacon);
            topping_mushroom2.setText(R.string.ToppingMushrooms);
            topping_chicken2.setText(R.string.ToppingChicken);
            topping_salami2.setText(R.string.ToppingSalami);
            topping_donair2.setText(R.string.ToppingDonair);

            /** size radio buttons */
            size_small_rb2.setText(R.string.SizeSmall);
            size_medium_rb2.setText(R.string.SizeMedium);
            size_large_rb2.setText(R.string.SizeLarge);

            onFocusChange();

            size_small_rb2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            size_medium_rb2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            size_large_rb2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            update_info_label.setText(R.string.UpdateOrderInformation);

            back_to_orders.setText(R.string.BackToOrderReview);

            /** change language display to farsi */
        } else if(s.equals("FARSI")) {

            /** submit order button */
            update_order.setText(R.string.FUpdateOrderButton);

            /** customer info */
            onFocusChangeFarsi();

            /** topping labels */
            topping_sausage2.setText(R.string.FToppingSausage);
            topping_peperoni2.setText(R.string.FToppingPeperoni);
            topping_bacon2.setText(R.string.FToppingBacon);
            topping_mushroom2.setText(R.string.FToppingMushrooms);
            topping_chicken2.setText(R.string.FToppingChicken);
            topping_salami2.setText(R.string.FToppingSalami);
            topping_donair2.setText(R.string.FToppingDonair);

            /** size radio buttons */
            size_small_rb2.setText(R.string.FSizeSmall);
            size_medium_rb2.setText(R.string.FSizeMedium);
            size_large_rb2.setText(R.string.FSizeLarge);

            size_small_rb2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            size_medium_rb2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            size_large_rb2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            update_info_label.setText(R.string.FUpdateOrderInformation);

            back_to_orders.setText(R.string.FBackToOrderReview);

        }

    };

    /** greys out the text for customer info field entries */
    private void onFocusChange() {
        customer_name2.setHint("Enter Name");
        customer_address2.setHint("Enter Address");
        customer_phone2.setHint("Enter Phone Number");
    };

    private void onFocusChangeFarsi() {
        customer_name2.setHint(R.string.FCustomerName);
        customer_address2.setHint(R.string.FCustomerAddress);
        customer_phone2.setHint(R.string.FCustomerPhoneNumber);
    };

    /** retrieves the language preference from saved language setting */
    private String loadLanguagePreference() {
        return MainActivity.LANGUAGE;
    };


    /** below are all the utility methods for input validation */

    /** checks to see if fields are not left empty */
    /** checks if at minimum one topping has been ordered, customer info
     * fields are filled out, and pizza size has been selected */
    private boolean checkOrder() {
        if(checkToppings() && checkCustomerInfo() && checkSizeIsSelected() && (topping_count == TOPPING_LIMIT)) {
            return true;
        } else {
            return false;
        }
    };

    private boolean checkToppings() {
        for (Integer i : toppings_ordered.values()) {
            if (i > 0) return true;
        }

        return false;
    };

    private boolean checkCustomerInfo() {
        if (customer_name2.getText().toString().isEmpty() ||
                customer_address2.getText().toString().isEmpty() ||
                customer_phone2.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    };

    private boolean checkSizeIsSelected() {
        if(size_rg2.getCheckedRadioButtonId() == -1) {
            return false;
        } else {
            return true;
        }
    };

    /** clears the entire order screen to default values */
    private void clearOrderScreen() {
        amount_sausage2.setText("0");
        amount_salami2.setText("0");
        amount_bacon2.setText("0");
        amount_chicken2.setText("0");
        amount_donair2.setText("0");
        amount_mushroom2.setText("0");
        amount_peperoni2.setText("0");

        customer_name2.setText("");
        customer_phone2.setText("");
        customer_address2.setText("");

        size_small_rb2.setChecked(false);
        size_large_rb2.setChecked(false);
        size_medium_rb2.setChecked(false);
    };

    /** method grabs all the toppings in a single string format */
    private String getToppings() {
        String t = "";
        for(String topping : toppings_ordered.keySet()) {
            if(toppings_ordered.get(topping) > 0) {
                t += (topping + ",").repeat(toppings_ordered.get(topping));
            }
        }

        return t;
    };

    /** gets the size of the pizza selected */
    private String getSizeSelected() {
        if(size_small_rb2.isChecked()) {
            return "SMALL";
        } else if(size_medium_rb2.isChecked()) {
            return "MEDIUM";
        } else if(size_large_rb2.isChecked()) {
            return "LARGE";
        } else {
            return "";
        }
    };

};





















