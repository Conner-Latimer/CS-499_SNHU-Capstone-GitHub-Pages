package com.cs360.mycs360application_connerlatimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button loginBtn;
    private Button signUpBtn;
    private SharedPreferences preferences;

    // Main functionality of the login screen
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Links the file to the activity_main.xml
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // When login button is pressed checks the entered username and password against a database of users
        loginBtn.setOnClickListener(v ->{
            String user = username.getText().toString();
            String pass = password.getText().toString();

            String savedPassword = preferences.getString(user, null);
            // If the saved password is not null, prints a toast message of nonmatching username
            if (savedPassword == null){
                showToast("Username does not match or does not exist");
            // if savedPassword is equal to entered password, logs user into the app
            } else if (savedPassword.equals(pass)){
                showToast("Login Successful!");
                Intent intent = new Intent(MainActivity.this, ItemDatabaseActivity.class);
                intent.putExtra("LOGGED_IN_USER", user);
                startActivity(intent);
                finish();
            } else {
                showToast("Incorrect Password entered");
            }
        });
        // Moves user to a new screen for registering a new users login information
        signUpBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    // functionality for making the Toast features
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}