package com.example.fms;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    private TextView tvTotalPrice;

    private String[] fruitOptions = {"Pineapple", "Melon"};
    private double[] fruitPrices = {1.5, 2.0}; // Prices for fruits
    private String[] cerealsOptions = {"Wimbi Porridge", "Oatmeal Porridge", "Weetabix", "Cornflakes"};
    private double[] cerealPrices = {3.0, 3.5, 4.0, 2.5}; // Prices for cereals
    private String[] starchOptions = {"Home Made Muesli", "White Bread Plain", "White Bread Toasted", "Brown Bread Plain", "Brown Bread Toasted"};
    private double[] starchPrices = {5.0, 1.5, 1.8, 1.6, 1.9}; // Prices for starch
    private String[] meatOptions = {"Chicken Sausage"};
    private double[] meatPrices = {3.5}; // Prices for meat
    private String[] spreadsOptions = {"Butter", "Marmalade", "Jam"};
    private double[] spreadsPrices = {0.5, 0.7, 0.8}; // Prices for spreads

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        ok = findViewById(R.id.btbfOk);
        mealsRecyclerView = findViewById(R.id.breakfast_recycler_view);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                return;
            }
        }

        // listener to update total price
        mealsAdapter.setOnItemSelectedListener(new MealsAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected() {
                calculateTotalPrice();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMealOrder();
            }
        });

        // Initial total price calculation
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        double totalPrice = 0.0;

        for (MealstItem item : mealItems) {
            String category = item.getCategory();
            int selectedIndex = item.getSelectedIndex();

            switch (category) {
                case "Fruit":
                    totalPrice += fruitPrices[selectedIndex];
                    break;
                case "Cereal":
                    totalPrice += cerealPrices[selectedIndex];
                    break;
                case "Starch":
                    totalPrice += starchPrices[selectedIndex];
                    break;
                case "Meat":
                    totalPrice += meatPrices[selectedIndex];
                    break;
                case "Spreads":
                    totalPrice += spreadsPrices[selectedIndex];
                    break;
            }
        }

        TextView totalPriceTextView = findViewById(R.id.tvTotalPrice);
        totalPriceTextView.setText(String.format("Total: $%.2f", totalPrice));
    }

    private void saveMealOrder() {
        MealstItem selectedFruit = mealItems.get(0);
        MealstItem selectedCereal = mealItems.get(1);
        MealstItem selectedStarch = mealItems.get(2);
        MealstItem selectedMeat = mealItems.get(3);
        MealstItem selectedSpreads = mealItems.get(4);

        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("UserID", userId);
            values.put("Fruit", selectedFruit.getSelectedOption());
            values.put("FruitPrice", fruitPrices[selectedFruit.getSelectedIndex()]); // Use getSelectedIndex()
            values.put("Cereal", selectedCereal.getSelectedOption());
            values.put("CerealPrice", cerealPrices[selectedCereal.getSelectedIndex()]); // Use getSelectedIndex()
            values.put("Starch", selectedStarch.getSelectedOption());
            values.put("StarchPrice", starchPrices[selectedStarch.getSelectedIndex()]); // Use getSelectedIndex()
            values.put("Meat", selectedMeat.getSelectedOption());
            values.put("MeatPrice", meatPrices[selectedMeat.getSelectedIndex()]); // Use getSelectedIndex()
            values.put("Spreads", selectedSpreads.getSelectedOption());
            values.put("SpreadsPrice", spreadsPrices[selectedSpreads.getSelectedIndex()]); // Use getSelectedIndex()
            values.put("OrderedBy", userId);
            values.put("TimeOrdered", getCurrentTime());

            long newRowId = db.insert("Meals", null, values);
            if (newRowId != -1) {
                Toast.makeText(MealsActivity.this, "Order made successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MealsActivity.this, WaitActivity.class).putExtra("USERNAME", username));
            } else {
                Toast.makeText(MealsActivity.this, "Failed to make order.", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            Toast.makeText(MealsActivity.this, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date());
    }
}
