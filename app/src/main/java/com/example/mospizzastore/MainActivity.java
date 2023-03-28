package com.example.mospizzastore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    /** database interface object */
    private DBInterface db_interface = new DBInterface(MainActivity.this);

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



    /** shared preferences object */
    private SharedPreferences shared_preferences;

    /** All remove and add buttons */
    private Button sausage_add_btn;
    private Button peperoni_add_btn;
    private Button bacon_add_btn;
    private Button mushroom_add_btn;
    private Button chicken_add_btn;
    private Button salami_add_btn;
    private Button donair_add_btn;

    private Button sausage_remove_btn;
    private Button peperoni_remove_btn;
    private Button bacon_remove_btn;
    private Button mushroom_remove_btn;
    private Button chicken_remove_btn;
    private Button salami_remove_btn;
    private Button donair_remove_btn;

    /** Language change switch */
    private Switch change_lang_switch;

    /** amount of toppings displays */
    private TextView amount_sausage;
    private TextView amount_peperoni;
    private TextView amount_bacon;
    private TextView amount_mushroom;
    private TextView amount_chicken;
    private TextView amount_salami;
    private TextView amount_donair;

    /** Customer info text fields */
    private TextView customer_name;
    private TextView customer_address;
    private TextView customer_phone;

    /** submit order button */
    private Button submit_order;

    /** Topping labels */
    private TextView topping_sausage;
    private TextView topping_peperoni;
    private TextView topping_bacon;
    private TextView topping_mushroom;
    private TextView topping_chicken;
    private TextView topping_salami;
    private TextView topping_donair;

    /** enter pizza info label */
    private TextView enter_info_label;

    /** pizza size radio buttons */
    private RadioGroup size_rg;
    private RadioButton size_small_rb;
    private RadioButton size_medium_rb;
    private RadioButton size_large_rb;

    private String size = "";

    /** language preference setting */
    public static final String LANGUAGE_PREFERENCE = "LANG_KEY";
    public static String LANGUAGE;

    /** order search button */
    private Button order_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        link_all_widgets();
        linkAddRemoveBtns();

        /** change language switch button */
        change_lang_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button_view, boolean is_checked) {
                shared_preferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = shared_preferences.edit();
                if(is_checked) {
                    LANGUAGE = "FARSI";
                    changeLanguage(LANGUAGE);
                    editor.putString(LANGUAGE_PREFERENCE, LANGUAGE);
                } else {
                    LANGUAGE = "ENGLISH";
                    changeLanguage(LANGUAGE);
                    editor.putString(LANGUAGE_PREFERENCE, LANGUAGE);
                }
                editor.apply();
            }
        });


        /** getting the saved language setting */
        shared_preferences = getPreferences(MODE_PRIVATE);
        LANGUAGE = shared_preferences.getString(LANGUAGE_PREFERENCE, "ENGLISH");
        changeLanguage(LANGUAGE);

        /** submitting an order */
        submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkOrder()) {

                    db_interface.submitOrder(customer_name.getText().toString(),
                                            customer_address.getText().toString(),
                                            customer_phone.getText().toString(),
                                            getToppings(),
                                            getSizeSelected());

                    clearOrderScreen();
                    Toast order = Toast.makeText(MainActivity.this, "Order Submitted", Toast.LENGTH_LONG);
                    order.show();
                } else {
                    /** if there are no toppings, customer info is missing, or size is
                     * not selected */
                    Toast pop = Toast.makeText(MainActivity.this, "Missing info!", Toast.LENGTH_LONG);
                    pop.show();
                }
            };
        });

        /** searching for an order */
        order_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(i);
            };
        });

    };


    private int topping_count = 0;
    /** simple utility function to change the topping count display */
    private void changeCountDisplay(TextView t, boolean add, String topping_key) {
        if (add) {
            if(topping_count >= 3) {
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
            if(topping_count <= 0 || t.getText().toString().equals("0")) {
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

    };

    /** Toppings button listener */
    public View.OnClickListener toppings_btns = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                /** add buttons */
                case R.id.addButtonSausage:
                    changeCountDisplay(amount_sausage, true, "SAUSAGE");
                    break;
                case R.id.addButtonBacon:
                    changeCountDisplay(amount_bacon, true, "BACON");
                    break;
                case R.id.addButtonChicken:
                    changeCountDisplay(amount_chicken, true, "CHICKEN");
                    break;
                case R.id.addButtonDonair:
                    changeCountDisplay(amount_donair, true, "DONAIR");
                    break;
                case R.id.addButtonSalami:
                    changeCountDisplay(amount_salami, true, "SALAMI");
                    break;
                case R.id.addButtonMushroom:
                    changeCountDisplay(amount_mushroom, true, "MUSHROOM");
                    break;
                case R.id.addButtonPeperoni:
                    changeCountDisplay(amount_peperoni, true, "PEPERONI");
                    break;

                /** remove buttons */
                case R.id.removeButtonSausage:
                    changeCountDisplay(amount_sausage, false, "SAUSAGE");
                    break;
                case R.id.removeButtonBacon:
                    changeCountDisplay(amount_bacon, false, "BACON");
                    break;
                case R.id.removeButtonChicken:
                    changeCountDisplay(amount_chicken, false, "CHICKEN");
                    break;
                case R.id.removeButtonDonair:
                    changeCountDisplay(amount_donair, false, "DONAIR");
                    break;
                case R.id.removeButtonSalami:
                    changeCountDisplay(amount_salami, false, "SALAMI");
                    break;
                case R.id.removeButtonMushroom:
                    changeCountDisplay(amount_mushroom, false, "MUSHROOM");
                    break;
                case R.id.removeButtonPeperoni:
                    changeCountDisplay(amount_peperoni, false, "PEPERONI");
                    break;
                default:
                    break;
            }
        };
    };


    /** Method that will change the language text to Farsi or English */
    private void changeLanguage(String s) {
        /** change language display to english */
        if(s.equals("ENGLISH")) {
            /** Language change switch */
            change_lang_switch.setText(getString(R.string.LanguageToggleLabel));

            /** submit order button */
            submit_order.setText(R.string.SubmitOrder);

            /** search order button */
            order_search.setText(R.string.SearchOrder);

            /** customer info */
            customer_name.setText(R.string.CustomerName);
            customer_address.setText(R.string.CustomerAddress);
            customer_phone.setText(R.string.CustomerPhoneNumber);

            /** topping labels */
            topping_sausage.setText(R.string.ToppingSausage);
            topping_peperoni.setText(R.string.ToppingPeperoni);
            topping_bacon.setText(R.string.ToppingBacon);
            topping_mushroom.setText(R.string.ToppingMushrooms);
            topping_chicken.setText(R.string.ToppingChicken);
            topping_salami.setText(R.string.ToppingSalami);
            topping_donair.setText(R.string.ToppingDonair);

            /** size radio buttons */
            size_small_rb.setText(R.string.SizeSmall);
            size_medium_rb.setText(R.string.SizeMedium);
            size_large_rb.setText(R.string.SizeLarge);

            onFocusChange();

            size_small_rb.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            size_medium_rb.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            size_large_rb.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            enter_info_label.setText(R.string.EnterInfoLabel);

        /** change language display to farsi */
        } else if(s.equals("FARSI")) {
            /** Language change switch */
            change_lang_switch.setText(getString(R.string.FLanguageToggleLabel));

            /** submit order button */
            submit_order.setText(R.string.FSubmitOrder);

            /** search order button */
            order_search.setText(R.string.FSearchOrder);

            /** customer info */
            onFocusChangeFarsi();

            /** topping labels */
            topping_sausage.setText(R.string.FToppingSausage);
            topping_peperoni.setText(R.string.FToppingPeperoni);
            topping_bacon.setText(R.string.FToppingBacon);
            topping_mushroom.setText(R.string.FToppingMushrooms);
            topping_chicken.setText(R.string.FToppingChicken);
            topping_salami.setText(R.string.FToppingSalami);
            topping_donair.setText(R.string.FToppingDonair);

            /** size radio buttons */
            size_small_rb.setText(R.string.FSizeSmall);
            size_medium_rb.setText(R.string.FSizeMedium);
            size_large_rb.setText(R.string.FSizeLarge);

            size_small_rb.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            size_medium_rb.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            size_large_rb.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            enter_info_label.setText(R.string.FEnterInfoLabel);
        }

    };


    /** Utility methods */

    /** linking all widgets */
    private void link_all_widgets() {
        /** All remove and add buttons */
        sausage_add_btn = findViewById(R.id.addButtonSausage);
        peperoni_add_btn = findViewById(R.id.addButtonPeperoni);
        bacon_add_btn = findViewById(R.id.addButtonBacon);
        mushroom_add_btn = findViewById(R.id.addButtonMushroom);
        chicken_add_btn = findViewById(R.id.addButtonChicken);
        salami_add_btn = findViewById(R.id.addButtonSalami);
        donair_add_btn = findViewById(R.id.addButtonDonair);

        sausage_remove_btn = findViewById(R.id.removeButtonSausage);
        peperoni_remove_btn = findViewById(R.id.removeButtonPeperoni);
        bacon_remove_btn = findViewById(R.id.removeButtonBacon);
        mushroom_remove_btn = findViewById(R.id.removeButtonMushroom);
        chicken_remove_btn = findViewById(R.id.removeButtonChicken);
        salami_remove_btn = findViewById(R.id.removeButtonSalami);
        donair_remove_btn = findViewById(R.id.removeButtonDonair);

        /** Language change switch */
        change_lang_switch = findViewById(R.id.languageChangeSwitch);

        /** amount of toppings displays */
        amount_sausage = findViewById(R.id.amountSausage);
        amount_peperoni = findViewById(R.id.amountPeperoni);
        amount_bacon = findViewById(R.id.amountBacon);
        amount_mushroom = findViewById(R.id.amountMushroom);
        amount_chicken = findViewById(R.id.amountChicken);
        amount_salami = findViewById(R.id.amountSalami);
        amount_donair = findViewById(R.id.amountDonair);

        /** submit order button */
        submit_order = findViewById(R.id.submitOrder);

        /** search for an order */
        order_search = findViewById(R.id.searchOrder);

        /** customer info */
        customer_name = findViewById(R.id.customerName);
        customer_address = findViewById(R.id.customerAddress);
        customer_phone = findViewById(R.id.customerPhone);

        /** topping labels */
        topping_sausage = findViewById(R.id.toppingSausage);
        topping_peperoni = findViewById(R.id.toppingPeperoni);
        topping_bacon = findViewById(R.id.toppingBacon);
        topping_mushroom = findViewById(R.id.toppingMushroom);
        topping_chicken = findViewById(R.id.toppingChicken);
        topping_salami = findViewById(R.id.toppingSalami);
        topping_donair = findViewById(R.id.toppingDonair);

        /** size radio buttons */
        size_rg = findViewById(R.id.pizzaSizeGroup);
        size_small_rb = findViewById(R.id.sizeSmall);
        size_medium_rb = findViewById(R.id.sizeMedium);
        size_large_rb = findViewById(R.id.sizeLarge);

        enter_info_label = findViewById(R.id.enterPizzaInfoLabel);

    };

    /** setting the on click listener for the add and remove buttons */
    private void linkAddRemoveBtns() {
        /** All remove and add buttons */
        sausage_add_btn.setOnClickListener(toppings_btns);
        peperoni_add_btn.setOnClickListener(toppings_btns);
        bacon_add_btn.setOnClickListener(toppings_btns);
        mushroom_add_btn.setOnClickListener(toppings_btns);
        chicken_add_btn.setOnClickListener(toppings_btns);
        salami_add_btn.setOnClickListener(toppings_btns);
        donair_add_btn.setOnClickListener(toppings_btns);

        sausage_remove_btn.setOnClickListener(toppings_btns);
        peperoni_remove_btn.setOnClickListener(toppings_btns);
        bacon_remove_btn.setOnClickListener(toppings_btns);
        mushroom_remove_btn.setOnClickListener(toppings_btns);
        chicken_remove_btn.setOnClickListener(toppings_btns);
        salami_remove_btn.setOnClickListener(toppings_btns);
        donair_remove_btn.setOnClickListener(toppings_btns);
    };

    /** greys out the text for customer info field entries */
    public void onFocusChange() {
        customer_name.setHint("Enter Name");
        customer_address.setHint("Enter Address");
        customer_phone.setHint("Enter Phone Number");
    };

    public void onFocusChangeFarsi() {
        customer_name.setHint(R.string.FCustomerName);
        customer_address.setHint(R.string.FCustomerAddress);
        customer_phone.setHint(R.string.FCustomerPhoneNumber);
    };


    /** checks if at minimum one topping has been ordered, customer info
     * fields are filled out, and pizza size has been selected */
    private boolean checkOrder() {
        if(checkToppings() && checkCustomerInfo() && checkSizeIsSelected()) {
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
        if (customer_name.getText().toString().isEmpty() ||
                customer_address.getText().toString().isEmpty() ||
                customer_phone.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    };

    private boolean checkSizeIsSelected() {
        if(size_rg.getCheckedRadioButtonId() == -1) {
            return false;
        } else {
            return true;
        }
    };

    /** clears the entire order screen to default values */
    private void clearOrderScreen() {
        amount_sausage.setText("0");
        amount_salami.setText("0");
        amount_bacon.setText("0");
        amount_chicken.setText("0");
        amount_donair.setText("0");
        amount_mushroom.setText("0");
        amount_peperoni.setText("0");

        customer_name.setText("");
        customer_phone.setText("");
        customer_address.setText("");

        size_small_rb.setChecked(false);
        size_large_rb.setChecked(false);
        size_medium_rb.setChecked(false);
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
        if(size_small_rb.isChecked()) {
            return "SMALL";
        } else if(size_medium_rb.isChecked()) {
            return "MEDIUM";
        } else if(size_large_rb.isChecked()) {
            return "LARGE";
        } else {
            return "";
        }
    };

};












