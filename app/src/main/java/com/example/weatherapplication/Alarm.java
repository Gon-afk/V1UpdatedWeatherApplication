package com.example.weatherapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;

public class Alarm extends AppCompatActivity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alarm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        readAllMessages();
        readCallLogs();
        Notification.createNotificationChannel(this);
        setAlarm();
    }
    private void setAlarm(){
        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent= new Intent(this, AlarmReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0, intent, PendingIntent.FLAG_IMMUTABLE);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, 3);

            if (alarmManager != null)
            {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Permission denied to schedule alarm", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, "An exception occur: " + e, Toast.LENGTH_SHORT).show();
        }
    }
    private void readAllMessages(){
        ListView listView = findViewById(R.id.list_View);

        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, null,null,null,null);

        String[] fromColNames = new String[]{"address", "body"};
        int[] toTexviewIds = new int[]{android.R.id.text1, android.R.id.text2};
        if (cursor.moveToFirst()) {
//            do {
//                // String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
//                // String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
//                // Toast.makeText(this, " "+ address+" "+body, Toast.LENGTH_SHORT).show();

//            } while (cursor.moveToNext());
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
                    cursor, fromColNames, toTexviewIds, 0);
            listView.setAdapter(cursorAdapter);
        }
    }

    private  void readCallLogs() {
        ListView listView = findViewById(R.id.log_ListView);
        Cursor cursor = getContentResolver().query((CallLog.Calls.CONTENT_URI), null, null, null, CallLog.Calls.DATE + " DESC");

        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);

       String[] fromColNames = new String[]{CallLog.Calls.NUMBER, CallLog.Calls.DATE};
       int[] toTexviewIds = new int[]{android.R.id.text1, android.R.id.text2};

        if (cursor.moveToFirst()) {
            do {
                String phNumber = cursor.getString(number);
                String callDate = cursor.getString(date);
                String calltype = cursor.getString(type);
                Date callDayTime = new Date(Long.valueOf(callDate));
                switch (calltype){
                    case "1":
                        calltype = "Incoming";
                        break;
                    case "2":
                        calltype = "Outgoing";
                        break;
                    case "3":
                        calltype = "Missed";
                        break;
                }
                Toast.makeText(this, "Phone: "+ phNumber+"Type: "+ calltype +"Date: "+callDayTime, Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());

            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
                    cursor, fromColNames, toTexviewIds, 0);
            listView.setAdapter(cursorAdapter);

        }
    }
}