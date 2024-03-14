package com.example.hospitalfood;

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


public class LunchActivity extends Activity {
    Button back, ok;
    AutoCompleteTextView etlSuop, etlSalad, etlVegeterian, etlBlended, etlNonvegeterian, etlStarch, etlDesert;

    private String[] soupOptions = {"Cream of vegetable soup"};
    private String[] saladOptions = {"Mixed grilled salad"};
    private String[] vegeterianOptions = {"Okra curry", "Authentic Beans Stew"};
    private String[] blendedOptions = {"Blended vegeterian", "NOn-vegeterian"};
    private String[] nonvegeterianOptions = {"Chicken satay (peanut sauce)", "Beef fillet migon (mushroom sauce)"};
    private String[] starchOptions = {"Risi bisi rice", "White rice", "Brown chapati", "Potato mash"};
    private String[] desertOptions = {"Fruit salad", "Tropical juice with mixed fruit compote"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);

        back = findViewById(R.id.btlBack);
        ok = findViewById(R.id.btlOk);

        etlSuop = findViewById(R.id.etlSuop);
        etlSalad = findViewById(R.id.etlSalad);
        etlVegeterian = findViewById(R.id.etlVegeterian);
        etlBlended = findViewById(R.id.etlBlended);
        etlNonvegeterian = findViewById(R.id.etlNonvegeterian);
        etlStarch = findViewById(R.id.etlStarch);
        etlDesert = findViewById(R.id.etlDesert);


        ArrayAdapter<String> soupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, soupOptions);
        etlSuop.setAdapter(soupAdapter);
        etlSuop.setThreshold(1);

        ArrayAdapter<String> saladAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, saladOptions);
        etlSalad.setAdapter(saladAdapter);
        etlSalad.setThreshold(1);

        ArrayAdapter<String> vegeterianAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vegeterianOptions);
        etlVegeterian.setAdapter(vegeterianAdapter);
        etlVegeterian.setThreshold(1);

        ArrayAdapter<String> blendedAdapter =new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, blendedOptions);
        etlBlended.setAdapter(blendedAdapter);
        etlBlended.setThreshold(1);

        ArrayAdapter<String> nonvegeterianAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nonvegeterianOptions);
        etlNonvegeterian.setAdapter(nonvegeterianAdapter);
        etlNonvegeterian.setThreshold(1);

        ArrayAdapter<String> starchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, starchOptions);
        etlStarch.setAdapter(starchAdapter);
        etlStarch.setThreshold(1);

        ArrayAdapter<String> desertAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, desertOptions);
        etlDesert.setAdapter(desertAdapter);
        etlDesert.setThreshold(1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LunchActivity.this, SelectMealActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSoup = etlSuop.getText().toString();
                String selectedSalad = etlSalad.getText().toString();
                String selectedVegetarian = etlVegeterian.getText().toString();
                String selectedBlended = etlBlended.getText().toString();
                String selectedNonVegetarian = etlNonvegeterian.getText().toString();
                String selectedStarch = etlStarch.getText().toString();
                String selectedDessert = etlDesert.getText().toString();

                HospitalDatabaseHelper dbHelper = new HospitalDatabaseHelper(LunchActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("Soup", selectedSoup);
                values.put("Salad", selectedSalad);
                values.put("Vegetarian", selectedVegetarian);
                values.put("Blended", selectedBlended);
                values.put("NonVegetarian", selectedNonVegetarian);
                values.put("Starch", selectedStarch);
                values.put("Dessert", selectedDessert);

                long newRowId = db.insert("Lunch", null, values);
                if (newRowId != -1) {
                    Toast.makeText(LunchActivity.this, "Lunch order saved successfully!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LunchActivity.this, "Failed to save lunch order.", Toast.LENGTH_LONG).show();
                }

                db.close();

                Intent intent = new Intent(LunchActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });


        etlSuop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etlSuop.showDropDown();
            }
        });

        etlSalad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etlSalad.showDropDown();
            }
        });

        etlVegeterian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etlVegeterian.showDropDown();
            }
        });

        etlBlended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etlBlended.showDropDown();
            }
        });

        etlNonvegeterian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etlNonvegeterian.showDropDown();
            }
        });

        etlStarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etlStarch.showDropDown();
            }
        });

        etlDesert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etlDesert.showDropDown();
            }
        });


    }
}
