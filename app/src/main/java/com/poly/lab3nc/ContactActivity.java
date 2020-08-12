package com.poly.lab3nc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    ListView lv_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        lv_contact = findViewById(R.id.lv_contact);

        String patch = "content://contacts/people";
        Uri uri = Uri.parse(patch);
        // sử dụng curosrLoader
        CursorLoader cursorLoader = new CursorLoader(this, uri, null, null, null, null);

        // hoi xin quyen nguoi dung
        if (ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            // chay query vao duong patch

            Cursor cursor = cursorLoader.loadInBackground();
            if (cursor.getCount() > 0) {
                Toast.makeText(this, "Co danh ba: " + cursor.getCount(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Khong danh ba: " + cursor.getCount(), Toast.LENGTH_SHORT).show();
            }

            cursor.moveToFirst();
            List<String> listContact = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                String s = "";
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                s += id + " - " + name;
                listContact.add(s);
                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContact);
            lv_contact.setAdapter(arrayAdapter);
        } else {
            // xin quyen
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 304);
        }
    }
    // overide lai ham nay

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "read contact", Toast.LENGTH_SHORT).show();
        }
    }
}
