package com.example.movieapp.Screens.Home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.movieapp.Adapters.RecycleView_Adapter;
import com.example.movieapp.EventBus.AddButtonClicked;
import com.example.movieapp.Models.MovieContent;
import com.example.movieapp.R;
import com.example.movieapp.SQLite.MovieAppContract;
import com.example.movieapp.Screens.Details.MovieDetailsFragment;
import com.example.movieapp.Screens.Sittings.Sittings;
import com.example.movieapp.Volley.AppController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

    String filmCategory;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    String str = "http://api.themoviedb.org/3/movie/popular?api_key=fffc11bad42e01fa3032fb760b319219";
    RecyclerView mreRecyclerView;
    RecycleView_Adapter mreAdapter;
    List<MovieContent> mMovieList;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mreRecyclerView = (RecyclerView) view.findViewById(R.id.rv_Movies);

        prefs = getActivity().getSharedPreferences("MyPre", MODE_PRIVATE);
        filmCategory = prefs.getString("Category", "popular");
        if (filmCategory.equals("favourite")) {
            getMoviesFromDB();
        } else {
            str = "http://api.themoviedb.org/3/movie/" + filmCategory + "?api_key=fffc11bad42e01fa3032fb760b319219";
            sendJsonRequest(str);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        prefs = getActivity().getSharedPreferences("MyPre", MODE_PRIVATE);
        filmCategory = prefs.getString("Category", "popular");
        boolean update = prefs.getBoolean("Changed", false);

        if (!update) {
            return;
        }
        if (filmCategory.equals("favourite")) {
            getMoviesFromDB();
        } else {
            str = "http://api.themoviedb.org/3/movie/" + filmCategory + "?api_key=fffc11bad42e01fa3032fb760b319219";
            sendJsonRequest(str);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor = getActivity().getSharedPreferences("MyPre", MODE_PRIVATE).edit();
        editor.putBoolean("Changed", true);
        editor.apply();

    }

    private void getMoviesFromDB() {

        Cursor cursor = getActivity().getContentResolver().query(MovieAppContract.FavoritesEntry.specific_Uri, null, null, null, null);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mreRecyclerView.setLayoutManager(layoutManager);
        mreRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mreAdapter = new RecycleView_Adapter(Get_List(cursor), (AppCompatActivity) getActivity());
        mreRecyclerView.setAdapter(mreAdapter);
        mreAdapter.notifyDataSetChanged();

        if (getActivity().findViewById(R.id.fr_detailFragment) != null) {
            Bundle arguments = new Bundle();
            arguments.putInt("position", 0);
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(arguments);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fr_detailFragment, fragment)
                    .commit();
        }

    }

    public List<MovieContent> Get_List(Cursor cursor) {
        List<MovieContent> Fav_List = new ArrayList<>();

        if (cursor == null)
            return Fav_List;
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            MovieContent newMovie = new MovieContent();
            newMovie.id = cursor.getString(cursor.getColumnIndex("id"));
            newMovie.original_title = cursor.getString(cursor.getColumnIndex("original_title"));
            newMovie.overview = cursor.getString(cursor.getColumnIndex("overview"));
            newMovie.release_date = cursor.getString(cursor.getColumnIndex("release_date"));
            newMovie.vote_average = cursor.getString(cursor.getColumnIndex("vote_average"));
            newMovie.runtime = cursor.getString(cursor.getColumnIndex("runtime"));
            newMovie.production_year = cursor.getString(cursor.getColumnIndex("production_year"));
            newMovie.production_day = cursor.getString(cursor.getColumnIndex("production_day"));
            newMovie.imdb_id = cursor.getString(cursor.getColumnIndex("imdb_id"));
            newMovie.tagline = cursor.getString(cursor.getColumnIndex("tagline"));
            newMovie.backdrop_path = cursor.getString(cursor.getColumnIndex("backdrop_path"));
            newMovie.poster_path = cursor.getString(cursor.getColumnIndex("poster_path"));
            newMovie.title = cursor.getString(cursor.getColumnIndex("title"));
            newMovie.URL = cursor.getString(cursor.getColumnIndex("URL"));
            Fav_List.add(newMovie);
            cursor.moveToNext();
        }
        return Fav_List;
    }

    public void sendJsonRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mMovieList = new ArrayList<MovieContent>();
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MovieContent mMovieContent = new MovieContent();
                        mMovieContent.poster_path = jsonObject.get("poster_path").toString();
                        mMovieContent.id = jsonObject.get("id").toString();
                        mMovieContent.cleanAttributes();
                        mMovieList.add(mMovieContent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("Error ", "I've  an Error ::)");
                }
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                mreRecyclerView.setLayoutManager(layoutManager);
                mreRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mreAdapter = new RecycleView_Adapter(mMovieList, (AppCompatActivity) getActivity());
                mreRecyclerView.setAdapter(mreAdapter);
                mreAdapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Onclicked(AddButtonClicked addButtonClicked) {
        Intent intent = new Intent(getActivity(), Sittings.class);
        startActivity(intent);
    }

}
