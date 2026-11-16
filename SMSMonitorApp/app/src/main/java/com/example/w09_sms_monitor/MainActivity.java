package com.example.w09_sms_monitor;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private List<MySMS> smsList = new ArrayList<>();
    private MyAdapter myAdapter;
    private TextView emptyView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.sms_list);
        emptyView = findViewById(R.id.empty_view);
        MaterialButton refreshButton = findViewById(R.id.button);

        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        refreshButton.setOnClickListener(v -> handleRefreshSMS());

        // Request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    PERMISSION_REQUEST_CODE);
        } else {
            handleRefreshSMS();
            registerSmsObserver();
        }
    }

    private void registerSmsObserver() {
        // Listen to all SMS changes
        getContentResolver().registerContentObserver(
                Uri.parse("content://sms"),
                true,
                new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        super.onChange(selfChange);
                        runOnUiThread(() -> {
                            handleRefreshSMS();
                            Toast.makeText(MainActivity.this, "New SMS received!", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
        );
    }

    public void handleRefreshSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "SMS permission not granted", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    PERMISSION_REQUEST_CODE);
            return;
        }

        smsList.clear();

        // Try both URIs (some emulators work better with one or the other)
        readSmsFromUri(Uri.parse("content://sms/inbox"));
        if (smsList.isEmpty()) {
            readSmsFromUri(Uri.parse("content://sms/"));
        }

        myAdapter.notifyDataSetChanged();
        updateEmptyView();

        if (smsList.isEmpty()) {
            Toast.makeText(this, "No SMS found. Please send a test SMS first using Extended Controls → Phone", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Loaded " + smsList.size() + " SMS", Toast.LENGTH_SHORT).show();
        }
    }

    private void readSmsFromUri(Uri uri) {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    uri,
                    new String[]{"_id", "address", "subject", "body", "date"},
                    null, null, "date DESC"
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String address = cursor.getString(1);
                    String subject = cursor.getString(2);
                    String body = cursor.getString(3);
                    String date = cursor.getString(4);

                    // Avoid duplicates
                    boolean exists = false;
                    for (MySMS s : smsList) {
                        if (s.get_id() == id) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        smsList.add(new MySMS(id, address, subject, body, date));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void updateEmptyView() {
        if (smsList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleRefreshSMS();
                registerSmsObserver();
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied. Cannot read SMS.", Toast.LENGTH_LONG).show();
            }
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return smsList.size();
        }

        @Override
        public Object getItem(int position) {
            return smsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return smsList.get(position).get_id();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.list_item, parent, false);

            MySMS mySMS = smsList.get(position);

            TextView tvAddress = view.findViewById(R.id.address);
            TextView tvBody = view.findViewById(R.id.subject);
            TextView tvDate = view.findViewById(R.id.date);

            tvAddress.setText(mySMS.getAddress() != null ? mySMS.getAddress() : "Unknown");
            tvBody.setText(mySMS.getBody() != null ? mySMS.getBody() : "(No content)");

            try {
                long timestamp = Long.parseLong(mySMS.getDate());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                tvDate.setText(sdf.format(new Date(timestamp)));
            } catch (Exception e) {
                tvDate.setText(mySMS.getDate() != null ? mySMS.getDate() : "");
            }

            return view;
        }
    }
}