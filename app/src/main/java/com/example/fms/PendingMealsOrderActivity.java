package com.example.fms;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class PendingMealsOrderActivity extends AppCompatActivity {
    private TextView mealFruitTextView;
    private TextView mealCerealTextView;
    private TextView mealStarchTextView;
    private TextView mealMeatTextView;
    private TextView mealSpreadsTextView;
    private TextView orderedByTextView;
    private TextView orderedTimeTextView;

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

        mealFruitTextView = findViewById(R.id.mealFruitTextView);
        mealCerealTextView = findViewById(R.id.mealCerealTextView);
        mealStarchTextView = findViewById(R.id.mealStarchTextView);
        mealMeatTextView = findViewById(R.id.mealMeatTextView);
        mealSpreadsTextView = findViewById(R.id.mealSpreadsTextView);
        orderedByTextView = findViewById(R.id.orderedBy);
        orderedTimeTextView = findViewById(R.id.orderedTime);

        previousButton = findViewById(R.id.previousButton);
        markAsServedButton = findViewById(R.id.markAsServedButton);
        nextButton = findViewById(R.id.nextButton);

        dbHelper = new DMDatabaseHelper(this);
        mealOrderIds = getAllMealOrderIds();

        showDataForCurrentIndex();

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex > 0) {
                    currentIndex--;
                    showDataForCurrentIndex();
                } else {
                    Toast.makeText(PendingMealsOrderActivity.this, "No previous orders", Toast.LENGTH_SHORT).show();
                }
            }
        });

        markAsServedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex < mealOrderIds.size() - 1) {
                    currentIndex++;
                    showDataForCurrentIndex();
                } else {
                    Toast.makeText(PendingMealsOrderActivity.this, "No more orders", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("Range")
    private ArrayList<Integer> getAllMealOrderIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Meals", new String[]{"MealID"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            ids.add(cursor.getInt(cursor.getColumnIndex("MealID")));
        }
        cursor.close();
        db.close();
        return ids;
    }

    @SuppressLint("Range")
    private void showDataForCurrentIndex() {
        if (mealOrderIds.size() > 0) {
            int mealId = mealOrderIds.get(currentIndex);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("Meals", null, "MealID = ?", new String[]{String.valueOf(mealId)}, null, null, null);
            if (cursor.moveToFirst()) {
                logCursorColumns(cursor);

                mealFruitTextView.setText("Fruit: " + cursor.getString(cursor.getColumnIndex("Fruit")));
                mealCerealTextView.setText("Cereal: " + cursor.getString(cursor.getColumnIndex("Cereal")));
                mealStarchTextView.setText("Starch: " + cursor.getString(cursor.getColumnIndex("Starch")));
                mealMeatTextView.setText("Meat: " + cursor.getString(cursor.getColumnIndex("Meat")));
                mealSpreadsTextView.setText("Spreads: " + cursor.getString(cursor.getColumnIndex("Spreads")));

                int userId = cursor.getInt(cursor.getColumnIndex("OrderedBy"));
                String orderedBy = dbHelper.getUserNameById(userId);
                orderedByTextView.setText("Made by: " + (orderedBy != null ? orderedBy : "Unknown"));

                String timeOrdered = cursor.getString(cursor.getColumnIndex("TimeOrdered"));
                orderedTimeTextView.setText("Time-Ordered: " + timeOrdered);
            }
            cursor.close();
            db.close();
        } else {
            mealFruitTextView.setText("Fruit: ");
            mealCerealTextView.setText("Cereal: ");
            mealStarchTextView.setText("Starch: ");
            mealMeatTextView.setText("Meat: ");
            mealSpreadsTextView.setText("Spreads: ");
            orderedByTextView.setText("Made by: ");
            orderedTimeTextView.setText("Time-Ordered: ");
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault()); // Use default timezone
        return sdf.format(new Date());
    }

    private void markCurrentOrderAsServed() {
        if (mealOrderIds.size() > 0) {
            int mealId = mealOrderIds.get(currentIndex);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                String currentTime = getCurrentTime(); // Get current time for served time
                db.execSQL("INSERT INTO ServedOrders (UserID, Fruit, Cereal, Starch, Meat, Spreads, OrderedBy, OrderedTime, ServedTime) " +
                                "SELECT UserID, Fruit, Cereal, Starch, Meat, Spreads, OrderedBy, TimeOrdered, ? FROM Meals WHERE MealID = ?",
                        new Object[]{currentTime, mealId});
                db.delete("Meals", "MealID = ?", new String[]{String.valueOf(mealId)});
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to mark order as served", Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }
        }
    }

    private void logCursorColumns(Cursor cursor) {
        String[] columnNames = cursor.getColumnNames();
        for (String columnName : columnNames) {
            int columnIndex = cursor.getColumnIndex(columnName);
            Log.d("CursorColumn", "Column: " + columnName + " Index: " + columnIndex);
        }
    }
}
