package com.example.fms;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fms.R;

public class ServedBreakfastActivity extends Activity {
    TextView breakfastFruitTextView, breakfastCerealTextView, breakfastStarchTextView, breakfastMeatTextView, breakfastSpreadsTextView;

    Cursor servedCursor;
    int currentRecordIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_served_breakfast_orders);

        // Initialize TextViews
        breakfastFruitTextView = findViewById(R.id.breakfastFruitTextView);
        breakfastCerealTextView = findViewById(R.id.breakfastCerealTextView);
        breakfastStarchTextView = findViewById(R.id.breakfastStarchTextView);
        breakfastMeatTextView = findViewById(R.id.breakfastMeatTextView);
        breakfastSpreadsTextView = findViewById(R.id.breakfastSpreadsTextView);

        Button nextButton = findViewById(R.id.nextButton);
        Button previousButton = findViewById(R.id.previousButton);

        DMDatabaseHelper dbHelper = new DMDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projectionServed = {
                "Fruit",
                "Cereal",
                "Starch",
                "Meat",
                "Spreads"
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
                    Toast.makeText(ServedBreakfastActivity.this, "No more records found!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ServedBreakfastActivity.this, "No more records found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (servedCursor == null || servedCursor.getCount() == 0) {
            Toast.makeText(ServedBreakfastActivity.this, "No served orders found.", Toast.LENGTH_SHORT).show();
        } else {
            showDataForCurrentIndex();
        }

        // Close the database as we no longer need it open
        db.close();
    }

    // Helper method to show data for the current record index
    private void showDataForCurrentIndex() {
        if (servedCursor != null && servedCursor.moveToPosition(currentRecordIndex)) {
            // Check if the cursor is positioned at a valid row
            int breakfastFruitColumnIndex = servedCursor.getColumnIndex("Fruit");
            int breakfastCerealColumnIndex = servedCursor.getColumnIndex("Cereal");
            int breakfastStarchColumnIndex = servedCursor.getColumnIndex("Starch");
            int breakfastMeatColumnIndex = servedCursor.getColumnIndex("Meat");
            int breakfastSpreadsColumnIndex = servedCursor.getColumnIndex("Spreads");

            if (breakfastFruitColumnIndex != -1 && breakfastCerealColumnIndex != -1 &&
                    breakfastStarchColumnIndex != -1 && breakfastMeatColumnIndex != -1 &&
                    breakfastSpreadsColumnIndex != -1) {

                // Retrieve and display data from the cursor
                String breakfastFruit = servedCursor.getString(breakfastFruitColumnIndex);
                String breakfastCereal = servedCursor.getString(breakfastCerealColumnIndex);
                String breakfastStarch = servedCursor.getString(breakfastStarchColumnIndex);
                String breakfastMeat = servedCursor.getString(breakfastMeatColumnIndex);
                String breakfastSpreads = servedCursor.getString(breakfastSpreadsColumnIndex);

                // Update TextViews with data
                breakfastFruitTextView.setText("Fruit: " + breakfastFruit);
                breakfastCerealTextView.setText("Cereal: " + breakfastCereal);
                breakfastStarchTextView.setText("Starch: " + breakfastStarch);
                breakfastMeatTextView.setText("Meat: " + breakfastMeat);
                breakfastSpreadsTextView.setText("Spreads: " + breakfastSpreads);
            } else {
                Toast.makeText(ServedBreakfastActivity.this, "Error: Invalid cursor column index", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ServedBreakfastActivity.this, "Error: Invalid cursor position", Toast.LENGTH_SHORT).show();
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