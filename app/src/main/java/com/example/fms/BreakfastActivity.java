package com.example.fms;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BreakfastActivity extends Activity {

    private Button ok;
    private RecyclerView breakfastRecyclerView;
    private BreakfastAdapter breakfastAdapter;
    private List<BreakfastItem> breakfastItems;

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
        breakfastRecyclerView = findViewById(R.id.breakfast_recycler_view);
        breakfastRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the breakfast items list
        breakfastItems = new ArrayList<>();
        breakfastItems.add(new BreakfastItem("Fruit", fruitOptions[0], fruitOptions));
        breakfastItems.add(new BreakfastItem("Cereal", cerealsOptions[0], cerealsOptions));
        breakfastItems.add(new BreakfastItem("Starch", starchOptions[0], starchOptions));
        breakfastItems.add(new BreakfastItem("Meat", meatOptions[0], meatOptions));
        breakfastItems.add(new BreakfastItem("Spreads", spreadsOptions[0], spreadsOptions));

        breakfastAdapter = new BreakfastAdapter(breakfastItems);
        breakfastRecyclerView.setAdapter(breakfastAdapter);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFruit = breakfastItems.get(0).getSelectedOption();
                String selectedCereal = breakfastItems.get(1).getSelectedOption();
                String selectedStarch = breakfastItems.get(2).getSelectedOption();
                String selectedMeat = breakfastItems.get(3).getSelectedOption();
                String selectedSpreads = breakfastItems.get(4).getSelectedOption();

                // Check if any of the fields are empty
                if (selectedFruit.isEmpty() || selectedCereal.isEmpty() || selectedStarch.isEmpty() || selectedMeat.isEmpty() || selectedSpreads.isEmpty()) {
                    Toast.makeText(BreakfastActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return; // Exit the method without proceeding further
                }

                // Proceed with inserting data into the database
                DMDatabaseHelper dbHelper = new DMDatabaseHelper(BreakfastActivity.this);
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
    }
}
