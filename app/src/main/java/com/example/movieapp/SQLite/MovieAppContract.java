package com.example.movieapp.SQLite;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.movieapp.Models.MovieContent;

/**
 * Created by Piso on 09/04/2017.
 */

public class MovieAppContract {
    public static final String CONTENT_AUTHORITY = "com.example.movieapp";  // Authority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavoritesEntry implements BaseColumns
    {
        public static String Table_name = "FavoriteMovies" ;
        public static final Uri specific_Uri = BASE_CONTENT_URI.buildUpon().appendPath(Table_name).build(); // for table
        public static final Uri Uri_For_Id = specific_Uri.buildUpon().appendPath("id").build(); // for specific id
        public static MovieContent favorite = new MovieContent(); // all my Columns
    }
}
