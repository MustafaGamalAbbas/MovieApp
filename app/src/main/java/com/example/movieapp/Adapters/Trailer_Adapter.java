package com.example.movieapp.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movieapp.Models.Trailer;
import com.example.movieapp.R;

import java.util.List;

/**
 * Created by Piso on 05/04/2017.
 */

public class Trailer_Adapter  extends RecyclerView.Adapter<Trailer_Adapter.ViewHolder>{

    List<Trailer> mTrailerList ;
    AppCompatActivity mAppCompatActivity ;
    public Trailer_Adapter(List<Trailer> trailers , AppCompatActivity appCompatActivity)
    {
        mTrailerList =trailers ;
        mAppCompatActivity =appCompatActivity ;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_cardview, null) ;
        Trailer_Adapter.ViewHolder mViewHolder = new Trailer_Adapter.ViewHolder(item);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
       holder.mTrailerName.setText(mTrailerList.get(position).title);

        holder.mTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str  = mTrailerList.get(position).key ;
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + str);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mAppCompatActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView mTrailer ;
        TextView mTrailerName ;
        public ViewHolder(View itemView) {
            super(itemView);
            mTrailerName = (TextView) itemView.findViewById(R.id.tv_trailerName);
            mTrailer = (CardView) itemView.findViewById(R.id.cv_trailer);
        }
    }
}
