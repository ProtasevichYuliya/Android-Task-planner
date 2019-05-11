package com.example.taskplanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView taskList;
    DatabaseHelper databaseHelper;
    DatabaseRepository databaseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Обработка нажатия на кнопку
        FloatingActionButton fab = findViewById(R.id.fab);
        final MainActivity that = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(that, TaskActivity.class);
                startActivity(intent);
            }
        });

        taskList = (ListView)findViewById(R.id.listTasks);
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        databaseRepository = new DatabaseRepository(this.getApplicationContext());
        databaseRepository.open();

        List<Task> tasksList = databaseRepository.getTasks();
        List<TaskListItemViewModel> taskViewModelList = new ArrayList<TaskListItemViewModel>();
        TaskListViewAdapter listViewAdapter = new TaskListViewAdapter(taskViewModelList);

        for (Task task : tasksList) {
            TaskListItemViewModel taskViewModel = new TaskListItemViewModel(this, task);
            taskViewModelList.add(taskViewModel);
            taskViewModel.addOnDeleted(t -> {
                taskViewModelList.remove(t);
                listViewAdapter.notifyDataSetInvalidated();
            });
        }

        taskList.setAdapter(listViewAdapter);
        databaseRepository.close();
    }

    public void addNewTask(View view)
    {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }
}
