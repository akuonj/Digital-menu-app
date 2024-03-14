package com.example.hospitalfood;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeletedActivity extends Activity {
    TextView breakfastFruitTextView, breakfastCerealTextView, breakfastStarchTextView, breakfastMeatTextView, breakfastSpreadsTextView;

    // Cursors for patient and breakfast data
    Cursor deletedCursor;
    int currentRecordIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted);

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
                if (currentRecordIndex < deletedCursor.getCount() - 1) {
                    currentRecordIndex++;
                    showDataForCurrentIndex();
                } else {
                    Toast.makeText(DeletedActivity.this, "No records found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        HospitalDatabaseHelper dbHelper = new HospitalDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projectionDeleted = {
                "Fruit",
                "Cereal",
                "Starch",
                "Meat",
                "Spreads"
        };



        String selection = null;
        String[] selectionArgs = null;

        deletedCursor = db.query(
                "DeletedOrders",
                projectionDeleted,
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
                    Toast.makeText(DeletedActivity.this, "No records found!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (deletedCursor.getCount() == 0) {
            Toast.makeText(DeletedActivity.this, "No seved orders found.", Toast.LENGTH_SHORT).show();
        } else {
            showDataForCurrentIndex();
        }

        db.close();
    }

    // Helper method to show data for the current record index
    private void showDataForCurrentIndex() {
        if (deletedCursor != null) {
            // Check if the cursor is positioned at a valid row
            int breakfastFruitColumnIndex = deletedCursor.getColumnIndex("Fruit");
            int patientCerealColumnIndex = deletedCursor.getColumnIndex("Cereal");
            int patientStarchColumnIndex = deletedCursor.getColumnIndex("Starch");
            int patientMeatColumnIndex = deletedCursor.getColumnIndex("Meat");
            int patientSpreadsColumnIndex = deletedCursor.getColumnIndex("Spreads");

            if (deletedCursor.moveToPosition(currentRecordIndex) ) {
                // Retrieve and display data from the cursors
                                String breakfastFruit = deletedCursor.getString(breakfastFruitColumnIndex);
                String breakfastCereal = deletedCursor.getString(patientCerealColumnIndex);
                String breakfastStarch = deletedCursor.getString(patientStarchColumnIndex);
                String breakfastMeat = deletedCursor.getString(patientMeatColumnIndex);
                String breakfastSpreads = deletedCursor.getString(patientSpreadsColumnIndex);

                // Update your TextViews with data
                breakfastFruitTextView.setText("Fruit:  " + breakfastFruit);
                breakfastCerealTextView.setText("Cereal:  " + breakfastCereal);
                breakfastStarchTextView.setText("Starch:  " + breakfastStarch);
                breakfastMeatTextView.setText("Meat:  " + breakfastMeat);
                breakfastSpreadsTextView.setText("Spreads:  " + breakfastSpreads);
            } else {
                // Handle the case where the cursor position is invalid
                Toast.makeText(DeletedActivity.this, "Error: Invalid cursor position", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where there is no data in the cursor
            Toast.makeText(DeletedActivity.this, "No data in the cursor", Toast.LENGTH_SHORT).show();
        }
    }
}