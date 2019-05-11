package com.example.taskplanner;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskViewModel extends BaseObservable {
    private Context context;
    private DatabaseRepository database;
    private Task task;
    private Uri lastPhotoUri;

    public TaskViewModel(Context context, long taskId){
        this.context = context;
        this.database = new DatabaseRepository(context);

        if (taskId == 0) {
            this.task = new Task();
            this.task.setType(TaskType.Normal);
        } else {
            this.database.open();
            this.task = database.getTask(taskId);
            this.database.close();
        }
    }

    public void save(){
        database.open();
        if (task.getId() > 0) {
            database.update(task);
        } else {
            database.insert(task);
        }

        database.close();
    }

    public Intent takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            return null;
        }

        File photoFile;
        try {
            photoFile = createPhotoFile();
        } catch (java.io.IOException exception) {
            return null;
        }

        Uri photoUri = FileProvider.getUriForFile(context.getApplicationContext(), "com.example.android.fileprovider", photoFile);
        lastPhotoUri = photoUri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        return intent;
    }

    public void completeTakePhoto(Intent data) {
        setPhotoUri(lastPhotoUri);
    }

    private File createPhotoFile() throws java.io.IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        return file;
    }

    public Intent selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        return intent;
    }

    public void completeSelectPhoto(Intent data) {
        Uri uri = data.getData();
        //context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        setPhotoUri(uri);
    }

    @Bindable
    public String getName() {
        return task.getName();
    }

    public void setName(String name) {
        if (task.getName() != name) {
            task.setName(name);
            notifyPropertyChanged(BR.name);
        }
    }

    @Bindable
    public String getDescription() {
        return task.getDescription();
    }

    public void setDescription(String description) {
        if (task.getDescription() != description) {
            task.setDescription(description);
            notifyPropertyChanged(BR.description);
        }
    }

    @Bindable
    public Date getDate() {
        return task.getDate();
    }

    public void setDate(Date date) {
        if (task.getDate() != date) {
            task.setDate(date);
            notifyPropertyChanged(BR.date);
            notifyPropertyChanged(BR.dateText);
        }
    }

    @Bindable
    public String getDateText() {
        Date date = task.getDate();
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(date);
        } else {
            return null;
        }
    }

    public void setDateText(String date) {
        Date parsedDate = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            parsedDate = format.parse(date);
        }
        catch (java.lang.Exception exception) {
        }

        if (task.getDate() != parsedDate) {
            task.setDate(parsedDate);
            notifyPropertyChanged(BR.date);
            notifyPropertyChanged(BR.dateText);
        }
    }

    @Bindable
    public TaskType getType() {
        return task.getType();
    }

    public void setType(TaskType type) {
        if (task.getType() != type) {
            task.setType(type);
            notifyPropertyChanged(BR.type);
            notifyPropertyChanged(BR.isUrgentTask);
            notifyPropertyChanged(BR.isNormalTask);
            notifyPropertyChanged(BR.isOptionalTask);
        }
    }

    @Bindable
    public Boolean getIsUrgentTask() {
        return task.getType() == TaskType.Urgent;
    }

    public void setIsUrgentTask(Boolean value) {
        TaskType newType = value ? TaskType.Urgent : task.getType();
        if (task.getType() != newType) {
            task.setType(newType);
            notifyPropertyChanged(BR.type);
            notifyPropertyChanged(BR.isUrgentTask);
            notifyPropertyChanged(BR.isNormalTask);
            notifyPropertyChanged(BR.isOptionalTask);
        }
    }

    @Bindable
    public Boolean getIsNormalTask() {
        return task.getType() == TaskType.Normal;
    }

    public void setIsNormalTask(Boolean value) {
        TaskType newType = value ? TaskType.Normal : task.getType();
        if (task.getType() != newType) {
            task.setType(newType);
            notifyPropertyChanged(BR.type);
            notifyPropertyChanged(BR.isUrgentTask);
            notifyPropertyChanged(BR.isNormalTask);
            notifyPropertyChanged(BR.isOptionalTask);
        }
    }

    @Bindable
    public Boolean getIsOptionalTask() {
        return task.getType() == TaskType.Optional;
    }

    public void setIsOptionalTask(Boolean value) {
        TaskType newType = value ? TaskType.Optional : task.getType();
        if (task.getType() != newType) {
            task.setType(newType);
            notifyPropertyChanged(BR.type);
            notifyPropertyChanged(BR.isUrgentTask);
            notifyPropertyChanged(BR.isNormalTask);
            notifyPropertyChanged(BR.isOptionalTask);
        }
    }

    @Bindable
    public Uri getPhotoUri() {
        return task.getPhotoUri();
    }

    public void setPhotoUri(Uri uri) {
        if (task.getPhotoUri() != uri) {
            task.setPhotoUri(uri);
            notifyPropertyChanged(BR.photoUri);
        }
    }

}
