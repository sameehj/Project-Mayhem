package com.example.personel.traverlgroup;

import android.app.Activity;
import android.app.Notification;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;

import java.security.AccessControlContext;
import java.util.Random;

/**
 * Created by khalid on 17.11.15
 */
public class validatePhoneNumber extends Activity {
    String number;
    String arbitNumber;
    private void sendSms(){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, "Traveler Group", arbitNumber, null, null);

    }

    public validatePhoneNumber(String number)
    {
        this.number = number;
        Random rn = new Random();
        Integer n = 9999 - 1111 + 1;
        Integer i = rn.nextInt() % n;
        arbitNumber = i.toString();
    }

    public boolean valideIt(ContentResolver c)
    {
        sendSms();
        return waitForAnswer(c);
    }

    /**
     * Waiting for 20 seconds, if no message relieved return error validating
     */
    public boolean waitForAnswer(ContentResolver c)
    {
        int trys = 10;
        if((--trys)==0)
        {
            return false;
        }
        Cursor cursor = c.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int count = 0;
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                    String body2 = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                if(body2.equals(arbitNumber))
                {
                    return true;
                }
                // use msgData
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }
        return waitForAnswer(c);
    }
}
