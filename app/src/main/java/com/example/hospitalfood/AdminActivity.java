package com.example.hospitalfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AdminActivity extends Activity {
    private Button textViewRegisterLink, btnPendingOrders, btnServedOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        textViewRegisterLink = findViewById(R.id.textViewRegisterLink);
        btnPendingOrders = findViewById(R.id.btnPendingOrders);
        btnServedOrders = findViewById(R.id.btnServedOrders);

        textViewRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnPendingOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        btnServedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, DeletedActivity.class);
                startActivity(intent);
            }
        });
    }
}
