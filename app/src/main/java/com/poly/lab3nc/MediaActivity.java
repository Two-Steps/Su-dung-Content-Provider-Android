package com.poly.lab3nc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MediaActivity extends AppCompatActivity {
    ListView lv_media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        lv_media = findViewById(R.id.lv_media);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            String[] projection = new String[]{
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    MediaStore.MediaColumns.DATE_ADDED,
                    MediaStore.MediaColumns.MIME_TYPE
            };

            CursorLoader cursorLoader = new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            Log.e("count", cursor.getCount() + "");

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                List<String> listMedia = new ArrayList<>();
                while (!cursor.isAfterLast()) {
                    String s = "";
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
                    String date = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED));
                    String type = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));

                    // chuyển từ giây sang ngày
                    Calendar calendar = Calendar.getInstance();
                    long second = Long.valueOf(date);
                    calendar.setTimeInMillis(second * 1000);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String day = simpleDateFormat.format(calendar.getTime());
                    Log.e("day", calendar.getTime() + " ");

                    s += "name: " + name + "\ndate: " + day + "\ntype: " + type;

                    listMedia.add(s);
                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listMedia);
                lv_media.setAdapter(arrayAdapter);
            } else {
                Toast.makeText(this, "media is emty", Toast.LENGTH_SHORT).show();
            }

        } else {
            // nếu chưa cấp quyền thì xin
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 112);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "read media", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "don't read media", Toast.LENGTH_SHORT).show();
        }
    }
}
