package com.example.fms;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fms.R;

public class PendingBOrderActivity extends Activity {
    // TextViews to display breakfast data
    TextView breakfastFruitTextView, breakfastCerealTextView, breakfastStarchTextView, breakfastMeatTextView, breakfastSpreadsTextView;

    // Cursor for breakfast data
    Cursor breakfastCursor;
    int currentRecordIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_breakfast_orders);

        // Initialize TextViews
        breakfastFruitTextView = findViewById(R.id.breakfastFruitTextView);
        breakfastCerealTextView = findViewById(R.id.breakfastCerealTextView);
        breakfastStarchTextView = findViewById(R.id.breakfastStarchTextView);
        breakfastMeatTextView = findViewById(R.id.breakfastMeatTextView);
        breakfastSpreadsTextView = findViewById(R.id.breakfastSpreadsTextView);

        Button nextButton = findViewById(R.id.nextButton);
        Button previousButton = findViewById(R.id.previousButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentRecordIndex < breakfastCursor.getCount() - 1) {
                    currentRecordIndex++;
                    showDataForCurrentIndex();
                } else {
                    Toast.makeText(PendingBOrderActivity.this, "No more records found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DMDatabaseHelper dbHelper = new DMDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projectionBreakfast = {
                "Fruit",
                "Cereal",
                "Starch",
                "Meat",
                "Spreads",
        };

        breakfastCursor = db.query(
                "Breakfast",
                projectionBreakfast,
                null,
                null,
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
                    Toast.makeText(PendingBOrderActivity.this, "No previous records found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (breakfastCursor.moveToPosition(currentRecordIndex)) {
                    // Check if the columns exist in the cursor
                    int breakfastFruitColumnIndex = breakfastCursor.getColumnIndex("Fruit");
                    int breakfastCerealColumnIndex = breakfastCursor.getColumnIndex("Cereal");
                    int breakfastStarchColumnIndex = breakfastCursor.getColumnIndex("Starch");
                    int breakfastMeatColumnIndex = breakfastCursor.getColumnIndex("Meat");
                    int breakfastSpreadsColumnIndex = breakfastCursor.getColumnIndex("Spreads");

                    // Check if any column index is -1, indicating the column does not exist
                    if (breakfastFruitColumnIndex == -1 || breakfastCerealColumnIndex == -1 ||
                            breakfastStarchColumnIndex == -1 || breakfastMeatColumnIndex == -1 ||
                            breakfastSpreadsColumnIndex == -1) {
                        // Handle the case where any column is not found in the cursor
                        Toast.makeText(PendingBOrderActivity.this, "One or more columns not found in cursor", Toast.LENGTH_SHORT).show();
                        return; // Exit the method to prevent further processing
                    }

                    // Retrieve data from the cursor
                    String breakfastFruit = breakfastCursor.getString(breakfastFruitColumnIndex);
                    String breakfastCereal = breakfastCursor.getString(breakfastCerealColumnIndex);
                    String breakfastStarch = breakfastCursor.getString(breakfastStarchColumnIndex);
                    String breakfastMeat = breakfastCursor.getString(breakfastMeatColumnIndex);
                    String breakfastSpreads = breakfastCursor.getString(breakfastSpreadsColumnIndex);

                    DMDatabaseHelper dbHelper = new DMDatabaseHelper(PendingBOrderActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // Insert data into DeletedOrders
                    ContentValues deletedOrderValues = new ContentValues();
                    deletedOrderValues.put("Fruit", breakfastFruit);
                    deletedOrderValues.put("Cereal", breakfastCereal);
                    deletedOrderValues.put("Starch", breakfastStarch);
                    deletedOrderValues.put("Meat", breakfastMeat);
                    deletedOrderValues.put("Spreads", breakfastSpreads);

                    long deletedOrderID = db.insert("ServedOrders", null, deletedOrderValues);

                    if (deletedOrderID != -1) {
                        // Record successfully moved to DeletedOrders
                        // Now, delete the original records
                        String selection = "Fruit = ?";
                        String[] selectionArgs = {breakfastFruit};
                        int deletedRows = db.delete("Breakfast", selection, selectionArgs);

                        if (deletedRows > 0) {
                            // Original records served successfully
                            Toast.makeText(PendingBOrderActivity.this, "Order Served!", Toast.LENGTH_SHORT).show();
                            currentRecordIndex = 0; // Move back to the first record or perform another action as needed
                            showDataForCurrentIndex();
                        } else {
                            // Served failed
                            Toast.makeText(PendingBOrderActivity.this, "Failed to serve order", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Insertion into Serve orders failed
                        Toast.makeText(PendingBOrderActivity.this, "Failed to move order to Served Orders", Toast.LENGTH_SHORT).show();
                    }

                    db.close();
                } else {
                    // Handle the case where moveToPosition returns false, indicating invalid cursor position
                    Toast.makeText(PendingBOrderActivity.this, "Error: Invalid cursor position", Toast.LENGTH_SHORT).show();
                }
            }
        });



        if (breakfastCursor.getCount() == 0) {
            Toast.makeText(PendingBOrderActivity.this, "No breakfast orders found.", Toast.LENGTH_SHORT).show();
        } else {
            showDataForCurrentIndex();
        }

        db.close();
    }

    // Helper method to show data for the current record index
    private void showDataForCurrentIndex() {
        if (breakfastCursor != null && breakfastCursor.getCount() > 0) {
            int breakfastFruitColumnIndex = breakfastCursor.getColumnIndex("Fruit");
            int breakfastCerealColumnIndex = breakfastCursor.getColumnIndex("Cereal");
            int breakfastStarchColumnIndex = breakfastCursor.getColumnIndex("Starch");
            int breakfastMeatColumnIndex = breakfastCursor.getColumnIndex("Meat");
            int breakfastSpreadsColumnIndex = breakfastCursor.getColumnIndex("Spreads");

            if (breakfastCursor.moveToPosition(currentRecordIndex)) {
                String breakfastFruit = breakfastCursor.getString(breakfastFruitColumnIndex);
                String breakfastCereal = breakfastCursor.getString(breakfastCerealColumnIndex);
                String breakfastStarch = breakfastCursor.getString(breakfastStarchColumnIndex);
                String breakfastMeat = breakfastCursor.getString(breakfastMeatColumnIndex);
                String breakfastSpreads = breakfastCursor.getString(breakfastSpreadsColumnIndex);

                breakfastFruitTextView.setText("Fruit:  " + breakfastFruit);
                breakfastCerealTextView.setText("Cereal:  " + breakfastCereal);
                breakfastStarchTextView.setText("Starch:  " + breakfastStarch);
                breakfastMeatTextView.setText("Meat:  " + breakfastMeat);
                breakfastSpreadsTextView.setText("Spreads:  " + breakfastSpreads);
            } else {
                Toast.makeText(PendingBOrderActivity.this, "Error: Invalid cursor position", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PendingBOrderActivity.this, "No data in the cursor", Toast.LENGTH_SHORT).show();
        }
    }
}
