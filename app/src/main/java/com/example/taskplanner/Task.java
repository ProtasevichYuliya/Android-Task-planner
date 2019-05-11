package com.example.taskplanner;

import android.net.Uri;

import java.util.Date;

public class Task {
    private long id;
    private String name;
    private String description;
    private java.util.Date date;
    private TaskType type;
    private Uri photoUri;

    public Task(){
    }

    public Task (long id, String name, String description, Date date, TaskType type, Uri photoUri){
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.type = type;
        this.photoUri = photoUri;
    }

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Date getDate(){
        return this.date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public TaskType getType(){
        return this.type;
    }

    public void setType(TaskType type){
        this.type = type;
    }

    public Uri getPhotoUri() {
        return this.photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }
}


