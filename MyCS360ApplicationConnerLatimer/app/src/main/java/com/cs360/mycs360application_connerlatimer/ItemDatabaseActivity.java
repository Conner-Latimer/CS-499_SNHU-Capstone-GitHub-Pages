package com.cs360.mycs360application_connerlatimer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ItemDatabaseActivity extends AppCompatActivity {
    EditText editItemName, editDate, editQuantity;
    Button btnAdd;
    Button btnSettings;
    ListView itemList;
    DatabaseHelper myDb;
    ArrayList<String[]> listItem;
    ItemAdapter adapter;
    String currentUsername;

    // Controls the functionality of adding items to a database, deleting, or changing the quantity of it
    // Also allows a screen change to the SMS settings screen to allow for SMS messages
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Links the activity_item_database.xml to this class
        setContentView(R.layout.activity_item_database);
        currentUsername = getIntent().getStringExtra("LOGGED_IN_USER");
        editItemName = findViewById(R.id.edit_text_item);
        editDate = findViewById(R.id.edit_text_date);
        editQuantity = findViewById(R.id.edit_text_quantity);
        btnAdd = findViewById(R.id.button_add);
        btnSettings = findViewById(R.id.button_settings);
        itemList = findViewById(R.id.item_list);

        // Creates a new database and ArrayList
        myDb = new DatabaseHelper(this);
        listItem = new ArrayList<>();

        viewData();
        // Adds items to the database
        btnAdd.setOnClickListener(v -> {
            String name = editItemName.getText().toString();
            String date = editDate.getText().toString();
            String qty = editQuantity.getText().toString();
            // Adds item IF the items name field is not empty AND is insertData is not false
            if (!name.isEmpty() && myDb.insertData(currentUsername, name, date, qty)) {
                viewData();
                editItemName.setText("");
                editDate.setText("");
                editQuantity.setText("");
                showToast("Item added");
            }
            else {
                showToast("Error adding item");
            }
        });
        // Changes screen to SettingsSms.xml if settings button is pressed
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(ItemDatabaseActivity.this, SettingsSms.class);
            startActivity(intent);
        });
    }

    // Populate a ListView with data from the database
    public void viewData() {
        // clears old data and allows for a new loading of data
        listItem.clear();
        // Retrieves a cursor from getAllData to load rows of data
        Cursor cursor = myDb.getAllData(currentUsername);
        // If cursor IS NOT null, WHILE cursor can keep moving/hasnt reached the end
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Store as {Name, Date, Quantity} and adds it to the ListView
                listItem.add(new String[]{cursor.getString(2), cursor.getString(3), cursor.getString(4)});
            }
            // frees up system resources by closing the cursor
            cursor.close();
        }
        // passes listItem and binds it to the UI component of the ListView
        adapter = new ItemAdapter(this, listItem, myDb, currentUsername);
        itemList.setAdapter(adapter);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}