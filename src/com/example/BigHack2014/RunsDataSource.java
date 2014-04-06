package com.example.BigHack2014;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RunsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_BITMAP, MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_NAME };

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
        String path = Environment.getExternalStorageDirectory().toString();
        values.put(MySQLiteHelper.COLUMN_BITMAP, run.getBitmap());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put(MySQLiteHelper.COLUMN_DATE, dateFormat.format(run.getDate()));
        values.put(MySQLiteHelper.COLUMN_NAME, run.getName());
        long insertId = database.insert(MySQLiteHelper.TABLE_MAPS, null,
                values);
        Log.e("ID", ""+insertId);
        Cursor cursor = null;
        cursor = database.query(MySQLiteHelper.TABLE_MAPS,
                allColumns, null, null,
                null, null, null);

        Log.e("CUROSE", cursor.toString());
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
        run.setBitmap(cursor.getString(1));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            run.setDate(dateFormat.parse(cursor.getString(2)));
        }catch(ParseException e){
            Log.e("e", "parse exception");
        }
        run.setName(cursor.getString(3));
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