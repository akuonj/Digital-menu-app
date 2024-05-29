package com.example.fms;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FMSDatabaseHelper extends SQLiteOpenHelper {
    // Database and table names
    private static final String DATABASE_NAME = "Hospital.db";
    private static final int DATABASE_VERSION = 5;

    // Users table and columns
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_ID = "UserID";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";

    // Other tables
    private static final String TABLE_BREAKFAST = "Breakfast";
    private static final String TABLE_LUNCH = "Lunch";
    private static final String TABLE_DINNER = "Dinner";
    private static final String TABLE_SERVED_ORDERS = "ServedOrders";

    public FMSDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        createUsersTable(db);
        createBreakfastTable(db);
        createLunchTable(db);
        createDinnerTable(db);
        createServedOrdersTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5 && !isColumnExists(db, TABLE_BREAKFAST, "Fruit")) {
            db.execSQL("ALTER TABLE " + TABLE_BREAKFAST + " ADD COLUMN Fruit TEXT;");
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
        String createTableSQL = "CREATE TABLE " + TABLE_BREAKFAST + " (" +
                "BreakfastID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "Fruit TEXT, " +
                "Cereal TEXT, " +
                "Starch TEXT, " +
                "Meat TEXT, " +
                "Spreads TEXT, " +
                "FOREIGN KEY(UserID) REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createTableSQL);
    }

    private void createLunchTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_LUNCH + " (" +
                "LunchID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "Soup TEXT, " +
                "Salad TEXT, " +
                "Vegetarian TEXT, " +
                "Blended TEXT, " +
                "NonVegetarian TEXT, " +
                "Starch TEXT, " +
                "Dessert TEXT, " +
                "FOREIGN KEY(UserID) REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createTableSQL);
    }

    private void createDinnerTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_DINNER + " (" +
                "DinnerID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "Soup TEXT, " +
                "Salad TEXT, " +
                "Vegetarian TEXT, " +
                "NonVegetarian TEXT, " +
                "Starch TEXT, " +
                "Dessert TEXT, " +
                "FOREIGN KEY(UserID) REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createTableSQL);
    }

    private void createServedOrdersTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_SERVED_ORDERS + " (" +
                "ServedOrderID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "Fruit TEXT, " +
                "Cereal TEXT, " +
                "Starch TEXT, " +
                "Meat TEXT, " +
                "Spreads TEXT, " +
                "FOREIGN KEY(UserID) REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
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
        Cursor cursor = null;
        try {
            if (!isTableExists(db, TABLE_USERS)) {
                Intent intent = new Intent(context, RegistrationActivity.class);
                context.startActivity(intent);
                return false;
            }
            cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                    COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                    new String[]{username, password}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                result = true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return result;
    }

    // Method to get the UserID of a user based on username
    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                    COLUMN_USERNAME + " = ?",
                    new String[]{username}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(COLUMN_ID);
                if (columnIndex != -1) {
                    userId = cursor.getInt(columnIndex);
                } else {
                    Log.e("CursorColumn", "Column index not found for column: " + COLUMN_ID);
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
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