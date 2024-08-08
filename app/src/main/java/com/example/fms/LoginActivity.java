package com.example.fms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    private DMDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        dbHelper = new DMDatabaseHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Validate input
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username and password are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Authenticate user using dbHelper
                boolean isAuthenticated = dbHelper.authenticateUser(username, password, LoginActivity.this);

                if (isAuthenticated) {
                    Intent intent;
                    if (username.equals("admin")) {
                        // Redirect to admin activity
                        intent = new Intent(LoginActivity.this, AdminActivity.class);
                    } else {
                        // Redirect to welcome activity for regular users
                        intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        // Pass the username to WelcomeActivity
                        intent.putExtra("USERNAME", username);
                    }
                    startActivity(intent);
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
