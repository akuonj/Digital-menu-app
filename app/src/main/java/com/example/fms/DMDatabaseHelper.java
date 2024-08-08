package com.example.fms;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DMDatabaseHelper extends SQLiteOpenHelper {
    // Database and table names
    private static final String DATABASE_NAME = "Hospital.db";
    private static final int DATABASE_VERSION = 6; // Incremented version

    // Users table and columns
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_ID = "UserID";
    public static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";

    // Meals table
    private static final String TABLE_MEALS = "Meals";
    private static final String TABLE_SERVED_ORDERS = "ServedOrders";

    public DMDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        createUsersTable(db);
        createMealsTable(db);
        createServedOrdersTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion < 6) {
                if (isTableExists(db, TABLE_SERVED_ORDERS)) {
                    // Add columns if they don't exist
                    if (!isColumnExists(db, TABLE_SERVED_ORDERS, "OrderedBy")) {
                        db.execSQL("ALTER TABLE " + TABLE_SERVED_ORDERS + " ADD COLUMN OrderedBy INTEGER;");
                    }
                    if (!isColumnExists(db, TABLE_SERVED_ORDERS, "OrderedTime")) {
                        db.execSQL("ALTER TABLE " + TABLE_SERVED_ORDERS + " ADD COLUMN OrderedTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP;");
                    }
                    if (!isColumnExists(db, TABLE_SERVED_ORDERS, "ServedTime")) {
                        db.execSQL("ALTER TABLE " + TABLE_SERVED_ORDERS + " ADD COLUMN ServedTime TIMESTAMP;");
                    }
                }
            }
        } catch (Exception e) {
            Log.e("DBUpgradeError", "Error during database upgrade: " + e.getMessage());
        }
    }

    public void createUsersTable(SQLiteDatabase db) {
        String createUserTableSQL = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUserTableSQL);
    }

    private void createMealsTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_MEALS + " (" +
                "MealID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER, " +
                "Fruit TEXT, " +
                "Cereal TEXT, " +
                "Starch TEXT, " +
                "Meat TEXT, " +
                "Spreads TEXT, " +
                "OrderedBy INTEGER, " +
                "TimeOrdered TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(UserID) REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(OrderedBy) REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
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
                "OrderedBy INTEGER, " +
                "OrderedTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "ServedTime TIMESTAMP, " +
                "FOREIGN KEY(UserID) REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(OrderedBy) REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createTableSQL);
    }

    // Method to hash a password
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public String getCurrentTimeInKenyanTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Africa/Nairobi"));
        return sdf.format(new Date());
    }

    // Method to add a new user with hashed password
    public boolean addUser(String username, String plainPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, hashPassword(plainPassword)); // Hash the password
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1; // Returns true if insertion was successful
    }

    // Method to authenticate a user based on provided username and password
    public boolean authenticateUser(String username, String plainPassword, Context context) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean result = false;
        Cursor cursor = null;

        try {
            // Check if credentials are for admin
            if (username.equals("admin") && plainPassword.equals("admin")) {
                Intent intent = new Intent(context, AdminActivity.class);
                context.startActivity(intent);
                return true; // Successful login as admin
            }

            // Check if users table exists
            if (!isTableExists(db, TABLE_USERS)) {
                Intent intent = new Intent(context, RegistrationActivity.class);
                context.startActivity(intent);
                return false;
            }

            cursor = db.query(TABLE_USERS, new String[]{COLUMN_PASSWORD},
                    COLUMN_USERNAME + " = ?",
                    new String[]{username}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String hashedPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                result = BCrypt.checkpw(plainPassword, hashedPassword); // Check hashed password
                if (result) {
                    // If authenticated, proceed to the main activity
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.putExtra("USERNAME", username); // Pass username to the next activity
                    context.startActivity(intent);
                } else {
                    // Invalid credentials
                    Log.e("Authentication", "Invalid credentials for user: " + username);
                }
            }
        } catch (SQLiteException e) {
            Log.e("DBError", "Database error: " + e.getMessage());
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
                @SuppressLint("Range") int columnIndex = cursor.getColumnIndex(COLUMN_ID);
                if (columnIndex != -1) {
                    userId = cursor.getInt(columnIndex);
                } else {
                    Log.e("CursorColumn", "Column index not found for column: " + COLUMN_ID);
                }
            }
        } catch (SQLiteException e) {
            Log.e("DBError", "Database error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return userId;
    }

    // Method to get the username by UserID
    @SuppressLint("Range")
    public String getUserNameById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String username = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME},
                    COLUMN_ID + " = ?",
                    new String[]{String.valueOf(userId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
            }
        } catch (SQLiteException e) {
            Log.e("DBError", "Database error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return username;
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            int nameIndex = cursor.getColumnIndex("name");
            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIndex);
                if (columnName.equals(name)) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e("DBError", "Error checking column existence: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }
}
