package com.example.BigHack2014;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RunsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_BITMAP, MySQLiteHelper.COLUMN_DATE };

    public RunsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Run createRun(Run run) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BITMAP, convertBitmap(run.getBitmap()));
        values.put(MySQLiteHelper.COLUMN_DATE, run.getDate().toString());
        long insertId = database.insert(MySQLiteHelper.TABLE_MAPS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MAPS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Run newRun = cursorToRun(cursor);
        cursor.close();
        return newRun;
    }

    public void deleteRun(Run run) {
        long id = run.getId();
        System.out.println("Run deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_MAPS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Run> getAllRuns() {
        List<Run> runs = new ArrayList<Run>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_MAPS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Run run = cursorToRun(cursor);
            runs.add(run);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return runs;
    }

    private Run cursorToRun(Cursor cursor) {
        Run run = new Run();
        run.setId(cursor.getLong(0));
        run.setDate(new Date(cursor.getString(2)));
        run.setBitmap(BitmapFactory.decodeByteArray(cursor.getBlob(1), 0, cursor.getBlob(1).length));
        return run;
    }

    private byte[] convertBitmap(Bitmap b){
        int bytes = b.getByteCount();

        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
        b.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

        byte[] array = buffer.array(); //Get the underlying array containing the data.
        return array;
    }
}