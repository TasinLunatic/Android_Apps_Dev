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
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Task> taskList = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        myAdapter = new MyAdapter();
        ((ListView) findViewById(R.id.listView)).setAdapter(myAdapter);
        refreshTaskList();
    }

    public void handleSaveTask(View view) {
        EditText taskInput = (EditText) findViewById(R.id.taskInput);
        String task = taskInput.getText().toString().trim();

        if (task.isEmpty()) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        MyDatabaseOpener databaseOpener = new MyDatabaseOpener(this);

        SQLiteDatabase writableDatabase = databaseOpener.getWritableDatabase();
        writableDatabase.insert("todo", null, contentValues);
        writableDatabase.close();

        taskInput.setText("");
        this.refreshTaskList();
    }

    private void refreshTaskList() {
        MyDatabaseOpener databaseOpener = new MyDatabaseOpener(this);
        SQLiteDatabase readableDatabase = databaseOpener.getReadableDatabase();
        Cursor cursor = readableDatabase.query("todo", null, null, null, null, null, null);

        taskList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String task = cursor.getString(1);
            Task taskBean = new Task(id, task);
            taskList.add(taskBean);
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
            Task task = taskList.get(position);
            View taskView = LayoutInflater.from(MainActivity.this).inflate(R.layout.task_item, null);
            ((TextView) taskView.findViewById(R.id.taskMessage)).setText(task.getTask());
            taskView.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDatabaseOpener databaseOpener = new MyDatabaseOpener(MainActivity.this);
                    SQLiteDatabase writableDatabase = databaseOpener.getWritableDatabase();
                    writableDatabase.delete("todo", "id=?", new String[]{"" + task.getId()});
                    writableDatabase.close();
                    refreshTaskList();
                }
            });

            return taskView;
        }
    }
}
