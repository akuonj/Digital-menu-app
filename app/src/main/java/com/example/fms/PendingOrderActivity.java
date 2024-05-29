package com.example.fms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hospitalfood.R;

public class PendingOrderActivity extends Activity {
    Button breakfast, lunch, dinner;
    TextView tvOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        // Initialize your buttons and TextView using findViewById
        breakfast = findViewById(R.id.btnViewbreakfastOrders);
        lunch = findViewById(R.id.btnViewLunchOrders);
        dinner = findViewById(R.id.btnViewDinnerOrders);
        tvOrders = findViewById(R.id.tvOrders);

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PendingOrderActivity.this, PendingBOrderActivity.class);
                startActivity(intent);
            }
        });

    }
}