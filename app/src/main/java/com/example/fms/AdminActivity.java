package com.example.fms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends Activity {
    private Button btnRegisterLink, btnPendingOrders, btnServedOrders, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize buttons
        btnRegisterLink = findViewById(R.id.textViewRegisterLink);
        btnPendingOrders = findViewById(R.id.btnPendingOrders);
        btnServedOrders = findViewById(R.id.btnServedOrders);
        btnLogout = findViewById(R.id.btnLogout);

        // Set click listener for the register link button
        btnRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the RegistrationActivity
                Intent intent = new Intent(AdminActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the pending orders button
        btnPendingOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the PendingMealsOrderActivity
                Intent intent = new Intent(AdminActivity.this, PendingMealsOrderActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the served orders button
        btnServedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the ServedMealActivity
                Intent intent = new Intent(AdminActivity.this, ServedMealActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the WelcomeActivity
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
