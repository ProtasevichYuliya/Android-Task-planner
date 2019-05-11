package com.example.taskplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.taskplanner.databinding.ActivityTaskBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    private TaskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTaskBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_task);
        viewModel = new TaskViewModel(this, getPassedTaskId());
        binding.setViewModel(viewModel);
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        Date date = this.viewModel.getDate();
        if (date == null) {
            date = Calendar.getInstance().getTime();
        }

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                viewModel.setDate(new Date(year - 1900, month, dayOfMonth));
            }
        },  date.getYear() + 1900,
            date.getMonth(),
            date.getDate()).show();
    }

    public void save(View view){
        String name = viewModel.getName();
        if (name.equals("")) {
            Toast.makeText(this, "Название обязательне поле", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.save();
        goHome();
    }

    public void cancel(View view){
        goHome();
    }

    public void selectPhoto(View view) {
        Intent intent = viewModel.selectPhoto();
        startActivityForResult(Intent.createChooser(intent, "Выбрать изображение"), 1);
    }

    public void takePhoto(View view) {
        Intent intent = viewModel.takePhoto();
        if (intent != null) {
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    viewModel.completeSelectPhoto(data);
                }
                return;
            case 2:
                if (resultCode == RESULT_OK) {
                    viewModel.completeTakePhoto(data);
                }
                return;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private long getPassedTaskId() {
        long taskId = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("taskId")) {
            taskId = extras.getLong("taskId");
        }

        return taskId;
    }
}
