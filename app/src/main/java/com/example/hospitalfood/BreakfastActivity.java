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


public class BreakfastActivity extends Activity {
    Button ok;
    AutoCompleteTextView etbfFruit, etbfCereal, etbfStarch, etbfMeat, etbfSpreads;

    private String[] fruitOptions = {"Pineapple", "Melon"};
    private String[] cerealsOptions = {"Wimbi Porridge", "Oatmeal Porridge", "Weetabix", "Cornflakes"};
    private String[] starchOptions = {"Home Made Muesli", "White Bread Plain", "White Bread Toasted", "Brown Bread Plain", "Brown Bread Toasted"};
    private String[] meatOptions = {"Chicken Sausage"};
    private String[] spreadsOptions = {"Butter", "Marmalade", "Jam"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast);

        ok = findViewById(R.id.btbfOk);

        etbfFruit = findViewById(R.id.etbfFruit);
        etbfCereal = findViewById(R.id.etbfCereal);
        etbfStarch = findViewById(R.id.etbfStarch);
        etbfMeat = findViewById(R.id.etbfMeat);
        etbfSpreads = findViewById(R.id.etbfSpreads);

        ArrayAdapter<String> fruitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fruitOptions);
        etbfFruit.setAdapter(fruitAdapter);
        etbfFruit.setThreshold(1);

        ArrayAdapter<String> cerealsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cerealsOptions);
        etbfCereal.setAdapter(cerealsAdapter);
        etbfCereal.setThreshold(1);

        ArrayAdapter<String> starchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, starchOptions);
        etbfStarch.setAdapter(starchAdapter);
        etbfStarch.setThreshold(1);

        ArrayAdapter<String> meatAdapter =new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, meatOptions);
        etbfMeat.setAdapter(meatAdapter);
        etbfMeat.setThreshold(1);

        ArrayAdapter<String> spreadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spreadsOptions);
        etbfSpreads.setAdapter(spreadAdapter);
        etbfSpreads.setThreshold(1);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFruit = etbfFruit.getText().toString().trim();
                String selectedCereal = etbfCereal.getText().toString().trim();
                String selectedStarch = etbfStarch.getText().toString().trim();
                String selectedMeat = etbfMeat.getText().toString().trim();
                String selectedSpreads = etbfSpreads.getText().toString().trim();

                // Check if any of the fields are empty
                if (selectedFruit.isEmpty() || selectedCereal.isEmpty() || selectedStarch.isEmpty() || selectedMeat.isEmpty() || selectedSpreads.isEmpty()) {
                    Toast.makeText(BreakfastActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return; // Exit the method without proceeding further
                }

                // Proceed with inserting data into the database
                HospitalDatabaseHelper dbHelper = new HospitalDatabaseHelper(BreakfastActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("Fruit", selectedFruit);
                values.put("Cereal", selectedCereal);
                values.put("Starch", selectedStarch);
                values.put("Meat", selectedMeat);
                values.put("Spreads", selectedSpreads);

                long newRowId = db.insert("Breakfast", null, values);
                if (newRowId != -1) {
                    Toast.makeText(BreakfastActivity.this, "Order made successfully!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(BreakfastActivity.this, "Failed to make an order.", Toast.LENGTH_LONG).show();
                }

                db.close();

                Intent intent = new Intent(BreakfastActivity.this, WaitActivity.class);
                startActivity(intent);
            }
        });



        etbfFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etbfFruit.showDropDown();
            }
        });

        etbfCereal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etbfCereal.showDropDown();
            }
        });

        etbfStarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etbfStarch.showDropDown();
            }
        });

        etbfMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etbfMeat.showDropDown();
            }
        });

        etbfSpreads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etbfSpreads.showDropDown();
            }
        });


    }
}