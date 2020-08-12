package com.poly.lab3nc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CallLogActivity extends AppCompatActivity {
    ListView lv_callLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);
        lv_callLog = findViewById(R.id.lv_callLog);

        // hoi quyen nguoi dung
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            String[] projection = new String[]{
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.DATE,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DURATION,
            };

            // sử dụng getContentResolver
            Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, CallLog.Calls.DURATION + "<?",
                    new String[]{"30"}, CallLog.Calls.DATE + " ASC");

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                List<String> listCall = new ArrayList<>();
                while (!cursor.isAfterLast()) {

                    String s = "";
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));

                    // chuyển từ giây sang ngày
                    Calendar calendar = Calendar.getInstance();
                    long t = Long.valueOf(date);
                    calendar.setTimeInMillis(t);
                    Log.e("day", calendar.getTime() + "");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String day = simpleDateFormat.format(calendar.getTime());

                    s += "Name: " + name + "\nDate: " + day + "\nNumber: " + number + "\nDuration: " + duration;
                    listCall.add(s);
                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listCall);
                lv_callLog.setAdapter(arrayAdapter);
            } else {
                Toast.makeText(this, "Khong co nhat ky cuoc goi", Toast.LENGTH_SHORT).show();
            }

        } else {
            // xin quyen
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 96);
        }

    }
    // overide lai ham

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Read call log", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Don't read call log", Toast.LENGTH_SHORT).show();
        }
    }
}
