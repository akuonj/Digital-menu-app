package com.example.fms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

    private TextView welcomeTextView;
    private Button startOrderButton;
    private Button logoutButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeTextView = findViewById(R.id.textView);
        startOrderButton = findViewById(R.id.buttonStartOrder);
        logoutButton = findViewById(R.id.buttonLogout);

        // Retrieve the username from the intent extras
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");

        // Display the username
        if (username != null) {
            welcomeTextView.setText("Welcome, " + username + "!");
        } else {
            welcomeTextView.setText("Welcome!");
        }

        // Set up button click listener for start order button
        startOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent makeOrderIntent = new Intent(WelcomeActivity.this, MealsActivity.class);
                makeOrderIntent.putExtra("USERNAME", username); // Pass username to MealsActivity
                startActivity(makeOrderIntent);
            }
        });

        // Set up button click listener for logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the current activity stack and start the login activity
                Intent logoutIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                finish();
            }
        });
    }
}
