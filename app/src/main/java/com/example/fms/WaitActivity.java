package com.example.fms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WaitActivity extends Activity {
    private Button wtbutton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        wtbutton = findViewById(R.id.wtbutton);

        // Retrieve the username from the intent extras
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USERNAME")) {
            username = intent.getStringExtra("USERNAME");
        } else {
            Toast.makeText(WaitActivity.this, "Username is not provided", Toast.LENGTH_SHORT).show();
            finish(); // Exit the activity if username is null
            return;
        }

        // Set up button click listener
        wtbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent welcomeIntent = new Intent(WaitActivity.this, WelcomeActivity.class);
                welcomeIntent.putExtra("USERNAME", username); // Pass username to WelcomeActivity
                startActivity(welcomeIntent);
            }
        });
    }
}
