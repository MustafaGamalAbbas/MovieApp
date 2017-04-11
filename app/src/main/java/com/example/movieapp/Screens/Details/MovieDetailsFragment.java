package com.example.movieapp.Screens.Details;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.movieapp.Adapters.Reviews_Adapter;
import com.example.movieapp.Adapters.Trailer_Adapter;
import com.example.movieapp.Models.MovieContent;
import com.example.movieapp.Models.Review;
import com.example.movieapp.Models.Trailer;
import com.example.movieapp.R;
import com.example.movieapp.SQLite.MovieAppContract;
import com.example.movieapp.Screens.FullScreen.FullscreenImage;
import com.example.movieapp.Volley.AppController;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {
    String mFilmId;
    Button mImdb;
    ImageView mCover, mPoster;
    TextView mTagline, mOverView, mMins, mYear, mRate, mData;
    CollapsingToolbarLayout collapsingToolbarLayout;
    List<Review> mReviews;
    List<Trailer> mTrailers;
    Reviews_Adapter mReviews_adapter;
    Trailer_Adapter mTrailer_adapter;
    RecyclerView mReviewRecycle, mTraliersRecycle;
    SharedPreferences sharedPreferences;
    FloatingActionButton mFab;
    MovieContent mMovieContent;
    SharedPreferences prefs;
    SharedPreferences.Editor editor ;

    public MovieDetailsFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_details, container, false);
        sharedPreferences = getActivity().getSharedPreferences("Tablet_UI", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("tablet", false)) {
            mFilmId = getArguments().getString("C_Movie");
         } else {
            Intent mIntent = getActivity().getIntent();
            if (mIntent != null) {
                mFilmId = mIntent.getStringExtra("C_Movie");
             }
        }

        mReviews = new ArrayList<>();
        mTrailers = new ArrayList<>();
        linkViews(root);
        getData_and_setViews();

        return root;
    }

    private void getData_and_setViews() {
        getMovieDetail();
        getReviews(mFilmId);
        getTrailers(mFilmId);
    }

    private void getReviews(String id) {
        String Review_Url = "http://api.themoviedb.org/3/movie/"
                + id + "/reviews?api_key=fffc11bad42e01fa3032fb760b319219";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Review_Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Review mReview = new Review();
                        mReview.authorName = jsonObject.get("author").toString();
                        mReview.content = jsonObject.get("content").toString();
                        mReviews.add(mReview);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                 }
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
                mReviewRecycle.setLayoutManager(layoutManager);
                mReviewRecycle.setItemAnimator(new DefaultItemAnimator());
                mReviews_adapter = new Reviews_Adapter(mReviews, (AppCompatActivity) getActivity());
                mReviewRecycle.setAdapter(mReviews_adapter);
                mReviews_adapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void getTrailers(String id) {
        String Review_Url = "http://api.themoviedb.org/3/movie/"
                + id + "/videos?api_key=fffc11bad42e01fa3032fb760b319219";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Review_Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Trailer mTrailer = new Trailer();
                        mTrailer.title = jsonObject.get("name").toString();
                        mTrailer.key = jsonObject.get("key").toString();
                        mTrailers.add(mTrailer);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                 }
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
                mTraliersRecycle.setLayoutManager(layoutManager);
                mTraliersRecycle.setItemAnimator(new DefaultItemAnimator());
                mTrailer_adapter = new Trailer_Adapter(mTrailers, (AppCompatActivity) getActivity());
                mTraliersRecycle.setAdapter(mTrailer_adapter);
                mTrailer_adapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void getMovieDetail() {
        String missed_Data = "https://api.themoviedb.org/3/movie/" + mFilmId + "?api_key=fffc11bad42e01fa3032fb760b319219";
        Ion.with(getActivity())
                .load(missed_Data)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {
                            mMovieContent = new MovieContent();
                            mMovieContent.id = result.get("id").toString();
                            mMovieContent.original_title = result.get("original_title").toString();
                            mMovieContent.release_date = result.get("release_date").toString();
                            mMovieContent.vote_average = result.get("vote_average").toString();
                            mMovieContent.runtime = result.get("runtime").toString();
                            mMovieContent.imdb_id = result.get("imdb_id").toString();
                            mMovieContent.adult = result.get("adult").toString();
                            mMovieContent.overview = result.get("overview").toString();
                            mMovieContent.poster_path = result.get("poster_path").toString();
                            mMovieContent.backdrop_path = result.get("backdrop_path").toString();
                            mMovieContent.tagline = result.get("tagline").toString();
                            mMovieContent.title = result.get("original_title").toString();
                            mMovieContent.cleanAttributes();
                            mMovieContent.detach_release_date();
                            setViews(mMovieContent);
                        }
                    }
                });
    }

    private void linkViews(View view) {
        mCover = (ImageView) view.findViewById(R.id.iv_backPhoto);
        mPoster = (ImageView) view.findViewById(R.id.iv_poster);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        mTagline = (TextView) view.findViewById(R.id.tv_tagline_text);
        mYear = (TextView) view.findViewById(R.id.tv_year);
        mMins = (TextView) view.findViewById(R.id.tv_runtime_txt);
        mRate = (TextView) view.findViewById(R.id.tv_rate_txt);
        mData = (TextView) view.findViewById(R.id.tv_date);
        mOverView = (TextView) view.findViewById(R.id.tv_overview_txt);
        mImdb = (Button) view.findViewById(R.id.bt_imdb);
        mReviewRecycle = (RecyclerView) view.findViewById(R.id.rv_Reviews);
        mTraliersRecycle = (RecyclerView) view.findViewById(R.id.rv_Trailers);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab_favorite);


    }

    private void setViews(final MovieContent movieContent) {
        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w342/" + movieContent.backdrop_path)
                .placeholder(R.drawable.movie)
                .into(mCover);
        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w342/" + movieContent.poster_path)
                .placeholder(R.drawable.movie)
                .into(mPoster);
        mTagline.setText(movieContent.tagline);
        mYear.setText(movieContent.production_year);
        mTagline.setText(movieContent.tagline);
        mRate.setText(movieContent.vote_average + " /10");
        mData.setText(movieContent.production_day);
        mOverView.setText(movieContent.overview);
        makeTextViewResizable(mOverView, 4, "See More", true);
        mMins.setText(movieContent.runtime);
        collapsingToolbarLayout.setTitle(movieContent.title);
        collapsingToolbarLayout.setTitleEnabled(true);
        mImdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://m.imdb.com/title/" + movieContent.imdb_id + "/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FullscreenImage.class);
                intent.putExtra("poster_id", movieContent.poster_path);
                intent.putExtra("title", movieContent.original_title);
                 getActivity().startActivity(intent);
            }
        });
        mCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FullscreenImage.class);
                intent.putExtra("poster_id", movieContent.backdrop_path);
                intent.putExtra("title", movieContent.original_title);
                getActivity().startActivity(intent);
            }
        });
        setFab();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = getContext().getContentResolver().query(MovieAppContract.FavoritesEntry.Uri_For_Id, null, mFilmId, null, null);
                if (cursor == null) {
                    // that's not found
                    mFab.setImageDrawable(getResources().getDrawable(R.drawable.full_heart));

                    getContext().getContentResolver().insert(MovieAppContract.FavoritesEntry.specific_Uri, getAsContentValues());
                    Snackbar.make(view, "Added To My Favourites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                } else {
                    mFab.setImageDrawable(getResources().getDrawable(R.drawable.empty_heart));
                    getContext().getContentResolver().delete(MovieAppContract.FavoritesEntry.specific_Uri, mFilmId, null);
                    Snackbar.make(view, "Removed From My Favourites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                update_Favourite_Movies();
            }
        });
    }

    private void update_Favourite_Movies() {
        prefs = getActivity().getSharedPreferences("MyPre", MODE_PRIVATE);
        if (!prefs.getString("Category", "popular").equals("favourite")) {
            return;
        }
        editor = getActivity().getSharedPreferences("MyPre", MODE_PRIVATE).edit();
        editor.putBoolean("Changed", true);
        editor.apply();
    }

    private void setFab() {
        if (getContext().getContentResolver().query(MovieAppContract.FavoritesEntry.Uri_For_Id, null, mFilmId, null, null) != null) {
            mFab.setImageDrawable(getResources().getDrawable(R.drawable.full_heart));
        } else {
            mFab.setImageDrawable(getResources().getDrawable(R.drawable.empty_heart));
        }
    }

    public ContentValues getAsContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", mMovieContent.id);
         contentValues.put("release_date", mMovieContent.release_date);
        contentValues.put("vote_average", mMovieContent.vote_average);
        contentValues.put("backdrop_path", mMovieContent.backdrop_path);
        contentValues.put("poster_path", mMovieContent.poster_path);
        contentValues.put("original_title", mMovieContent.original_title);
        contentValues.put("overview", mMovieContent.overview);
        contentValues.put("title", mMovieContent.title);
        contentValues.put("URL", mMovieContent.URL);
        contentValues.put("production_year", mMovieContent.production_year);
        contentValues.put("production_day", mMovieContent.production_day);
        contentValues.put("runtime", mMovieContent.runtime);
        contentValues.put("imdb_id", mMovieContent.imdb_id);
        contentValues.put("tagline", mMovieContent.tagline);
        return contentValues;
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                 } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);

                    String text = maxLine == -1 ? tv.getText().subSequence(0, lineEndIndex) + " " + expandText :
                            tv.getText().subSequence(0, lineEndIndex).toString();
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "See More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

}
