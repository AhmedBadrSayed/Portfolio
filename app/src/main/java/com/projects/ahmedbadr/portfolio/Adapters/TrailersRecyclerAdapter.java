package com.projects.ahmedbadr.portfolio.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.ahmedbadr.portfolio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Badr on 8/12/2016.
 */
public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailersRecyclerAdapter.TrailersViewHolder> {

    ArrayList<String> TrailersArray;
    //Context activity;
    private static MyClickListener myClickListener;

    public TrailersRecyclerAdapter(ArrayList<String> TrailersArray){
        this.TrailersArray = TrailersArray;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_card, parent, false);
        TrailersViewHolder pvh = new TrailersViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        holder.trailer.setText(TrailersArray.get(position));
    }

    @Override
    public int getItemCount() {
        return TrailersArray.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView trailer;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.trailer_card);
            trailer = (TextView)itemView.findViewById(R.id.movie_trailer);
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
