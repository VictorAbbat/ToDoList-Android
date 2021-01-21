package ca.victor.mytodolist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.victor.mytodolist.models.Tasks;

public class DatabaseAdapter {

    public DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;
    public Context context;

    public DatabaseAdapter(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public void open(){
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteDatabase.close();
    }

    public long addData(Tasks tasks)
    {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_1,tasks.name);
        values.put(DatabaseHelper.COLUMN_2,tasks.description);
        values.put(DatabaseHelper.COLUMN_3,tasks.important);
        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE,null,values);
        close();
        return id;
    }

    public List<Tasks> getData()
    {
        open();
        List<Tasks> tasksList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE, null, null,null,null,null,null);
        while (cursor.moveToNext()){
            tasksList.add(new Tasks(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_1)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_2)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_3))));
        }
        cursor.close();
        return tasksList;
    }

    public Tasks getById(long position) {
        open();
        String[] selectionId = {String.valueOf(position)};
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE, null, DatabaseHelper._ID + " = ?", selectionId,null,null,null);
        cursor.moveToFirst();
        Tasks tasks = new Tasks(cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_1)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_2)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_3)));

        cursor.close();
        close();
        return tasks;
    }

    public void modifyById(long id, Tasks tasks){
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_1, tasks.name);
        values.put(DatabaseHelper.COLUMN_2, tasks.description);
        values.put(DatabaseHelper.COLUMN_3, tasks.important);
        String selection = DatabaseHelper._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        sqLiteDatabase.update(DatabaseHelper.TABLE, values, selection, selectionArgs);
        close();
    }

    public void deleteById(long id){
        open();
        String selection = DatabaseHelper._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        sqLiteDatabase.delete(DatabaseHelper.TABLE, selection, selectionArgs);
        close();
    }

    public void allDelete() {
        open();
        sqLiteDatabase.delete(DatabaseHelper.TABLE, null, null);
        close();
    }

}
