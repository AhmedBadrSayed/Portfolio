package com.projects.ahmedbadr.portfolio.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.projects.ahmedbadr.portfolio.DataObjects.MovieObject;
import com.projects.ahmedbadr.portfolio.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ahmed Badr on 10/11/2016.
 */
public class PostersRecyclerViewAdapter extends RecyclerView.Adapter<PostersRecyclerViewAdapter.MoviesViewHolder> {

    List<MovieObject> movieObjectList;
    Context activity;
    private static MyClickListener myClickListener;

    public PostersRecyclerViewAdapter(List<MovieObject> movieObjectList, Context activity){
        this.movieObjectList = movieObjectList;
        this.activity = activity;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        MoviesViewHolder pvh = new MoviesViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        holder.tittle.setText(movieObjectList.get(position).movieName);
        Picasso.with(activity).load(movieObjectList.get(position).moviePoster).into(holder.poster);
        holder.poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.date.setText(movieObjectList.get(position).movieDate);
    }

    @Override
    public int getItemCount() {
        return movieObjectList.size();
    }

    public void addItem(MovieObject movieObject, int index) {
        movieObjectList.add(index, movieObject);
        notifyItemInserted(index);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView tittle;
        ImageView poster;
        TextView date;


        public MoviesViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.movie_card);
            tittle = (TextView)itemView.findViewById(R.id.tittle);
            poster = (ImageView)itemView.findViewById(R.id.poster);
            date = (TextView)itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
