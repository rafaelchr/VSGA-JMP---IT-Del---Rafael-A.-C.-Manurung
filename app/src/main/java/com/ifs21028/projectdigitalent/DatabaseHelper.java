package com.ifs21028.projectdigitalent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "SignLog.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "SignLog.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create Table users(email TEXT primary key, password TEXT)");
        MyDatabase.execSQL("create Table biodata(nim TEXT primary key, nama TEXT, kelamin TEXT, prodi TEXT, kelas TEXT, lahir TEXT, alamat TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists biodata");
    }

    public Boolean insertData(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDatabase.insert("users", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean insertBio(String nama, String kelamin, String nim, String prodi, String kelas, String lahir, String alamat) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nim", nim);
        contentValues.put("nama", nama);
        contentValues.put("kelamin", kelamin);
        contentValues.put("prodi", prodi);
        contentValues.put("kelas", kelas);
        contentValues.put("lahir", lahir);
        contentValues.put("alamat", alamat);

        long result = MyDatabase.insert("biodata", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean editBio(String nama, String kelamin, String nim, String prodi, String kelas, String lahir, String alamat) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("nama", nama);
        contentValues.put("kelamin", kelamin);
        contentValues.put("prodi", prodi);
        contentValues.put("kelas", kelas);
        contentValues.put("lahir", lahir);
        contentValues.put("alamat", alamat);

        long result = MyDatabase.update("biodata", contentValues, "nim = ?", new String[]{nim});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});

        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }

    public Boolean checkNim(String nim) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from biodata where nim = ?", new String[]{nim});

        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});

        if (cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }

    public Cursor getBio() {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM biodata", null);

        return cursor;
    }

    // Di dalam DatabaseHelper
    public String getNameByNim(String nim) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT nama FROM biodata WHERE nim = ?", new String[]{nim});

        String nama = null;

        if (cursor != null && cursor.moveToFirst()) {
            int indexNama = cursor.getColumnIndex("nama");

            nama = cursor.getString(indexNama);

            cursor.close();
        }

        return nama;
    }

    public Cursor getDetailByNim(String nim) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM biodata WHERE nim = ?", new String[]{nim});
        return cursor;
    }

    public Boolean deleteBio(String nim) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        int result = MyDatabase.delete("biodata", "nim = ?", new String[]{nim});

        return result > 0;
    }
}
