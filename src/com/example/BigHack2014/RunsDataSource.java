package com.example.BigHack2014;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RunsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_RUN };

    public RunsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Run createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_RUN, comment);
        long insertId = database.insert(MySQLiteHelper.TABLE_MAPS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MAPS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Run newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteComment(Run comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_MAPS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Run> getAllComments() {
        List<Run> comments = new ArrayList<Run>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_MAPS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Run comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Run cursorToComment(Cursor cursor) {
        Run comment = new Run();
        comment.setId(cursor.getLong(0));
        comment.setDate(new Date());
        //comment.setBitmap();
        return comment;
    }
}