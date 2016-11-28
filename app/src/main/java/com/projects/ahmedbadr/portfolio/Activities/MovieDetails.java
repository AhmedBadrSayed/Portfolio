package com.projects.ahmedbadr.portfolio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.projects.ahmedbadr.portfolio.DataStore.MoviesDB;
import com.projects.ahmedbadr.portfolio.Fragments.MovieDetailsFragment;
import com.projects.ahmedbadr.portfolio.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.POST;

public class MovieDetails extends AppCompatActivity {

    @BindView(R.id.movie_poster)
    ImageView Poster;
    private String MovieDetails="", PosterPath = "", MovieTitle = "", Overview = "", VoteAverage = "", Date = "", ID = "";
    private String[] DetailsArray;
    private MoviesDB moviesDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        /*if (savedInstanceState!=null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_details_container,new MovieDetailsFragment())
                    .commit();
        }*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();

        ButterKnife.bind(this);
        moviesDB = new MoviesDB(this);
        Intent intent = this.getIntent();
        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            MovieDetails = intent.getStringExtra(Intent.EXTRA_TEXT);
            viewAdapter(MovieDetails);
            actionBar.setTitle(MovieTitle);
            System.out.print("tittle "+MovieTitle);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fav_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moviesDB.isInDataBase(MovieTitle)==true){
                    moviesDB.deleteMovie(MovieTitle);
                    Snackbar.make(view, "Removed from favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if(moviesDB.isInDataBase(MovieTitle)==false){
                    moviesDB.addMovie(ID, MovieTitle, Overview, VoteAverage, Date, PosterPath);
                    Snackbar.make(view, "Added to favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void viewAdapter(String movieDetails) {
        DetailsArray = movieDetails.split("=");
        ID = DetailsArray[0];
        MovieTitle = DetailsArray[1];
        Overview = DetailsArray[2];
        VoteAverage = DetailsArray[3];
        Date = DetailsArray[4];
        PosterPath = DetailsArray[5];
        Picasso.with(this).load(PosterPath).into(Poster);
    }
}
