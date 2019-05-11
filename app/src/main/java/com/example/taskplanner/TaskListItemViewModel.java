package com.example.taskplanner;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskListItemViewModel extends BaseObservable {
    private Context context;
    private Task task;

    private List<DeleteCallback> deleteCallbacks = new ArrayList<DeleteCallback>();

    public TaskListItemViewModel(Context context, Task task) {
        this.context = context;
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void openTask() {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra("taskId", getTask().getId());
        context.startActivity(intent);
    }

    public void deleteTask() {
        DatabaseRepository repository = new DatabaseRepository(context);
        repository.open();
        repository.delete(getTask().getId());
        repository.close();

        for (DeleteCallback callback: deleteCallbacks) {
            callback.onDeleted(this);
        }
    }

    public void addOnDeleted(DeleteCallback callback) {
        deleteCallbacks.add(callback);
    }

    @Bindable
    public String getName() {
        return task.getName();
    }

    @Bindable
    public String getDescription() {
        return task.getDescription();
    }

    @Bindable
    public String getDate() {
        Date date = task.getDate();
        if (date != null){
            return new SimpleDateFormat("d MMM\nyyyy", Locale.US).format(date);
        } else {
            return null;
        }
    }

    @Bindable
    public Drawable getIcon() {
        TaskType type = task.getType();
        switch (type) {
            case Urgent:
                return context.getDrawable(R.drawable.icon_view_urgent);
            case Optional:
                return context.getDrawable(R.drawable.icon_view_optional);
            default:
                return context.getDrawable(R.drawable.icon_view);
        }
    }

    @Bindable
    public Uri getPhotoUri() {
        return task.getPhotoUri();
    }

    public interface DeleteCallback {
        void onDeleted(TaskListItemViewModel task);
    }
}
