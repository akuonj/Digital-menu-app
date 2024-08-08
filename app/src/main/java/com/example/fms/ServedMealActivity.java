package com.example.fms;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ServedMealActivity extends AppCompatActivity {
    // TextViews to display meal data
    TextView mealFruitTextView, mealCerealTextView, mealStarchTextView,
            mealMeatTextView, mealSpreadsTextView, orderedBy, servedTime;

    Cursor servedCursor;
    int currentRecordIndex = 0;
    DMDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_served_meal_orders);

        // Initialize TextViews
        mealFruitTextView = findViewById(R.id.mealFruitTextView);
        mealCerealTextView = findViewById(R.id.mealCerealTextView);
        mealStarchTextView = findViewById(R.id.mealStarchTextView);
        mealMeatTextView = findViewById(R.id.mealMeatTextView);
        mealSpreadsTextView = findViewById(R.id.mealSpreadsTextView);
        orderedBy = findViewById(R.id.orderedBy);
        servedTime = findViewById(R.id.servedTime);

        Button nextButton = findViewById(R.id.nextButton);
        Button previousButton = findViewById(R.id.previousButton);

        dbHelper = new DMDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to fetch data from the served orders table
        String[] projectionServed = {
                "Fruit",
                "Cereal",
                "Starch",
                "Meat",
                "Spreads",
                "OrderedBy",
                "ServedTime"  // Update to use ServedTime
        };

        servedCursor = db.query(
                "ServedOrders",
                projectionServed,
                null,
                null,
                null,
                null,
                null
        );

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (servedCursor != null && currentRecordIndex < servedCursor.getCount() - 1) {
                    currentRecordIndex++;
                    showDataForCurrentIndex();
                } else {
                    Toast.makeText(ServedMealActivity.this, "No more records found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (servedCursor != null && currentRecordIndex > 0) {
                    currentRecordIndex--;
                    showDataForCurrentIndex();
                } else {
                    Toast.makeText(ServedMealActivity.this, "No previous records found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (servedCursor == null || servedCursor.getCount() == 0) {
            Toast.makeText(ServedMealActivity.this, "No served orders found.", Toast.LENGTH_SHORT).show();
        } else {
            showDataForCurrentIndex();
        }

        // Close the database as we no longer need it open
        db.close();
    }

    // Helper method to show data for the current record index
    private void showDataForCurrentIndex() {
        if (servedCursor != null && servedCursor.moveToPosition(currentRecordIndex)) {
            int fruitColumnIndex = servedCursor.getColumnIndex("Fruit");
            int cerealColumnIndex = servedCursor.getColumnIndex("Cereal");
            int starchColumnIndex = servedCursor.getColumnIndex("Starch");
            int meatColumnIndex = servedCursor.getColumnIndex("Meat");
            int spreadsColumnIndex = servedCursor.getColumnIndex("Spreads");
            int orderedByIndex = servedCursor.getColumnIndex("OrderedBy");
            int servedTimeIndex = servedCursor.getColumnIndex("ServedTime");  // Update to use ServedTime

            if (fruitColumnIndex != -1 && cerealColumnIndex != -1 &&
                    starchColumnIndex != -1 && meatColumnIndex != -1 &&
                    spreadsColumnIndex != -1 && orderedByIndex != -1 &&
                    servedTimeIndex != -1) {

                String fruit = servedCursor.getString(fruitColumnIndex);
                String cereal = servedCursor.getString(cerealColumnIndex);
                String starch = servedCursor.getString(starchColumnIndex);
                String meat = servedCursor.getString(meatColumnIndex);
                String spreads = servedCursor.getString(spreadsColumnIndex);
                int userId = servedCursor.getInt(orderedByIndex);
                String servedTimeValue = servedCursor.getString(servedTimeIndex);  // Update to use ServedTime

                // Retrieve the username using the userId
                String orderedByName = dbHelper.getUserNameById(userId);

                // Update TextViews with data
                mealFruitTextView.setText("Fruit: " + fruit);
                mealCerealTextView.setText("Cereal: " + cereal);
                mealStarchTextView.setText("Starch: " + starch);
                mealMeatTextView.setText("Meat: " + meat);
                mealSpreadsTextView.setText("Spreads: " + spreads);
                orderedBy.setText("Made by: " + (orderedByName != null ? orderedByName : "Unknown"));
                servedTime.setText("Served Time: " + servedTimeValue);  // Update to display ServedTime
            } else {
                Toast.makeText(ServedMealActivity.this, "Error: Invalid cursor column index", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ServedMealActivity.this, "Error: Invalid cursor position", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (servedCursor != null) {
            servedCursor.close();
        }
    }
}
