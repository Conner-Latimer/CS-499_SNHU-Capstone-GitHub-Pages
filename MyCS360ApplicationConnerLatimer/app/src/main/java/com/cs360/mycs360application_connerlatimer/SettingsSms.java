package com.cs360.mycs360application_connerlatimer;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

public class SettingsSms extends AppCompatActivity {
    private SwitchCompat smsSwitch;
    private SharedPreferences preferences;
    private EditText editPhone;
    private Button btnSave;
    private Button btnBack;

    // Controls button functionality for transitions and activities
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_layout);

        smsSwitch = findViewById(R.id.sms_switch);
        editPhone = findViewById(R.id.edit_phone_number);
        btnSave = findViewById(R.id.btn_save_settings);
        btnBack = findViewById(R.id.btnBack);

        preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        // popup for is sms is enabled when moving to the screen based on stored information in SharedPrefrences
        smsSwitch.setChecked(preferences.getBoolean("sms_enabled", false));
        editPhone.setText(preferences.getString("alert_phone", ""));

        // Load saved state
        boolean isSmsEnabled = preferences.getBoolean("sms_enabled", false);
        smsSwitch.setChecked(isSmsEnabled);

        // if switch is pressed, then asks for permission to enable it
        smsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                requestSmsPermission();
            }
            // Save preference for user settings
            preferences.edit().putBoolean("sms_enabled", isChecked).apply();
            String status = isChecked ? "SMS Notifications Enabled" : "SMS Notifications Disabled";
            showToast(status);
        });

        // When btnSave is pressed, take phone number and sees if the switch is on or off
        btnSave.setOnClickListener(v -> {
            String phone = editPhone.getText().toString();
            boolean isEnabled = smsSwitch.isChecked();
            // map user data to specific keys and saves the changes
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("sms_enabled", isEnabled);
            editor.putString("alert_phone", phone);
            editor.apply();

            showToast("Settings Saved");
            finish(); // Optional: Close settings and go back
        });

        // when btnBack is pressed, closes settings screen back to inventory screen
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    // checks to see if app already has the SEND_SMS permission
    private void requestSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}