package com.example.movieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.movieapp.Models.MovieContent;
import com.example.movieapp.Screens.Details.MovieDetails;
import com.example.movieapp.Screens.Details.MovieDetailsFragment;
import com.example.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Piso on 03/04/2017.
 */

public class RecycleView_Adapter extends RecyclerView.Adapter<RecycleView_Adapter.MViewHolder> {

    private AppCompatActivity appCompatActivity ;
    private List<MovieContent> movieContentList ;
   public RecycleView_Adapter(List<MovieContent> list , AppCompatActivity compatActivity  ){
           movieContentList  =   list  ;
           appCompatActivity = compatActivity ;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cardview, null) ;
        MViewHolder mViewHolder = new MViewHolder(item);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, final int position) {

        movieContentList.get(position).setUrl(342);
        Picasso.with(appCompatActivity)
                .load("http://image.tmdb.org/t/p/w342/"+movieContentList.get(position).poster_path)
                .error(R.drawable.movie)
                .placeholder(R.drawable.movie)
                .into(holder.mImageview);
        holder.mImageview.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  SharedPreferences sharedPreferences = appCompatActivity.getSharedPreferences("Tablet_UI", Context.MODE_PRIVATE);
                  Bundle bundle = new Bundle();
                  bundle.putString("C_Movie",movieContentList.get(position).id);
                  if(sharedPreferences.getBoolean("tablet",false)){
                      MovieDetailsFragment fragment = new MovieDetailsFragment();
                      fragment.setArguments(bundle);
                      appCompatActivity.getSupportFragmentManager().beginTransaction()
                              .replace(R.id.fr_detailFragment, fragment)
                              .commit();
                  }
                  else {

                      appCompatActivity.startActivity(new Intent(appCompatActivity, MovieDetails.class)
                              .putExtra("C_Movie", (movieContentList.get(position).id)));
                  }
              }
          });
    }

    @Override
    public int getItemCount() {
        return movieContentList.size();
    }

    class  MViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageview ;
        public MViewHolder(View itemView) {
            super(itemView);
            mImageview = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
