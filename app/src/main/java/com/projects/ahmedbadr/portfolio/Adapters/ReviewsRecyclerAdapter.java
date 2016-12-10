package com.projects.ahmedbadr.portfolio.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.ahmedbadr.portfolio.R;

import java.util.ArrayList;

/**
 * Created by Ahmed Badr on 8/12/2016.
 */
public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.ReviewsViewHolder> {

    ArrayList<String> AuthorsArray;
    //Context activity;
    private static MyClickListener myClickListener;

    public ReviewsRecyclerAdapter(ArrayList<String> AuthorsArray){
        this.AuthorsArray = AuthorsArray;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        ReviewsViewHolder pvh = new ReviewsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.auther.setText(AuthorsArray.get(position));
    }

    @Override
    public int getItemCount() {
        return AuthorsArray.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView auther;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.review_card);
            auther = (TextView)itemView.findViewById(R.id.auther);
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
