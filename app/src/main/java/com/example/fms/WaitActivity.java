package com.example.fms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hospitalfood.R;

public class WaitActivity extends Activity {
    private Button wtbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        wtbutton = findViewById(R.id.wtbutton);

        wtbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaitActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
