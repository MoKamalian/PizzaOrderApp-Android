/**
 *
 * author: amir kamalian
 * date:   17 Mar 2023
 *
 *
 */


package com.example.mospizzastore;


import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.database.SQLException;


public class DBAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "customerName";
    public static final String KEY_ADDRESS = "customerAddress";
    public static final String KEY_PHONE = "customerPhoneNumber";
    public static final String KEY_TOPPINGS = "toppings";
    public static final String KEY_SIZE = "size";

    public static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MosPizzaDB.db";
    private static final String DATABASE_TABLE = "Orders";
    private static final int DATABASE_VERSION = 2;

    /** creation of the database command */
    private static final String DATABASE_CREATE =
            "CREATE TABLE Orders(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "customerName TEXT not null,customerAddress TEXT not null,"
            + "customerPhoneNumber TEXT not null,toppings TEXT not null,"
            + "size TEXT not null)";


    private final Context context;
    private DBHelper db_helper;
    private SQLiteDatabase db;

    /** constructor */
    public DBAdapter(Context context) {
        this.context = context;
        db_helper = new DBHelper(context);
    };


    private static class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        };


        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        };


        @Override
        public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
            Log.w(TAG, "Upgrade database from version " + old_version +
                    " to " + new_version + ", note this will destroy all old data.");
            db.execSQL("DROP TABLE IF EXISTS Orders");
            onCreate(db);
        };

    }; /* END DBHelper class */


    /** opening the database */
    public DBAdapter open() throws SQLException {
        db = db_helper.getWritableDatabase();
        return this;
    };

    /** closing the database */
    public void close() {
        db_helper.close();
    };

    /** insert an order into the database */
    public long inserOrder(String c_name, String c_address, String c_phone, String toppings,
                           String size) {
        ContentValues content_values = new ContentValues();
        content_values.put(KEY_NAME, c_name);
        content_values.put(KEY_ADDRESS, c_address);
        content_values.put(KEY_PHONE, c_phone);
        content_values.put(KEY_TOPPINGS, toppings);
        content_values.put(KEY_SIZE, size);

        return db.insert(DATABASE_TABLE, null, content_values);
    };

    /** deleting a pizza order */
    public boolean deleteOrder(String order_id) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + " = " + "'" + order_id + "'", null) > 0;
    };


    /** retrieve all the pizzas ordered under a specific name */
    public Cursor getAllOrders(String name) {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_NAME,KEY_ADDRESS,
                        KEY_PHONE,KEY_TOPPINGS,KEY_SIZE},KEY_NAME + " = " + "'" + name + "'",null,null,null,null);
    };

    /** getting a single order from the orders table */
    public Cursor getOrder(long order_id) throws SQLException {
        Cursor cursor = db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,KEY_NAME,KEY_ADDRESS,
                KEY_PHONE,KEY_TOPPINGS,KEY_SIZE},KEY_ROWID + "=" + order_id, null, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    };

    /** updating a pizza order */
    public boolean updateOrder(long order_id, String c_name, String c_address, String c_phone, String toppings,
                                 String size) {
        ContentValues c_values = new ContentValues();
        c_values.put(KEY_NAME, c_name);
        c_values.put(KEY_ADDRESS, c_address);
        c_values.put(KEY_PHONE, c_phone);

        return db.update(DATABASE_TABLE, c_values, KEY_ROWID + "=" + order_id, null) > 0;
    };



}; /* END OF ADAPTER CLASS */








