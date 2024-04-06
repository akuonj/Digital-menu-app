package com.example.fms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

import com.example.hospitalfood.R;


public class DinnerActivity extends Activity {
    Button back, ok;
    AutoCompleteTextView etdSuop, etdSalad, etdVegeterian, etdNonvegeterian, etdStarch, etdDesert;

    private String[] soupOptions = {"Pumpkin soup"};
    private String[] saladOptions = {"Cobb salad"};
    private String[] vegeterianOptions = {"Potato methi and Peas stew", "Potato methi and whole moong curry"};
    private String[] nonvegeterianOptions = {"Roast leg of lamb with mint sauce", "Catch of the day perch fillet with dill and tomato sauce"};
    private String[] starchOptions = {"Rosemary potatoes", "Roti", "Fried rice", "Steamed rice"};
    private String[] desertOptions = {"Fruit slices", "Apple and cannamon crumble with whipping cream"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner);

        back = findViewById(R.id.btdBack);
        ok = findViewById(R.id.btdOk);

        etdSuop = findViewById(R.id.etdSuop);
        etdSalad = findViewById(R.id.etdSalad);
        etdVegeterian = findViewById(R.id.etdVegeterian);
        etdNonvegeterian = findViewById(R.id.etdNonvegeterian);
        etdStarch = findViewById(R.id.etdStarch);
        etdDesert = findViewById(R.id.etdDesert);


        ArrayAdapter<String> soupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, soupOptions);
        etdSuop.setAdapter(soupAdapter);
        etdSuop.setThreshold(1);

        ArrayAdapter<String> saladAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, saladOptions);
        etdSalad.setAdapter(saladAdapter);
        etdSalad.setThreshold(1);

        ArrayAdapter<String> vegeterianAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vegeterianOptions);
        etdVegeterian.setAdapter(vegeterianAdapter);
        etdVegeterian.setThreshold(1);

        ArrayAdapter<String> nonvegeterianAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nonvegeterianOptions);
        etdNonvegeterian.setAdapter(nonvegeterianAdapter);
        etdNonvegeterian.setThreshold(1);

        ArrayAdapter<String> starchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, starchOptions);
        etdStarch.setAdapter(starchAdapter);
        etdStarch.setThreshold(1);

        ArrayAdapter<String> desertAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, desertOptions);
        etdDesert.setAdapter(desertAdapter);
        etdDesert.setThreshold(1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DinnerActivity.this, SelectMealActivity.class);
                startActivity(intent);
                finish();

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSoup = etdSuop.getText().toString();
                String selectedSalad = etdSalad.getText().toString();
                String selectedVegetarian = etdVegeterian.getText().toString();
                String selectedNonVegetarian = etdNonvegeterian.getText().toString();
                String selectedStarch = etdStarch.getText().toString();
                String selectedDessert = etdDesert.getText().toString();

                FMSDatabaseHelper dbHelper = new FMSDatabaseHelper(DinnerActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("Soup", selectedSoup);
                values.put("Salad", selectedSalad);
                values.put("Vegetarian", selectedVegetarian);
                values.put("NonVegetarian", selectedNonVegetarian);
                values.put("Starch", selectedStarch);
                values.put("Dessert", selectedDessert);

                long newRowId = db.insert("Dinner", null, values);
                if (newRowId != -1) {
                    Toast.makeText(DinnerActivity.this, "Dinner order saved successfully!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DinnerActivity.this, "Failed to save dinner order.", Toast.LENGTH_LONG).show();
                }

                db.close();

                Intent intent = new Intent(DinnerActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });


        etdSuop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etdSuop.showDropDown();
            }
        });

        etdSalad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etdSalad.showDropDown();
            }
        });

        etdVegeterian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etdVegeterian.showDropDown();
            }
        });

        etdNonvegeterian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etdNonvegeterian.showDropDown();
            }
        });

        etdStarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etdStarch.showDropDown();
            }
        });

        etdDesert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etdDesert.showDropDown();
            }
        });


    }
}


