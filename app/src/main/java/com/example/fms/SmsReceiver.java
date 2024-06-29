package com.example.fms;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.database.sqlite.SQLiteDatabase;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsReceiver extends BroadcastReceiver {
    private static final int SMS_SEND_PERMISSION_REQUEST = 1;
    private static final int SMS_RECEIVE_PERMISSION_REQUEST = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if SEND_SMS permission is granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Request the SEND_SMS permission
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, SMS_SEND_PERMISSION_REQUEST);
            return;
        }

        // Check if RECEIVE_SMS permission is granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Request the RECEIVE_SMS permission
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_RECEIVE_PERMISSION_REQUEST);
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getOriginatingAddress(); // Sender's phone number
                    String message = smsMessage.getMessageBody(); // SMS message content

                    // Check if the sender's phone number is authorized (e.g., 0741435350)
                    if (sender.equals("0741435350")) {
                        // Assuming the message is the patient's IDNumber
                        String patientIDNumber = message;

                        // Perform a database lookup to retrieve breakfast order details
                        String breakfastOrderDetails = retrieveBreakfastOrder(context, patientIDNumber);

                        // Send the breakfast order details as an SMS response
                        sendSMS(context, sender, breakfastOrderDetails);
                    }
                }
            }
        }
    }

    // Implement this method to retrieve breakfast order details based on the patient's IDNumber
    private String retrieveBreakfastOrder(Context context, String patientIDNumber) {
        DMDatabaseHelper dbHelper = new DMDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "Fruit",
                "Cereal",
                "Starch",
                "Meat",
                "Spreads"
        };

        String selection = "IDNumber = ?";
        String[] selectionArgs = {patientIDNumber};

        Cursor breakfastCursor = db.query(
                "Breakfast",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String breakfastOrderDetails = "Breakfast Order Details: ";

        if (breakfastCursor.moveToFirst()) {
            int breakfastFruitColumnIndex = breakfastCursor.getColumnIndex("Fruit");
            int patientCerealColumnIndex = breakfastCursor.getColumnIndex("Cereal");
            int patientStarchColumnIndex = breakfastCursor.getColumnIndex("Starch");
            int patientMeatColumnIndex = breakfastCursor.getColumnIndex("Meat");
            int patientSpreadsColumnIndex = breakfastCursor.getColumnIndex("Spreads");

            String breakfastFruit = breakfastCursor.getString(breakfastFruitColumnIndex);
            String breakfastCereal = breakfastCursor.getString(patientCerealColumnIndex);
            String breakfastStarch = breakfastCursor.getString(patientStarchColumnIndex);
            String breakfastMeat = breakfastCursor.getString(patientMeatColumnIndex);
            String breakfastSpreads = breakfastCursor.getString(patientSpreadsColumnIndex);

            breakfastOrderDetails += "\nFruit: " + breakfastFruit
                    + "\nCereal: " + breakfastCereal
                    + "\nStarch: " + breakfastStarch
                    + "\nMeat: " + breakfastMeat
                    + "\nSpreads: " + breakfastSpreads;

            breakfastCursor.close();
        } else {
            breakfastOrderDetails = "No breakfast order found for IDNumber: " + patientIDNumber;
        }

        db.close();

        return breakfastOrderDetails;
    }

    private void sendSMS(Context context, String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
