package com.example.taskplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseRepository {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseRepository(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseRepository open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllEntries(){
        String[] columns = new String[] {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_TYPE,
                DatabaseHelper.COLUMN_PHOTO_URI};
        return  database.query(DatabaseHelper.TABLE, columns, null, null, null, null, null);
    }

    public List<Task> getTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst()){
            do{
                tasks.add(readCursor(cursor));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  tasks;
    }

    public long getCount() {
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE);
    }

    public Task getTask(long id){
        Task task = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            task = readCursor(cursor);
        }
        cursor.close();
        return  task;
    }

    public long insert(Task task){
        return  database.insert(DatabaseHelper.TABLE, null, this.writeContentValues(task));
    }

    public long delete(long taskId){

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(taskId)};
        return database.delete(DatabaseHelper.TABLE, whereClause, whereArgs);
    }

    public long update(Task task){

        String whereClause = DatabaseHelper.COLUMN_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(task.getId())};
        return database.update(DatabaseHelper.TABLE, this.writeContentValues(task), whereClause, whereArgs);
    }

    private Task readCursor(Cursor cursor) {
        long id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = formatter.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)));
        }
        catch (java.lang.Exception exception) {
            date = null;
        }

        TaskType type = TaskType.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE)));
        String photoUriString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHOTO_URI));
        Uri photoUri = null;
        if (photoUriString != null && !photoUriString.equals("")) {
            photoUri = Uri.parse(photoUriString);
        }

        return new Task(id, name, description, date, type, photoUri);
    }

    private ContentValues writeContentValues(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, task.getName());
        cv.put(DatabaseHelper.COLUMN_DESCRIPTION, task.getDescription());
        if (task.getDate() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            cv.put(DatabaseHelper.COLUMN_DATE, formatter.format(task.getDate()));
        } else {
            cv.putNull(DatabaseHelper.COLUMN_DATE);
        }
        cv.put(DatabaseHelper.COLUMN_TYPE, task.getType().name());
        if (task.getPhotoUri() != null) {
            cv.put(DatabaseHelper.COLUMN_PHOTO_URI, task.getPhotoUri().toString());
        } else {
            cv.put(DatabaseHelper.COLUMN_PHOTO_URI, "");
        }

        return cv;
    }
}
