package com.cs360.mycs360application_connerlatimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText regUsername, regPassword, regConfirmPassword;
    private Button signUpBtn, backToLogin;
    private SharedPreferences preferences;

    // Controls the user registering a new account
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        regUsername = findViewById(R.id.reg_username);
        regPassword = findViewById(R.id.reg_password);
        regConfirmPassword = findViewById(R.id.reg_confirmPassword);
        signUpBtn = findViewById(R.id.signUpBtn);
        backToLogin = findViewById(R.id.backToLogin);
        preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // When signUpBtn is pressed, takes text in input fields
        signUpBtn.setOnClickListener(v -> {
            String user = regUsername.getText().toString();
            String pass = regPassword.getText().toString();
            String confirm = regConfirmPassword.getText().toString();
            // Checks to see if username OR password is empty
            if (user.isEmpty() || pass.isEmpty()) {
                showToast("Please fill all fields");
           // Checks to see if password matches the confirmation password field
            } else if (!pass.equals(confirm)) {
                showToast("Passwords do not match");
            } else {
                // Save to SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(user, pass);
                editor.apply();

                showToast("Account Created Successfully");
                // Returns back to MainActivity screen
                finish();
            }
        });
        // if backToLogin is pressed, finishes screen and goes back to login screen
        backToLogin.setOnClickListener(v -> finish());
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}