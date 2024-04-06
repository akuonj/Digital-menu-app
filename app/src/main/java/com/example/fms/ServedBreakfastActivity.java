package com.example.fms;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalfood.R;

public class ServedBreakfastActivity extends Activity {
    TextView breakfastFruitTextView, breakfastCerealTextView, breakfastStarchTextView, breakfastMeatTextView, breakfastSpreadsTextView;

    // Cursors for patient and breakfast data
    Cursor ServedCursor;
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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentRecordIndex < ServedCursor.getCount() - 1) {
                    currentRecordIndex++;
                    showDataForCurrentIndex();
                } else {
                    Toast.makeText(ServedBreakfastActivity.this, "No records found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FMSDatabaseHelper dbHelper = new FMSDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projectionServed = {
                "Fruit",
                "Cereal",
                "Starch",
                "Meat",
                "Spreads"
        };



        String selection = null;
        String[] selectionArgs = null;

        ServedCursor = db.query(
                "ServedOrders",
                projectionServed,
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentRecordIndex > 0) {
                    currentRecordIndex--;
                    showDataForCurrentIndex();
                } else {
                    Toast.makeText(ServedBreakfastActivity.this, "No records found!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (ServedCursor.getCount() == 0) {
            Toast.makeText(ServedBreakfastActivity.this, "No seved orders found.", Toast.LENGTH_SHORT).show();
        } else {
            showDataForCurrentIndex();
        }

        db.close();
    }

    // Helper method to show data for the current record index
    private void showDataForCurrentIndex() {
        if (ServedCursor != null) {
            // Check if the cursor is positioned at a valid row
            int breakfastFruitColumnIndex = ServedCursor.getColumnIndex("Fruit");
            int patientCerealColumnIndex = ServedCursor.getColumnIndex("Cereal");
            int patientStarchColumnIndex = ServedCursor.getColumnIndex("Starch");
            int patientMeatColumnIndex = ServedCursor.getColumnIndex("Meat");
            int patientSpreadsColumnIndex = ServedCursor.getColumnIndex("Spreads");

            if (ServedCursor.moveToPosition(currentRecordIndex) ) {
                // Retrieve and display data from the cursors
                                String breakfastFruit = ServedCursor.getString(breakfastFruitColumnIndex);
                String breakfastCereal = ServedCursor.getString(patientCerealColumnIndex);
                String breakfastStarch = ServedCursor.getString(patientStarchColumnIndex);
                String breakfastMeat = ServedCursor.getString(patientMeatColumnIndex);
                String breakfastSpreads = ServedCursor.getString(patientSpreadsColumnIndex);

                // Update your TextViews with data
                breakfastFruitTextView.setText("Fruit:  " + breakfastFruit);
                breakfastCerealTextView.setText("Cereal:  " + breakfastCereal);
                breakfastStarchTextView.setText("Starch:  " + breakfastStarch);
                breakfastMeatTextView.setText("Meat:  " + breakfastMeat);
                breakfastSpreadsTextView.setText("Spreads:  " + breakfastSpreads);
            } else {
                // Handle the case where the cursor position is invalid
                Toast.makeText(ServedBreakfastActivity.this, "Error: Invalid cursor position", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where there is no data in the cursor
            Toast.makeText(ServedBreakfastActivity.this, "No data in the cursor", Toast.LENGTH_SHORT).show();
        }
    }
}