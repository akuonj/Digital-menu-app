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
    TextView fruitPriceTextView, cerealPriceTextView, starchPriceTextView,
            meatPriceTextView, spreadsPriceTextView, totalPriceTextView;

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

        fruitPriceTextView = findViewById(R.id.mealFruitPriceTextView);
        cerealPriceTextView = findViewById(R.id.mealCerealPriceTextView);
        starchPriceTextView = findViewById(R.id.mealStarchPriceTextView);
        meatPriceTextView = findViewById(R.id.mealMeatPriceTextView);
        spreadsPriceTextView = findViewById(R.id.mealSpreadsPriceTextView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        Button nextButton = findViewById(R.id.nextButton);
        Button previousButton = findViewById(R.id.previousButton);

        dbHelper = new DMDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to fetch data from the served orders table
        String[] projectionServed = {
                "Fruit",
                "FruitPrice",
                "Cereal",
                "CerealPrice",
                "Starch",
                "StarchPrice",
                "Meat",
                "MeatPrice",
                "Spreads",
                "SpreadsPrice",
                "OrderedBy",
                "ServedTime"
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
            int fruitPriceColumnIndex = servedCursor.getColumnIndex("FruitPrice");
            int cerealColumnIndex = servedCursor.getColumnIndex("Cereal");
            int cerealPriceColumnIndex = servedCursor.getColumnIndex("CerealPrice");
            int starchColumnIndex = servedCursor.getColumnIndex("Starch");
            int starchPriceColumnIndex = servedCursor.getColumnIndex("StarchPrice");
            int meatColumnIndex = servedCursor.getColumnIndex("Meat");
            int meatPriceColumnIndex = servedCursor.getColumnIndex("MeatPrice");
            int spreadsColumnIndex = servedCursor.getColumnIndex("Spreads");
            int spreadsPriceColumnIndex = servedCursor.getColumnIndex("SpreadsPrice");
            int orderedByIndex = servedCursor.getColumnIndex("OrderedBy");
            int servedTimeIndex = servedCursor.getColumnIndex("ServedTime");

            if (fruitColumnIndex != -1 && fruitPriceColumnIndex != -1 &&
                    cerealColumnIndex != -1 && cerealPriceColumnIndex != -1 &&
                    starchColumnIndex != -1 && starchPriceColumnIndex != -1 &&
                    meatColumnIndex != -1 && meatPriceColumnIndex != -1 &&
                    spreadsColumnIndex != -1 && spreadsPriceColumnIndex != -1 &&
                    orderedByIndex != -1 && servedTimeIndex != -1) {

                String fruit = servedCursor.getString(fruitColumnIndex);
                double fruitPrice = servedCursor.getDouble(fruitPriceColumnIndex);
                String cereal = servedCursor.getString(cerealColumnIndex);
                double cerealPrice = servedCursor.getDouble(cerealPriceColumnIndex);
                String starch = servedCursor.getString(starchColumnIndex);
                double starchPrice = servedCursor.getDouble(starchPriceColumnIndex);
                String meat = servedCursor.getString(meatColumnIndex);
                double meatPrice = servedCursor.getDouble(meatPriceColumnIndex);
                String spreads = servedCursor.getString(spreadsColumnIndex);
                double spreadsPrice = servedCursor.getDouble(spreadsPriceColumnIndex);
                int userId = servedCursor.getInt(orderedByIndex);
                String servedTimeValue = servedCursor.getString(servedTimeIndex);

                // Calculate the total price
                double totalPrice = fruitPrice + cerealPrice + starchPrice + meatPrice + spreadsPrice;

                // Retrieve the username using the userId
                String orderedByName = dbHelper.getUserNameById(userId);

                // Update TextViews with data
                mealFruitTextView.setText("Fruit: " + fruit);
                fruitPriceTextView.setText("Price: " + String.format("%.2f", fruitPrice));
                mealCerealTextView.setText("Cereal: " + cereal);
                cerealPriceTextView.setText("Price: " + String.format("%.2f", cerealPrice));
                mealStarchTextView.setText("Starch: " + starch);
                starchPriceTextView.setText("Price: " + String.format("%.2f", starchPrice));
                mealMeatTextView.setText("Meat: " + meat);
                meatPriceTextView.setText("Price: " + String.format("%.2f", meatPrice));
                mealSpreadsTextView.setText("Spreads: " + spreads);
                spreadsPriceTextView.setText("Price: " + String.format("%.2f", spreadsPrice));
                totalPriceTextView.setText("Total Price: " + String.format("%.2f", totalPrice));
                orderedBy.setText("Made by: " + (orderedByName != null ? orderedByName : "Unknown"));
                servedTime.setText("Served Time: " + servedTimeValue);
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
