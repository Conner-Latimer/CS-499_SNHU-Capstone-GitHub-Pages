package com.cs360.mycs360application_connerlatimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// DatabaseHelper.java
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Inventory.db";
    private static final String TABLE_NAME = "items_table";
    private static final String COL_ID = "ID";
    private static final String COL_USER = "USERNAME";
    private static final String COL_NAME = "NAME";
    private static final String COL_DATE = "DATE";
    private static final String COL_QTY = "QUANTITY";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    // onCreate that creates a SQLLite database that will store items
    // Auto increments Item ID to fit into a listview table in activity_itemdatabase.xml
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER + " TEXT, " +
                COL_NAME + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_QTY + " INTEGER)");    }

    //
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Takes user login username and enter the item being added into the users own unique database
    // Put an items name, date, and quantity into the database that matches the users username
    public boolean insertData(String username, String name, String date, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER, username);
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_QTY, qty);
        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    // Retrieves all data
    // Passes username to getAllData and loads the corresponding database for the user
    public Cursor getAllData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USER + " = ?", new String[]{username});
    }

    // takes username and updates the quantity of an item and updates the SQLite database to reflect anytime the user reopens the application
    public boolean updateQuantity(String username, String name, String newQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_QTY, newQty);
        long result = db.update(TABLE_NAME, contentValues, COL_USER + " = ? AND " + COL_NAME + " = ?", new String[]{username, name});
        return result > 0;
    }

    // Deletes items form database when passed username and name of the item and writes it to the SQLite database
    public Integer deleteData(String username, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_USER + " = ? AND " + COL_NAME + " = ?", new String[]{username, name});
    }
}