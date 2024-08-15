package com.example.fms;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class PendingMealsOrderActivity extends AppCompatActivity {
    // TextView elements to display food items and prices
    private TextView mealFruitTextView;
    private TextView mealFruitPriceTextView;
    private TextView mealCerealTextView;
    private TextView mealCerealPriceTextView;
    private TextView mealStarchTextView;
    private TextView mealStarchPriceTextView;
    private TextView mealMeatTextView;
    private TextView mealMeatPriceTextView;
    private TextView mealSpreadsTextView;
    private TextView mealSpreadsPriceTextView;
    private TextView orderedByTextView;
    private TextView orderedTimeTextView;
    private TextView totalPriceTextView;

    private Button previousButton;
    private Button markAsServedButton;
    private Button nextButton;

    private DMDatabaseHelper dbHelper;
    private ArrayList<Integer> mealOrderIds;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_meal_orders);

        // Initialize TextView elements
        mealFruitTextView = findViewById(R.id.mealFruitTextView);
        mealFruitPriceTextView = findViewById(R.id.mealFruitPriceTextView);
        mealCerealTextView = findViewById(R.id.mealCerealTextView);
        mealCerealPriceTextView = findViewById(R.id.mealCerealPriceTextView);
        mealStarchTextView = findViewById(R.id.mealStarchTextView);
        mealStarchPriceTextView = findViewById(R.id.mealStarchPriceTextView);
        mealMeatTextView = findViewById(R.id.mealMeatTextView);
        mealMeatPriceTextView = findViewById(R.id.mealMeatPriceTextView);
        mealSpreadsTextView = findViewById(R.id.mealSpreadsTextView);
        mealSpreadsPriceTextView = findViewById(R.id.mealSpreadsPriceTextView);
        orderedByTextView = findViewById(R.id.orderedBy);
        orderedTimeTextView = findViewById(R.id.orderedTime);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        // Initialize Buttons
        previousButton = findViewById(R.id.previousButton);
        markAsServedButton = findViewById(R.id.markAsServedButton);
        nextButton = findViewById(R.id.nextButton);

        dbHelper = new DMDatabaseHelper(this);
        mealOrderIds = getAllMealOrderIds();

        showDataForCurrentIndex();

        // Set OnClickListener for previous button
        previousButton.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showDataForCurrentIndex();
            } else {
                Toast.makeText(PendingMealsOrderActivity.this, "No previous orders", Toast.LENGTH_SHORT).show();
            }
        });

        // Set OnClickListener for mark as served button
        markAsServedButton.setOnClickListener(v -> {
            if (mealOrderIds.size() > 0) {
                markCurrentOrderAsServed();
                mealOrderIds.remove(currentIndex);
                if (currentIndex >= mealOrderIds.size() && currentIndex > 0) {
                    currentIndex--;
                }
                showDataForCurrentIndex();
            } else {
                Toast.makeText(PendingMealsOrderActivity.this, "No orders to mark as served", Toast.LENGTH_SHORT).show();
            }
        });

        // Set OnClickListener for next button
        nextButton.setOnClickListener(v -> {
            if (currentIndex < mealOrderIds.size() - 1) {
                currentIndex++;
                showDataForCurrentIndex();
            } else {
                Toast.makeText(PendingMealsOrderActivity.this, "No more orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("Range")
    private ArrayList<Integer> getAllMealOrderIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query("Meals", new String[]{"MealID"}, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                ids.add(cursor.getInt(cursor.getColumnIndex("MealID")));
            }
        }
        return ids;
    }

    @SuppressLint("Range")
    private void showDataForCurrentIndex() {
        if (mealOrderIds.size() > 0) {
            int mealId = mealOrderIds.get(currentIndex);
            try (SQLiteDatabase db = dbHelper.getReadableDatabase();
                 Cursor cursor = db.query("Meals", null, "MealID = ?", new String[]{String.valueOf(mealId)}, null, null, null)) {
                if (cursor.moveToFirst()) {
                    // Display food items
                    mealFruitTextView.setText("Fruit: " + cursor.getString(cursor.getColumnIndex("Fruit")));
                    mealCerealTextView.setText("Cereal: " + cursor.getString(cursor.getColumnIndex("Cereal")));
                    mealStarchTextView.setText("Starch: " + cursor.getString(cursor.getColumnIndex("Starch")));
                    mealMeatTextView.setText("Meat: " + cursor.getString(cursor.getColumnIndex("Meat")));
                    mealSpreadsTextView.setText("Spreads: " + cursor.getString(cursor.getColumnIndex("Spreads")));

                    // Display prices
                    double fruitPrice = cursor.getDouble(cursor.getColumnIndex("FruitPrice"));
                    double cerealPrice = cursor.getDouble(cursor.getColumnIndex("CerealPrice"));
                    double starchPrice = cursor.getDouble(cursor.getColumnIndex("StarchPrice"));
                    double meatPrice = cursor.getDouble(cursor.getColumnIndex("MeatPrice"));
                    double spreadsPrice = cursor.getDouble(cursor.getColumnIndex("SpreadsPrice"));

                    mealFruitPriceTextView.setText(String.format("$%.2f", fruitPrice));
                    mealCerealPriceTextView.setText(String.format("$%.2f", cerealPrice));
                    mealStarchPriceTextView.setText(String.format("$%.2f", starchPrice));
                    mealMeatPriceTextView.setText(String.format("$%.2f", meatPrice));
                    mealSpreadsPriceTextView.setText(String.format("$%.2f", spreadsPrice));

                    double totalPrice = fruitPrice + cerealPrice + starchPrice + meatPrice + spreadsPrice;
                    totalPriceTextView.setText(String.format("Total Price: $%.2f", totalPrice));

                    // Display order details
                    orderedByTextView.setText("Made by: " + dbHelper.getUserNameById(cursor.getInt(cursor.getColumnIndex("OrderedBy"))));
                    orderedTimeTextView.setText("Time-Ordered: " + cursor.getString(cursor.getColumnIndex("TimeOrdered")));
                }
            }
        } else {
            // Clear the text views if no data is available
            mealFruitTextView.setText("Fruit: ");
            mealFruitPriceTextView.setText("Price: ");
            mealCerealTextView.setText("Cereal: ");
            mealCerealPriceTextView.setText("Price: ");
            mealStarchTextView.setText("Starch: ");
            mealStarchPriceTextView.setText("Price: ");
            mealMeatTextView.setText("Meat: ");
            mealMeatPriceTextView.setText("Price: ");
            mealSpreadsTextView.setText("Spreads: ");
            mealSpreadsPriceTextView.setText("Price: ");
            orderedByTextView.setText("Made by: ");
            orderedTimeTextView.setText("Time-Ordered: ");
            totalPriceTextView.setText("Total Price: ");
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault()); // Use default timezone
        return sdf.format(new Date());
    }

    @SuppressLint("Range")
    private void markCurrentOrderAsServed() {
        if (mealOrderIds.size() > 0) {
            int mealId = mealOrderIds.get(currentIndex);
            try (SQLiteDatabase db = dbHelper.getWritableDatabase();
                 Cursor cursor = db.rawQuery("SELECT UserID, Fruit, FruitPrice, Cereal, CerealPrice, Starch, StarchPrice, Meat, MeatPrice, Spreads, SpreadsPrice, OrderedBy, TimeOrdered " +
                         "FROM Meals WHERE MealID = ?", new String[]{String.valueOf(mealId)})) {

                if (cursor != null && cursor.moveToFirst()) {
                    int userId = cursor.getInt(cursor.getColumnIndex("UserID"));
                    String fruit = cursor.getString(cursor.getColumnIndex("Fruit"));
                    double fruitPrice = cursor.getDouble(cursor.getColumnIndex("FruitPrice"));
                    String cereal = cursor.getString(cursor.getColumnIndex("Cereal"));
                    double cerealPrice = cursor.getDouble(cursor.getColumnIndex("CerealPrice"));
                    String starch = cursor.getString(cursor.getColumnIndex("Starch"));
                    double starchPrice = cursor.getDouble(cursor.getColumnIndex("StarchPrice"));
                    String meat = cursor.getString(cursor.getColumnIndex("Meat"));
                    double meatPrice = cursor.getDouble(cursor.getColumnIndex("MeatPrice"));
                    String spreads = cursor.getString(cursor.getColumnIndex("Spreads"));
                    double spreadsPrice = cursor.getDouble(cursor.getColumnIndex("SpreadsPrice"));
                    String orderedBy = cursor.getString(cursor.getColumnIndex("OrderedBy"));
                    String timeOrdered = cursor.getString(cursor.getColumnIndex("TimeOrdered"));

                    // Insert the served meal into the ServedMeals table
                    db.execSQL("INSERT INTO ServedOrders (UserID, Fruit, FruitPrice, Cereal, CerealPrice, Starch, StarchPrice, Meat, MeatPrice, Spreads, SpreadsPrice, OrderedBy, OrderedTime, ServedTime) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            new Object[]{userId, fruit, fruitPrice, cereal, cerealPrice, starch, starchPrice, meat, meatPrice, spreads, spreadsPrice, orderedBy, timeOrdered, getCurrentTime()});

                    // Delete the meal from the Meals table
                    db.execSQL("DELETE FROM Meals WHERE MealID = ?", new String[]{String.valueOf(mealId)});
                }
            }
        }
    }
}
