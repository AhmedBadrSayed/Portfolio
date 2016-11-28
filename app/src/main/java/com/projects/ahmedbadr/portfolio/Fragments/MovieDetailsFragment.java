package com.projects.ahmedbadr.portfolio.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.projects.ahmedbadr.portfolio.DataStore.MoviesDB;
import com.projects.ahmedbadr.portfolio.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    @BindView(R.id.movie_description)
    TextView overview;
    @BindView(R.id.movie_vote)
    TextView vote;
    @BindView(R.id.movie_date)
    TextView date;
    private String MovieDetails="", PosterPath = "", MovieTitle = "", Overview = "", VoteAverage = "", Date = "", ID = "";
    private String[] DetailsArray;
    private MoviesDB moviesDB;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this,rootview);
        moviesDB = new MoviesDB(getActivity());
        Intent intent = getActivity().getIntent();
        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            MovieDetails = intent.getStringExtra(Intent.EXTRA_TEXT);
            viewAdapter(MovieDetails);
        }

        return rootview;
    }

    public void viewAdapter(String movieDetails) {
        DetailsArray = movieDetails.split("=");
        ID = DetailsArray[0];
        MovieTitle = DetailsArray[1];
        Overview = DetailsArray[2];
        VoteAverage = DetailsArray[3];
        Date = DetailsArray[4];
        PosterPath = DetailsArray[5];
        overview.setText(Overview);
        vote.setText(VoteAverage+"/10");
        date.setText(Date);
//        Picasso.with(getActivity()).load(PosterPath).into(poster);
//        poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

}
