package com.example.movieapp.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Piso on 09/04/2017.
 */

public class MovieAppHelper extends SQLiteOpenHelper {

    private static final String ColumnType  = " TEXT";
    private static final  int Version = 1 ;
    public  static final String NameOfDataBase = "Favourite_Movies.db";
    private static final String SQL_CREATE_DATABASE = "CREATE TABLE "+ "FavoriteMovies"+ " ( "+
                  MovieAppContract.FavoritesEntry.favorite.vote_average + ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.runtime + ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.imdb_id + ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.tagline + ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.backdrop_path+ ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.poster_path + ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.title + ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.id+ ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.original_title + ColumnType+
            "," + MovieAppContract.FavoritesEntry.favorite.overview + ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.release_date + ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.URL + ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.production_year + ColumnType +
            "," + MovieAppContract.FavoritesEntry.favorite.production_day + ColumnType +" ); ";

    private static final String SQL_DELETE_DATABASE = "DROP TABLE IF EXIST " + "FavoriteMovies" ;

    public MovieAppHelper(Context context ) {
        super(context, NameOfDataBase, null, Version);
    }
     @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
     }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }
}
