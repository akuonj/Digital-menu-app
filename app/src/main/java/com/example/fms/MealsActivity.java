package com.example.fms;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MealsActivity extends Activity {
    private Button ok;
    private RecyclerView mealsRecyclerView;
    private MealsAdapter mealsAdapter;
    private List<MealstItem> mealItems;
    private DMDatabaseHelper dbHelper;
    private int userId;
    private String username;

    private String[] fruitOptions = {"Pineapple", "Melon"};
    private String[] cerealsOptions = {"Wimbi Porridge", "Oatmeal Porridge", "Weetabix", "Cornflakes"};
    private String[] starchOptions = {"Home Made Muesli", "White Bread Plain", "White Bread Toasted", "Brown Bread Plain", "Brown Bread Toasted"};
    private String[] meatOptions = {"Chicken Sausage"};
    private String[] spreadsOptions = {"Butter", "Marmalade", "Jam"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        ok = findViewById(R.id.btbfOk);
        mealsRecyclerView = findViewById(R.id.breakfast_recycler_view);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the meal items list
        mealItems = new ArrayList<>();
        mealItems.add(new MealstItem("Fruit", fruitOptions[0], fruitOptions));
        mealItems.add(new MealstItem("Cereal", cerealsOptions[0], cerealsOptions));
        mealItems.add(new MealstItem("Starch", starchOptions[0], starchOptions));
        mealItems.add(new MealstItem("Meat", meatOptions[0], meatOptions));
        mealItems.add(new MealstItem("Spreads", spreadsOptions[0], spreadsOptions));

        mealsAdapter = new MealsAdapter(mealItems);
        mealsRecyclerView.setAdapter(mealsAdapter);

        dbHelper = new DMDatabaseHelper(this);
        username = getIntent().getStringExtra("USERNAME");

        if (username != null) {
            userId = dbHelper.getUserId(username);
            if (userId == -1) {
                Toast.makeText(MealsActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                finish(); // Exit the activity if userId is -1
                return;
            }
        } else {
            Toast.makeText(MealsActivity.this, "Username is not provided", Toast.LENGTH_SHORT).show();
            finish(); // Exit the activity if username is null
            return;
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFruit = mealItems.get(0).getSelectedOption();
                String selectedCereal = mealItems.get(1).getSelectedOption();
                String selectedStarch = mealItems.get(2).getSelectedOption();
                String selectedMeat = mealItems.get(3).getSelectedOption();
                String selectedSpreads = mealItems.get(4).getSelectedOption();

                if (selectedFruit.isEmpty() || selectedCereal.isEmpty() || selectedStarch.isEmpty() || selectedMeat.isEmpty() || selectedSpreads.isEmpty()) {
                    Toast.makeText(MealsActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase db = null;
                try {
                    db = dbHelper.getWritableDatabase();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("Africa/Nairobi"));
                    String currentTime = sdf.format(new Date());

                    ContentValues values = new ContentValues();
                    values.put("Fruit", selectedFruit);
                    values.put("Cereal", selectedCereal);
                    values.put("Starch", selectedStarch);
                    values.put("Meat", selectedMeat);
                    values.put("Spreads", selectedSpreads);
                    values.put("OrderedBy", userId);
                    values.put("TimeOrdered", currentTime);

                    long newRowId = db.insert("Meals", null, values);
                    if (newRowId != -1) {
                        Toast.makeText(MealsActivity.this, "Order made successfully!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MealsActivity.this, "Failed to make an order.", Toast.LENGTH_LONG).show();
                    }
                } catch (SQLiteException e) {
                    Toast.makeText(MealsActivity.this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    if (db != null) {
                        db.close();
                    }
                }

                Intent intent = new Intent(MealsActivity.this, WaitActivity.class);
                intent.putExtra("USERNAME", username); // Pass username to WaitActivity
                startActivity(intent);
            }
        });
    }
}
