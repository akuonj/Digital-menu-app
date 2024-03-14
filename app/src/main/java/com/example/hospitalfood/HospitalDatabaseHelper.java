package com.example.hospitalfood;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HospitalDatabaseHelper extends SQLiteOpenHelper {
    // Database and table names
    private static final String DATABASE_NAME = "Hospital.db";
    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_ID = "UserID";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";

    public HospitalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        createUsersTable(db);
        createBreakfastTable(db);
        createLunchTable(db);
        createDinnerTable(db);
        createDeletedOrdersTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade logic
        if (oldVersion < 5 && !isColumnExists(db, "Breakfast", "Fruit")) {
            db.execSQL("ALTER TABLE Breakfast ADD COLUMN Fruit TEXT;");
        }
    }

    private void createUsersTable(SQLiteDatabase db) {
        String createUserTableSQL = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUserTableSQL);
    }

    private void createBreakfastTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE Breakfast (" +
                "BreakfastID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "Fruit TEXT, " +
                "Cereal TEXT, " +
                "Starch TEXT, " +
                "Meat TEXT, " +
                "Spreads TEXT, " +
                "FOREIGN KEY(UserID) REFERENCES Users(UserID))";
        db.execSQL(createTableSQL);
    }

    private void createLunchTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE Lunch (" +
                "LunchID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "Soup TEXT, " +
                "Salad TEXT, " +
                "Vegetarian TEXT, " +
                "Blended TEXT, " +
                "NonVegetarian TEXT, " +
                "Starch TEXT, " +
                "Dessert TEXT, " +
                "FOREIGN KEY(UserID) REFERENCES Users(UserID))";
        db.execSQL(createTableSQL);
    }

    private void createDinnerTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE Dinner (" +
                "DinnerID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "Soup TEXT, " +
                "Salad TEXT, " +
                "Vegetarian TEXT, " +
                "NonVegetarian TEXT, " +
                "Starch TEXT, " +
                "Dessert TEXT, " +
                "FOREIGN KEY(UserID) REFERENCES Users(UserID))";
        db.execSQL(createTableSQL);
    }

    private void createDeletedOrdersTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE DeletedOrders (" +
                "DeletedOrderID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "Fruit TEXT, " +
                "Cereal TEXT, " +
                "Starch TEXT, " +
                "Meat TEXT, " +
                "Spreads TEXT, " +
                "FOREIGN KEY(UserID) REFERENCES Users(UserID))";
        db.execSQL(createTableSQL);
    }

    // Method to add a new user to the Users table
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1; // Returns true if insertion was successful
    }

    // Method to authenticate a user based on provided username and password
    public boolean authenticateUser(String username, String password, Context context) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean result = false;
        try {
            // Check if the Users table exists
            if (!isTableExists(db, TABLE_USERS)) {
                // Start the register activity
                Intent intent = new Intent(context, RegistrationActivity.class);
                context.startActivity(intent);
                return false;
            }

            // Continue with authentication if the Users table exists
            Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                    COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                    new String[]{username, password}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Authentication successful
                result = true;
                cursor.close();
            }
        } catch (SQLiteException e) {
            // Handle the error here, such as logging or displaying an error message
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    // Method to get the UserID of a user based on username
    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Default value if user not found
        try {
            Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                    COLUMN_USERNAME + " = ?",
                    new String[]{username}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // Log column names available in the cursor for debugging
                String[] columnNames = cursor.getColumnNames();
                for (String columnName : columnNames) {
                    Log.d("CursorColumn", "Column name: " + columnName);
                }

                // Retrieve the UserID column value
                int columnIndex = cursor.getColumnIndex(COLUMN_ID);
                if (columnIndex != -1) {
                    userId = cursor.getInt(columnIndex);
                } else {
                    Log.e("CursorColumn", "Column index not found for column: " + COLUMN_ID);
                }

                cursor.close();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return userId;
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
                new String[]{"table", tableName});
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int count = cursor.getInt(0);
                    return count > 0;
                }
            } finally {
                cursor.close();
            }
        }
        return false;
    }

    private boolean isColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        if (cursor != null) {
            try {
                int nameIndex = cursor.getColumnIndex("name");
                while (cursor.moveToNext()) {
                    String name = cursor.getString(nameIndex);
                    if (name != null && name.equals(columnName)) {
                        return true;
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return false;
    }
}
