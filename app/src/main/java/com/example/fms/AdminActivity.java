package com.example.fms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.hospitalfood.R;
import com.google.android.material.tabs.TabLayout;

public class AdminActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private AdminPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize views
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        // Set up ViewPager
        pagerAdapter = new AdminPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Connect the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Handle tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        // Handle tab 1 selection
                        break;
                    case 1:
                        // Handle tab 2 selection
                        startActivity(new Intent(AdminActivity.this, RegistrationActivity.class));
                        break;
                    case 2:
                        // Handle tab 3 selection
                        startActivity(new Intent(AdminActivity.this, PendingOrderActivity.class));
                        break;
                    case 3:
                        // Handle tab 4 selection
                        startActivity(new Intent(AdminActivity.this, ServedBreakfastActivity.class));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselected
            }
        });
    }
}
