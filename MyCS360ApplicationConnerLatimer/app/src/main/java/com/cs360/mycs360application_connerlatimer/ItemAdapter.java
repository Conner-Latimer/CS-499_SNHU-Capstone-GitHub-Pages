package com.cs360.mycs360application_connerlatimer;


import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class ItemAdapter extends ArrayAdapter<String[]> {
    SmsManager smsManager;
    private final Context context;
    private final ArrayList<String[]> items;
    private final DatabaseHelper db;
    private final String username;
    TextView nameTxt;
    TextView dateTxt;
    TextView qtyTxt;
    Button btnPlus;
    Button btnMinus;
    Button btnDelete;


    public ItemAdapter(Context context, ArrayList<String[]> items, DatabaseHelper db, String username) {
        super(context, R.layout.list_item, items);
        this.context = context;
        this.items = items;
        this.db = db;
        this.username = username;
    }

    // calls the overlay file of list_item.xml to place the minus, plus and delete buttons inline with item information
    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
        // Places item information into designated spots
        String[] currentItem = items.get(position);
        String name = currentItem[0];
        String date = currentItem[1];
        String qty = currentItem[2];

        // Links the variables of this file with the matching names of buttons/fields in list_item.xml
        nameTxt = convertView.findViewById(R.id.text_name);
        dateTxt = convertView.findViewById(R.id.text_date);
        qtyTxt = convertView.findViewById(R.id.text_qty);
        btnPlus = convertView.findViewById(R.id.btn_plus);
        btnMinus = convertView.findViewById(R.id.btn_minus);
        btnDelete = convertView.findViewById(R.id.btn_delete_item);
        // sets the variables for name, date, and quantity
        nameTxt.setText(name);
        dateTxt.setText(date);
        qtyTxt.setText(qty);
        // when plus button is pressed, increase quantity and update database to save the changes with newQty
        btnPlus.setOnClickListener(v -> {
            int newQty = Integer.parseInt(qty) + 1;
            db.updateQuantity(username, name, String.valueOf(newQty));
            ((ItemDatabaseActivity)context).viewData(); // Refresh list
        });

        // when minus button is pressed decrease the quantity by one and update the database to save changes with newQty
        btnMinus.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(qty);
            if (currentQty > 0) {
                int newQty = currentQty - 1;
                db.updateQuantity(username, name, String.valueOf(newQty));

                // If it hits zero, check for SMS permission and send SMS message
                if (newQty == 0) {
                    checkAndSendSms(name);
                }
                // refreshes the screen and updates the display of the quantity
                ((ItemDatabaseActivity)context).viewData();
            }
        });
        // calls viewData to refresh the data displayed in the ListView and deletes the item from the database and screen
        btnDelete.setOnClickListener(v -> {
            db.deleteData(username, name);
            ((ItemDatabaseActivity)context).viewData();
        });
        return convertView;
    }

    // Sends out of stock SMS alert to a phone number entered by the user
    private void checkAndSendSms(String itemName) {
        SharedPreferences prefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        boolean isSmsEnabled = prefs.getBoolean("sms_enabled", false);
        String phoneNumber = prefs.getString("alert_phone", "");
        // If the sms button is enabled/permission granted, AND the phone number field is not empty, will send the alert message
        if (isSmsEnabled && !phoneNumber.isEmpty()) {
            try {
                smsManager = context.getSystemService(SmsManager.class);
                smsManager.sendTextMessage(phoneNumber, null,
                        "Alert: " + itemName + " is now out of stock!", null, null);
                showToast("Low stock SMS sent to ");
            } catch (Exception e) {
                showToast("SMS Failed: Check permissions.");
            }
        }
    }

    private void showToast(String message){
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}