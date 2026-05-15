package com.example.tayang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "tayang.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_FAVORITE = "favorites";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Buat tabel favorit
        db.execSQL("CREATE TABLE " + TABLE_FAVORITE + " (" +
                "id INTEGER PRIMARY KEY, " +
                "title TEXT, " +
                "poster_path TEXT, " +
                "backdrop_path TEXT, " +
                "overview TEXT, " +
                "rating REAL, " +
                "media_type TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(db);
    }

    // Tambah film ke favorit
    public void addFavorite(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", movie.getId());
        values.put("title", movie.getTitle());
        values.put("poster_path", movie.getPosterPath());
        values.put("backdrop_path", movie.getBackdropPath());
        values.put("overview", movie.getOverview());
        values.put("rating", movie.getVoteAverage());
        values.put("media_type", movie.getMediaType() != null ? movie.getMediaType() : "movie");
        db.insertOrThrow(TABLE_FAVORITE, null, values);
        db.close();
    }

    // Hapus film dari favorit
    public void removeFavorite(int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE, "id=?", new String[]{String.valueOf(movieId)});
        db.close();
    }

    // Cek apakah film sudah difavoritkan
    public boolean isFavorite(int movieId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_FAVORITE + " WHERE id=?",
                new String[]{String.valueOf(movieId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Ambil semua favorit
    public List<Movie> getAllFavorites() {
        List<Movie> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAVORITE, null);
        if (cursor.moveToFirst()) {
            do {
                FavoriteMovie fm = new FavoriteMovie();
                fm.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                fm.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                fm.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow("poster_path")));
                fm.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow("backdrop_path")));
                fm.setOverview(cursor.getString(cursor.getColumnIndexOrThrow("overview")));
                fm.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow("rating")));
                fm.setMediaType(cursor.getString(cursor.getColumnIndexOrThrow("media_type")));
                list.add(fm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}