package com.example.w07_sqllite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Task> taskList = new ArrayList<>();
    private MyAdapter myAdapter;
    private EditText taskInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskInput = findViewById(R.id.taskInput);
        ImageView addButton = findViewById(R.id.imageView);

        myAdapter = new MyAdapter();
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(myAdapter);

        // Click listener for the + button (more reliable than android:onClick)
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveTask();
            }
        });

        // Also allow pressing Enter / Done on keyboard to add
        taskInput.setOnEditorActionListener((v, actionId, event) -> {
            handleSaveTask();
            return true;
        });

        refreshTaskList();
    }

    private void handleSaveTask() {
        String task = taskInput.getText().toString().trim();

        if (task.isEmpty()) {
            Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);

        MyDatabaseOpener databaseOpener = new MyDatabaseOpener(this);
        SQLiteDatabase writableDatabase = databaseOpener.getWritableDatabase();
        writableDatabase.insert("todo", null, contentValues);
        writableDatabase.close();

        taskInput.setText("");
        refreshTaskList();

        Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
    }

    private void refreshTaskList() {
        MyDatabaseOpener databaseOpener = new MyDatabaseOpener(this);
        SQLiteDatabase readableDatabase = databaseOpener.getReadableDatabase();
        Cursor cursor = readableDatabase.query("todo", null, null, null, null, null, null);

        taskList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String task = cursor.getString(1);
            taskList.add(new Task(id, task));
        }
        cursor.close();
        readableDatabase.close();
        myAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return taskList.size();
        }

        @Override
        public Object getItem(int position) {
            return taskList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return taskList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Task task = taskList.get(position);

            View taskView = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.task_item, parent, false);

            TextView taskMessage = taskView.findViewById(R.id.taskMessage);
            taskMessage.setText(task.getTask());

            ImageView deleteButton = taskView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDatabaseOpener databaseOpener = new MyDatabaseOpener(MainActivity.this);
                    SQLiteDatabase writableDatabase = databaseOpener.getWritableDatabase();
                    writableDatabase.delete("todo", "id=?", new String[]{String.valueOf(task.getId())});
                    writableDatabase.close();
                    refreshTaskList();
                    Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                }
            });

            return taskView;
        }
    }
}