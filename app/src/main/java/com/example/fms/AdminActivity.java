package com.example.fms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fms.R;

public class AdminActivity extends Activity {
    private Button btnRegisterLink, btnPendingOrders, btnServedOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize buttons
        btnRegisterLink = findViewById(R.id.textViewRegisterLink);
        btnPendingOrders = findViewById(R.id.btnPendingOrders);
        btnServedOrders = findViewById(R.id.btnServedOrders);

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
                // Navigate to the PendingOrderActivity
                Intent intent = new Intent(AdminActivity.this, PendingOrderActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the served orders button
        btnServedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the ServedBreakfastActivity
                Intent intent = new Intent(AdminActivity.this, ServedBreakfastActivity.class);
                startActivity(intent);
            }
        });
    }
}