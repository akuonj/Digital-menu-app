package com.example.hospitalfood;

import android.app.PendingIntent;
import android.content.Context;
import android.Manifest;
import android.telephony.SmsManager;

public class SmsSender {
    public static void sendSMS(Context context, String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
