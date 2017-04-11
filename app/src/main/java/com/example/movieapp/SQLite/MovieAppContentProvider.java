package com.example.movieapp.SQLite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
/**
 * Created by Piso on 09/04/2017.
 */

public class MovieAppContentProvider extends ContentProvider {
    private MovieAppHelper mMovieAppHelper;
    private SQLiteDatabase database;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = MovieAppContract.CONTENT_AUTHORITY;
        // add two matcher in Local Match one for Table and the Another for Columns
        matcher.addURI(authority, MovieAppContract.FavoritesEntry.Table_name, 10);
        matcher.addURI(authority, MovieAppContract.FavoritesEntry.Table_name + "/id", 15);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMovieAppHelper = new MovieAppHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        database = mMovieAppHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case 10: {
                String selectQuery = "select * from " + MovieAppContract.FavoritesEntry.Table_name;
                cursor = database.rawQuery(selectQuery, null);
                break;
            }
            case 15: {
                String selectQuery = "select * from " + MovieAppContract.FavoritesEntry.Table_name + " where id =" + selection;
                cursor = database.rawQuery(selectQuery, null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        if (cursor.isAfterLast()) {
            return null;
        }
        database.close();
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        database = mMovieAppHelper.getWritableDatabase();
        long row_ID = database.insert(MovieAppContract.FavoritesEntry.Table_name, null, values);
        if (row_ID > 0) {
            Uri uri_id = ContentUris.withAppendedId(MovieAppContract.FavoritesEntry.specific_Uri, row_ID);
            getContext().getContentResolver().notifyChange(uri_id, null);
            return uri_id;
        }
        throw new SQLException("Failed to insert new Movie ");

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        database = mMovieAppHelper.getWritableDatabase();
        int c = 0;

        switch (uriMatcher.match(uri)) {
            case 10: {
                c = database.delete(MovieAppContract.FavoritesEntry.Table_name, "id=" + selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
